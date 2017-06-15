package com.zbaccp.bananaplan.util;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangbin on 2017/4/9.
 */
public class SimilarityAnalysis {
    private String content1;
    private String content2;

    public SimilarityAnalysis() {

    }

    public SimilarityAnalysis(String text1, String text2) {
        content1 = text1;
        content2 = text2;
    }

    public String tidy(String text) {
        return text.replaceAll("//.*", "").replaceAll("\\r|\\n", " ").replaceAll("/\\*.*?\\*/", "").replaceAll("\\s+", " ").trim();
    }

    public int check(String text1, String text2) {
        int similar = 0;

        content1 = text1;
        content2 = text2;

        System.out.println(content1);
        try {
            System.out.println(content1.equals(new String(content1.getBytes("utf-8"), "utf-8")));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println(content2);
        try {
            System.out.println(content2.equals(new String(content2.getBytes("utf-8"), "utf-8")));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        String content1Tidy = tidy(text1);
        String content2Tidy = tidy(text2);

        if (content1Tidy.equals(content2Tidy)) {
            similar += 80;
        } else if (content1Tidy.equalsIgnoreCase(content2Tidy)) {
            similar += 75;
        } else {
            similar += calcSimilarWords(content1Tidy.split(" "), content2Tidy.split(" "));
        }

        return similar;
    }

    private int calcSimilarWords(String[] words1, String[] words2) {
        int same = 0;

        HashMap<String, Integer> map1 = calcSameWordCount(words1);
        HashMap<String, Integer> map2 = calcSameWordCount(words2);

        int sameCount = 0;
        for (Map.Entry<String, Integer> entry : map1.entrySet()) {
            if (map2.containsKey(entry.getKey())) {
                int singleSameCount = entry.getValue() < map2.get(entry.getKey()) ? entry.getValue() : map2.get(entry.getKey());
                sameCount += singleSameCount * 2;
            }
        }

//        System.out.println(words1.length + " - " + words2.length);
//        System.out.println(words1.length + words2.length - sameCount);

        return (int) ((double) sameCount / (words1.length + words2.length) * 0.75 * 100);
    }

    private HashMap<String, Integer> calcSameWordCount(String[] words) {
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        for (int i = 0; i < words.length; i++) {
            int count = 1;
            if (map.get(words[i]) != null) {
                count = map.get(words[i]) + 1;
            }
            map.put(words[i], count);
        }

        return map;
    }

    public static void main(String[] args) {
        SimilarityAnalysis sa = new SimilarityAnalysis();
        sa.calcSimilarWords(new String[]{"int", "num1", "int", "num2"}, new String[]{"int", "num1"});
    }

}
