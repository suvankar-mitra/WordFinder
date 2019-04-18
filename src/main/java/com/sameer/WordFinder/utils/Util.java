package com.sameer.WordFinder.utils;

public class Util {
    public static String capitalizeFirstLetter(String string) {
        return (string == null || string.isEmpty()) ? "" : Character.toUpperCase(string.charAt(0))+string.substring(1);
    }
}
