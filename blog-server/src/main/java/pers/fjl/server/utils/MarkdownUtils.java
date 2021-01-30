package pers.fjl.server.utils;

import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TableBlock;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.ext.heading.anchor.HeadingAnchorExtension;
import org.commonmark.node.Link;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.AttributeProvider;
import org.commonmark.renderer.html.AttributeProviderContext;
import org.commonmark.renderer.html.AttributeProviderFactory;
import org.commonmark.renderer.html.HtmlRenderer;

import java.util.*;

public class MarkdownUtils {

    /**
     * markdown格式转换成HTML格式
     * @param markdown
     * @return
     */
    public static String markdownToHtml(String markdown) {
        Parser parser = Parser.builder().build();
        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder().build();
        return renderer.render(document);
    }

    /**
     * 增加扩展[标题锚点，表格生成]
     * Markdown转换成HTML
     * @param markdown
     * @return
     */
    public static String markdownToHtmlExtensions(String markdown) {
        //h标题生成id
        Set<Extension> headingAnchorExtensions = Collections.singleton(HeadingAnchorExtension.create());
        //转换table的HTML
        List<Extension> tableExtension = Arrays.asList(TablesExtension.create());
        Parser parser = Parser.builder()
                .extensions(tableExtension)
                .build();
        Node document = parser.parse(markdown);
        HtmlRenderer renderer = HtmlRenderer.builder()
                .extensions(headingAnchorExtensions)
                .extensions(tableExtension)
                .attributeProviderFactory(new AttributeProviderFactory() {
                    public AttributeProvider create(AttributeProviderContext context) {
                        return new CustomAttributeProvider();
                    }
                })
                .build();
        return renderer.render(document);
    }

    /**
     * 处理标签的属性
     */
    static class CustomAttributeProvider implements AttributeProvider {
        @Override
        public void setAttributes(Node node, String tagName, Map<String, String> attributes) {
            //改变a标签的target属性为_blank
            if (node instanceof Link) {
                attributes.put("target", "_blank");
            }
            if (node instanceof TableBlock) {
                attributes.put("class", "ui celled table");
            }
        }
    }


