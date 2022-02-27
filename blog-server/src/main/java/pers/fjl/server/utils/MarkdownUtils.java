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
        String b="&ensp;页面展示如下↓\n" +
                "![在这里插入图片描述](https://img-blog.csdnimg.cn/20210121194516152.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0RsaWhjdGNlZnJlcA==,size_16,color_FFFFFF,t_70)\n" +
                "本demo采用前后端分离服务器的方式完成，首先先从后端开始。\n" +
                "\n" +
                "@[TOC]( )\n" +
                "\n" +
                "## 1、后端开发\n" +
                "### 1.1 如何爬取到京东的数据？\n" +
                "\n" +
                "&ensp;我们使用的是jsoup来解析浏览器的数据，在爬取数据之前，我们要先明白，我们需要的数据是什么，因此让我们来看看京东的商品搜索页面。\n" +
                "\n" +
                "![在这里插入图片描述](https://img-blog.csdnimg.cn/20210121200720531.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0RsaWhjdGNlZnJlcA==,size_16,color_FFFFFF,t_70)\n" +
                "&ensp;初步看，如果要完成爬取数据到我们的页面上，至少需要获取到商品的标题，图片以及价格，因此F12查看一下网页源码。\n" +
                "![在这里插入图片描述](https://img-blog.csdnimg.cn/20210121201158477.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0RsaWhjdGNlZnJlcA==,size_16,color_FFFFFF,t_70)\n" +
                "&ensp;易知，在J_goodsList标签下，一个个li标签中就有我们想要获取的数据，因此我们可以开始编写后端代码。\n" +
                "![在这里插入图片描述](https://img-blog.csdnimg.cn/20210121201744799.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0RsaWhjdGNlZnJlcA==,size_16,color_FFFFFF,t_70)\n" +
                "### 1.2 相关依赖的导入\n" +
                "&ensp;在pom文件引入jsoup和springboot、elasticSearch的依赖\n" +
                "\n" +
                "```java\n" +
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<project xmlns=\"http://maven.apache.org/POM/4.0.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
                "         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd\">\n" +
                "    <modelVersion>4.0.0</modelVersion>\n" +
                "    <parent>\n" +
                "        <groupId>org.springframework.boot</groupId>\n" +
                "        <artifactId>spring-boot-starter-parent</artifactId>\n" +
                "        <version>2.4.2</version>\n" +
                "        <relativePath/> <!-- lookup parent from repository -->\n" +
                "    </parent>\n" +
                "    <groupId>pers.fjl</groupId>\n" +
                "    <artifactId>es-jd</artifactId>\n" +
                "    <version>0.0.1-SNAPSHOT</version>\n" +
                "    <name>es-jd</name>\n" +
                "    <description>Demo project for Spring Boot</description>\n" +
                "    <properties>\n" +
                "        <java.version>1.8</java.version>\n" +
                "        <!--自定义es版本依赖否则会出bug-->\n" +
                "        <elasticsearch.version>7.6.1</elasticsearch.version>\n" +
                "    </properties>\n" +
                "    <dependencies>\n" +
                "<!--        解析网页-->\n" +
                "        <dependency>\n" +
                "            <groupId>org.jsoup</groupId>\n" +
                "            <artifactId>jsoup</artifactId>\n" +
                "            <version>1.10.2</version>\n" +
                "        </dependency>\n" +
                "        <dependency>\n" +
                "            <groupId>com.alibaba</groupId>\n" +
                "            <artifactId>fastjson</artifactId>\n" +
                "            <version>1.2.56</version>\n" +
                "        </dependency>\n" +
                "        <dependency>\n" +
                "            <groupId>org.springframework.boot</groupId>\n" +
                "            <artifactId>spring-boot-starter-data-elasticsearch</artifactId>\n" +
                "        </dependency>\n" +
                "        <dependency>\n" +
                "            <groupId>org.springframework.boot</groupId>\n" +
                "            <artifactId>spring-boot-starter-thymeleaf</artifactId>\n" +
                "        </dependency>\n" +
                "        <dependency>\n" +
                "            <groupId>org.springframework.boot</groupId>\n" +
                "            <artifactId>spring-boot-starter-web</artifactId>\n" +
                "        </dependency>\n" +
                "\n" +
                "        <dependency>\n" +
                "            <groupId>org.springframework.boot</groupId>\n" +
                "            <artifactId>spring-boot-devtools</artifactId>\n" +
                "            <scope>runtime</scope>\n" +
                "            <optional>true</optional>\n" +
                "        </dependency>\n" +
                "        <dependency>\n" +
                "            <groupId>org.springframework.boot</groupId>\n" +
                "            <artifactId>spring-boot-configuration-processor</artifactId>\n" +
                "            <optional>true</optional>\n" +
                "        </dependency>\n" +
                "        <dependency>\n" +
                "            <groupId>org.projectlombok</groupId>\n" +
                "            <artifactId>lombok</artifactId>\n" +
                "            <optional>true</optional>\n" +
                "        </dependency>\n" +
                "        <dependency>\n" +
                "            <groupId>org.springframework.boot</groupId>\n" +
                "            <artifactId>spring-boot-starter-test</artifactId>\n" +
                "            <scope>test</scope>\n" +
                "        </dependency>\n" +
                "    </dependencies>\n" +
                "\n" +
                "    <build>\n" +
                "        <plugins>\n" +
                "            <plugin>\n" +
                "                <groupId>org.springframework.boot</groupId>\n" +
                "                <artifactId>spring-boot-maven-plugin</artifactId>\n" +
                "                <configuration>\n" +
                "                    <excludes>\n" +
                "                        <exclude>\n" +
                "                            <groupId>org.projectlombok</groupId>\n" +
                "                            <artifactId>lombok</artifactId>\n" +
                "                        </exclude>\n" +
                "                    </excludes>\n" +
                "                </configuration>\n" +
                "            </plugin>\n" +
                "        </plugins>\n" +
                "    </build>\n" +
                "\n" +
                "</project>\n" +
                "\n" +
                "```\n" +
                "### 1.3 Jsoup工具类的编写\n" +
                "&ensp;把获取的数据字段封装到Content实体类，加入到list中后返回。\n" +
                "```java\n" +
                "package pers.fjl.utils;\n" +
                "\n" +
                "import org.jsoup.Jsoup;\n" +
                "import org.jsoup.nodes.Document;\n" +
                "import org.jsoup.nodes.Element;\n" +
                "import org.jsoup.select.Elements;\n" +
                "import pers.fjl.po.Content;\n" +
                "import java.io.IOException;\n" +
                "import java.net.URL;\n" +
                "import java.util.ArrayList;\n" +
                "import java.util.List;\n" +
                "\n" +
                "public class HtmlParseUtil {\n" +
                "    public static void main(String[] args) throws IOException{\n" +
                "        new HtmlParseUtil().parseJD(\"java\").forEach(System.out::println);\n" +
                "    }\n" +
                "\n" +
                "    public List<Content> parseJD(String keyword) throws IOException {\n" +
                "        // 获取请求 https://search.jd.com/Search?keyword=java\n" +
                "        String url = \"https://search.jd.com/Search?keyword=\" + keyword + \"&enc=utf-8\";\n" +
                "        // 解析网页,返回浏览器document页面对象\n" +
                "        Document document = Jsoup.parse(new URL(url), 30000);\n" +
                "        // 所有js能用的方法在这都可使用\n" +
                "        Element element = document.getElementById(\"J_goodsList\");   // 获取div标签J_goodsList\n" +
                "        System.out.println(element.html());\n" +
                "        // 数据都在li标签中\n" +
                "        Elements lis = element.getElementsByTag(\"li\");\n" +
                "\n" +
                "        ArrayList<Content> goodsList = new ArrayList<>();\n" +
                "\n" +
                "        for (Element el : lis) {\n" +
                "            // 网页采用了懒加载，所以在src中无法直接获取\n" +
                "            String img = el.getElementsByTag(\"img\").eq(0).attr(\"data-lazy-img\");\n" +
                "            String price = el.getElementsByClass(\"p-price\").eq(0).text();\n" +
                "            String title = el.getElementsByClass(\"p-name\").eq(0).text();\n" +
                "\n" +
                "            Content content = new Content();\n" +
                "            content.setImg(img);\n" +
                "            content.setPrice(price);\n" +
                "            content.setTitle(title);\n" +
                "            goodsList.add(content);\n" +
                "        }\n" +
                "\n" +
                "        return goodsList;\n" +
                "    }\n" +
                "}\n" +
                "\n" +
                "```\n" +
                "### 1.4 将返回的list加入到elasticSearch中\n" +
                "返回的list可以选择放入到数据库或者elasticSearch中，这里展示后者。\n" +
                "\n" +
                "```java\n" +
                "@Service\n" +
                "public class ContentService {\n" +
                "    @Resource\n" +
                "    private RestHighLevelClient restHighLevelClient;\n" +
                "\n" +
                "    // 1、解析数据放入es索引中\n" +
                "    public Boolean parseContent(String keyword) throws Exception {\n" +
                "        List<Content> contents = new HtmlParseUtil().parseJD(keyword);\n" +
                "        // 把查询的数据放入到es中\n" +
                "        BulkRequest bulkRequest = new BulkRequest();\n" +
                "        bulkRequest.timeout(\"2m\");  // 超市时间2min\n" +
                "\n" +
                "        for (int i = 0; i < contents.size(); i++) {\n" +
                "            // 批量添加\n" +
                "            bulkRequest.add(new IndexRequest(ESconst.ES_INDEX)  // 存到jd_goods索引中\n" +
                "                    .source(JSON.toJSONString(contents.get(i)), XContentType.JSON));\n" +
                "        }\n" +
                "\n" +
                "        BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, RequestOptions.DEFAULT);\n" +
                "        return !bulk.hasFailures();\n" +
                "    }\n" +
                "}\n" +
                "```\n" +
                "&ensp;controller层简单的两个方法，\n" +
                "\n" +
                "```java\n" +
                "package pers.fjl.controller;\n" +
                "\n" +
                "import org.springframework.web.bind.annotation.CrossOrigin;\n" +
                "import org.springframework.web.bind.annotation.GetMapping;\n" +
                "import org.springframework.web.bind.annotation.PathVariable;\n" +
                "import org.springframework.web.bind.annotation.RestController;\n" +
                "import pers.fjl.service.ContentService;\n" +
                "\n" +
                "import javax.annotation.Resource;\n" +
                "import java.io.IOException;\n" +
                "import java.util.List;\n" +
                "import java.util.Map;\n" +
                "\n" +
                "@CrossOrigin\n" +
                "@RestController\n" +
                "public class ContentController {\n" +
                "    @Resource\n" +
                "    private ContentService contentService;\n" +
                "\n" +
                "    @GetMapping(\"/parse/{keyword}\")\n" +
                "    public Boolean parse(@PathVariable(\"keyword\") String keyword) throws Exception {\n" +
                "        return contentService.parseContent(keyword);\n" +
                "    }\n" +
                "\n" +
                "    @GetMapping(\"/search/{keyword}/{currentPage}/{pageSize}\")\n" +
                "    public List<Map<String, Object>> search(@PathVariable(\"keyword\") String keyword,\n" +
                "                                            @PathVariable(\"currentPage\") int currentPage,\n" +
                "                                            @PathVariable(\"pageSize\") int pageSize) throws IOException {\n" +
                "        return contentService.highLightSearchPage(keyword, currentPage, pageSize);\n" +
                "    }\n" +
                "\n" +
                "}\n" +
                "\n" +
                "```\n" +
                "&ensp;请求对应路径调用控制层方法\n" +
                "![在这里插入图片描述](https://img-blog.csdnimg.cn/20210121204009511.png)\n" +
                "&ensp;此处可看到，数据已经添加到es索引中。\n" +
                "![在这里插入图片描述](https://img-blog.csdnimg.cn/2021012120420368.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0RsaWhjdGNlZnJlcA==,size_16,color_FFFFFF,t_70)\n" +
                "### 1.5 将es中的数据返回到前端\n" +
                "&ensp;其实只需要把需要的数据从mysql中或者es索引中拿出并封装到list返回即可。\n" +
                "```java\n" +
                "package pers.fjl.service;\n" +
                "\n" +
                "import com.alibaba.fastjson.JSON;\n" +
                "import org.elasticsearch.action.bulk.BulkRequest;\n" +
                "import org.elasticsearch.action.bulk.BulkResponse;\n" +
                "import org.elasticsearch.action.index.IndexRequest;\n" +
                "import org.elasticsearch.action.search.SearchRequest;\n" +
                "import org.elasticsearch.action.search.SearchResponse;\n" +
                "import org.elasticsearch.client.RequestOptions;\n" +
                "import org.elasticsearch.client.RestHighLevelClient;\n" +
                "import org.elasticsearch.common.text.Text;\n" +
                "import org.elasticsearch.common.unit.TimeValue;\n" +
                "import org.elasticsearch.common.xcontent.XContentType;\n" +
                "import org.elasticsearch.index.query.QueryBuilders;\n" +
                "import org.elasticsearch.index.query.TermQueryBuilder;\n" +
                "import org.elasticsearch.search.SearchHit;\n" +
                "import org.elasticsearch.search.builder.SearchSourceBuilder;\n" +
                "import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;\n" +
                "import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;\n" +
                "import org.springframework.stereotype.Service;\n" +
                "import pers.fjl.po.Content;\n" +
                "import pers.fjl.utils.ESconst;\n" +
                "import pers.fjl.utils.HtmlParseUtil;\n" +
                "import javax.annotation.Resource;\n" +
                "import java.io.IOException;\n" +
                "import java.util.ArrayList;\n" +
                "import java.util.List;\n" +
                "import java.util.Map;\n" +
                "import java.util.concurrent.TimeUnit;\n" +
                "\n" +
                "@Service\n" +
                "public class ContentService {\n" +
                "    @Resource\n" +
                "    private RestHighLevelClient restHighLevelClient;\n" +
                "\n" +
                "    // 3、获取这些数据实现搜索高亮功能\n" +
                "    public List<Map<String, Object>> highLightSearchPage(String keyword, int currentPage, int pageSize) throws IOException {\n" +
                "        if (currentPage <= 1) {\n" +
                "            currentPage = 1;\n" +
                "        }\n" +
                "\n" +
                "        // 条件搜索\n" +
                "        SearchRequest searchRequest = new SearchRequest(\"jd_goods\");\n" +
                "        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();\n" +
                "\n" +
                "        // 分页\n" +
                "        // 从第几条数据开始\n" +
                "        sourceBuilder.from((currentPage - 1) * pageSize);\n" +
                "        sourceBuilder.size(pageSize);\n" +
                "\n" +
                "        // 精准匹配\n" +
                "        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery(\"title\", keyword);\n" +
                "        sourceBuilder.query(termQueryBuilder);\n" +
                "        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));\n" +
                "\n" +
                "        // 高亮\n" +
                "        HighlightBuilder highlightBuilder = new HighlightBuilder();\n" +
                "        highlightBuilder.field(\"title\");\n" +
                "        highlightBuilder.requireFieldMatch(false); // 只高亮显示第一个即可\n" +
                "        highlightBuilder.preTags(\"<span style = 'color:red'>\");\n" +
                "        highlightBuilder.postTags(\"</span>\");\n" +
                "        sourceBuilder.highlighter(highlightBuilder);\n" +
                "\n" +
                "        // 执行搜素\n" +
                "        searchRequest.source(sourceBuilder);\n" +
                "        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);\n" +
                "\n" +
                "        // 解析结果\n" +
                "        ArrayList<Map<String, Object>> list = new ArrayList<>();\n" +
                "        for (SearchHit hit : searchResponse.getHits().getHits()) {  //hits中的字段没有被高亮，因此要将es返回highlight中的高亮字段替换到hit中\n" +
                "            // 解析高亮字段\n" +
                "            Map<String, HighlightField> highlightFields = hit.getHighlightFields();\n" +
                "            HighlightField title = highlightFields.get(\"title\");\n" +
                "            Map<String, Object> sourceAsMap = hit.getSourceAsMap(); // 原来的字段结果\n" +
                "            if (title != null) {   // 解析高亮字段，将原来字段替换为高亮字段\n" +
                "                Text[] fragments = title.fragments();\n" +
                "\n" +
                "                String n_title = \"\";\n" +
                "                for (Text text : fragments) {\n" +
                "                    n_title += text;\n" +
                "                }\n" +
                "                sourceAsMap.put(\"title\", n_title); //高亮字段替换掉原来内容\n" +
                "            }\n" +
                "            sourceAsMap.put(\"id\",hit.getId());\n" +
                "            list.add(hit.getSourceAsMap());\n" +
                "        }\n" +
                "        return list;\n" +
                "    }\n" +
                "}\n" +
                "\n" +
                "```\n" +
                "\n" +
                "## 2、前端开发\n" +
                "### 2.1 页面的编写\n" +
                "&ensp;前端代码写的比较简洁，不难看懂，在此不做赘述。\n" +
                "\n" +
                "main.js\n" +
                "```javascript\n" +
                "import Vue from 'vue'\n" +
                "import App from './App.vue'\n" +
                "import router from './router'\n" +
                "import axios from 'axios'\n" +
                "// 导入全局样式表\n" +
                "import './assets/css/global.css'\n" +
                "import ElementUI from 'element-ui'\n" +
                "import 'element-ui/lib/theme-chalk/index.css'\n" +
                "\n" +
                "Vue.use(ElementUI)\n" +
                "\n" +
                "Vue.config.productionTip = false\n" +
                "Vue.prototype.$http = axios\n" +
                "// 配置请求的跟路径\n" +
                "axios.defaults.baseURL = 'http://127.0.0.1:9090/'\n" +
                "\n" +
                "new Vue({\n" +
                "  router,\n" +
                "  el: '#app',\n" +
                "  render: h => h(App)\n" +
                "}).$mount('#app')\n" +
                "\n" +
                "```\n" +
                "商品详情页的vue\n" +
                "```javascript\n" +
                "<template>\n" +
                "  <div>\n" +
                "        <div id=\"mallPage\" class=\" mallist tmall- page-not-market \">\n" +
                "\n" +
                "          <!-- 头部搜索 -->\n" +
                "          <div id=\"header\" class=\" header-list-app\">\n" +
                "            <div class=\"headerLayout\">\n" +
                "              <div class=\"headerCon \">\n" +
                "                <!-- Logo-->\n" +
                "                <h1 id=\"mallLogo\">\n" +
                "                  <img src=\"../assets/images/jdlogo.png\" alt=\"\">\n" +
                "                </h1>\n" +
                "\n" +
                "                <div class=\"header-extra\">\n" +
                "\n" +
                "                  <!--搜索-->\n" +
                "                  <div id=\"mallSearch\" class=\"mall-search\">\n" +
                "                    <form name=\"searchTop\" class=\"mallSearch-form clearfix\">\n" +
                "                      <fieldset>\n" +
                "                        <legend>天猫搜索</legend>\n" +
                "                        <div class=\"mallSearch-input clearfix\">\n" +
                "                          <div class=\"s-combobox\" id=\"s-combobox-685\">\n" +
                "                            <div class=\"s-combobox-input-wrap\">\n" +
                "                              <input v-model=\"pagination.keyword\" type=\"text\" autocomplete=\"off\" value=\"dd\" id=\"mq\"\n" +
                "                                     class=\"s-combobox-input\" aria-haspopup=\"true\">\n" +
                "                            </div>\n" +
                "                          </div>\n" +
                "                          <button type=\"submit\" @click=\"searchItem\" id=\"searchbtn\">搜索</button>\n" +
                "                        </div>\n" +
                "                      </fieldset>\n" +
                "                    </form>\n" +
                "                    <ul class=\"relKeyTop\">\n" +
                "                      <li><a>Java</a></li>\n" +
                "                      <li><a>前端</a></li>\n" +
                "                      <li><a>Linux</a></li>\n" +
                "                      <li><a>大数据</a></li>\n" +
                "                      <li><a>理财</a></li>\n" +
                "                    </ul>\n" +
                "                  </div>\n" +
                "                </div>\n" +
                "              </div>\n" +
                "            </div>\n" +
                "          </div>\n" +
                "\n" +
                "          <!-- 商品详情页面 -->\n" +
                "          <div id=\"content\">\n" +
                "            <div class=\"main\">\n" +
                "              <!-- 品牌分类 -->\n" +
                "              <form class=\"navAttrsForm\">\n" +
                "                <div class=\"attrs j_NavAttrs\" style=\"display:block\">\n" +
                "                  <div class=\"brandAttr j_nav_brand\">\n" +
                "                    <div class=\"j_Brand attr\">\n" +
                "                      <div class=\"attrKey\">\n" +
                "                        品牌\n" +
                "                      </div>\n" +
                "                      <div class=\"attrValues\">\n" +
                "                        <ul class=\"av-collapse row-2\">\n" +
                "                          <li><a href=\"#\"> vue </a></li>\n" +
                "                          <li><a href=\"#\"> Java </a></li>\n" +
                "                        </ul>\n" +
                "                      </div>\n" +
                "                    </div>\n" +
                "                  </div>\n" +
                "                </div>\n" +
                "              </form>\n" +
                "\n" +
                "              <!-- 排序规则 -->\n" +
                "              <div class=\"filter clearfix\">\n" +
                "                <a class=\"fSort fSort-cur\">综合<i class=\"f-ico-arrow-d\"></i></a>\n" +
                "                <a class=\"fSort\">人气<i class=\"f-ico-arrow-d\"></i></a>\n" +
                "                <a class=\"fSort\">新品<i class=\"f-ico-arrow-d\"></i></a>\n" +
                "                <a class=\"fSort\">销量<i class=\"f-ico-arrow-d\"></i></a>\n" +
                "                <a class=\"fSort\">价格<i class=\"f-ico-triangle-mt\"></i><i class=\"f-ico-triangle-mb\"></i></a>\n" +
                "              </div>\n" +
                "\n" +
                "              <!-- 商品详情 -->\n" +
                "              <div class=\"view grid-nosku\">\n" +
                "\n" +
                "                <div class=\"product\" v-for=\"result in results\" v-bind:key=\"result.id\">\n" +
                "                  <div class=\"product-iWrap\">\n" +
                "                    <!--商品封面-->\n" +
                "                    <div class=\"productImg-wrap\">\n" +
                "                      <a class=\"productImg\">\n" +
                "                        <img v-bind:src = \"result.img\">\n" +
                "                      </a>\n" +
                "                    </div>\n" +
                "                    <!--价格-->\n" +
                "                    <p class=\"productPrice\">\n" +
                "                      <em><b>¥</b>{{result.price}}</em>\n" +
                "                    </p>\n" +
                "                    <!--标题-->\n" +
                "                    <p class=\"productTitle\">\n" +
                "                      <a v-html=\"result.title\"></a>\n" +
                "                    </p>\n" +
                "                    <!-- 店铺名 -->\n" +
                "                    <div class=\"productShop\">\n" +
                "                      <span>店铺： Java </span>\n" +
                "                    </div>\n" +
                "                    <!-- 成交信息 -->\n" +
                "                    <p class=\"productStatus\">\n" +
                "                      <span>月成交<em>999笔</em></span>\n" +
                "                      <span>评价 <a>3</a></span>\n" +
                "                    </p>\n" +
                "                  </div>\n" +
                "                </div>\n" +
                "              </div>\n" +
                "            </div>\n" +
                "          </div>\n" +
                "          <div>\n" +
                "\n" +
                "          </div>\n" +
                "        </div>\n" +
                "    <!-- 卡片视图区域 -->\n" +
                "    <el-card>\n" +
                "      <!-- 搜索与添加区域 -->\n" +
                "      <el-pagination align=\"center\"\n" +
                "        class=\"pagination\"\n" +
                "        @size-change=\"handleSizeChange\"\n" +
                "        @current-change=\"handleCurrentChange\"\n" +
                "        :current-page=\"pagination.currentPage\"\n" +
                "        :page-sizes=\"[2,5,10,15]\"\n" +
                "        :page-size=\"pagination.pageSize\"\n" +
                "        layout=\"total, sizes, prev, pager, next, jumper\"\n" +
                "        :total=\"100\">\n" +
                "      </el-pagination>\n" +
                "    </el-card>\n" +
                "  </div>\n" +
                "</template>\n" +
                "\n" +
                "<script>\n" +
                "// import '../assets/js/jquery.min'\n" +
                "export default {\n" +
                "  data () {\n" +
                "    return {\n" +
                "      test: 1,\n" +
                "      pagination: { // 分页相关模型数据\n" +
                "        currentPage: 1, // 当前页码\n" +
                "        pageSize: 10, // 每页显示的记录数\n" +
                "        total: 50, // 总记录数\n" +
                "        keyword: null // 查询条件\n" +
                "      },\n" +
                "      results: [] // 搜索结果\n" +
                "    }\n" +
                "  },\n" +
                "  methods: {\n" +
                "    // 监听 switch 开关状态的改变\n" +
                "    async searchItem () {\n" +
                "      const keyword = this.pagination.keyword\n" +
                "      const pageSize = this.pagination.pageSize\n" +
                "      const currentPage = this.pagination.currentPage\n" +
                "      const { data: res } = await this.$http.get(\n" +
                "        `/search/${keyword}/${currentPage}/${pageSize}`\n" +
                "      )\n" +
                "      console.log(res)\n" +
                "      this.results = res\n" +
                "    },\n" +
                "    // 切换页码\n" +
                "    handleCurrentChange (currentPage) {\n" +
                "      // 设置最新的页码\n" +
                "      this.pagination.currentPage = currentPage\n" +
                "      // 重新调用分页方法进行分页查询\n" +
                "      this.searchItem()\n" +
                "    },\n" +
                "    handleSizeChange (newSize) {\n" +
                "      this.pagination.pageSize = newSize\n" +
                "      this.searchItem()\n" +
                "    }\n" +
                "  }\n" +
                "}\n" +
                "</script>\n" +
                "\n" +
                "<style>\n" +
                "  /*@import \"../assets/css/style.css\";*/\n" +
                "</style>\n" +
                "\n" +
                "```\n";
        System.out.println(markdownToHtmlExtensions(b));
    }
}
