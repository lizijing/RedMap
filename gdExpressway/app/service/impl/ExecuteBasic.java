package service.impl;

import org.apache.hadoop.util.RunJar;

/**
 * 创建runJar的Runnable接口
 * 
 * @author amber
 * 
 */
public class ExecuteBasic implements Runnable {
	/**
	 * 记录用户名
	 */
	private String userName = "";

	/**
	 * 设置用户名
	 * 
	 * @param name
	 *            用户名
	 */
	public void setUserName(String name) {
		userName = "/" + name;
		jarParams[1] = userName + jarParams[1];
	}

	/**
	 * jar文件参数
	 */
	String[] jarParams;

	/**
	 * 记录jar参数
	 * 
	 * @param jarParam
	 *            jar参数
	 */
	ExecuteBasic(String[] jarParam) {
		jarParams = jarParam;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			RunJar.main(jarParams);
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
