package cn.amaging.encology.common.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by DuQiyu on 2018/10/16 14:44.
 */
public class RandomCodeUtil {

    private static final Random RND = new Random(System.currentTimeMillis());
    // 通配符
    private final static char PATTERN_PLACEHOLDER = '#';
    // 随机码默认长度
    private final static int DEFAULT_LENGTH = 8;

    private static final String CHARSET_ALPHABETIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String CHARSET_ALPHANUMERIC = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String CHARSET_NUMBERS = "0123456789";

    /**
     * 生成随机大写英文字符
     * @param length
     * @return String
     * */
    public static String alphabetic(Integer length) {
        return generate(length, CHARSET_ALPHABETIC, null, null, null);
    }

    /**
     * 生成随机大写英文字符
     * @param length
     * @param prefix 固定前缀（不计入length长度）
     * @param postfix 固定后缀（不计入length长度）
     * @return String
     * */
    public static String alphabetic(Integer length, String prefix, String postfix) {
        return generate(length, CHARSET_ALPHABETIC, null, prefix, postfix);
    }

    /**
     * 生成随机大写英文字符，长度为pattern.length
     * @param pattern pattern中的'#'将被随机符替换，例如：2018#1#2#3 -> 2018A1B2C3
     * @return String
     * */
    public static String alphabeticWithPattern(String pattern) {
        return generate(null, CHARSET_ALPHABETIC, pattern, null, null);
    }

    /**
     * 生成随机大小写英文字符或数字
     * @param length
     * @return String
     * */
    public static String alphanumeric(Integer length) {
        return generate(length, CHARSET_ALPHANUMERIC, null, null, null);
    }

    /**
     * 生成随机大小写英文字符或数字
     * @param length
     * @param prefix 固定前缀（不计入length长度）
     * @param postfix 固定后缀（不计入length长度）
     * @return String
     * */
    public static String alphanumeric(Integer length, String prefix, String postfix) {
        return generate(length, CHARSET_ALPHANUMERIC, null, prefix, postfix);
    }

    /**
     * 生成随机大小写英文字符或数字，长度为pattern.length
     * @param pattern pattern中的'#'将被随机符替换，例如：2018#1#2#3 -> 2018A1B2C3
     * @return String
     * */
    public static String alphanumericWithPattern(String pattern) {
        return generate(null, CHARSET_ALPHANUMERIC, pattern, null, null);
    }

    /**
     * 生成随机数字
     * @param length
     * @return String
     * */
    public static String numbers(Integer length) {
        return generate(length, CHARSET_NUMBERS, null, null, null);
    }

    /**
     * 生成随机数字
     * @param length
     * @param prefix 固定前缀（不计入length长度）
     * @param postfix 固定后缀（不计入length长度）
     * @return String
     * */
    public static String numbers(Integer length, String prefix, String postfix) {
        return generate(length, CHARSET_NUMBERS, null, prefix, postfix);
    }

    /**
     * 生成随机数字，长度为pattern.length
     * @param pattern pattern中的'#'将被随机符替换，例如：2018#1#2#3 -> 2018A1B2C3
     * @return String
     * */
    public static String numbersWithPattern(String pattern) {
        return generate(null, CHARSET_NUMBERS, pattern, null, null);
    }

    /**
     * 自定义生成随机字符
     * @param length
     * @param charset 自定义字符库
     * @return String
     * */
    public static String customize(Integer length, String charset) {
        return generate(length, charset, null, null, null);
    }

    /**
     * 自定义生成随机字符
     * @param length
     * @param charset 自定义字符库
     * @param prefix 固定前缀（不计入length长度）
     * @param postfix 固定后缀（不计入length长度）
     * @return String
     * */
    public static String customize(Integer length, String charset, String prefix, String postfix) {
        return generate(length, charset, null, prefix, postfix);
    }

    /**
     * 自定义生成随机字符，长度为pattern.length
     * @param charset 自定义字符库
     * @param pattern pattern中的'#'将被随机符替换，例如：2018#1#2#3 -> 2018A1B2C3
     * @return String
     * */
    public static String customizeWithPattern(String charset, String pattern) {
        return generate(null, charset, pattern, null, null);
    }

    /**
     * @param length 随机码长度，默认：{@value DEFAULT_LENGTH}
     * @param charset 随机码取值，默认：{@value CHARSET_ALPHABETIC}
     * @param pattern 匹配模式，pattern中的{@value PATTERN_PLACEHOLDER}将被替换成随机码
     * @param prefix 固定前缀（不计入length长度）
     * @param postfix 固定后缀（不计入length长度）
     * @return String
     * */
    private static String generate(Integer length, String charset, String pattern, String prefix, String postfix)  {
        if (StringUtils.isBlank(charset)) {
            charset = CHARSET_ALPHABETIC;
        }
        if (StringUtils.isBlank(pattern)) {
            if (null == length || length <= 0) {
                length = DEFAULT_LENGTH;
            }
            char[] chars = new char[length];
            Arrays.fill(chars, PATTERN_PLACEHOLDER);
            pattern = new String(chars);
        }
        StringBuilder sb = new StringBuilder();
        char[] charsetChars = charset.toCharArray();
        char[] patternChars = pattern.toCharArray();
        if (StringUtils.isNotBlank(prefix)) {
            sb.append(prefix);
        }
        for (char patternChar : patternChars) {
            if (patternChar == PATTERN_PLACEHOLDER) {
                sb.append(charsetChars[RND.nextInt(charsetChars.length)]);
            } else {
                sb.append(patternChar);
            }
        }
        if (StringUtils.isNotBlank(postfix)) {
            sb.append(postfix);
        }
        return sb.toString();
    }
}
