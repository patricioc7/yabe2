package controllers;
 
import models.*;
 
public class Security extends Secure.Security {
	
	static boolean authenticate(String username, String password) {
	   return User.connect(username, password) != null;
		//return true;
	}
	
	static void onDisconnected() {
	    Application.index();
	}
	
	static void onAuthenticated() {
	    Admin.index();
	}
	
	static boolean check(String profile) {
	    if("admin".equals(profile)) {
	        //return User.find("byEmail", connected()).<User>first().isAdmin;
	        //return (User.q().filter("email", connected()).filter("isAdmin", true)._get() != null);
	    	return true;
	    }
	    return false;
	}
}