package preset.tool;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.JobID;
import org.apache.hadoop.mapred.JobStatus;
import org.apache.hadoop.mapred.RunningJob;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HadoopJobStatus {
	/**
	 * 根据JobName获取JobID
	 * 
	 * @param jobName_str
	 * @return JobID
	 */
	public static JobID getJobIDByJobName(JobClient jobClient,
			JobStatus[] jobStatus, String jobName_str) {
		JobID jobID = null;
		try {
			for (int i = 0; i < jobStatus.length; i++) {
				RunningJob rj = jobClient.getJob(jobStatus[i].getJobID());
				if (rj.getJobName().trim().equals(jobName_str)) {
					jobID = jobStatus[i].getJobID();
					break;
				}
			}
		} catch (IOException e) {
			System.out.print("getJobID Error!");
		}
		return jobID;
	}

	/**
	 * 
	 * 根据JobID获取Job状态
	 * 
	 * @param jobClient
	 * @param jobStatus
	 * @param jobID
	 * @return RUNNING = 1,SUCCEEDED = 2,FAILED = 3,PREP = 4,KILLED = 5
	 * @throws IOException
	 */
	public static String getStatusByJobID(JobClient jobClient,
			JobStatus[] jobStatus, JobID jobID) throws IOException {
		int status_int = 0;
		jobStatus = jobClient.getAllJobs();
		for (int i = 0; i < jobStatus.length; i++) {
			if (jobStatus[i].getJobID().getId() == jobID.getId()) {
				status_int = jobStatus[i].getRunState();
				break;
			}
		}
		String desc_str = "";
		switch (status_int) {
		case 1:
			desc_str = "RUNNING";
			break;
		case 2:
			desc_str = "SUCCEEDED";
			break;
		case 3:
			desc_str = "FAILED";
			break;
		case 4:
			desc_str = "PREP";
			break;
		case 5:
			desc_str = "KILLED";
			break;
		default:
			break;
		}
		return desc_str;
	}

	/**
	 * 获取正在运行的JobID的列表
	 * 
	 * @param jobClient
	 * @return ArrayList<JobID>
	 */

	public static ArrayList<JobID> getRunningJobList(JobClient jobClient) {
		ArrayList<JobID> runningJob_list = new ArrayList<JobID>();
		JobStatus[] js;
		try {
			js = jobClient.getAllJobs();
			for (int i = 0; i < js.length; i++) {
				if (js[i].getRunState() == JobStatus.RUNNING
						|| js[i].getRunState() == JobStatus.PREP) {
					runningJob_list.add(js[i].getJobID());
				}
			}
		} catch (IOException e) {
			System.out.print("getRunningJobList Error!");
		}
		return runningJob_list;
	}

	/**
	 * 
	 * 获取所有的Job的列表和状态
	 * 
	 * 
	 * @return JSONArray
	 */

	public static JSONArray getAllJobList() {
		JobConf conf = new JobConf();
		conf.set("mapred.job.tracker", PresetValue.jobTrackerIp + ":"
				+ PresetValue.jobTrackerPort);
		JobClient jobClient = null;
		try {
			jobClient = new JobClient(conf);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		JobStatus[] js;
		JSONArray module = new JSONArray();
		try {
			js = jobClient.getAllJobs();
			for (int i = 0; i < js.length; i++) {
				RunningJob rj = jobClient.getJob(js[i].getJobID());
				JSONObject jo = new JSONObject();
				int status_int = js[i].getRunState();
				String desc_str = "";
				switch (status_int) {
				case 1:
					desc_str = "RUNNING";
					break;
				case 2:
					desc_str = "SUCCEEDED";
					break;
				case 3:
					desc_str = "FAILED";
					break;
				case 4:
					desc_str = "PREP";
					break;
				case 5:
					desc_str = "KILLED";
					break;
				default:
					break;
				}
				try {
					jo.put("JobName", rj.getJobName().trim());
					String[] jobXml = jo.get("JobName").toString().split("-");
					jo.put("Status", desc_str);
					jo.put("xmlPath", "."+PresetValue.tempFile + jobXml[1] + ".xml");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					System.out.println("getAllJob Error!");
					e.printStackTrace();
				}
				module.put(jo);
			}
		} catch (IOException e) {

			System.out.print("getRunningJobList Error!");

		}
		return module;
	}
	
	public static JSONArray getJobDetail() {
		JobConf conf = new JobConf();
		conf.set("mapred.job.tracker", PresetValue.jobTrackerIp + ":"
				+ PresetValue.jobTrackerPort);
		JobClient jobClient = null;
		try {
			jobClient = new JobClient(conf);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		JobStatus[] js;
		JSONArray module = new JSONArray();
		try {
			js = jobClient.getAllJobs();
			for (int i = 0; i < js.length; i++) {
				RunningJob rj = jobClient.getJob(js[i].getJobID());
				JSONObject jo = new JSONObject();
				int status_int = js[i].getRunState();
				String desc_str = "";
				switch (status_int) {
				case 1:
					desc_str = "RUNNING";
					break;
				case 2:
					desc_str = "SUCCEEDED";
					break;
				case 3:
					desc_str = "FAILED";
					break;
				case 4:
					desc_str = "PREP";
					break;
				case 5:
					desc_str = "KILLED";
					break;
				default:
					break;
				}
				try {
					jo.put("JobName", rj.getJobName().trim());
					String[] jobXml = jo.get("JobName").toString().split("-");
					jo.put("Status", desc_str);
					jo.put("xmlPath", PresetValue.tempFile + jobXml[1] + ".xml");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					System.out.println("getAllJob Error!");
					e.printStackTrace();
				}
				module.put(jo);
			}
		} catch (IOException e) {

			System.out.print("getRunningJobList Error!");

		}
		return module;
	}
}
