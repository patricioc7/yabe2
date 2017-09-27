package models;
 
import java.util.*;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;

import play.modules.morphia.Model;
 

 
@Entity
public class Post extends Model {
 
    public String title;
    public Date postedAt;
    
    public String content;
    
    @Reference(ignoreMissing = true)
    public User author;
    
    @Reference(ignoreMissing = true)
    public List<Comment> comments;
     
    @Reference(ignoreMissing = true)
    public Set<Tag> tags;
     
    public Post(User author, String title, String content) {
        this.comments = new ArrayList<Comment>();
        this.tags = new TreeSet<Tag>();
        this.author = author;
        this.title = title;
        this.content = content;
        this.postedAt = new Date();
    }
    
    
    public Post addComment(String author, String content) {
        Comment newComment = new Comment(this, author, content).save();
        this.comments.add(newComment);
        this.save();
        return this;
    }
    
    public Post tagItWith(String name) {
        tags.add(Tag.findOrCreateByName(name));
        return this;
    }
    
    public Post previous() {
       // return Post.find("postedAt < ? order by postedAt desc", postedAt).first();
    	MorphiaQuery query = Post.q();
    	query.field("postedAt").lessThan(postedAt);
        return query.first();
    }
     
    public Post next() {
    	MorphiaQuery query = Post.q();
    	query.field("postedAt").greaterThan(postedAt);
        return query.first();
    }
    
    public static List<Post> findTaggedWith(String tag) {
    	return Post.q().filter("tag", tag).asList();
    	
        /*return Post.find(
            "select distinct p from Post p join p.tags as t where t.name = ?", tag
        ).fetch();*/
    }
    
    public String toString() {
    	return title;
    }
    /* TODO
    public static List<Post> findTaggedWith(String... tags) {
        
    	
    	
    	return Post.find(
                "select distinct p from Post p join p.tags as t where t.name in (:tags) group by p.id, p.author, p.title, p.content,p.postedAt having count(t.id) = :size"
        ).bind("tags", tags).bind("size", tags.length).fetch();
    }*/
}