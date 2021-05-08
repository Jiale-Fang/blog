package pers.fjl.crawler.hadoop.mapreduce.partition;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * 驱动类,使用工具类ToolRunner提交作业
 */
public class WordCountDriver2 extends Configured implements Tool {
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        int status = ToolRunner.run(conf, new WordCountDriver2(), args);
        System.exit(status);
    }

    @Override
    public int run(String[] args) throws Exception {
        //构建job作业实例
        Job job = Job.getInstance(getConf(), WordCountDriver2.class.getSimpleName());
        //设置mr程序主类
        job.setJarByClass(WordCountDriver2.class);
        job.setMapperClass(WordCountMapper.class);
        job.setReducerClass(WordCountReducer.class);

        //指定mapper程序输出的key value
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(LongWritable.class);

        //指定reduce输出的key value
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(LongWritable.class);

        //配置输入输出路径
        FileInputFormat.setInputPaths(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        return job.waitForCompletion(true)? 0 :1;
    }
}
