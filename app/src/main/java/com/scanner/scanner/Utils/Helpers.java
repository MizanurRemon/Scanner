package com.scanner.scanner.Utils;

public class Helpers {
    public static String convertImageToString() {
        String imgToString = "";

        return imgToString;
    }

    public static Boolean validatePhoneNumber(String phone) {

        String REGEX = "^(?:\\+8801|8801|01)[^012]{1}[0-9]{8}$";

        return phone.matches(REGEX);
    }
}
