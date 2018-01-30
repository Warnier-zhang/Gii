package org.warnier.zhang.gii.util;

import difflib.DiffRow;
import difflib.DiffRowGenerator;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StringUtils {
    /**
     * 比较两个字符串数组的差别。
     *
     * @param a
     * @param b
     * @return
     */
    public static String diff(List<String> a, List<String> b) {
        StringBuilder sb = new StringBuilder();

        DiffRowGenerator generator = new DiffRowGenerator.Builder()
                .showInlineDiffs(true)
                .columnWidth(Integer.MAX_VALUE)
                .build();
        List<DiffRow> rows = generator.generateDiffRows(a, b);
        if (rows != null && rows.size() > 0) {
            for (DiffRow row : rows) {
                if (row.getTag() == DiffRow.Tag.INSERT) {
                    sb.append("<ins>" + row.getNewLine() + "</ins><br/>");
                } else if (row.getTag() == DiffRow.Tag.DELETE) {
                    sb.append("<del>" + row.getOldLine() + "</del><br/>");
                } else if (row.getTag() == DiffRow.Tag.CHANGE) {

                }
            }
        }
        return sb.toString();
    }

    public static boolean isEmpty(String text) {
        return org.apache.commons.lang3.StringUtils.isEmpty(text);
    }

    /**
     * 首字母大写。
     *
     * @return
     */
    public static String upperCase(String text) {
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }

    /**
     * 首字母小写。
     *
     * @return
     */
    public static String lowerCase(String text) {
        return text.substring(0, 1).toLowerCase() + text.substring(1);
    }

    /**
     * 把以下划线（_）等分割的单词转成符合驼峰命名规则的字符串。
     *
     * @param text
     * @return
     */
    public static String toCamelCase(String text, String separator) {
        StringBuilder sb = new StringBuilder();

        String[] texts = text.split(separator);
        if (texts.length > 1) {
            sb.append(texts[0].toLowerCase());
            for (int i = 1; i < texts.length; i++) {
                sb.append(upperCase(texts[i].toLowerCase()));
            }
        } else {
            sb.append(text.toLowerCase());
        }
        return sb.toString();
    }

    /**
     * 把以下划线（_）分割的单词转成符合帕斯卡命名规则的字符串。
     *
     * @return
     */
    public static String toPascalCase(String text, String separator) {
        StringBuilder sb = new StringBuilder();

        String[] texts = text.split(separator);
        for (int i = 0; i < texts.length; i++) {
            sb.append(upperCase(texts[i].toLowerCase()));
        }
        return sb.toString();
    }

    /**
     * 转换类型K-V形式的MyBatis 3 查询结果集。
     *
     * @param rows
     * @return
     */
    public static List<Map<String, Object>> toCamelCase(List<Map<String, Object>> rows) {
        List<Map<String, Object>> resultSets = null;

        if (rows != null && rows.size() > 0) {
            resultSets = new ArrayList<Map<String, Object>>();
            for (Map<String, Object> row : rows) {
                Map<String, Object> resultSet = new HashMap<String, Object>();

                String field;
                for (Map.Entry<String, Object> entry : row.entrySet()) {
                    field = toCamelCase(entry.getKey(), "_");
                    resultSet.put(field, entry.getValue());
                }
                resultSets.add(resultSet);
            }
        }
        return resultSets;
    }

    /**
     * MD5加密。
     *
     * @param text
     * @return
     */
    public static String md5(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(text.getBytes());
            return new BigInteger(1, md.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 转换NULL值为“”；
     *
     * @param text
     * @return
     */
    public static String wrapNull(String text) {
        return text == null ? "" : text;
    }
}
