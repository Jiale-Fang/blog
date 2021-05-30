package pers.fjl.crawler.hadoop.mapreduce.util;

import pers.fjl.crawler.util.IKUtil;

import java.io.*;
import java.util.*;
import java.util.Map.Entry;

/**
 * @author fjl
 * <p>
 * <p>
 * 实现从文件中读入英文文章，统计单词个数,并按值从大到小输出
 */

public class WordCount {

    public static void main(String[] args) throws Exception {

        BufferedReader br = new BufferedReader(new FileReader("G:\\repository\\crawler\\article.txt"));
        List<String> lists = new ArrayList<String>();  //存储过滤后单词的列表
        String readLine = null;
        while ((readLine = br.readLine()) != null) {
            String[] wordsArr1 = readLine.split("[^(a-zA-Z\\u4e00-\\u9fa5)]");  //过滤出只含有字母和汉字的
            for (String word : wordsArr1) {
                if (word.length() != 0 && word.length() <= 15) {  //去除长度为0和大于15的行
                    lists.add(word);
                }
            }
        }

        br.close();

        Map<String, Integer> wordsCount = new TreeMap<String, Integer>();  //存储单词计数信息，key值为单词，value为单词数

        //单词的词频统计
        for (String li : lists) {
            if (wordsCount.get(li) != null) {
                wordsCount.put(li, wordsCount.get(li) + 1);
            } else {
                wordsCount.put(li, 1);
            }

        }

        SortMap(wordsCount);    //按值进行排序
    }

    /**
     * 排序并写入文件
     *
     * @param oldmap
     */
    public static void SortMap(Map<String, Integer> oldmap) {

        ArrayList<Entry<String, Integer>> list = new ArrayList<Entry<String, Integer>>(oldmap.entrySet());

        Collections.sort(list, new Comparator<Entry<String, Integer>>() {
            @Override
            public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
                return o2.getValue() - o1.getValue();  //降序
            }
        });
        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(new File("G:\\repository\\crawler\\wordCountDesc.txt"));
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getValue() <= 4) { //去掉小于5次的行
                    continue;
                }
                String split = list.get(i).getKey() + " " + list.get(i).getValue();
                printWriter.println(split);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (printWriter != null)
            printWriter.close();
    }

}
