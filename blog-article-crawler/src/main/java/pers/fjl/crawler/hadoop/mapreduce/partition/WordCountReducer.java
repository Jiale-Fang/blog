package pers.fjl.crawler.hadoop.mapreduce.partition;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * 输入正好是map的输出
 */
public class WordCountReducer extends Reducer<Text, LongWritable, Text, LongWritable> {

    private LongWritable outValue = new LongWritable();
    /*
    处理过程
    1. 排序 根据字典序
    2. 分组 key相同的分成一组
    3. 同一组的要数据组成一个新的kv键值对，调用一次reduce方法。 一个分组调用一次
    新的key是该组共同的key
    新value：是所有value组成的一个迭代器Iterable
     */
    @Override
    protected void reduce(Text key, Iterable<LongWritable> values, Context context) throws IOException, InterruptedException {
        long count = 0;
        //遍历该组的value
        for (LongWritable value : values) {
            count += value.get();
        }
        outValue.set(count);
        //最终使用上下文对象输出
        context.write(key,outValue);
    }
}
