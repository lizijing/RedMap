package service.inter;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

/**
 * hdfs文件操作
 * @author amber
 *
 */
public interface FileControl {
	/**
	 * 查看hdfs文件内容
	 * @param tarUri 要查看的目标URI
	 * @return String[]类型的内容
	 */
	public String[] FileSystemCat(String tarUri);
	/**
	 * 以本地路径形式复制文件到hdfs
	 * @param srcFile 上传文件本地路径
	 * @param dstFile 目标文件hdfs路径
	 * @return true为成功，false为失败
	 */
	public boolean CopyToHdfs(String srcFile, String dstFile);
	/**
	 * 以远程URI形式复制文件到hdfs
	 * @param srcpath 上传文件URI地址
	 * @param dstFile 目标文件hdfs路径
	 * @return true为成功，false为失败
	 */
	public boolean CopyToHdfs(URI srcpath, String dstFile);
	/**
	 * 在hdfs上创建空白文件
	 * @param fileName 目标hdfs路径
	 * @return true为成功，false为失败
	 */
	public boolean CreateHdfsFile(String fileName);
	/**
	 * 重命名hdfs上文件
	 * @param FromFileName 目标hdfs路径
	 * @param ToFileName 更改成的路径及名称
	 * @return true为成功，false为失败
	 */
	public boolean RenameHdfsFile(String FromFileName, String ToFileName);
	/**
	 * 从hdfs上删除文件
	 * @param fileName 删除文件的hdfs地址
	 * @return true为成功，false为失败
	 */
	public boolean DeleteFile(String fileName);
	/**
	 * 判断目标文件是否存在
	 * @param FileName 目标文件hdfs地址
	 * @return 存在返回true，不存在返回false
	 */
	boolean IfFileExits(String FileName);
	/**
	 * 显示多组Hdfs路径的文件信息
	 * @param tarURI 要查询的目标hdfs地址
	 * @return String[] 存在的文件地址
	 */
	String[] ListStatus(String[] tarURI);

}
