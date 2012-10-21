package service.impl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import preset.tool.PresetValue;

/**
 * 用于提交Jar作业任务
 * 
 * @author amber
 * 
 */
public class RunJarTool {
	/**
	 * 记录用户名
	 */
	private String userName = null;

	/**
	 * 设置用户名
	 * 
	 * @param name
	 *            用户名
	 */
	public void setUserName(String name) {
		userName = new String(name);
	}
	
	/**
	 * 记录任务名称
	 */
	private String taskName = null ;
	/**
	 * 设置任务名称
	 * @param name
	 */
	public void taskName(String name) {
		taskName = new String(name);
	}

	/**
	 * 多线程管理器
	 */
	private static ExecutorService exec = Executors.newCachedThreadPool();

	/**
	 * 提交jar作业
	 * 
	 * @param jarParams
	 *            从第一个传递参数中获取jar包的hdfs地址，并试图从jar中包中获取manifest信息，以查找mainclass
	 *            name。如果查找不到mainclass name，则把传递参数中的第二个设为mainclass
	 *            name，然后以传递参数中剩下的参数作为调用参数
	 * 
	 */
	public void executeJar(String[] jarParams) {
		PresetValue val = new PresetValue();
		FileControl fileControl = new FileControl();
		if (userName!=null) fileControl.setUsername(userName);
		fileControl.getHdfsFile(jarParams[0], val.jarTmpPath);
		jarParams[0] = val.jarTmpPath;
		ExecuteBasic executeBasic = new ExecuteBasic(jarParams);
		executeBasic.setUserName(userName);
		exec.execute(executeBasic);
	}
}
