package controllers;
 
import java.util.*;
 
import play.*;
import play.data.validation.Required;
import play.libs.Codec;
import play.libs.Images;
import play.modules.morphia.Model.MorphiaQuery;
import play.mvc.*;
import play.cache.*;
 
import models.*;
 
public class Application extends Controller {
	
    @Before
    static void addDefaults() {
        renderArgs.put("blogTitle", Play.configuration.getProperty("blog.title"));
        renderArgs.put("blogBaseline", Play.configuration.getProperty("blog.baseline"));
    }
    
    public static void index() {
    	
        //Post frontPost = Post.find("order by postedAt desc").first();
    	Post frontPost;
    	 List<Post> posts = Post.q().order("-_created").asList();
    	if (posts.size() > 0) {
    		 frontPost = (Post) Post.q().order("-_created").asList().get(0);
    	}else {
    		frontPost = new Post(null, "FirstPost", "First post");
    		frontPost.save();
    	}
    	
    	
        /**List<Post> olderPosts = Post.find(
            "order by postedAt desc"
        ).from(1).fetch(10);**/
    	List<Post> olderPosts = Post.q().order("-_created").asList();
    	
    	//VILLEREADA ALERT
    	if(olderPosts.size()>10) {
    		olderPosts = olderPosts.subList(1, 10);
    	}else {
    		olderPosts.remove(0);
    	}
    	
        render(frontPost, olderPosts);
    }

    public static void show(String id) {
    	
    	Post post = Post.findById(id);
        //Post post = Post.findById(id);
        String randomID = Codec.UUID();
        render(post, randomID);
    }
    
    public static void postComment(
            String postId, 
            @Required(message="Author is required") String author, 
            @Required(message="A message is required") String content, 
            @Required(message="Please type the code") String code, 
            String randomID) 
    {
        //Post post = Post.findById(postId);
        Post post = Post.findById(postId);
        validation.equals(
            code, Cache.get(randomID)
        ).message("Invalid code. Please type it again");
        if(validation.hasErrors()) {
            render("Application/show.html", post, randomID);
        }
        post.addComment(author, content);
        flash.success("Thanks for posting %s", author);
        Cache.delete(randomID);
        show(postId);
    } 
    
    public static void captcha(String id) {
        Images.Captcha captcha = Images.captcha();
        String code = captcha.getText("#E4EAFD");
        Cache.set(id, code, "10mn");
        renderBinary(captcha);
    }
    
    public static void listTagged(String tag) {
        List<Post> posts = Post.findTaggedWith(tag);
        render(tag, posts);
    }
    
    public static void signUp(String email, String password, String fullname, boolean isAdmin) {
    	if(session.get("username") != null ) {
    		flash.success("Already Logged in");
    	}else{
    		MorphiaQuery query = User.q().findBy("email", email);
        	User checkUser = query.get();
        	if(checkUser == null) {
        		User user = new User(email, password, fullname, isAdmin);
                // Validate
        	
                validation.valid(user);
                if(validation.hasErrors()) {
                    render("@signUp", user);
                }
                
        		user.save();
        		flash.success("User created");
        		
        		
        	}else {
        		//flash.success("User already exists");
        	}
    	};
    	

        render();
    }
 
}