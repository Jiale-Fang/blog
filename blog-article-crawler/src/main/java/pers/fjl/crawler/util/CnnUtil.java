package pers.fjl.crawler.util;

import org.deeplearning4j.iterator.CnnSentenceDataSetIterator;
import org.deeplearning4j.iterator.LabeledSentenceProvider;
import org.deeplearning4j.iterator.provider.FileLabeledSentenceProvider;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;
import org.deeplearning4j.nn.conf.ComputationGraphConfiguration;
import org.deeplearning4j.nn.conf.ConvolutionMode;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.graph.MergeVertex;
import org.deeplearning4j.nn.conf.inputs.InputType;
import org.deeplearning4j.nn.conf.layers.ConvolutionLayer;
import org.deeplearning4j.nn.conf.layers.GlobalPoolingLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.conf.layers.PoolingType;
import org.deeplearning4j.nn.graph.ComputationGraph;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import pers.fjl.crawler.vo.TextClassifyVo;

import javax.xml.soap.Text;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * CNN工具类
 */
public class CnnUtil {

    /**
     * 创建计算图（卷积神经网络）
     *
     * @param cnnLayerFeatureMaps 卷积核的数量(=词向量维度)
     * @return 计算图
     */
    public static ComputationGraph createComputationGraph(int cnnLayerFeatureMaps) {
        //训练模型
        int vectorSize = 128;               //向量大小
        //int cnnLayerFeatureMaps = 100;      每种大小卷积层的卷积核的数量=词向量维度
        ComputationGraphConfiguration config = new NeuralNetConfiguration.Builder()
                .convolutionMode(ConvolutionMode.Same)// 设置卷积模式
                .updater(new Adam(0.01))//aa
                .l2(0.0001)//aa
                .graphBuilder()
                .addInputs("input")
                .addLayer("cnn1", new ConvolutionLayer.Builder()//卷积层
                        .kernelSize(3, vectorSize)//卷积区域尺寸
                        .stride(1, vectorSize)//卷积平移步幅
                        .nIn(1)
                        .nOut(cnnLayerFeatureMaps)
                        .build(), "input")
                .addLayer("cnn2", new ConvolutionLayer.Builder()
                        .kernelSize(4, vectorSize)
                        .stride(1, vectorSize)
                        .nIn(1)
                        .nOut(cnnLayerFeatureMaps)
                        .build(), "input")
                .addLayer("cnn3", new ConvolutionLayer.Builder()
                        .kernelSize(5, vectorSize)
                        .stride(1, vectorSize)
                        .nIn(1)
                        .nOut(cnnLayerFeatureMaps)
                        .build(), "input")
                .addVertex("merge", new MergeVertex(), "cnn1", "cnn2", "cnn3")//全连接层
                .addLayer("globalPool", new GlobalPoolingLayer.Builder()//池化层
                        .poolingType(PoolingType.MAX)//aa
                        .dropOut(0.5)//aa
                        .build(), "merge")
                .addLayer("out", new OutputLayer.Builder()//输出层
                        .lossFunction(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)//aa
                        .activation(Activation.SOFTMAX)//aa
                        .nIn(3 * cnnLayerFeatureMaps)
                        .nOut(3)
                        .build(), "globalPool")
                .setOutputs("out")
                .setInputTypes(InputType.convolutional(256,vectorSize,1))//aa
                .build();
        ComputationGraph net = new ComputationGraph(config);
        net.init();
        return net;
    }

    /**
     * 获取训练数据集
     *
     * @param path       分词语料库根目录
     * @param childPaths 分词语料库子文件夹
     * @param vecModel   词向量模型
     * @return DataSetIterator
     */
    public static DataSetIterator getDataSetIterator(String path, String[] childPaths, String vecModel, int minibatchSize) {
        //加载词向量模型
//        WordVectors wordVectors = WordVectorSerializer.loadStaticModel(new File(vecModel));
        WordVectors wordVectors = WordVectorSerializer.readWord2VecModel(new File(vecModel));
        //词标记分类比标签
        Map<String, List<File>> reviewFilesMap = new HashMap<>();

        for (String childPath : childPaths) {
            System.out.println("childpath:" + childPath);
            File[] files = new File(path + "/" + childPath).listFiles();
            if (files != null) {
                reviewFilesMap.put(childPath, Arrays.asList(files));
            }
        }
        //标记跟踪
        LabeledSentenceProvider sentenceProvider = new FileLabeledSentenceProvider(reviewFilesMap, new Random(12345));
        return new CnnSentenceDataSetIterator.Builder(CnnSentenceDataSetIterator.Format.CNN2D)
                .sentenceProvider(sentenceProvider)
                .wordVectors(wordVectors)
                .minibatchSize(minibatchSize)
                .maxSentenceLength(256)
                .useNormalizedWordVectors(false)
                .build();
    }

    /**
     * 预言
     *
     * @param vecModel   词向量模型
     * @param cnnModel   卷积神经网络模型
     * @param dataPath   分词语料库根目录
     * @param childPaths 分词语料库文件夹
     * @param content    预言的文本内容
     * @return  map
     * @throws IOException
     */
    public static List<TextClassifyVo> predictions(InputStream is,String vecModel, String cnnModel, String dataPath, String[] childPaths, String content) throws IOException {
        Map<String, Double> map = new HashMap<>();

        //模型应用
//        ComputationGraph model = ModelSerializer.restoreComputationGraph(cnnModel);//通过cnn模型获取计算图对象
        ComputationGraph model = ModelSerializer.restoreComputationGraph(is);

        //加载数据集
        DataSetIterator dataSet = CnnUtil.getDataSetIterator(dataPath, childPaths, vecModel,32);
        //通过句子获取概率矩阵对象
        INDArray featuresFirstNegative = ((CnnSentenceDataSetIterator) dataSet).loadSingleSentence(content);
        INDArray predictionsFirstNegative = model.outputSingle(featuresFirstNegative);
        List<String> labels = dataSet.getLabels();
        List<TextClassifyVo> textClassifyVos = new ArrayList<>();
        for (int i = 0; i < labels.size(); i++) {
            TextClassifyVo textClassifyVo = new TextClassifyVo();
            textClassifyVo.setType(labels.get(i)).setResult(predictionsFirstNegative.getDouble(i));
            textClassifyVos.add(textClassifyVo);
            System.out.println("i:"+i+"====>"+labels.get(i)+"======>"+predictionsFirstNegative.getDouble(i));
            map.put(labels.get(i) + "", predictionsFirstNegative.getDouble(i));
        }
        return textClassifyVos;
    }

}
