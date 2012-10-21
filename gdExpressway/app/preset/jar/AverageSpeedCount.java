package preset.jar;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.util.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.LongWritable;

import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * 统计固定时间内各个路段的平均用时 五个参数，第一个数据地址，第二个输出地址，第三个开始时间，第四个结束时间，时间格式YYYYMMDD  第五个为指定的车型
 * 输出<路段,车流量-时间> 
 * @author amber
 * 
 */
public class AverageSpeedCount {
	
	public static String calculateTime(String startTime, String endTime) {
		int ya, mona, da, ha, ma;
		int yb = 0, monb = 0, db = 0, hb = 0, mb = 0;
		ya = Integer.parseInt(startTime.substring(1, 5));
		mona = Integer.parseInt(startTime.substring(6, 8));
		da = Integer.parseInt(startTime.substring(9, 11));
		ha = Integer.parseInt(startTime.substring(12, 14));
		ma = Integer.parseInt(startTime.substring(15, 17));
		yb = Integer.parseInt(endTime.substring(1, 5));
		monb = Integer.parseInt(startTime.substring(6, 8)); 
		db = Integer.parseInt(endTime.substring(9, 11));
		hb = Integer.parseInt(endTime.substring(12, 14));
		mb = Integer.parseInt(endTime.substring(15, 17));
		long result = (((((yb - ya) * 12 + monb - mona) * 30 + db - da) * 24
				+ hb - ha)
				* 60 + mb - ma);
		return String.valueOf(result);
	}

	public static class TimeMapper extends
			Mapper<LongWritable, Text, Text, Text> {
		@Override
		protected void map(LongWritable key, Text value, Context context)
				throws IOException, InterruptedException {
			String line = value.toString();
			// 读取初始日期何结束日期
			if (line.length() > 9) {
				long x = 0, y = 0, date = Long.parseLong(line.substring(0, 8));
				x = Long.parseLong(context.getConfiguration().get("startTime"));
				y = Long.parseLong(context.getConfiguration().get("endTime"));
				if (x <= date && date <= y) {
					// inAreaNo，inEntryNo，outAreaNo，outEntryNo分别储存入口路段，入口站，出口路段，出口站。last用于辅助记录
					// model 为车型
					int inAreaNo = -1, inEntryNo = -1, outAreaNo = -1, outEntryNo = -1, last = 0, model = -1;
					String startTime = null , endTime = null ;
					int sum = 0, i;

					for (i = 0; i < line.length(); i++) {
						if (line.charAt(i) == ',') {
							sum++;
						}
						if (sum == 2 && inAreaNo == -1) {
							inAreaNo = Integer
									.parseInt(line.substring(last, i));
						}
						if (sum == 3 && inEntryNo == -1) {
							inEntryNo = Integer.parseInt(line
									.substring(last, i));
						}
						if (sum == 4 && startTime == null ) {
							startTime = new String(line.substring(last, i));
						}
						if (sum == 5 && outAreaNo == -1) {
							outAreaNo = Integer.parseInt(line
									.substring(last, i));
						}
						if (sum == 6 && outEntryNo == -1) {
							outEntryNo = Integer.parseInt(line.substring(last,
									i));
						}
						if (sum == 7 && endTime == null ) {
							endTime = new String(line.substring(last, i));
						}
						if (sum == 8 && model == -1) {
							model = Integer.parseInt(line.substring(last, i));
						}
						if (line.charAt(i) == ',') {
							last = i + 1;
						}
					}
					Text a = new Text();
					Text b = new Text();
					// 若数据正常则输出
					if (inAreaNo != 0 && outAreaNo != 0&& model == Long.parseLong(context.getConfiguration().get("selectedModel")) ) 
					{
						a.set(String.valueOf(inAreaNo) + "-"
								+ String.valueOf(inEntryNo) + "+"
								+ String.valueOf(outAreaNo) + "-"
								+ String.valueOf(outEntryNo));
						b.set("1-"+calculateTime(startTime,endTime));
						context.write(a, b);

					}

				}
			}
		}
	}

	public static class HadoopReducer extends
			Reducer<Text, Text, Text, Text> {
		@Override
		protected void reduce(Text key, Iterable<Text> values,
				Context context) throws IOException, InterruptedException {
			long time = 0,nums = 0;
			for (Text value : values) {
				String timeAndNums = value.toString();
				String[] timeNum = timeAndNums.split("-");
				try
				{
					long tmptime = Integer.parseInt(timeNum[1]);
					long tmpnums = Integer.parseInt(timeNum[0]);
					time = (time * nums + tmptime*tmpnums) / (nums+tmpnums);
					nums = nums+tmpnums;
				}catch (Exception e) {
					System.out.println(key.toString()+":"+timeAndNums);
				}
			
			}
			Text val = new Text();
			val.set(String.valueOf(nums)+"-"+String.valueOf(time));
			context.write(key, val);
		}
	}

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		conf.set("startTime", args[2]);
		conf.set("endTime", args[3]);
		conf.set("selectedModel", args[4]);
		conf.set("mapred.job.tracker", args[5]);
		conf.set("fs.default.name", args[6]);
		conf.set("Hadoop.job.user", args[7]);
		Job job = new Job(conf, "AverageSpeedCount-" + args[7] + "_" + args[8]);
		((JobConf) job.getConfiguration()).setJar("AverageSpeedCount.jar");
		job.setJarByClass(AverageSpeedCount.class);
		job.setMapperClass(TimeMapper.class);
		job.setReducerClass(HadoopReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		job.waitForCompletion(true);
	}

}