package pers.fjl.blogai.service;

public interface PoemService {
    /**
     * 生成随机古诗
     * @return
     */
    String randomPoem(String methodNo);

    /**
     * 生成藏头诗
     * @param words
     * @param methodNo
     * @return
     */
    String acrosticPoem(String words, String methodNo);

    /**
     * 给出部分信息的情况下，随机生成剩余部分古诗
     * @param words
     * @param methodNo
     * @return
     */
    String randomPoem2(String words, String methodNo);
}
