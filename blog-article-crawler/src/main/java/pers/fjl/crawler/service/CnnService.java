package pers.fjl.crawler.service;

import lombok.extern.slf4j.Slf4j;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.exception.ND4JIllegalStateException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pers.fjl.common.utils.IKUtil;
import pers.fjl.crawler.util.CnnUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * 智能分类模型
 */
@Slf4j
@Service
public class CnnService {

    @Value("${ai.cnnModel}")
    private String cnnModel;

    @Value("${ai.dataPath}")
    private String dataPath;

    @Value("${ai.vecModel}")
    private String vecModel;

    /**
     * 构建卷积模型
     */
    public void build() {
        try {
            // 创建计算图对象
            ComputationGraph computationGraph = CnnUtil.createComputationGraph(100);
            // 加载词向量，训练数据集
            String[] childPaths = {"java", "ops", "python"};
            DataSetIterator dataSetIterator = CnnUtil.getDataSetIterator(dataPath, childPaths, vecModel);
            // 训练
            computationGraph.fit(dataSetIterator);
            // 删除之前生成卷积模型

            // 保存
            new File(cnnModel).delete();
            ModelSerializer.writeModel(computationGraph, cnnModel, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 返回各分类的百分比
     *
     * @param content
     * @return
     */
    public Map textClassify(String content) {
        System.out.println("content:" + content); //分词
        String[] childPaths = {"java", "ops", "python"}; //获取预言结果
        Map<String, Double> predictions = null;
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("article.cnnmodel");
        try {
            content = IKUtil.split(content, " ");
            predictions = CnnUtil.predictions(inputStream, vecModel, cnnModel, dataPath, childPaths, content);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ND4JIllegalStateException e2) {
            predictions = new HashMap<>();
            predictions.put("训练集中没有与该段话匹配的结果", 0.0);
            return predictions;
        }
        return predictions;
    }

}
