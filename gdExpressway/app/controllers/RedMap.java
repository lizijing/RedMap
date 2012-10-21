package controllers;

import play.*;
import play.mvc.*;
import service.inter.*;
import service.impl.*;
import service.impl.returnStatus;
import service.inter.*;
import preset.jar.*;
import java.util.*;
import preset.tool.*;

import org.json.JSONException;
import org.json.JSONObject;
import models.*;
import com.google.gson.GsonBuilder;

public class RedMap extends Controller {

	/*
	 * 返回首頁
	 */
	public static void returnStatus() {
		/*
		 * JSONObject status; try { returnStatus jobStatus=new returnStatus();
		 * status = jobStatus.getStatus(); renderJSON(status); } catch
		 * (Exception e) { System.err.println("Exception error"); }
		 */
		render();

	}

	public static void index() {
		render();
	}

	/*
	 * 返回選擇HDFS操作頁面
	 */
	public static void HdfsOperation() {
		render();
	}

	public static void MainFunctionChosen() {
		render();
	}

	/*
	 * jquery尝试
	 */
	public static void jqueryui() {
		render();
	}

	public static void getUserInfo(String UserName) {

		render(UserName);
	}

	/*
	 * 测试查看Hdfs文件
	 */
	public static void getFile(String FileType) {

		if (FileType.equals("DataFile")) {
			// 以List方式返回指定用户的在HDFS数据文件的列表
		}
		if (FileType.equals("JarFile")) {
			// 以List方式返回指定用户的在HDFSjar文件的列表
		}
		if (FileType.equals("ParaFile")) {
			// 以List方式返回指定用户的在HDFS参数文件的列表
		}

		List<String> files = null;
		// 以List方式返回指定用户的在HDFS数据文件的列表
		render(files);
	}

	/*
	 * visualchart test
	 */
	public static void visualchart() {
		render();
	}

	/*
	 * 测试图表
	 */
	public static void highchart() {
		PresetValue val = new PresetValue();
		int id = val.getTaskID();
		String chartPath = val.xmlHome;

		render(chartPath);
	}

	/*
	 * 返回Hdfs文件类型（DataFile、 JarFile、ParaFile)
	 */
	public static void fileTypeChosen(String userName) {
		render(userName);
	}

	/*
	 * 预置job选择
	 */
	public static void jobChosen(String jobName) {
		render();
	}

	/*
	 * Hdfs操作选择
	 */
	public static void hdfsOperationChosen(String operationType) {
		render();
	}

	/*
	 * run job
	 */
	public static void runJob() {
		PresetJar presetjar = new PresetJar();
		String[] s = new String[4];
		s[0] = "/user/amber/input";
		s[1] = "/user/amber/output";
		s[2] = "20111201";
		s[3] = "20111231";
		presetjar.rountCountOnDate(s);
	}

}
