package service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.JSONException;
import org.json.JSONObject;

import service.inter.ReturnSystemTime;

public class ReturnTime implements ReturnSystemTime{
	@Override
	public JSONObject systemTime()
	{
		JSONObject time  = new JSONObject();
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Calendar cal = Calendar.getInstance();
	    String tmpTime;
	    tmpTime = dateFormat.format(cal.getTime());
//	    tmpTime = "1900" + tmpTime.substring(4);
	    try {
			time.put("time", tmpTime);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return time;
	}

}