    public static void main(String[] args) {
        String table = "| hello | hi   | 哈哈哈   |\n" +
                "| ----- | ---- | ----- |\n" +
                "| 斯维尔多  | 士大夫  | f啊    |\n" +
                "| 阿什顿发  | 非固定杆 | 撒阿什顿发 |\n" +
                "\n";
        String a = "[imCoding 爱编程](http://www.baidu.com)";
        String b="# 个人项目：论文查重\n" +
                "这个作业要求在哪里     | https://edu.cnblogs.com/campus/gdgy/Networkengineering1834/homework/11146\n" +
                "-------- | -----\n" +
                "传送门  | https://github.com/asiL-tcefreP/-software-engineering-2/tree/master\n" +
                "\n" +
                "## 一、模块接口的设计与实现过程</br>\n" +
                "### 1.1 算法来源\n" +
                ">文本相似度计算常用于网页去重以及NLP里文本分析等场景。文本相似度，可以分为两种，一种是字面相似度，另一种是语义相似度。本文记录的是文本的字面相似度的计算及实现，语义相似度计算则需要海量数据去计算语义值，较为复杂。\n" +
                "最常用的且最简单的两种文本相似检测方法：局部敏感hash、余弦相似度\n" +
                "\n" +
                "在本案例中，用到的是局部敏感hash(LSH)中的simhash。计算出simhash值后，再计算hash值得汉明距离，即可得到文本的相似程度。\n" +
                "\n" +
                "**汉明距离：**</br>\n" +
                "定义：两个长度相同的字符串对应位字符不同的个数</br>\n" +
                "两个关键点：\n" +
                "- 长度相同\n" +
                "- 对应位字符不同\n" +
                "传送门: [SimHash详细介绍](https://blog.csdn.net/wxgxgp/article/details/104106867).\n" +
                "\n" +
                "### 1.2 项目结构\n" +
                "![](https://img-blog.csdnimg.cn/img_convert/971412fd7e34fe61415b460dc9d7dba3.png)\n" +
                "包含文件读写类以及算法的实现类\n" +
                "\n" +
                "![](https://img-blog.csdnimg.cn/img_convert/569ab068579ac000d14787f32fcbc2c1.png)\n" +
                "\n" +
                "方法的接口如下：\n" +
                "```java\n" +
                "package pers.fjl.papercheck.service;\n" +
                "/**\n" +
                " * @program: PaperCheck\n" +
                " *\n" +
                " * @description: ${description}\n" +
                " *\n" +
                " * @author: Fang Jiale\n" +
                " *\n" +
                " * @create: 2020-10-24 17:05\n" +
                " **/\n" +
                "import pers.fjl.papercheck.service.impl.SimHashImpl;\n" +
                "\n" +
                "import java.math.BigInteger;\n" +
                "import java.util.List;\n" +
                "\n" +
                "public interface SimHash {\n" +
                "    /**\n" +
                "     * SimHash模块\n" +
                "     * @return\n" +
                "     */\n" +
                "    BigInteger simHash();\n" +
                "\n" +
                "    /**\n" +
                "     *计算哈希值\n" +
                "     * @param source\n" +
                "     * @return\n" +
                "     */\n" +
                "    BigInteger hash(String source);\n" +
                "\n" +
                "    /**\n" +
                "     * 汉明距离\n" +
                "     * @param other\n" +
                "     * @return\n" +
                "     */\n" +
                "    int hammingDistance(SimHashImpl other);\n" +
                "\n" +
                "    /**\n" +
                "     *计算汉明距离\n" +
                "     * @param str1\n" +
                "     * @param str2\n" +
                "     * @return\n" +
                "     */\n" +
                "    double getDistance(String str1, String str2);\n" +
                "\n" +
                "    /**\n" +
                "     *获取特征值\n" +
                "     * @param simHashImpl\n" +
                "     * @param distance\n" +
                "     * @return\n" +
                "     */\n" +
                "    List subByDistance(SimHashImpl simHashImpl, int distance);\n" +
                "}\n" +
                "\n" +
                "```\n" +
                "实现类\n" +
                "\n" +
                "```java\n" +
                "package pers.fjl.papercheck.service.impl;\n" +
                "/**\n" +
                " * @program: PaperCheck\n" +
                " *\n" +
                " * @description: ${description}\n" +
                " *\n" +
                " * @author: Fang Jiale\n" +
                " *\n" +
                " * @create: 2020-10-24 17:05\n" +
                " **/\n" +
                "import pers.fjl.papercheck.file.FileInput;\n" +
                "import pers.fjl.papercheck.service.SimHash;\n" +
                "\n" +
                "import java.math.BigInteger;\n" +
                "import java.util.ArrayList;\n" +
                "import java.util.List;\n" +
                "import java.util.StringTokenizer;\n" +
                "\n" +
                "public class SimHashImpl implements SimHash {\n" +
                "\n" +
                "    private String tokens;\n" +
                "\n" +
                "    private BigInteger intSimHash;\n" +
                "\n" +
                "    private String strSimHash;\n" +
                "\n" +
                "    private int hashbits = 64;\n" +
                "\n" +
                "    public SimHashImpl(String tokens, int hashbits) {\n" +
                "        this.tokens = tokens;\n" +
                "        this.hashbits = hashbits;\n" +
                "        this.intSimHash = this.simHash();\n" +
                "    }\n" +
                "\n" +
                "    public BigInteger simHash() {\n" +
                "        // 定义特征向量/数组\n" +
                "        int[] v = new int[this.hashbits];\n" +
                "        StringTokenizer stringTokens = new StringTokenizer(this.tokens);\n" +
                "        while (stringTokens.hasMoreTokens()) {\n" +
                "            String temp = stringTokens.nextToken();\n" +
                "            //2、将每一个分词hash为一组固定长度的数列.比如 64bit 的一个整数.\n" +
                "            BigInteger t = this.hash(temp);\n" +
                "            for (int i = 0; i < this.hashbits; i++) {\n" +
                "                BigInteger bitmask = new BigInteger(\"1\").shiftLeft(i);\n" +
                "                // 3、建立一个长度为64的整数数组(假设要生成64位的数字指纹,也可以是其它数字),\n" +
                "                // 对每一个分词hash后的数列进行判断,如果是1000...1,那么数组的第一位和末尾一位加1,\n" +
                "                // 中间的62位减一,也就是说,逢1加1,逢0减1.一直到把所有的分词hash数列全部判断完毕.\n" +
                "                if (t.and(bitmask).signum() != 0) {\n" +
                "                    v[i] += 1;\n" +
                "                } else {\n" +
                "                    v[i] -= 1;\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "        BigInteger fingerprint = new BigInteger(\"0\");\n" +
                "        StringBuffer simHashBuffer = new StringBuffer();\n" +
                "        for (int i = 0; i < this.hashbits; i++) {\n" +
                "            // 4、最后对数组进行判断,大于0的记为1,小于等于0的记为0,得到一个 64bit 的数字指纹/签名.\n" +
                "            if (v[i] >= 0) {\n" +
                "                fingerprint = fingerprint.add(new BigInteger(\"1\").shiftLeft(i));\n" +
                "                simHashBuffer.append(\"1\");\n" +
                "            }else{\n" +
                "                simHashBuffer.append(\"0\");\n" +
                "            }\n" +
                "        }\n" +
                "        this.strSimHash = simHashBuffer.toString();\n" +
                "        setStrSimHash(strSimHash);\n" +
                "//        System.out.println(this.strSimHash + \" length \" + this.strSimHash.length());\n" +
                "        return fingerprint;\n" +
                "    }\n" +
                "\n" +
                "    public String getStrSimHash() {\n" +
                "        return strSimHash;\n" +
                "    }\n" +
                "\n" +
                "    public void setStrSimHash(String strSimHash) {\n" +
                "        this.strSimHash = strSimHash;\n" +
                "    }\n" +
                "\n" +
                "    public BigInteger hash(String source) {\n" +
                "        if (source == null || source.length() == 0) {\n" +
                "            return new BigInteger(\"0\");\n" +
                "        } else {\n" +
                "            char[] sourceArray = source.toCharArray();\n" +
                "            BigInteger x = BigInteger.valueOf(((long) sourceArray[0]) << 7);\n" +
                "            BigInteger m = new BigInteger(\"1000003\");\n" +
                "            BigInteger mask = new BigInteger(\"2\").pow(this.hashbits).subtract(\n" +
                "                    new BigInteger(\"1\"));\n" +
                "            for (char item : sourceArray) {\n" +
                "                BigInteger temp = BigInteger.valueOf((long) item);\n" +
                "                x = x.multiply(m).xor(temp).and(mask);\n" +
                "            }\n" +
                "            x = x.xor(new BigInteger(String.valueOf(source.length())));\n" +
                "            if (x.equals(new BigInteger(\"-1\"))) {\n" +
                "                x = new BigInteger(\"-2\");\n" +
                "            }\n" +
                "            return x;\n" +
                "        }\n" +
                "    }\n" +
                "\n" +
                "    public int hammingDistance(SimHashImpl other) {\n" +
                "\n" +
                "        BigInteger x = this.intSimHash.xor(other.intSimHash);\n" +
                "        int tot = 0;\n" +
                "\n" +
                "        //统计x中二进制位数为1的个数\n" +
                "        //我们想想，一个二进制数减去1，那么，从最后那个1（包括那个1）后面的数字全都反了，对吧，然后，n&(n-1)就相当于把后面的数字清0，\n" +
                "        //我们看n能做多少次这样的操作就OK了。\n" +
                "\n" +
                "        while (x.signum() != 0) {\n" +
                "            tot += 1;\n" +
                "            x = x.and(x.subtract(new BigInteger(\"1\")));\n" +
                "        }\n" +
                "        return tot;\n" +
                "    }\n" +
                "\n" +
                "    public double getDistance(String str1, String str2) {\n" +
                "        double distance;\n" +
                "        if (str1.length() != str2.length()) {\n" +
                "            distance = -1;\n" +
                "        } else {\n" +
                "            distance = 0;\n" +
                "            for (int i = 0; i < str1.length(); i++) {\n" +
                "                if (str1.charAt(i) != str2.charAt(i)) {\n" +
                "                    distance++;\n" +
                "                }\n" +
                "            }\n" +
                "        }\n" +
                "        return distance;\n" +
                "    }\n" +
                "\n" +
                "\n" +
                "    public List subByDistance(SimHashImpl simHashImpl, int distance){\n" +
                "        // 分成几组来检查\n" +
                "        int numEach = this.hashbits/(distance+1);\n" +
                "        List characters = new ArrayList();\n" +
                "\n" +
                "        StringBuffer buffer = new StringBuffer();\n" +
                "\n" +
                "        int k = 0;\n" +
                "        for( int i = 0; i < this.intSimHash.bitLength(); i++){\n" +
                "            // 当且仅当设置了指定的位时，返回 true\n" +
                "            boolean sr = simHashImpl.intSimHash.testBit(i);\n" +
                "\n" +
                "            if(sr){\n" +
                "                buffer.append(\"1\");\n" +
                "            }\n" +
                "            else{\n" +
                "                buffer.append(\"0\");\n" +
                "            }\n" +
                "\n" +
                "            if( (i+1)%numEach == 0 ){\n" +
                "                // 将二进制转为BigInteger\n" +
                "                BigInteger eachValue = new BigInteger(buffer.toString(),2);\n" +
                "//                System.out.println(\"----\" +eachValue );\n" +
                "                buffer.delete(0, buffer.length());\n" +
                "                characters.add(eachValue);\n" +
                "            }\n" +
                "        }\n" +
                "        return characters;\n" +
                "    }\n" +
                "\n" +
                "//    public double distance(String strSimHash1,String strSimHash2){\n" +
                "//        double distance;\n" +
                "//        return hash1.getDistance(hash1.strSimHash,hash2.strSimHash);\n" +
                "//    }\n" +
                "\n" +
                "    public static void main(String[] args) {\n" +
                "        String origin=\"G:\\\\download\\\\app\\\\Git\\\\gitRepos\\\\paperpass\\\\src\\\\main\\\\resources\\\\orig.txt\";\n" +
                "        String[] s={\n" +
                "        \"G:\\\\download\\\\app\\\\Git\\\\gitRepos\\\\paperpass\\\\src\\\\main\\\\resources\\\\orig_0.8_add.txt\",\n" +
                "        \"G:\\\\download\\\\app\\\\Git\\\\gitRepos\\\\paperpass\\\\src\\\\main\\\\resources\\\\orig_0.8_del.txt\",\n" +
                "        \"G:\\\\download\\\\app\\\\Git\\\\gitRepos\\\\paperpass\\\\src\\\\main\\\\resources\\\\orig_0.8_dis_1.txt\",\n" +
                "                \"G:\\\\download\\\\app\\\\Git\\\\gitRepos\\\\paperpass\\\\src\\\\main\\\\resources\\\\orig_0.8_dis_10.txt\",\n" +
                "                \"G:\\\\download\\\\app\\\\Git\\\\gitRepos\\\\paperpass\\\\src\\\\main\\\\resources\\\\orig_0.8_dis_15.txt\"};\n" +
                "        FileInput fileInput = new FileInput();\n" +
                "        SimHashImpl hash1 = new SimHashImpl(fileInput.readString(origin), 64);\n" +
                "        hash1.subByDistance(hash1, 3);\n" +
                "\n" +
                "        for (String s1 : s) {\n" +
                "            SimHashImpl hash2 = new SimHashImpl(fileInput.readString(s1), 64);\n" +
                "            hash2.subByDistance(hash2, 3);\n" +
                "            double distance = hash1.getDistance(hash1.strSimHash,hash2.strSimHash);\n" +
                "            System.out.println(\"该文章与原文相似度为：\"+(100-distance*100/128)+\"%\");\n" +
                "        }\n" +
                "    }\n" +
                "}\n" +
                "```\n" +
                "\n" +
                "```java\n" +
                "package pers.fjl.papercheck.file;\n" +
                "/**\n" +
                " * @program: PaperCheck\n" +
                " *\n" +
                " * @description: ${description}\n" +
                " *\n" +
                " * @author: Fang Jiale\n" +
                " *\n" +
                " * @create: 2020-10-24 17:05\n" +
                " **/\n" +
                "import java.io.*;\n" +
                "\n" +
                "public class FileInput {\n" +
                "\n" +
                "    public String readString(String FI){\n" +
                "        int len=0;\n" +
                "        StringBuffer str=new StringBuffer(\"\");\n" +
                "        File file = new File(FI);\n" +
                "        try {\n" +
                "            FileInputStream fileInputStream = new FileInputStream(file);\n" +
                "            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);\n" +
                "            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);\n" +
                "            String line=null;\n" +
                "            while((line=bufferedReader.readLine())!=null){\n" +
                "                if (len!=0){\n" +
                "                    str.append(\"\\r\\n\"+line);\n" +
                "                }else {\n" +
                "                    str.append(line);\n" +
                "                }\n" +
                "                len++;\n" +
                "            }\n" +
                "            bufferedReader.close();\n" +
                "            fileInputStream.close();\n" +
                "        } catch (FileNotFoundException e) {\n" +
                "            e.printStackTrace();\n" +
                "        } catch (IOException e) {\n" +
                "            e.printStackTrace();\n" +
                "        }\n" +
                "        return str.toString();\n" +
                "    }\n" +
                "\n" +
                "    public static void main(String[] args) {\n" +
                "        FileInput fileInput = new FileInput();\n" +
                "        String s = fileInput.readString(\"G:\\\\orig.txt\");\n" +
                "        System.out.println(s);\n" +
                "    }\n" +
                "\n" +
                "}\n" +
                "\n" +
                "```\n" +
                "\n" +
                "## 二、测试\n" +
                "### 2.1 单元测试\n" +
                "这次测试只完成了空指针异常的测试，还应包括读写文件错误异常的测试。（后面有时间再commit）\n" +
                "\n" +
                "```java\n" +
                "package pers.fjl.test;\n" +
                "\n" +
                "import org.junit.Test;\n" +
                "import pers.fjl.papercheck.file.FileInput;\n" +
                "import pers.fjl.papercheck.service.impl.SimHashImpl;\n" +
                "\n" +
                "import java.math.BigInteger;\n" +
                "\n" +
                "public class AllTest {\n" +
                "    String origin=\"G:\\\\download\\\\app\\\\Git\\\\gitRepos\\\\paperpass\\\\src\\\\main\\\\resources\\\\orig.txt\";\n" +
                "    String[] s={\n" +
                "            \"G:\\\\download\\\\app\\\\Git\\\\gitRepos\\\\paperpass\\\\src\\\\main\\\\resources\\\\orig_0.8_add.txt\",\n" +
                "            \"G:\\\\download\\\\app\\\\Git\\\\gitRepos\\\\paperpass\\\\src\\\\main\\\\resources\\\\orig_0.8_del.txt\",\n" +
                "            \"G:\\\\download\\\\app\\\\Git\\\\gitRepos\\\\paperpass\\\\src\\\\main\\\\resources\\\\orig_0.8_dis_1.txt\",\n" +
                "            \"G:\\\\download\\\\app\\\\Git\\\\gitRepos\\\\paperpass\\\\src\\\\main\\\\resources\\\\orig_0.8_dis_10.txt\",\n" +
                "            \"G:\\\\download\\\\app\\\\Git\\\\gitRepos\\\\paperpass\\\\src\\\\main\\\\resources\\\\orig_0.8_dis_15.txt\"};\n" +
                "\n" +
                "    @org.junit.Test\n" +
                "    public void addTest(){\n" +
                "        FileInput fileInput = new FileInput();\n" +
                "        SimHashImpl hash1 = new SimHashImpl(fileInput.readString(origin), 64);\n" +
                "        hash1.subByDistance(hash1, 3);\n" +
                "        SimHashImpl hash2 = new SimHashImpl(fileInput.readString(s[0]), 64);\n" +
                "        hash2.subByDistance(hash2, 3);\n" +
                "        double distance = hash1.getDistance(hash1.getStrSimHash(),hash2.getStrSimHash());\n" +
                "        System.out.println(\"该文章与原文相似度为：\"+(100-distance*100/128)+\"%\");\n" +
                "    }\n" +
                "\n" +
                "    @org.junit.Test\n" +
                "    public void delTest(){\n" +
                "        FileInput fileInput = new FileInput();\n" +
                "        SimHashImpl hash1 = new SimHashImpl(fileInput.readString(origin), 64);\n" +
                "        hash1.subByDistance(hash1, 3);\n" +
                "        SimHashImpl hash2 = new SimHashImpl(fileInput.readString(s[1]), 64);\n" +
                "        hash2.subByDistance(hash2, 3);\n" +
                "        double distance = hash1.getDistance(hash1.getStrSimHash(),hash2.getStrSimHash());\n" +
                "        System.out.println(\"该文章与原文相似度为：\"+(100-distance*100/128)+\"%\");\n" +
                "    }\n" +
                "\n" +
                "    @org.junit.Test\n" +
                "    public void dis_1Test(){\n" +
                "        FileInput fileInput = new FileInput();\n" +
                "        SimHashImpl hash1 = new SimHashImpl(fileInput.readString(origin), 64);\n" +
                "        hash1.subByDistance(hash1, 3);\n" +
                "        SimHashImpl hash2 = new SimHashImpl(fileInput.readString(s[2]), 64);\n" +
                "        hash2.subByDistance(hash2, 3);\n" +
                "        double distance = hash1.getDistance(hash1.getStrSimHash(),hash2.getStrSimHash());\n" +
                "        System.out.println(\"该文章与原文相似度为：\"+(100-distance*100/128)+\"%\");\n" +
                "    }\n" +
                "\n" +
                "    @org.junit.Test\n" +
                "    public void dis_10Test(){\n" +
                "        FileInput fileInput = new FileInput();\n" +
                "        SimHashImpl hash1 = new SimHashImpl(fileInput.readString(origin), 64);\n" +
                "        hash1.subByDistance(hash1, 3);\n" +
                "        SimHashImpl hash2 = new SimHashImpl(fileInput.readString(s[3]), 64);\n" +
                "        hash2.subByDistance(hash2, 3);\n" +
                "        double distance = hash1.getDistance(hash1.getStrSimHash(),hash2.getStrSimHash());\n" +
                "        System.out.println(\"该文章与原文相似度为：\"+(100-distance*100/128)+\"%\");\n" +
                "    }\n" +
                "\n" +
                "    @org.junit.Test\n" +
                "    public void dis_15Test(){\n" +
                "        FileInput fileInput = new FileInput();\n" +
                "        SimHashImpl hash1 = new SimHashImpl(fileInput.readString(origin), 64);\n" +
                "        hash1.subByDistance(hash1, 3);\n" +
                "        SimHashImpl hash2 = new SimHashImpl(fileInput.readString(s[4]), 64);\n" +
                "        hash2.subByDistance(hash2, 3);\n" +
                "        double distance = hash1.getDistance(hash1.getStrSimHash(),hash2.getStrSimHash());\n" +
                "        System.out.println(\"该文章与原文相似度为：\"+(100-distance*100/128)+\"%\");\n" +
                "    }\n" +
                "\n" +
                "//    @org.junit.Test\n" +
                "//    public void FileNotFoundException(){\n" +
                "//        FileInput fileInput = new FileInput();\n" +
                "//        SimHashImpl hash1 = new SimHashImpl(fileInput.readString(origin), 64);\n" +
                "//        hash1.subByDistance(hash1, 3);\n" +
                "//        SimHashImpl hash2 = new SimHashImpl(fileInput.readString(\"G:\\\\1.txt\"), 64);\n" +
                "//        hash2.subByDistance(hash2, 3);\n" +
                "//        double distance = hash1.getDistance(hash1.getStrSimHash(),hash2.getStrSimHash());\n" +
                "//        System.out.println(\"该文章与原文相似度为：\"+(100-distance*100/128)+\"%\");\n" +
                "//    }\n" +
                "}\n" +
                "\n" +
                "```\n" +
                "\n" +
                "### 2.2 覆盖率\n" +
                "![](https://img-blog.csdnimg.cn/img_convert/0c2a5c0261a9003cde0f79623d4c9a6f.png)\n" +
                "![](https://img-blog.csdnimg.cn/img_convert/bd6bcdaa4425233ab7126e84cd6edc3f.png)\n" +
                "![](https://img-blog.csdnimg.cn/img_convert/d3877fd896402508eb9da877f0fe116b.png)\n" +
                "\n" +
                "## 三、性能检测\n" +
                "![](https://img-blog.csdnimg.cn/img_convert/a9ee30d2baea873fe12a69b8ae55f40d.png)\n" +
                "![](https://img-blog.csdnimg.cn/img_convert/30a3812d85bb450e5409b199e46bb09e.png)\n" +
                "![](https://img-blog.csdnimg.cn/img_convert/7687ef8cb388b8afcf5d5ec7a4759599.png)\n" +
                "对该性能分析工具的使用还不太熟练，但可以看见的是，使用了GC之后，char,与String依旧占据内存的大部分。\n" +
                "![](https://img-blog.csdnimg.cn/img_convert/ed82db430614426a86b7982b93e8a322.png)";
        System.out.println(markdownToHtmlExtensions(b));
    }
}
