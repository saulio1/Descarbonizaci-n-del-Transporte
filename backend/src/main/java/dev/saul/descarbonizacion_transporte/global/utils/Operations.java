package dev.saul.descarbonizacion_transporte.global.utils;

public class Operations {

    public static String trimBrackets(String message){
        return message.replaceAll("[\\[\\]]", "");
    }
}
