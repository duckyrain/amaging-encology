package cn.amaging.encology.util;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * Created by DuQiyu on 2018/10/24 16:59.
 */
public class ValidateUtil {

    // 邮箱
    private static final String REGEX_EMAIL = "^[a-z0-9]([a-z0-9]*[-_.+]?[a-z0-9]+)*@([a-z0-9]*[-_]?[a-z0-9]+)+[.][a-z]{2,3}([.][a-z]{2,4})?$";
    // 身份证号
    private static final String REGEX_CARD_ID = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}(\\d|X|x)$";
    // 中文
    private static final String REGEX_CHINESE_TEXT="[\\u4e00-\\u9fa5]+";
    // 手机号(13+任意数, 15+除4的任意数, 18+除1和4的任意数, 17+除9的任意数, 147)
    private static final String REGEX_PHONE_CHINA = "^((13[0-9])|(15[0-3, 5-9])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$";
    // MAC
    private static final String REGEX_MAC = "^([A-Fa-f0-9]{2}:){5}[A-Fa-f0-9]{2}$";

    public static boolean isEmail(String email) {
        if (StringUtils.isBlank(email)) {
            return false;
        }
        return Pattern.matches(REGEX_EMAIL, email);
    }

    public static boolean isCardId(String cardId) {
        if (StringUtils.isBlank(cardId)) {
            return false;
        }
        return Pattern.matches(REGEX_CARD_ID, cardId);
    }

    public static boolean isPhone(String phone) {
        if (StringUtils.isBlank(phone)) {
            return false;
        }
        return Pattern.matches(REGEX_PHONE_CHINA, phone);
    }

    public static boolean isChineseText(String chinaText){
        if(StringUtils.isBlank(chinaText)){
            return false;
        }
        return Pattern.matches(REGEX_CHINESE_TEXT,chinaText);
    }

    public static boolean isMac(String mac) {
        if (StringUtils.isBlank(mac)) {
            return false;
        }
        return Pattern.matches(REGEX_MAC, mac);
    }
}
