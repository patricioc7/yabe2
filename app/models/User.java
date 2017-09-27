package models;
 
import java.util.*;

import org.mongodb.morphia.annotations.Entity;

import play.data.validation.Email;
import play.data.validation.Required;
import play.modules.morphia.Model;
 
@Entity
public class User extends Model {
 
    @Email
    @Required
    public String email;
    
    @Required
    public String password;
    public String fullname;
    public boolean isAdmin;
    
    public User(String email, String password, String fullname) {
        this.email = email;
        this.password = password;
        this.fullname = fullname;
    }
    
    public User(String email, String password, String fullname, boolean isAdmin) {
        this.email = email;
        this.password = password;
        this.fullname = fullname;
        this.isAdmin = isAdmin;
    }

    public static User connect(String email, String password) {
        //return find("byEmailAndPassword", email, password).first();
    	return q().filter("email", email).filter("password", password).get();
    }
    
    public String toString() {
        return email;
    }
 
}