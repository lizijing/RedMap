package controllers;

import play.*;
import play.mvc.*;
import service.inter.ReturnSystemTime;
import preset.jar.*;
import java.util.*;

import models.*;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.GsonBuilder;
import preset.jar.PresetJar;
import preset.tool.PresetValue;

/**
 * @author itzijing@gmail:
 * @version 创建时间：2012-10-4 下午12:27:34 类说明
 */
public class RunJob extends Controller {
	/*
	 * 测试highchart
	 */
	/*
	 * public static void runRouteCountOnDate() {
	 * 
	 * PresetValue val = new PresetValue(); int id=val.getTaskID(); String
	 * chartPath=val.xmlHome;//实际是chartPath=outputPath+id;
	 * 
	 * render(chartPath); }
	 */
	/*
	 * getChart
	 */
	public static String tempath = "tempath";
	public static Chart routeCountChart = new Chart(tempath);
	public static Chart averageSpeedCountChart = new Chart(tempath);
	public static Chart modelsCountChart = new Chart(tempath);

	public static void getRouteCountOnDateChart(String chartPath) {
		String path = chartPath;
		render(path);

	}
	public static void getXML(String xmlPath){
		String path= xmlPath;
		render(path);
	}

	public static void getAverageSpeedCountChart() {
		String path = averageSpeedCountChart.chartPath;
		render(path);

	}

	public static void getModelsCountChart() {
		String path = modelsCountChart.chartPath;
		render(path);

	}

	/*
	 * 执行AverageSpeed
	 */
	public static void runAverageSpeedCount(String inputPath,
			String outputPath, String startDate, String endDate, String carType) {
		try {
			PresetValue val = new PresetValue();
			int id = val.getTaskID();
			PresetJar presetjar = new PresetJar();
			presetjar.setUserName(User.name);
			presetjar.setTaskID(id);
			presetjar.setXName("路段");
			presetjar.setYName("平均车速");
			presetjar.setTaskName("AverageSpeedCount-Task:"
					+ String.valueOf(id));
			String[] s = new String[5];
			s[0] = inputPath;
			s[1] = outputPath;
			s[2] = startDate;
			s[3] = endDate;
			s[4] = carType;
			presetjar.averageSpeedCount(s);
			String chartPath = PresetValue.tempFile + String.valueOf(id)
					+ ".xml";

			averageSpeedCountChart.chartPath = chartPath;
			// 测试用的String chartPath=val.xmlHome;

			redirect("/MainFunction/showStatus");
		} catch (Exception e) {
			System.err.println("Exception error");
		}

	}

	/**
	 * 执行RouteCountOnData
	 */

	public static void runRouteCountOnData(String inputPath, String outputPath,
			String startDate, String endDate) {

		try {
			PresetValue val = new PresetValue();
			int id = val.getTaskID();
			PresetJar presetjar = new PresetJar();
			presetjar.setUserName(User.name);
			presetjar.setTaskID(id);
			presetjar.setXName("路段");
			presetjar.setYName("车流量");
			presetjar
					.setTaskName("RouteCountOnData-Task:" + String.valueOf(id));
			String[] s = new String[4];
			s[0] = inputPath;
			s[1] = outputPath;
			s[2] = startDate;
			s[3] = endDate;
			presetjar.rountCountOnDate(s);
			String chartPath = outputPath + "/part-r-00000.xml";
			routeCountChart.chartPath = chartPath;
			// 测试用的String chartPath=val.xmlHome;

			redirect("/MainFunction/jobStatus");
		} catch (Exception e) {
			System.err.println("Exception error");
		}
		
	}

	public static void runModelsCount(String inputPath, String outputPath,
			String startDate, String endDate, String sectionId) {
		try {
			PresetValue val = new PresetValue();
			int id = val.getTaskID();
			PresetJar presetjar = new PresetJar();
			presetjar.setUserName(User.name);
			presetjar.setTaskID(id);
			presetjar.setTaskName(sectionId);
			presetjar.setXName("车型");
			presetjar.setYName("车流量");
			String[] s = new String[5];
			s[0] = inputPath;
			s[1] = outputPath;
			s[2] = startDate;
			s[3] = endDate;
			s[4] = sectionId;
			presetjar.modelsCount(s);
			String chartPath = PresetValue.tempFile + String.valueOf(id)
					+ ".xml";

			modelsCountChart.chartPath = chartPath;
			// 测试用的String chartPath=val.xmlHome;

			redirect("/MainFunction/showStatus");
		} catch (Exception e) {
			System.err.println("Exception error");
		}

	}

}
