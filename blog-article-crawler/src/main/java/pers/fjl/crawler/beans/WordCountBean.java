package pers.fjl.crawler.beans;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class WordCountBean implements WritableComparable<WordCountBean> {
    private long count;

    public WordCountBean() {
    }

    public WordCountBean(long count) {
        this.count = count;
    }

    //自己封装对象的set方法 用于对象属性赋值
    public void set(long count) {
        this.count = count;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return count+"\t";
    }

    /**
     *  序列化方法 可以控制将哪些字段序列化出去
     */
    @Override
    public void write(DataOutput out) throws IOException {
        out.writeLong(count);
    }

    /**
     *  反序列化方法 todo 注意反序列化的顺序和序列化一致
     */
    @Override
    public void readFields(DataInput in) throws IOException {
        this.count = in.readLong();
    }

    /**
     *  自定义对象的排序方法
     *  返回结果 0 相等  负数 小于  正数 大于
     *  倒序精髓： 如果大于 强制返回负数，如果小于 强制返回正数
     */
    @Override
    public int compareTo(WordCountBean o) {
        return this.count - o.getCount() > 0 ? -1:(this.count - o.getCount() <0 ? 1:0);
    }
}
