package pers.fjl.crawler;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pers.fjl.crawler.service.CnnService;
import pers.fjl.crawler.service.Word2VecService;

import javax.annotation.Resource;

import java.io.File;
import java.io.IOException;

import static pers.fjl.crawler.util.FileUtil.readToString;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class Word2VecServiceTest {
    @Resource
    private Word2VecService word2VecService;
    @Resource
    private CnnService cnnService;

    @Value("${ai.cnnModel}")
    private String cnnModel;

    @Value("${ai.dataPath}")
    private String dataPath;

    @Test
    public void test1() throws IOException {
//        word2VecService.mergeWord();
        String txt = readToString("G:\\repository\\crawler\\ai\\1cdb7dce-690a-4e02-81c2-4085b43363f2.txt");
        System.out.println(txt);
    }

    @Test
    public void test2() {
        System.out.println("开始构建神经网络卷积模型");
        cnnService.build();
        System.out.println("构建神经网络卷积模型结束");
    }

    @Test
    public void test3() {
        String[] childPaths = {"java", "ops", "python"};
        for (String childPath : childPaths) {

            File[] files = new File(dataPath + "/" + childPath).listFiles();
            if (files != null) {
                for (File file : files) {
                    System.out.println(file);
                }
            }
        }
    }

    @Test
    public void test4() {
        File[] files = new File(dataPath + "/" + "machinelearning").listFiles();
        if (files != null) {
            System.out.println("yes");
        }
    }

}
