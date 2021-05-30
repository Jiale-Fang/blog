package pers.fjl.crawler.service;

import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.sentenceiterator.LineSentenceIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pers.fjl.crawler.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class Word2VecService {
    //合并后的分词语料库文件名
    @Value("${ai.wordLib}")
    private String wordLib;

    //合并前的分词语料库的数据路径
    @Value("${ai.dataPath}")
    private String dataPath;

    //模型训练保存的路径
    @Value("${ai.vecModel}")
    private String vecModel;

    public void mergeWord() {
        System.out.println("dataPath" + dataPath);
        System.out.println("wordLib" + wordLib);
        List<String> fileNames = FileUtil.getFiles(dataPath);
        System.out.println(fileNames);
        try {
            FileUtil.merge(wordLib, fileNames);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void build() {
        try {//加载数爬虫分词数据集
            SentenceIterator iter = new LineSentenceIterator(new File(wordLib));
            Word2Vec vec = new Word2Vec.Builder()
                    .minWordFrequency(4).  // 分词语料库出现的最少次数
                    iterations(10).  // 设置迭代次数
                    layerSize(100). // 词向量维度
                    seed(42).   // 随机种子
                    windowSize(5).  //  当前词与预测词在一个句子中的最大距离
                    iterate(iter).build();
                vec.fit(); //保存模型之前先删除
            new File(vecModel).delete();//删除
            WordVectorSerializer.writeWord2VecModel(vec, vecModel);
//            WordVectorSerializer.writeWordVectors(vec, vecModel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
