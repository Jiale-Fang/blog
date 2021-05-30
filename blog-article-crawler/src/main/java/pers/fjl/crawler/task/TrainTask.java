package pers.fjl.crawler.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pers.fjl.crawler.service.CnnService;
import pers.fjl.crawler.service.Word2VecService;

import javax.annotation.Resource;

@Component
public class TrainTask {

    @Resource
    private Word2VecService word2VecService;

    @Resource
    private CnnService cnnService;

//    /**
//     * 训练博客分类模型
//     */
//    @Scheduled(cron = "39 01 00 * * ?")
//    public void trainBlogModel() {
////        System.out.println("开始合并语料库......");
////        word2VecService.mergeWord();
////        System.out.println("合并语料库结束‐‐‐‐‐‐");
//
//        System.out.println("开始构建词向量模型");
//        word2VecService.build();
//        System.out.println("构建词向量模型结束");
//
//        System.out.println("开始构建神经网络卷积模型");
//        cnnService.build();
//        System.out.println("构建神经网络卷积模型结束");
//    }
}

