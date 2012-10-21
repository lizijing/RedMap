package preset.tool;

public class PresetValue {
	/**
	 * 存放RunJar时临时jar包的地址
	 */
	final public static String jarTmpPath = "./tmp/hadoop/tmpJar.jar";
	// final public static String jarTmpPath =
	// "/home/amber/tmp/hadoop/jar/tmpJar.jar";
	/**
	 * 存放路径恢复文件的地址
	 */
	final public static String recoverPath = "./pathRecover.txt";
	/**
	 * 存放里程表文件的地址
	 */
	final public static String ratePath = "./rateRoute.txt";
	/**
	 * 存放hdfs路径恢复文件的地址
	 */
	final public static String hdfsRecoverPath = "/pathRecover.txt";
	/**
	 * 存放ids文件地址
	 */
	final public static String idsPath = "/ids.txt";
	/**
	 * 存放id和名称对应关系的文件
	 */
	final public static String nameIdsPath = "/nameID.txt";
	/**
	 * 路径复原文件最大行数
	 */
	final public static int recoverPathLine = 52;
	/**
	 * 里程表文件最大行数
	 */
	final public static int ratePathLine = 500;
	/**
	 * 存放hdfs地址
	 */
	final public static String hdfsPath = "hdfs://localhost:9000/user/";
	/**
	 * 存放hdfs首地址
	 */
	final public static String hdfsHead = "hdfs://localhost:9000/";
	/**
	 * 存放jobTracker Ip
	 */
	final public static String jobTrackerIp = "localhost";
	/**
	 * 存放jobTracker Port
	 */
	final public static String jobTrackerPort = "9001";
	/**
	 * 临时文件放置地址
	 */
	final public static String tempFile = "./public/temp/";
	// final public static String tempFile = "/home/amber/tmp/hadoop/tempFile/";
	/**
	 * 记录任务ID
	 */
	private static int taskID = 0;

	/**
	 * 返回任务ID
	 * 
	 * @return 任务ID
	 */
	public int getTaskID() {
		taskID++;
		return taskID;
	}

	/**
	 * xml文件的根目录
	 */
	// final public static String xmlHome = "/home/amber/tmp/hadoop/xml/";
	final public static String xmlHome = "/public/data.xml";
	/**
	 * 车型数
	 */
	final public static int modelsNum = 5;
	/**
	 * job的最大数量
	 */
	final public static int jobNum = 100;
}
