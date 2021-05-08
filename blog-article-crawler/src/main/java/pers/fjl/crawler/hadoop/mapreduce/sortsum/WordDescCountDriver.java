package pers.fjl.crawler.hadoop.mapreduce.sortsum;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import pers.fjl.crawler.beans.WordCountBean;

/**
 * 驱动类
 */
public class WordDescCountDriver {
    public static void main(String[] args) throws Exception {
        //创建配置对象
        Configuration conf = new Configuration();
        conf.set("mapreduce.framework.name","local");
        //构建job作业实例
        Job job = Job.getInstance(conf, WordDescCountDriver.class.getSimpleName());
        //设置mr程序主类
        job.setJarByClass(WordDescCountDriver.class);
        job.setMapperClass(WordDescCountMapper.class);
        job.setReducerClass(WordDescCountReducer.class);

        //指定mapper程序输出的key value
        job.setMapOutputKeyClass(WordCountBean.class);
        job.setMapOutputValueClass(Text.class);

        //指定reduce输出的key value
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(WordCountBean.class);

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
