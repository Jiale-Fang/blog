package pers.fjl.crawler.service;

import lombok.extern.slf4j.Slf4j;
import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.exception.ND4JIllegalStateException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pers.fjl.crawler.util.CnnUtil;
import pers.fjl.crawler.util.IKUtil;
import pers.fjl.crawler.vo.TextClassifyVo;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
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
            System.out.println(computationGraph.summary());
            // 加载词向量，训练数据集
            String[] childPaths = { "db","java", "python"};
            DataSetIterator dataSetIterator = CnnUtil.getDataSetIterator(dataPath, childPaths, vecModel,32);
            DataSetIterator testIter = CnnUtil.getDataSetIterator(dataPath, childPaths, vecModel,1);
            //使用单条记录并且打印每一层网络的输入和输出信息
            INDArray input = testIter.next().getFeatures();
            System.out.println(input.shapeInfoToString());
            Map<String, INDArray> map = computationGraph.feedForward(input, false);
            for (Map.Entry<String, INDArray> entry : map.entrySet()) {
                System.out.println(entry.getKey() + ":" + entry.getValue().shapeInfoToString());
                System.out.println();
            }

            // 训练开始
            System.out.println("Starting training");
            computationGraph.setListeners(new ScoreIterationListener(1));
            for (int i = 0; i < 40; i++){
                computationGraph.fit(dataSetIterator);
                System.out.println("Epoch " + i + " complete. Starting evaluation:");
                Evaluation evaluation = computationGraph.evaluate(dataSetIterator);
                dataSetIterator.reset();
                testIter.reset();
                System.out.println(evaluation.stats());
            }

            // 删除之前生成卷积模型
            new File(cnnModel).delete();
            // 保存
            ModelSerializer.writeModel(computationGraph, cnnModel, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 返回各博客分类的百分比
     *
     * @param content
     * @return
     */
    public List<TextClassifyVo> textClassify(String content) {
        System.out.println("content:" + content); //分词
        String[] childPaths = { "db","java", "python"}; //获取预言结果
//        Map<String, Double> predictions = null;
        List<TextClassifyVo> predictions = new ArrayList<>();
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("article.cnnmodel");
        try {
            content = IKUtil.split(content, " ");
            predictions = CnnUtil.predictions(inputStream, vecModel, cnnModel, dataPath, childPaths, content);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ND4JIllegalStateException e2) {
            TextClassifyVo textClassifyVo = new TextClassifyVo();
            textClassifyVo.setType("训练集中没有与该段话匹配的结果").setResult(0.0);
            predictions.add(textClassifyVo);
//            predictions = new HashMap<>();
//            predictions.put("训练集中没有与该段话匹配的结果", 0.0);
            return predictions;
        }
        return predictions;
    }

}
