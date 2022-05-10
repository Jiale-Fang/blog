package pers.fjl.common.utils;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import org.apache.commons.codec.binary.Hex;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 文件工具类
 */
public class FileUtil {

    /**
     * 将多个文本文件合并为一个文本文件
     *
     * @param outFileName
     * @param inFileNames
     * @throws IOException
     */
    public static void merge(String outFileName, List<String> inFileNames) throws IOException {
        FileWriter writer = new FileWriter(outFileName, false);
        for (String inFileName : inFileNames) {
            System.out.println();
            try {
                String txt = readToString(inFileName);
                writer.write(txt);
                System.out.println(txt);
            } catch (Exception e) {
            }
        }
        writer.close();
    }

    /**
     * 查找某目录下的所有文件名称
     *
     * @param path
     * @return
     */
    public static List<String> getFiles(String path) {
        List<String> files = new ArrayList<String>();
        File file = new File(path);
        File[] tempList = file.listFiles();
        for (int i = 0; i < tempList.length; i++) {
            if (tempList[i].isFile()) { //如果是文件
                files.add(tempList[i].toString());
            }
            if (tempList[i].isDirectory()) {    //如果是文件夹
                files.addAll(getFiles(tempList[i].toString()));
            }
        }
        return files;
    }

    /**
     * 读取文本文件内容到字符串
     *
     * @param fileName
     * @return
     * @throws IOException
     */
    public static String readToString(String fileName) throws IOException {
        String encoding = "UTF‐8";
        File file = new File(fileName);
        Long filelength = file.length();
        byte[] filecontent = new byte[filelength.intValue()];
        FileInputStream in = new FileInputStream(file);
        in.read(filecontent);
        in.close();
//        return new String(filecontent, encoding);
        return new String(filecontent, encoding);
    }

    /**
     * 获取文件md5值
     *
     * @param inputStream 文件输入流
     * @return {@link String} 文件md5值
     */
    public static String getMd5(InputStream inputStream) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("md5");
            byte[] buffer = new byte[8192];
            int length;
            while ((length = inputStream.read(buffer)) != -1) {
                md5.update(buffer, 0, length);
            }
            return new String(Hex.encodeHex(md5.digest()));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 得到文件扩展名
     *
     * @param fileName 文件名称
     * @return {@link String} 文件后缀
     */
    public static String getExtName(String fileName) {
        if (StringUtils.isBlank(fileName)) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }

    /**
     * 转换file
     *
     * @param multipartFile 多部分文件
     * @return {@link File} file
     */
    public static File multipartFileToFile(MultipartFile multipartFile) {
        File file = null;
        try {
            String originalFilename = multipartFile.getOriginalFilename();
            String[] filename = Objects.requireNonNull(originalFilename).split("\\.");
            file = File.createTempFile(filename[0], filename[1]);
            multipartFile.transferTo(file);
            file.deleteOnExit();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }


    /**
     * 自动调节精度(经验数值)
     *
     * @param size 源图片大小
     * @return 图片压缩质量比
     */
    private static double getAccuracy(long size) {
        double accuracy;
        if (size < 900) {
            accuracy = 0.85;
        } else if (size < 2048) {
            accuracy = 0.6;
        } else if (size < 3072) {
            accuracy = 0.44;
        } else {
            accuracy = 0.4;
        }
        return accuracy;
    }
}
