package service.impl;
/** 
 * @author itzijing@gmail: 
 * @version 创建时间：2012-10-9 下午6:54:19 
 * 类说明 
 */


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import service.inter.ReturnSystemTime;



public class returnStatus  {
	//@Override
	public JSONObject getStatus()
	{
		JSONObject Module  = new JSONObject();
		
	    try {
			Module.put("JobName", "routeCount");
			Module.put("SubmitTime", "yymmdd");
			Module.put("FinishTime","yymmdd");
			Module.put("Status", "running");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Module;
	}

}
