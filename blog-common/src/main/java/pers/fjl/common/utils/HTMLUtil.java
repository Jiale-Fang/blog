package pers.fjl.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * html标签处理工具类
 * （html中的标签对分词毫无意义，所以要做处理）
 */
public class HTMLUtil {
    public static String delHTMLTag(String htmlStr) {
        String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; //定义 script的正则表达式
        String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>";//定义 style的正则表达式
        String regEx_html = "<[^>]+>";// 定义HTML标签的正则表达式

        Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(htmlStr);
        htmlStr = m_script.replaceAll("");//过滤script标签

        Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(htmlStr);
        htmlStr = m_style.replaceAll("");//过滤style标签

        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll("");//过滤html标签
        htmlStr = htmlStr.replaceAll ("nbsp", "");
        htmlStr = htmlStr.replaceAll ("lt;", "");
        htmlStr = htmlStr.replaceAll ("gt;", "");
        return htmlStr.trim();//返回文本字符串
    }
}
