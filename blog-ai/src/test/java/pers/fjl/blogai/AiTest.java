package pers.fjl.blogai;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest
public class AiTest {
    @Test
    public void test1() throws IOException, InterruptedException {
        Process pr = Runtime.getRuntime().exec("G:\\download\\python\\TensorFlow\\Miniconda\\python.exe G:\\download\\python\\project\\DeepLearningExamples-master\\tf2-rnn-poetry-generator\\eval.py 虽然 ");

        System.out.println(pr.waitFor());
        BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream(), "GBK"));
        BufferedReader stderrReader = new BufferedReader(new InputStreamReader(pr.getErrorStream(), "GBK"));
        String myString = null;
        while ((myString = in.readLine()) != null)
            System.out.println(myString);

        System.out.println("===python===控制台信息");
        while ((myString = stderrReader.readLine()) != null) {
            System.out.println(myString);
        }
    }
}
