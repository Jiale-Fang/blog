package pers.fjl.crawler.hadoop.mapreduce.partition;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * 驱动类
 */
public class WordCountDriver {
    public static void main(String[] args) throws Exception {
        //创建配置对象
        Configuration conf = new Configuration();
        conf.set("mapreduce.framework.name","yarn");
        //构建job作业实例
        Job job = Job.getInstance(conf, WordCountDriver.class.getSimpleName());
        //设置mr程序主类
        job.setJarByClass(WordCountDriver.class);
        job.setMapperClass(WordCountMapper.class);
        job.setReducerClass(WordCountReducer.class);

        //指定mapper程序输出的key value
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(LongWritable.class);

        //指定reduce输出的key value
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(LongWritable.class);

        Path input = new Path(args[0]);
        Path output = new Path(args[1]);

        //配置输入输出路径
        FileInputFormat.setInputPaths(job, input);
        FileOutputFormat.setOutputPath(job, output);

        //判断输出路径是否存在
        FileSystem fs = FileSystem.get(conf);
        if (fs.exists(output)){
            fs.delete(output,true);
        }

        //提交作业
        boolean resultFlag = job.waitForCompletion(true);
        System.exit(resultFlag ? 0 : 1);

    }
}
