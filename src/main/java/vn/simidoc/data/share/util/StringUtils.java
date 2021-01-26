package vn.simidoc.data.share.util;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class StringUtils {

    public static String standardizeText(String text) {
        StringBuilder result = new StringBuilder();
        if(text.isEmpty()) return "";
        char[] chars = text.toCharArray();
        StringBuilder builder = new StringBuilder();
        int i = -1;
        while(++i<chars.length){
            char c = chars[i];
            if(!Character.isLetterOrDigit(c)){
                if(builder.length()>0) {
                    result.append(builder.toString()).append(" ");
                    builder = new StringBuilder();
                }
            } else {
                builder.append(c);
            }
        }
        if(builder.length()>0){
            result.append(builder.toString()).append(" ");
        }
        return result.toString().trim();
    }

    public static List<String> extractSentence(String content){
        List<String> sentences= new ArrayList<>();
        BreakIterator iterator = BreakIterator.getSentenceInstance(Locale.ENGLISH);
        iterator.setText(content);
        int start = iterator.first();
        for (int end = iterator.next();end != BreakIterator.DONE; start = end, end = iterator.next()) {
            sentences.add(content.substring(start,end));
        }
        return sentences;
    }
}
