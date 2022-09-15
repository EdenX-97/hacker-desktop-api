package com.eden.hackerdesktopapi.utils;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;


/**
 * Util for regex check
 *
 * @author Mo Xu
 * @date 2022/08/31
 */
@Component
public class RegexUtil {

    public static boolean checkEmail(String email) {
        String emailRegex = "^([a-zA-Z0-9_.-])+@(([a-zA-Z0-9-])+\\.)+([a-zA-Z0-9]{2,4})+$";
        return Pattern.matches(emailRegex, email);
    }

    public static boolean checkURL(String url) {
        String urlRegex = "^(http(s)?:\\/\\/)\\w+[^\\s]+(\\.[^\\s]+){1,}$\n";
        return Pattern.matches(urlRegex, url);
    }
}
