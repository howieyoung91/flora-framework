package xyz.yanghaoyu.flora.util;

/**
 * @author <a href="https://www.yanghaoyu.xyz">Howie Young</a><i>on 2021/8/8 11:34<i/>
 * @version 1.0
 */


public class StringUtil {
    private StringUtil() { }

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static String firstLowerCase(String str) {
        char c = str.charAt(0);
        if (c >= 'a') {
            return str;
        }
        return str.replace(c, (char) (c + 32));
    }
}
