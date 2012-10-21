package preset.tool;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import service.impl.FileControl;

public class ToXML {
	/**
	 * 记录临时存放文件地址
	 */
	private String outputPath = null;

	/**
	 * 设置本地输出路径
	 * 
	 * @param path
	 *            本地路径
	 */
	public void setOutputPath(String path) {
		outputPath = new String(path);
	}

	/**
	 * 记录hdfs上传地址
	 */
	private String uploadPath = null;

	/**
	 * 设置hdfs上传路径
	 * 
	 * @param path
	 *            hdfs上传路径
	 */
	public void setUploadPath(String path) {
		uploadPath = new String(path);
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

	// void run(Record[] records, int size) {
	void run(Object[] keys, Object[] values) {
		// void run(Record[] records , int size) {
		assert (outputPath != null);
		assert (uploadPath != null);
		File file = new File(outputPath + ".xml");
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

			writer.write("<chart>");
			writer.newLine();
			writer.write("<categories>");
			writer.newLine();
			int i = 0;
			Object[] newkeys=new String[keys.length];
			Object[] newvalues=new Object[values.length];
			
			String[] mainpath={
					"1-2+1-6",
					"1-6+1-8",
					"1-8+1-10",
					"1-10+1-12",
					"1-12+1-14",
					"1-14+1-16",
					"1-16+1-18",
					"1-18+1-20",
					"1-20+1-24",
					"1-24+1-26",
					"1-26+1-28",
					"1-28+1-30",
					"1-30+1-36",
					"1-36+1-38",
					"1-38+1-40",
					"1-40+1-37",
					"1-37+1-35",
					"1-35+1-33",
					"1-33+1-29",
					"1-29+1-27",
					"1-27+1-25",
					"1-25+1-23",
					"1-23+1-19",
					"1-19+1-17",
					"1-17+1-15",
					"1-15+1-13",
					"1-13+1-11",
					"1-11+1-9",
					"1-9+1-7",
					"1-7+1-5",
					"1-5+1-2",
};          
			int j=0,k=0,m=0;
			
			while(j<mainpath.length&&m<keys.length)
			{
				for( k=0;k<keys.length;k++)
				{
					if(keys[k].equals(mainpath[j]))
					{
						newkeys[m]=keys[k];
						newvalues[m]=values[k];
						m++;
						break;
					}
				}
				
				j++;
			}
			Object []newKeys=new String[m];
			Object []newValues=new Object[m];
			for(int p=0;p<m;p++)
			{
				newKeys[p]=newkeys[p];
				newValues[p]=newvalues[p];
			}
			
			while (i < keys.length) {
				writer.write("<item>"+newKeys[i]+"</item>");
				writer.newLine();
				i++;
			}
			writer.write("</categories>");
			writer.newLine();
			writer.write("<series>");
			writer.newLine();
			writer.write("<name>");
			writer.write(taskName);
			writer.write("</name>");
			writer.newLine();
			writer.write("<xname>"+xName+"</xname>");
			writer.newLine();
			writer.write("<yname>"+yName+"</yname>");
			writer.newLine();
			writer.write("<data>");
			writer.newLine();
			i = 0;
			while (i < values.length) {
				writer.write("<point>"+String.valueOf(newValues[i])+"</point>");
				writer.newLine();
				i++;
			}
			writer.write("</data>");
			writer.newLine();
			writer.write("</series>");
			writer.newLine();
			writer.write("</chart>");
			writer.newLine();
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
		FileControl fc = new FileControl();
		fc.setUsername(userName);
		// System.out.println(uploadPath+".xml");
		fc.CopyToHdfs(outputPath + ".xml", uploadPath + ".xml");

	}

}
