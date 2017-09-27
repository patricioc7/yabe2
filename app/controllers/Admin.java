package controllers;
 
import play.*;
import play.modules.morphia.Model.MorphiaQuery;
import play.mvc.*;
 
import java.util.*;
 
import models.*;
 
@With(Secure.class)
public class Admin extends Controller {
	
	private static User defaultUser;
    
    @Before
    static void setConnectedUser() {
        if(Security.isConnected()) {
            User user = User.find("byEmail", Security.connected()).first();
            //VILLEREADA
            if(user==null) {
	            User fistUserEver = User.q().filter("email", "admin@yabe.com").get();
	            if(fistUserEver==null) {
		          	defaultUser = new User("admin@yabe.com", "pass1234", "FirstAdminEver", true).save();
		            renderArgs.put("user", defaultUser.fullname);
	            }else {
	            	renderArgs.put("user", fistUserEver.fullname);
	            }
            }else {
            	renderArgs.put("user", user.fullname);
            }
        }
    }
 
    public static void index() {
        String user = Security.connected();
        // //List<Post> posts = Post.find("author.email", user).fetch();
        
        //List<Post> posts = Post.q().filter("user", user).asList();
        
        List<Post> posts = Post.q().asList();
        //VILLEREADA
        if (posts.isEmpty()) {
        	List<Post> fakePostsList = new ArrayList<Post>();
        	fakePostsList.add(new Post(defaultUser, "Generic Author", "Generic Author").save());
        	render(fakePostsList);
        }else {
        	render(posts);
        }
    }
    
    public static void form() {
        render();
    }
    
    public static void save(Long id, String title, String content, String tags) {
        Post post;
        
        if(id == null) {
            // Create post
            User author = User.find("byEmail", Security.connected()).first();
            post = new Post(author, title, content);
        } else {
            // Retrieve post
            post = Post.findById(id);
            // Edit
            post.title = title;
            post.content = content;
            post.tags.clear();
        }
        // Set tags list
        for(String tag : tags.split("\\s+")) {
            if(tag.trim().length() > 0) {
                post.tags.add(Tag.findOrCreateByName(tag));
            }
        }
        // Validate
        validation.valid(post);
        if(validation.hasErrors()) {
            render("@form", post);
        }
        // Save
        post.save();
        index();
    }
        
    
    public static void form(String id) {
        if(id != null) {
        	
            Post post = Post.findById(id);
            render(post);
        }
        render();
    }
       
    
}