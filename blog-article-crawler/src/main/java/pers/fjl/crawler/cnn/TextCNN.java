package pers.fjl.crawler.cnn;

import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.iterator.CnnSentenceDataSetIterator;
import org.deeplearning4j.iterator.LabeledSentenceProvider;
import org.deeplearning4j.iterator.provider.CollectionLabeledSentenceProvider;
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
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;

public class TextCNN {

    public void build(){
        getTextCNN(32,100,256);
    }

    public static ComputationGraph getTextCNN(final int vectorSize, final int numFeatureMap,
                                              final int corpusLenLimit){
        ComputationGraphConfiguration config = new NeuralNetConfiguration.Builder()
                .weightInit(WeightInit.RELU)
                .activation(Activation.LEAKYRELU)
                .updater(new Adam(0.01))
                .convolutionMode(ConvolutionMode.Same)
                .l2(0.0001)
                .graphBuilder()
                .addInputs("input")
                .addLayer("2-gram", new ConvolutionLayer.Builder()
                        .kernelSize(2,vectorSize)
                        .stride(1,vectorSize)
                        .nIn(1)
                        .nOut(numFeatureMap)
                        .build(), "input")
                .addLayer("3-gram", new ConvolutionLayer.Builder()
                        .kernelSize(3,vectorSize)
                        .stride(1,vectorSize)
                        .nIn(1)
                        .nOut(numFeatureMap)
                        .build(), "input")
                .addLayer("4-gram", new ConvolutionLayer.Builder()
                        .kernelSize(4,vectorSize)
                        .stride(1,vectorSize)
                        .nIn(1)
                        .nOut(numFeatureMap)
                        .build(), "input")
                .addVertex("merge", new MergeVertex(), "2-gram", "3-gram", "4-gram")
                .addLayer("globalPool", new GlobalPoolingLayer.Builder()
                        .poolingType(PoolingType.MAX)
                        .dropOut(0.5)
                        .build(), "merge")
                .addLayer("out", new OutputLayer.Builder()
                        .lossFunction(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                        .activation(Activation.SOFTMAX)
                        .nIn(300)
                        .nOut(2)
                        .build(), "globalPool")
                .setOutputs("out")
                .setInputTypes(InputType.convolutional(corpusLenLimit, vectorSize, 1))
                .build();

        ComputationGraph net = new ComputationGraph(config);
        net.init();
        return net;
    }

    private static DataSetIterator getDataSetIterator(WordVectors wordVectors, int minibatchSize,
                                                      int maxSentenceLength){
        String corpusPath = "G:\\1.txt";
        String labelPath = "G:\\2.txt";
        String line;
        List<String> sentences = new LinkedList<>();
        List<String> labels = new LinkedList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(corpusPath))){
            while((line = br.readLine()) != null)sentences.add(line);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        //
        try(BufferedReader br = new BufferedReader(new FileReader(labelPath))){
            while((line = br.readLine()) != null)labels.add(line);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        //
        LabeledSentenceProvider sentenceProvider = new CollectionLabeledSentenceProvider(sentences, labels);
        TokenizerFactory tokenizerFactory = new DefaultTokenizerFactory();
        tokenizerFactory.setTokenPreProcessor(new CommonPreprocessor());
        System.out.println("DataSetIter 2 Current Num of Classes:" + sentenceProvider.numLabelClasses());
        System.out.println("DataSetIter 2 Total Num of samples: " + sentenceProvider.totalNumSentences());
        //
        return new CnnSentenceDataSetIterator.Builder(CnnSentenceDataSetIterator.Format.CNN2D)
                .sentenceProvider(sentenceProvider)
                .wordVectors(wordVectors)
                .minibatchSize(minibatchSize)
                .maxSentenceLength(maxSentenceLength)
                .tokenizerFactory(tokenizerFactory)
                .useNormalizedWordVectors(false)
                .build();
    }

    public static void main(String[] args) {
        final int batchSize = 32;
        final int corpusLenLimit = 256;
        final int vectorSize = 128;
        final int numFeatureMap = 100;
        final int nEpochs = Integer.parseInt("1");
        //读取预先训练好的Word2Vec的模型，并且构建训练和验证数据集
        WordVectors wordVectors = WordVectorSerializer.loadStaticModel(new File("G:\\repository\\crawler\\test.vecmodel"));
        DataSetIterator trainIter = getDataSetIterator(wordVectors, batchSize, corpusLenLimit);
        DataSetIterator testIter = getDataSetIterator(wordVectors, 1, corpusLenLimit);
        //生成TextCNN模型，并打印模型结构信息
        ComputationGraph net = getTextCNN(vectorSize, numFeatureMap, corpusLenLimit);
        System.out.println(net.summary());
        //使用单条记录并且打印每一层网络的输入和输出信息
//        INDArray input = testIter.next().getFeatures();
//        System.out.println(input.shapeInfoToString());
//        Map<String,INDArray> map = net.feedForward(input, false);
//        for( Map.Entry<String, INDArray> entry : map.entrySet() ){
//            System.out.println(entry.getKey() + ":" + entry.getValue().shapeInfoToString());
//            System.out.println();
//        }
        //训练开始。。。
        System.out.println("Starting training");
        net.setListeners(new ScoreIterationListener(1));
        for (int i = 0; i < nEpochs; i++) {
            net.fit(trainIter);
            System.out.println("Epoch " + i + " complete. Starting evaluation:");
            Evaluation evaluation = net.evaluate(trainIter);
            trainIter.reset();
            testIter.reset();
            System.out.println(evaluation.stats());
        }
    }
}
