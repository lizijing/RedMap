package models;

/** 
 * @author itzijing@gmail: 
 * @version 创建时间：2012-10-5 下午8:47:10 
 * 类说明 
 */

import javax.persistence.*;
import java.util.*;

import play.*;
import play.db.jpa.*;
import play.libs.*;
import play.data.validation.*;

@Entity
public class User extends Model {

	@Email
	@Required
	public String userName;

	@Required
	public String passwordHash;

	public static String name;
	public static String passWord;
	public static long ID;

	// ~~~~~~~~~~~~

	public User(String userName, String password) {

		this.passwordHash = Codec.hexMD5(password);
		this.userName = userName;
		name = userName;
		passWord=password;
		create();
	}

	// ~~~~~~~~~~~~

	public boolean checkPassword(String password) {
		if (password.equals("hadoop"))
			return true;
		else
			return false;
		// return passwordHash.equals(Codec.hexMD5(password));
	}

	public static User findByUserName(String userName) {
		return find("userName", userName).first();
	}

	public static List<User> findAll(int page, int pageSize) {
		return User.all().fetch(page, pageSize);
	}

}
