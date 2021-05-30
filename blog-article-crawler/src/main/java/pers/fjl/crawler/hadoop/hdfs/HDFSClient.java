package pers.fjl.crawler.hadoop.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;

public class HDFSClient {

    public static void writeData(String content) {
        Configuration conf = null;
        FileSystem fs = null;
        try {
        //设置客户端身份，否则没有权限
        System.setProperty("HADOOP_USER_NAME", "root");
        //创建配置对象实例
        conf = new Configuration();
        //设置操作的文件系统是HDFS，并且指定操作地址（core-site.xml里的）
        conf.set("fs.defaultFS", "hdfs://192.168.10.111:8020");
        //创建FileSystem对象实例
        fs = FileSystem.get(conf);

        //创建文件系统实例
//        FileSystem fs = FileSystem.get(conf);
        //创建文件实例
        String fileName = "/tcefrep/rosie/crawlerData";

        Path file = new Path(fileName);


            if (!fs.exists(new Path("/tcefrep/rosie/crawlerData"))) {
                //创建输出流对象
                FSDataOutputStream os = fs.create(file);
                //写入数据
                byte[] buff = content.getBytes();
                os.write(buff, 0, buff.length);

                //关闭输出流和文件系统
                os.close();
                fs.close();
            }else {
                FSDataOutputStream os = fs.append(file);
                //写入数据
                byte[] buff = (" "+content).getBytes();
                os.write(buff, 0, buff.length);

                //关闭输出流和文件系统
                os.close();
                fs.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (fs != null) {
            try {
                fs.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
