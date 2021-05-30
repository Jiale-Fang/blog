package pers.fjl.crawler.hadoop.mapreduce.partition;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * KEYIN：表示map阶段输入kv中的k类型，在默认组件下，是起始位置偏移量，因此是LongWritable
 * VALUEIN：表示map阶段输入kv中的v类型， 是Text
 * k：每一行的起始位置偏移量，通常无意义
 * v:这一行的文本内容
 * KEYOUT: 是单词名
 * VALUEOUT: 是词频（数字）
 */
public class WordCountMapper extends Mapper<LongWritable, Text, Text, LongWritable> {

    private Text outKey = new Text();
    private static final LongWritable outValue = new LongWritable(1);

    /*
    这是核心方法，也是具体逻辑实现的方法
    注意，该方法被调用的次数和输入的kv有关，每读取返回一个kv就调用一次map方法进行业务处理
     */
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        //拿取一行数据转换成string
        String line = value.toString();
        //根据分隔符切割
        String[] words = line.split("\\s+");
        //遍历数组
        for (String word : words) {
            outKey.set(word);
            //将每个单词标记为 输出为<单词，1>
            context.write(outKey, outValue);
        }
    }
}
