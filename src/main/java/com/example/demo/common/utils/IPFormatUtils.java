package com.example.demo.common.utils;

import com.example.demo.erros.InvalidFormatException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IPFormatUtils {
    public static void checkIP(String ip, String message) throws InvalidFormatException {
        if (ip == null) throw new InvalidFormatException("ip must be provided.");

        final String IP_ADDRESS_PATTERN = "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$";
        Pattern pattern = Pattern.compile(IP_ADDRESS_PATTERN);
        Matcher matcher = pattern.matcher(ip);
        if (!matcher.matches()) throw new InvalidFormatException(message);
    }
}
