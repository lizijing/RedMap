package service.inter;

import org.json.JSONObject;

public interface ReturnSystemTime {
	/**
	 * 取得系统时间
	 * @return 以JSONobject格式返回系统时间
	 */
	JSONObject systemTime();

}
