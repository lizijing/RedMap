package controllers;

import java.util.*;
import play.mvc.Controller;
import models.*;

public class Users extends Controller {

	public static void index(Integer page) {
		render(page);
	}

	public static void show(Long id) {
		User user = User.findById(id);
		notFoundIfNull(user);
		render(user);
	}
}
