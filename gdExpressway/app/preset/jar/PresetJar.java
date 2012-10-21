package preset.jar;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import preset.tool.PostProcessing;
import preset.tool.PresetValue;

/**
 * 预置类作业管理器，实例化后调用内部函数
 * 
 * @author amber
 * 
 */
public class PresetJar {
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
	private String taskName = null;

	/**
	 * 设置任务名称
	 * 
	 * @param name
	 */
	public void setTaskName(String name) {
		taskName = new String(name);
	}

	/**
	 * 记录任务ID
	 */
	private int taskID = -1;

	/**
	 * 设这任务ID
	 * 
	 * @param id
	 *            任务ID
	 */
	public void setTaskID(int id) {
		taskID = id;
	}
	/**
	 * 记录x轴名称
	 */
	private String xName = null;

	/**
	 * 设置x轴名称
	 * 
	 * @param name
	 */
	public void setXName(String name) {
		xName = new String(name);
	}
	/**
	 * 记录Y轴名称
	 */
	private String yName = null;

	/**
	 * 设置x轴名称
	 * 
	 * @param name
	 */
	public void setYName(String name) {
		yName = new String(name);
	}

	/**
	 * 多线程管理器
	 */
	private static ExecutorService exec = Executors.newCachedThreadPool();

	/**
	 * 返回完整的hdfs路径
	 * 
	 * @param path
	 *            用户相对路径
	 * @return 完整的hdfs路径
	 */
	public String fullHdfsPath(String path) {
		if (userName != null)
			return userName + "/" + path;
		return "/" + path;
	}

	/**
	 * 指定时间段，统计各个路段车流量
	 * 
	 * @param args
	 *            全部参数不需要包含hdfs地址，第一个参数为输入文件路径，第二个为输出文件路径，第三个为开始日期，第四个为结束日期，
	 *            日期格式为YYYYMMDD
	 * @return
	 */
	public boolean rountCountOnDate(String[] args) {

		class RountCountOnDateBasic implements Runnable {
			/**
			 * jar文件参数
			 */
			String[] jarParams;
			String outputPath;

			/**
			 * 记录jar参数
			 * 
			 * @param jarParam
			 *            jar参数
			 */
			RountCountOnDateBasic(String[] jarParam) {
				outputPath = new String(jarParam[1]);
				jarParams = new String[jarParam.length + 4];
				for (int i = 0; i < jarParam.length; i++)
					jarParams[i] = jarParam[i];
				jarParams[jarParam.length] = PresetValue.jobTrackerIp + ":"
						+ PresetValue.jobTrackerPort;
				jarParams[jarParam.length + 1] = PresetValue.hdfsHead;
				jarParams[jarParam.length + 2] = userName;
				jarParams[jarParam.length + 3] = String.valueOf(taskID);
			}

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					RouteCount.main(jarParams);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				PostProcessing pp = new PostProcessing();

				pp.setTaskID(taskID);
				pp.setUserName(userName);
				pp.setTaskName(taskName);
				pp.setXName(xName);
				pp.setYName(yName);
				assert (taskName != null);
				pp.routeCount(outputPath + "/part-r-00000");
			}
		}

		if (args.length < 4)
			return false;
		PresetValue val = new PresetValue();
		RountCountOnDateBasic RCOD = new RountCountOnDateBasic(args);
		exec.execute(RCOD);

		return true;

	}

	/**
	 * 统计固定时间内各个路段的车流量
	 * 
	 * @param args
	 *            五个参数，第一个数据地址，第二个输出地址，第三个开始时间，第四个结束时间，时间格式YYYYMMDD
	 *            输出路段:车型-车流量,第五个为指定要统计的路段ID
	 * @return
	 */
	public boolean modelsCount(String[] args) {
		class ModelsCountBasic implements Runnable {
			/**
			 * jar文件参数
			 */
			String[] jarParams;
			String outputPath;

			/**
			 * 记录jar参数
			 * 
			 * @param jarParam
			 *            jar参数
			 */
			ModelsCountBasic(String[] jarParam) {
				outputPath = new String(jarParam[1]);
				jarParams = new String[jarParam.length + 4];
				for (int i = 0; i < jarParam.length; i++)
					jarParams[i] = jarParam[i];
				jarParams[jarParam.length] = PresetValue.jobTrackerIp + ":"
						+ PresetValue.jobTrackerPort;
				jarParams[jarParam.length + 1] = PresetValue.hdfsHead;
				jarParams[jarParam.length + 2] = userName;
				jarParams[jarParam.length + 3] = String.valueOf(taskID);
			}

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					ModelsCount.main(jarParams);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				PostProcessing pp = new PostProcessing();

				pp.setTaskID(taskID);
				pp.setUserName(userName);
				pp.setTaskName(taskName);
				pp.setXName(xName);
				pp.setYName(yName);
				assert (taskName != null);
				pp.modelDivided(outputPath + "/part-r-00000", jarParams[4]);
			}
		}
		if (args.length < 5)
			return false;
		PresetValue val = new PresetValue();
		ModelsCountBasic mcb = new ModelsCountBasic(args);
		exec.execute(mcb);

		return true;

	}

	/**
	 * 统计固定时间内各个路段的平均车速
	 * 
	 * @param args
	 *            统计固定时间内各个路段的平均用时
	 *            五个参数，第一个数据地址，第二个输出地址，第三个开始时间，第四个结束时间，时间格式YYYYMMDD 第五个为指定的车型
	 * @return
	 */
	public boolean averageSpeedCount(String[] args) {
		class AverageSpeedCountBasic implements Runnable {
			/**
			 * jar文件参数
			 */
			String[] jarParams;
			String outputPath;

			/**
			 * 记录jar参数
			 * 
			 * @param jarParam
			 *            jar参数
			 */
			AverageSpeedCountBasic(String[] jarParam) {
				outputPath = new String(jarParam[1]);
				jarParams = new String[jarParam.length + 4];
				for (int i = 0; i < jarParam.length; i++)
					jarParams[i] = jarParam[i];
				jarParams[jarParam.length] = PresetValue.jobTrackerIp + ":"
						+ PresetValue.jobTrackerPort;
				jarParams[jarParam.length + 1] = PresetValue.hdfsHead;
				jarParams[jarParam.length + 2] = userName;
				jarParams[jarParam.length + 3] = String.valueOf(taskID);
			}

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					AverageSpeedCount.main(jarParams);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				PostProcessing pp = new PostProcessing();

				pp.setTaskID(taskID);
				pp.setUserName(userName);
				pp.setTaskName(taskName);
				pp.setXName(xName);
				pp.setYName(yName);
				assert (taskName != null);
				pp.averageSpeedCount(outputPath + "/part-r-00000");
			}
		}
		if (args.length < 5)
			return false;
		PresetValue val = new PresetValue();
		AverageSpeedCountBasic ascb = new AverageSpeedCountBasic(args);
		exec.execute(ascb);

		return true;
	}
}
