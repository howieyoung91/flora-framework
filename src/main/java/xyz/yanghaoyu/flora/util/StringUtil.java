package xyz.yanghaoyu.flora.util;

import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;

/**
 * @author <a href="https://www.yanghaoyu.xyz">Howie Young</a><i>on 2021/8/8 11:34<i/>
 * @version 1.0
 */


public class StringUtil {
    public static final String EMPTY = "";

    private StringUtil() {}

    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    public static String lowerFirstChar(String str) {
        char c = str.charAt(0);
        if (c >= 'a') {
            return str;
        }
        return str.replace(c, (char) (c + 32));
    }

    public static boolean hasLength(String str) {
        return (str != null && !str.isEmpty());
    }

    public static String trimAllWhitespace(String str) {
        if (!hasLength(str)) {
            return str;
        }

        int len = str.length();
        StringBuilder sb = new StringBuilder(str.length());
        for (int i = 0; i < len; i++) {
            char c = str.charAt(i);
            if (!Character.isWhitespace(c)) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    private static final String[] EMPTY_STRING_ARRAY = {};

    public static String[] toStringArray(Collection<String> collection) {
        return (!CollectionUtils.isEmpty(collection) ? collection.toArray(EMPTY_STRING_ARRAY) : EMPTY_STRING_ARRAY);
    }

    public static String[] toStringArray(Enumeration<String> enumeration) {
        return (enumeration != null ? toStringArray(Collections.list(enumeration)) : EMPTY_STRING_ARRAY);
    }
}
