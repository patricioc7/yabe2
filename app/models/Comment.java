package models;
 
import java.util.*;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Reference;

import net.sf.oval.constraint.MaxSize;
import play.data.validation.Required;
import play.modules.morphia.Model;
 
@Entity
public class Comment extends Model {
 
	@Required
    public String author;
	
	@Required
    public Date postedAt;
     
    @Required
    @MaxSize(10000)
    public String content;
    
    @Reference(ignoreMissing = true)
    @Required
    public Post post;
    
    public Comment(Post post, String author, String content) {
        this.post = post;
        this.author = author;
        this.content = content;
        this.postedAt = new Date();
        this.save();
    }
    
    public String toString() {
        return content;
    }
 
}