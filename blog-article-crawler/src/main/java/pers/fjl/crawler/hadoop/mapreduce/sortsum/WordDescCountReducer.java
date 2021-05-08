package pers.fjl.crawler.hadoop.mapreduce.sortsum;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import pers.fjl.crawler.beans.WordCountBean;

import java.io.IOException;

/**
 * 输入正好是map的输出
 */
public class WordDescCountReducer extends Reducer<WordCountBean, Text, Text, WordCountBean> {

    @Override
    protected void reduce(WordCountBean key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
        //使用自定义对象作为key，并且没有重写分组规则，默认是比较对象地址，即每个kv就是一组
        Text outKey = values.iterator().next();
        context.write(outKey,key);
    }
}
