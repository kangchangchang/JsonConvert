package com.datacenter.utils;

import org.apache.commons.lang3.RegExUtils;

import javax.validation.constraints.NotNull;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author pc
 * @date Create in  2023/2/23
 */
public class RegexUtils extends RegExUtils {
    public static final String REGEX_ARR_SIGN = "\\[\\]";
    public static final String REGEX_MORE_NUM = "[0-9]+";
    public static final String REGEX_NUM = "\\[[0-9]*\\]";

    public RegexUtils() {
    }

    public static int countMarcher(@NotNull String str, @NotNull String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(str);

        int i;
        for(i = 0; matcher.find(); ++i) {
        }

        return i;
    }
}
