package org.example.support;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.stereotype.Service;

@Service
public interface HtmlTextSupport {

    public static String replaceHtmlBasicTag(String content, String replacement) {
    
        String hml = content;
        if (hml == null) return "";
       
        if (hml.contains("iframe")) {
        	return hml;
        }
        
        Matcher htmlTag = Pattern.compile("<script [^>]*>(.*?)</script>",
                Pattern.DOTALL | Pattern.CASE_INSENSITIVE | Pattern.COMMENTS).matcher(hml);

        if (htmlTag.find()) {
            hml = htmlTag.replaceAll(replacement);
        }
        htmlTag = Pattern.compile("<style [^>]*>(.*?)</style>",
                Pattern.DOTALL | Pattern.CASE_INSENSITIVE | Pattern.COMMENTS).matcher(hml);
        if (htmlTag.find()) {
            hml = htmlTag.replaceAll(replacement);
        }
        htmlTag = Pattern.compile("<link [^>]*>(.*?)</link>",
                Pattern.DOTALL | Pattern.CASE_INSENSITIVE | Pattern.COMMENTS).matcher(hml);
        if (htmlTag.find()) {
            hml = htmlTag.replaceAll(replacement);
        }
        htmlTag = Pattern.compile("<([^>]*)>", Pattern.DOTALL | Pattern.CASE_INSENSITIVE).matcher(hml);
        if (htmlTag.find()) {
            hml = htmlTag.replaceAll(replacement);
        }

        return hml;
    }

}
