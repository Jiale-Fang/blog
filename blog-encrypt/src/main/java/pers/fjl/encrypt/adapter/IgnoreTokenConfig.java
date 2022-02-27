package pers.fjl.encrypt.adapter;
import java.util.Arrays;
import java.util.List;
import org.springframework.util.AntPathMatcher;
/**
 * 忽略token 配置类
 */
public class IgnoreTokenConfig {
    public static final List<String> LIST = Arrays.asList(
            "/error",
            "/gate/**",
            "/static/**",
            "/**/admapi/**",
            "/**/admin/chat/**",
            "/**/login/**",
            "/**/logout/**",
            "/**/blog/**",
            "/**/message/**",
            "/**/home/**",
            "/**/typeShow/**",
            "/**/tagShow/**",
            "/**/link/**",
            "/**/crawler/**",
            "/**/crawler/**",
            "/**/comment/**",
            "/**/search/**",
            "/**/archives/**",
            "/**/groupChat/**",
            "/**/friends/**",
            "/**/chatLog/**",
            "/**/user/**",
            "/**/type/**",
            "/**/tag/**",
            "/**//**",
            "/**/swagger-ui.html",
            "/**/doc.html",
            "/**/webjars/**",
            "/**/v2/api-docs/**",
            "/**/v2/api-docs-ext/**",
            "/**/swagger-resources/**",
            "/menu/router/**"
    );
    private static final AntPathMatcher ANT_PATH_MATCHER = new AntPathMatcher();

    public static boolean isIgnoreToken(String currentUri) {
        return isIgnore(LIST, currentUri);
    }

    public static boolean isIgnore(List<String> list, String currentUri) {
        if (list.isEmpty()) {
            return false;
        }
        if (currentUri.startsWith("/**/admin/**") || ANT_PATH_MATCHER.match("/**/admin/**", currentUri)){
            return false;
        }
        for (String url : list) {
            if (currentUri.startsWith(url) || ANT_PATH_MATCHER.match(url, currentUri)) {
                return true;
            }
        }
        return false;
    }
}
