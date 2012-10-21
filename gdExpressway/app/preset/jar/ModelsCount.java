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
 * 统计固定时间内各个路段的车流量 四个参数，第一个数据地址，第二个输出地址，第三个开始时间，第四个结束时间，时间格式YYYYMMDD 输出路段:车型-车流量
 * 
 * @author amber
 * 
 */
public class ModelsCount {

	public static class TimeMapper extends
			Mapper<LongWritable, Text, Text, LongWritable> {
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
						if (sum == 5 && outAreaNo == -1) {
							outAreaNo = Integer.parseInt(line
									.substring(last, i));
						}
						if (sum == 6 && outEntryNo == -1) {
							outEntryNo = Integer.parseInt(line.substring(last,
									i));
						}
						if (sum == 8 && model == -1) {
							model = Integer.parseInt(line.substring(last, i));
						}
						if (line.charAt(i) == ',') {
							last = i + 1;
						}
					}
					Text a = new Text();
					// 若数据正常则输出
					if (inAreaNo != 0 && outAreaNo != 0) {

						a.set(String.valueOf(inAreaNo) + "-"
								+ String.valueOf(inEntryNo) + "+"
								+ String.valueOf(outAreaNo) + "-"
								+ String.valueOf(outEntryNo) + ":"
								+ String.valueOf(model));
						context.write(a, new LongWritable(1));

					}

				}
			}
		}
	}

	public static class HadoopReducer extends
			Reducer<Text, LongWritable, Text, LongWritable> {
		@Override
		protected void reduce(Text key, Iterable<LongWritable> values,
				Context context) throws IOException, InterruptedException {
			long sum = 0;
			for (LongWritable value : values) {
				sum += value.get();
			}
			context.write(key, new LongWritable(sum));
		}
	}

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		conf.set("startTime", args[2]);
		conf.set("endTime", args[3]);
		conf.set("mapred.job.tracker", args[5]);
		conf.set("fs.default.name", args[6]);
		conf.set("Hadoop.job.user", args[7]);
		Job job = new Job(conf, "ModelsCount-" + args[7] + "_" + args[8]);
		((JobConf) job.getConfiguration()).setJar("ModelsCount.jar");
		job.setJarByClass(ModelsCount.class);
		job.setMapperClass(TimeMapper.class);
		job.setReducerClass(HadoopReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(LongWritable.class);
		FileInputFormat.addInputPath(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		job.waitForCompletion(true);
	}

}