package controllers;

import play.*;
import play.mvc.*;
import com.google.gson.GsonBuilder;
import com.sun.xml.internal.txw2.Document;
import java.io.File;
import service.impl.FileControl;
import service.inter.ReturnSystemTime;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.*;
import models.*;

public class FileOperationSample extends Controller {

	public static void index() throws Exception {
		render();
	}

	/*
	 * 返回到CopyToHdfs上传参数的页面
	 */
	public static void CopyToHdfs() {
		render();
	}

	/*
	 * 接收CopyToHdfs上传文件及路径参数
	 */
	public static void CopyToHdfsGetPara(File hFile, String hPath) // CopyToHdfs
																	// 功能里面获取源文件和目标地址
	{
		try {
			FileControl user = new FileControl();
            user.CopyToHdfs(hFile.toString(), hPath);
			if (hFile != null) {
				System.out.println("i am the server");
				
				//String dPath = "hdfs://localhost:9000/home/" + User.name + "/";
				if (hPath == null) {
					user.CopyToHdfs(hFile.toURI(), "");
					
					
				} else {
					if(user.CopyToHdfs(hFile.toURI(), hPath))
						renderText("文件上传成功");
					else
					{
						renderText("文件上传失败");
					}
				}
				
			//	renderText(hFile.getName().toString() + "******" + hPath);
				// renderText(filePath);

			} 
						} catch (Exception e) {renderText("文件上传失败");

							
			System.err.println("Exception error");
		}
	}

	/*
	 * 返回CreateHdfsFile上传参数页面
	 */
	public static void CreateHdfsFile() {
		render();
	}

	/*
	 * 接收CreateHdfsFileGetPara上传路径参数
	 */
	public static void CreateHdfsFileGetPara(String hPath) {
		try {
			FileControl user = new FileControl();
			user.setUsername(User.name);
           
			if (user.CreateHdfsFile(hPath)){
				
				renderText("文件创建成功"); 
				
			} else
				renderText("文件创建失败"); 
		} catch (Exception e) {
			System.err.println("Exception error");
		}
	
		//redirect("/Application/authenticate?userName='+User.name+'&password='+User.password+");
		//"@{Topics.show(topic.forum.id, topic.id)}".put("id", User.ID);
	
		
	}

	/*
	 * 返回DeleteFile上传参数页面
	 */
	public static void DeleteFile() {
		
	}

	/*
	 * 接收DeleteFile上传文件参数
	 */
	public static void DeleteFileGetPara(String hFile) {
		try {
			FileControl user = new FileControl();
            user.setUsername(User.name);
			if (user.DeleteFile(hFile)) {
				
				renderText("文件删除成功"); 
				

			} else
				 renderText("文件删除失败"); 
		} catch (Exception e) {
			System.err.println("Exception error");
		}
		// render("RedMap/index.html");测试代码
	}

	/*
	 * 返回RenameHdfsFile上傳參數頁面
	 */
	public static void RenameHdfsFile() {
		render();
	}

	/*
	 * 接收RenameHdfsFile上傳路径參數
	 */
	public static void RenameHdfsFileGetPara(String fromPath,String toPath) {
		try {
			FileControl user = new FileControl();

			if (user.RenameHdfsFile(fromPath, toPath)) {
				 user.setUsername(User.name);

				renderText("文件重命名成功");  } 
			else {
					 renderText("文件重命名失败"); }
					  
					  
				//renderText(hFile.getName().toString() + "******" + hPath);
				// renderText(filePath);

			
		} catch (Exception e) {
			System.err.println("Exception error");
		}
		
	}

	/*
	 * 返回FileSystemCat上傳參數頁面
	 */
	public static void FileSystemCat() {
		try {
			FileControl user = new FileControl();
			 user.setUsername(User.name);

			String tarUri = "hdfs://localhost:9000/home/" + User.name + "/";
			String Files[] = user.FileSystemCat(tarUri);
			List<String> files = new ArrayList<String>(Files.length);
			for (int i = 0; i < Files.length; i++) {
				files.add(Files[i]);
			}
			render(Files);

		} catch (Exception e) {
			System.err.println("Exception error");
		}

		render();
	}

	/*
	 * 接收FileSystemCat上傳路径參數
	 */
	public static void FileSystemCatGetPara(String hPath) {
		FileControl fs = new FileControl();
		// String []list=fs.FileSystemCat(hPath);
		String[] list = { "hdfs://localhost:9000/home/amber/input", "hdfs://localhost:9000/home/amber/routeCountOutput" };
		List<String> files = new ArrayList<String>(list.length);
		for (int i = 0; i < list.length; i++) {
			files.add(list[i]);
		}
		render(files);

	}

	/*
	 * 返回ListStatus上傳參數頁面
	 */
	public static void ListStatus() {
		render();
	}

	/*
	 * 接收ListStatus上傳路径參數
	 */
	public static void ListStatusGetPara(String hPath) {
		String[]str= {hPath};
		FileControl fs = new FileControl();
		// String []list=fs.FileSystemCat(hPath);
		String[] list = fs.ListStatus(str);
		List<String> files = new ArrayList<String>(list.length);
		for (int i = 0; i < list.length; i++) {
			files.add(list[i]);
		}
		render(files);
	
	}

	/*
	 * 接收getHdfsFile上传参数页面
	 */
	public static void getHdfsFile() {

		render();
	}

	/*
	 * 接收getHdfsFile上传Hdfs文件以及本地文件路径参数
	 */
	public static void getHdfsFileGetPara(String sPath, String dPath) {
		
		 try { FileControl fs = new FileControl(); 
		 fs.setUsername(User.name);
		 //sPath ="hdfs://localhost:9000/home/amber/" + sPath; 
		 if
		 (fs.getHdfsFile(sPath, dPath)) {
			 renderText("文件下载成功"); 
		 } else {
		  renderText("文件下载失败");}
		  
		  } catch (Exception e) { System.err.println("Exception error"); }
		 
	
		 // flash.error("文件下载成功"); render("/Users/show.html");
	
	}
}
