package service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.OutputStream;
import java.io.FileInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URI;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.util.Progressable;
import org.apache.hadoop.util.RunJar;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.BlockLocation;
import org.apache.hadoop.fs.FileUtil;

import preset.tool.PresetValue;

/**
 * hdfs文件操作类，负责对hdfs上文件进行管理
 * 
 * @author amber
 * 
 */
public class FileControl {
	/**
	 * 记录每次操作的执行者
	 */
	private String userName = null;

	/**
	 * 设置用户名
	 * 
	 * @param name
	 *            用户名
	 */
	public void setUsername(String name) {
		userName = name;
	}

	/**
	 * 返回完整的hdfs路径
	 * 
	 * @param path
	 *            用户相对路径
	 * @return 完整的hdfs路径
	 */
	public String fullHdfsPath(String path) {
		PresetValue val = new PresetValue();
		if (userName != null)
			return val.hdfsPath + "/" + userName + "/" + path;
		return val.hdfsPath + "/" + path;
	}

	/**
	 * 查看hdfs文件内容
	 * 
	 * @param tarUri
	 *            要查看的目标地址
	 * @return String[]类型的内容
	 */
	public String[] FileSystemCat(String tarUri) {
		String uri = fullHdfsPath(tarUri);
		Configuration conf = new Configuration();
		FileSystem fs = null;
		try {
			fs = FileSystem.get(URI.create(uri), conf);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		InputStream in = null;
		// 暂时假设string的行数不超过255
		String[] returnString = new String[255];
		int SLength = 0;
		try {
			in = fs.open(new Path(uri));
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			returnString[SLength++] = br.readLine().toString();
			// IOUtils.copyBytes(in, System.out, 4096, false);// IOUtils
			// //
			// 类，可以在输入流和输出流之间复制数据（这里的in和System.out，4096指复制的缓冲区大小，false指复制后选择自行关闭输入流
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			IOUtils.closeStream(in);
		}
		String[] finalReturn = new String[SLength];
		for (int i = 0; i < SLength; i++) {
			finalReturn[i] = returnString[i];
		}
		return finalReturn;
	}

	/**
	 * 以本地路径形式复制文件到hdfs
	 * 
	 * @param srcFile
	 *            上传文件本地路径
	 * @param dstFile
	 *            目标文件hdfs路径
	 * @return true为成功，false为失败
	 */
	public boolean CopyToHdfs(String srcFile, String dstFile) {
		Configuration config = new Configuration();
		dstFile = fullHdfsPath(dstFile);
		FileSystem hdfs = null;
		try {
			hdfs = FileSystem.get(URI.create(dstFile), config);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		Path srcPath = new Path(srcFile);
		Path dstPath = new Path(dstFile);
		try {
			hdfs.copyFromLocalFile(srcPath, dstPath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 以远程URI形式复制文件到hdfs
	 * 
	 * @param srcpath
	 *            上传文件URI地址
	 * @param dstFile
	 *            目标文件hdfs路径
	 * @return true为成功，false为失败
	 */
	public boolean CopyToHdfs(URI srcpath, String dstFile) // 从本地文件系统复制文件至Hdfs
	{
		Configuration config = new Configuration();
		dstFile = fullHdfsPath(dstFile);
		FileSystem hdfs = null;
		try {
			hdfs = FileSystem.get(URI.create(dstFile), config);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		Path path = new Path(srcpath);
		Path dstPath = new Path(dstFile);
		try {
			hdfs.copyFromLocalFile(path, dstPath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 在hdfs上创建空白文件
	 * 
	 * @param fileName
	 *            目标hdfs路径
	 * @return true为成功，false为失败
	 */
	public boolean CreateHdfsFile(String fileName) {
		Configuration conf = new Configuration();
		fileName = fullHdfsPath(fileName);
		FileSystem fs;
		try {
			fs = FileSystem.get(URI.create(fileName), conf);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		Path path = new Path(fileName);
		try {
			FSDataOutputStream outputStream = fs.create(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 重命名hdfs上文件
	 * 
	 * @param FromFileName
	 *            目标hdfs路径
	 * @param ToFileName
	 *            更改成的路径及名称
	 * @return true为成功，false为失败
	 */
	public boolean RenameHdfsFile(String fromFileName, String toFileName) {
		Configuration conf = new Configuration();
		fromFileName = fullHdfsPath(fromFileName);
		toFileName = fullHdfsPath(toFileName);
		FileSystem fs;
		try {
			fs = FileSystem.get(URI.create(fromFileName), conf);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		Path fromPath = new Path(fromFileName);
		Path toPath = new Path(toFileName);
		try {
			fs.rename(fromPath, toPath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;

	}

	/**
	 * 从hdfs上删除文件
	 * 
	 * @param fileName
	 *            删除文件的hdfs地址
	 * @return true为成功，false为失败
	 */
	public boolean DeleteFile(String fileName) {
		Configuration config = new Configuration();
		fileName = fullHdfsPath(fileName);
		FileSystem hdfs;
		try {
			hdfs = FileSystem.get(URI.create(fileName), config);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		Path path = new Path(fileName);
		try {
			hdfs.delete(path, true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 判断目标文件是否存在
	 * 
	 * @param FileName
	 *            目标文件hdfs地址
	 * @return 存在返回true，不存在返回false
	 */
	public boolean IfFileExits(String fileName) {
		Configuration conf = new Configuration();
		fileName = fullHdfsPath(fileName);
		FileSystem fs;
		try {
			fs = FileSystem.get(URI.create(fileName), conf);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		Path path = new Path(fileName);
		try {
			fs.exists(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 显示多组Hdfs路径的文件信息
	 * 
	 * @param tarURI
	 *            要查询的目标hdfs地址
	 * @return String[] 存在的文件地址
	 */
	public String[] ListStatus(String tarURI[]) {
		Path[] paths = new Path[tarURI.length];
		for (int i = 0; i < paths.length; i++) {
			tarURI[i] = fullHdfsPath(tarURI[i]);
			paths[i] = new Path(tarURI[i]);
		}
		String uri = tarURI[0];
		Configuration conf = new Configuration();
		FileSystem fs = null;
		try {
			fs = FileSystem.get(URI.create(uri), conf);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		FileStatus[] status = null;
		try {
			status = fs.listStatus(paths);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Path[] listedPaths = FileUtil.stat2Paths(status);
		String[] outputPath = new String[listedPaths.length];
		// for(Path p: listedPaths){
		// System.out.println(p);
		// }
		for (int i = 0; i < listedPaths.length; i++) {
			outputPath[i] = listedPaths[i].toString();
		}
		return outputPath;
	}

	/**
	 * 从hdfs上下载文件至本地路径
	 * 
	 * @param src
	 *            hdfs文件路径
	 * @param dst
	 *            复制到的本地的路径名称
	 * @return 执行成功true，否则false
	 */
	public boolean getHdfsFile(String src, String dst) {
		Configuration config = new Configuration();
		src = fullHdfsPath(src);
		FileSystem hdfs = null;
		try {
			hdfs = FileSystem.get(URI.create(src), config);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		Path srcPath = new Path(src);
		Path dstPath = new Path(dst);
		try {
			hdfs.copyToLocalFile(srcPath, dstPath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
}