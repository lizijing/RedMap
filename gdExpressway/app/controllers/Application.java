package controllers;

import models.*;
import play.*;
import play.mvc.*;
import java.util.*;
import java.io.File;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.GsonBuilder;

public class Application extends Controller {

	/*
	 * 返回登录页面
	 */
	public static void login() {
		render();
	}

	/*
	 * 登录验证
	 */

	public static void authenticate(String userName, String password) {
		// renderText(email);
		User user = new User(userName, password);
		System.out.println(user.userName);
		if (user == null || !user.checkPassword(password)) {
			flash.error("Bad email or bad password");
			flash.put("userName", userName);
			login();
		}
		connect(user);
		// flash.success("Welcome back %s !", user.userName);
		Users.show(user.id);
	}

	static void connect(User user) {
		session.put("logged", user.id);

	}

	static User connectedUser() {
		String userId = session.get("logged");
		return userId == null ? null : (User) User.findById(Long
				.parseLong(userId));
	}

}
