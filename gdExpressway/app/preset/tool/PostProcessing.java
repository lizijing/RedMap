package preset.tool;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;

import com.sun.xml.bind.v2.runtime.Name;

import service.impl.FileControl;

/**
 * 后期处理管理器
 * 
 * @author amber
 * 
 */
public class PostProcessing {
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
	 * 设置任务ID
	 * 
	 * @param id
	 *            任务ID
	 */
	public void setTaskID(int id) {
		taskID = id;
	}

	/**
	 * 路径复原功能
	 * 
	 * @param readyFile
	 *            目标文件hdfs地址
	 */
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
	public void routeCount(String readyFile) {

		FileControl fc = new FileControl();
		fc.setUsername(userName);
		BufferedReader read = null;
		File file;
		RecoverPath rp = new RecoverPath();
		fc.getHdfsFile(readyFile, PresetValue.tempFile + String.valueOf(taskID));
		file = new File(PresetValue.tempFile + String.valueOf(taskID));
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		Map<String, Integer> mapInt =  new HashMap<String, Integer>();
		try {
			InputStreamReader isr = new InputStreamReader(new FileInputStream(
					file), "UTF-8");
			read = new BufferedReader(isr);
			String recordread;
			while ((recordread = read.readLine()) != null) {
				String[] keyValue = recordread.split("	");
				Integer num  = Integer.parseInt(keyValue[1]);
				String tmp = keyValue[0];
				String[] startEndKey = tmp.split("\\+");
				String[] toPuts;
				toPuts = rp.getRoute(startEndKey[0],startEndKey[1]);
				if (toPuts==null) continue;
				for(String toPut:toPuts)
				{
					if (mapInt.get(toPut)==null) mapInt.put(toPut,num);
					else mapInt.put(toPut,mapInt.get(toPut)+num);
				}
			}
		} catch (NumberFormatException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			try {
				read.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		file = new File(PresetValue.tempFile + userName + "_"
				+ String.valueOf(taskID));
		if (!file.exists() != false) {
			try {
				file.createNewFile();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		BufferedWriter writer = null;
		
		@SuppressWarnings("null")
		Object[] keys = mapInt.keySet().toArray();
		Object[] values = mapInt.values().toArray();
		try {
			OutputStreamWriter osw = new OutputStreamWriter(
					new FileOutputStream(file), "UTF-8");
			writer = new BufferedWriter(osw);

			for (int i=0;i<mapInt.size();i++)
			{
				writer.write(keys[i] + "	");
				writer.write(String.valueOf(values[i]));
				writer.newLine();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		fc.CopyToHdfs(
				PresetValue.tempFile + userName + "_" + String.valueOf(taskID),
				readyFile + "-" + userName + "_" + String.valueOf(taskID));
		ToXML tx = new ToXML();
		tx.setTaskName(taskName);
		tx.setTaskID(taskID);
		tx.setUserName(userName);
		tx.setOutputPath(PresetValue.tempFile + userName + "_"
				+ String.valueOf(taskID));
		tx.setUploadPath(readyFile);
		tx.setXName(xName);
		tx.setYName(yName);
		tx.run(keys,values);
	}

	/**
	 * 指定路段车型划分功能
	 * 
	 * @param readyFile
	 *            准备被划分hdfs文件
	 * @param target
	 *            目标路段
	 */
	public void modelDivided(String readyFile, String target) {
		FileControl fc = new FileControl();
		fc.setUsername(userName);
		BufferedReader read = null;
		File file;
		Integer[]  count = new Integer[PresetValue.modelsNum];
		for (int i=0;i<PresetValue.modelsNum;i++)
		{
			count[i] = 0;
		}
		RecoverPath rp = new RecoverPath();
		file = new File(PresetValue.recoverPath);
		// System.out.print(readyFile);
		fc.getHdfsFile(readyFile, PresetValue.tempFile + String.valueOf(taskID));
		file = new File(PresetValue.tempFile + taskID);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Map<String, Integer> mapInt =  new HashMap<String, Integer>();
		try {
			InputStreamReader isr = new InputStreamReader(new FileInputStream(
					file), "UTF-8");
			read = new BufferedReader(isr);
			String recordread;
			while ((recordread = read.readLine()) != null) {
				// System.out.print(record);
				String[] keyValue = recordread.split("	");
				// System.out.print(keyValue[0]);

				String[] tmp = keyValue[0].split(":");
				Integer model = Integer.parseInt(tmp[1]);
				String[] startEndKey = tmp[0].split("\\+");
				Integer num  = Integer.parseInt(keyValue[1]);
				String[] toPuts;
				toPuts = rp.getRoute(startEndKey[0],startEndKey[1]);
				if (toPuts==null) continue;
				for(String toPut:toPuts)
				{
					String[] twoid = toPut.split("\\+");
					if (twoid[0].equals(target)||twoid[1].equals(target)) 
					{
						count[model-1] += num;
						break;
					}
				}
				
			}
		} catch (NumberFormatException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			try {
				read.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		file = new File(PresetValue.tempFile + userName + "_"
				+ String.valueOf(taskID));
		if (!file.exists() != false) {
			try {
				file.createNewFile();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		BufferedWriter writer = null;
		try {
			OutputStreamWriter osw = new OutputStreamWriter(
					new FileOutputStream(file), "UTF-8");
			writer = new BufferedWriter(osw);
			int i = 0;
			while (i < PresetValue.modelsNum) {
				writer.write(String.valueOf(i + 1) + "	");
				writer.write(String.valueOf(count[i]));
				writer.newLine();
				i++;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		String[] name = new String[PresetValue.modelsNum];
		for (int i = 0; i < PresetValue.modelsNum; i++) {
			name[i] = String.valueOf(i + 1);
		}
		fc.CopyToHdfs(
				PresetValue.tempFile + userName + "_" + String.valueOf(taskID),
				readyFile + "-" + userName + "_" + String.valueOf(taskID));
		ToXML tx = new ToXML();
		tx.setTaskName(taskName);
		tx.setTaskID(taskID);
		tx.setUserName(userName);
		tx.setOutputPath(PresetValue.tempFile + userName + "_"
				+ String.valueOf(taskID));
		tx.setUploadPath(readyFile);
		tx.setXName(xName);
		tx.setYName(yName);
		tx.run(name, count);
	}

	public void averageSpeedCount(String readyFile) {
		// TODO Auto-generated method stub
		FileControl fc = new FileControl();
		fc.setUsername(userName);
		BufferedReader read = null;
		File file;
		RecoverPath rp = new RecoverPath();
		file = new File(PresetValue.recoverPath);
		String[] ids = new String[PresetValue.recoverPathLine];
		int[] nums = new int[PresetValue.recoverPathLine];
		int[] speeds = new int[PresetValue.recoverPathLine];
		for (int i = 0; i < PresetValue.recoverPathLine; i++) {
			nums[i] = 0;
			speeds[i] = 0;
		}

		file = new File(PresetValue.ratePath);
		Map<String, Integer> rateMap = new HashMap<String, Integer>();
		Map<String, Integer> speedMap = new HashMap<String, Integer>();
		Map<String, Integer> numsMap = new HashMap<String, Integer>();
		String recordread = "nothing";
		try {
			InputStreamReader isr = new InputStreamReader(new FileInputStream(
					file), "UTF-8");
			read = new BufferedReader(isr);
			
			while ((recordread = read.readLine()) != null && (!recordread.equals("") )) {
//				System.out.println(recordread);
				String[] keyValue = recordread.split(" ");
//				System.out.println(keyValue[0]);
				rateMap.put(keyValue[0], Integer.parseInt(keyValue[1]));
			}
		} catch (NumberFormatException | IOException e1) {
			// TODO Auto-generated catch block
//			System.out.println(recordread);
			e1.printStackTrace();
		} finally {
			try {
				read.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		fc.getHdfsFile(readyFile, PresetValue.tempFile + String.valueOf(taskID));
		
		file = new File(PresetValue.tempFile + String.valueOf(taskID));
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		try {
			InputStreamReader isr = new InputStreamReader(new FileInputStream(
					file), "UTF-8");
			read = new BufferedReader(isr);
			while ((recordread = read.readLine()) != null) {
				
				String[] keyValue = recordread.split("	");
				
				String tmp = keyValue[0];
				try
				{
					int mile = rateMap.get(keyValue[0]);
					String[] startEndKey = tmp.split("\\+");
					String[] numsTime = keyValue[1].split("-");
					Integer people = Integer.parseInt(numsTime[0]); 
					Integer speed =  (int) (((double) mile)
							/ Integer.parseInt(numsTime[1]) * 60 / 1000);
					String[] toPuts;
					toPuts = rp.getRoute(startEndKey[0],startEndKey[1]);
					if (toPuts==null) continue;
					for(String toPut:toPuts)
					{
						if (speedMap.get(toPut)==null) 
						{
							speedMap.put(toPut,speed);
							numsMap.put(toPut, people);
						}
						else
						{
							speedMap.put(toPut,(speed*people+speedMap.get(toPut)*numsMap.get(toPut))/(numsMap.get(toPut)+people));
							numsMap.put(toPut, people+numsMap.get(toPut));
						}
					}
				} catch (Exception e) {
					System.out.println("Not GS data!");					
				}
			}
		} catch (NumberFormatException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} finally {
			try {
				read.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		file = new File(PresetValue.tempFile + userName + "_"
				+ String.valueOf(taskID));
		if (!file.exists() != false) {
			try {
				file.createNewFile();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		BufferedWriter writer = null;
		Object[] keys = speedMap.keySet().toArray();
		Object[] values = speedMap.values().toArray();
		try {
			OutputStreamWriter osw = new OutputStreamWriter(
					new FileOutputStream(file), "UTF-8");
			writer = new BufferedWriter(osw);

			for (int i=0;i<speedMap.size();i++)
			{
				writer.write(keys[i] + "	");
				writer.write(String.valueOf(values[i]));
				writer.newLine();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		fc.CopyToHdfs(
				PresetValue.tempFile + userName + "_" + String.valueOf(taskID),
				readyFile + "-" + userName + "_" + String.valueOf(taskID));
		ToXML tx = new ToXML();
		tx.setTaskName(taskName);
		tx.setTaskID(taskID);
		tx.setUserName(userName);
		tx.setOutputPath(PresetValue.tempFile + String.valueOf(taskID));
		tx.setUploadPath(readyFile);
		tx.setXName(xName);
		tx.setYName(yName);
		tx.run(keys,values);
	}
}
