package pers.fjl.crawler;


import lombok.extern.slf4j.Slf4j;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.sentenceiterator.LineSentenceIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nd4j.linalg.io.ClassPathResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import pers.fjl.crawler.service.CnnService;
import pers.fjl.crawler.service.Word2VecService;
import pers.fjl.crawler.task.BlogTask;
import pers.fjl.crawler.util.HTMLUtil;
import pers.fjl.crawler.util.IKUtil;

import javax.annotation.Resource;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static pers.fjl.crawler.util.FileUtil.readToString;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@Slf4j
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
        String[] childPaths = { "db","java", "python"};
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

    @Test
    public void test5() throws IOException {

        log.info("加载并向量化句子....");
        //每一行之间用空格分割
        SentenceIterator iter = new LineSentenceIterator(new File("G:\\repository\\comment\\mergeText.txt"));
        //在每行用用空格分割以得到单词
        TokenizerFactory t = new DefaultTokenizerFactory();
        t.setTokenPreProcessor(new CommonPreprocessor());
        log.info("Building model....");
        Word2Vec vec = new Word2Vec.Builder()
                .minWordFrequency(5)
                .layerSize(200)
                .seed(42)
                .windowSize(5)
                .iterate(iter)
                .tokenizerFactory(t)
                .build();

        log.info("Fitting Word2Vec model....");
        vec.fit();
        log.info("保存向量....");
        System.out.println(vec.toString());
        WordVectorSerializer.writeWordVectors(vec, "G:\\repository\\crawler\\test.vecmodel");
    }

    @Test
    public void test6() throws Exception {
        BufferedReader br = new BufferedReader(new FileReader("G:\\1.txt"));
        String readLine = null;
        PrintWriter printWriter = new PrintWriter(new File("G:\\7.txt"));
        while ((readLine = br.readLine()) != null) {
            String split = IKUtil.split(readLine + " ", " ");
            printWriter.println(split);
        }
        br.close();
        printWriter.close();
    }

    /**
     * 将文本分词
     * @throws IOException
     */
    @Test
    public void FileUtilTest() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("G:\\repository\\comment\\neg.txt"));
        String readLine = null;
        PrintWriter printWriter = new PrintWriter(new File("G:\\repository\\comment\\negtive\\negSplit.txt"));
        while ((readLine = br.readLine()) != null) {
            String split = IKUtil.split(readLine + " ", " ");
            printWriter.println(split);
        }
        br.close();
        printWriter.close();
    }

    @Test
    public void commentWord2VecTest(){
        PrintWriter printWriter = null;
        try {
            printWriter = new PrintWriter(new File("G:\\repository\\comment\\label2.txt"));
            for (int i = 0; i < 11428; i++) {
                printWriter.println("差评");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
