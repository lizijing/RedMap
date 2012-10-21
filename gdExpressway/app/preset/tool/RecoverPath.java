package preset.tool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;



/**
 * 利用路径文件还原原来路径，向static方法getRoute()传递开始点和结束点，返回String[]路过站点
 * @author amber
 *
 */
public class RecoverPath {

	Boolean[][] load;
	RecoverPath()
	{
		load = readRecover();
	}
	public static Boolean[][] readRecover() {
		File file = new File(PresetValue.recoverPath);
		Boolean [][] load = new Boolean[PresetValue.recoverPathLine][PresetValue.recoverPathLine];
		BufferedReader read = null;
		try {
			// BufferedReader br = new BufferedReader(new FileReader(file));
			InputStreamReader isr = new InputStreamReader(new FileInputStream(
					file), "UTF-8");
			read = new BufferedReader(isr);
			String recordread;
			for (int i=0;i<PresetValue.recoverPathLine;i++)
			{
				recordread = read.readLine();
//				if (recordread.startsWith("1")) load[i][0] = true;
				for (int j=0;j<PresetValue.recoverPathLine;j++)
				{
					if (recordread.substring(2*j, 2*j+1).equals("1")) load[i][j] = true;
					else load[i][j] = false;
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				read.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return load;
	}
	Boolean isGS(String check)
	{
		String[] tmp = check.split("-");
		if (Integer.parseInt(tmp[0])==1) return true;
		else return false;
	}
	public  String[] getRoute(String startKey,String endKey)
	{
		if (!(isGS(startKey)&&isGS(endKey))) return null;
		String[] tmp = startKey.split("-");
		Integer start = Integer.parseInt(tmp[1]);
		tmp = endKey.split("-");
		Integer end = Integer.parseInt(tmp[1]);
		Integer[] route = new Integer[PresetValue.recoverPathLine];
		Integer[] last = new Integer[PresetValue.recoverPathLine];
		Boolean[] flag = new Boolean[PresetValue.recoverPathLine];
//		Integer[] next = new Integer[PresetValue.recoverPathLine];
//		next[0] = 1;
		route[0] = 2;
		route[1] = start;
		for (int i=0;i<PresetValue.recoverPathLine;i++)
		{
			flag [i]  = true;
		}
		int now = start;
		flag[now-1] = false;
		int theone = 2;
		while (!load[now-1][end-1])
		{
			for (int i=1;i<=PresetValue.recoverPathLine;i++)
			{
				if (load[now-1][i-1]&&flag[i-1])
				{
					last[i-1] = now;
					flag[i-1] = false;
					route[route[0]] = i ;
					route[0] ++;
				}
			}
			if (theone<route[0]) {
				now = route[theone];
				theone++;
			}
			else break;
		}
		if (!load[now-1][end-1]) return null;
		Integer[] result = new Integer[PresetValue.recoverPathLine];
		result[1] = end;
		result[2] = now;
		result[0] = 3;
		
		while(now!=start)
		{
			now = last[now-1];
			result[result[0]] = now;
			result[0]++;
		}
		
		String[] reresult = new String[result[0]-2];
		for (int i=1;i<result[0]-1;i++)
			reresult[i-1] = "1-" + String.valueOf(result[result[0]-i])+"+1-"+String.valueOf(result[result[0]-i-1]);
		return reresult;
	}
	
	public static void main(String[] args)
	{
		RecoverPath rp = new RecoverPath();
		String[] route = rp.getRoute("1-21","1-40");
		for (int i=0;i<route.length;i++)
		{
			System.out.println(route[i]);
		}
	}
	
}
