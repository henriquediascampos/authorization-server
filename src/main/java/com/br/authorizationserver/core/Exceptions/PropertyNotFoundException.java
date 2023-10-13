package com.br.authorizationserver.core.Exceptions;

public class PropertyNotFoundException extends Exception {
    public static final String TEXT_RESET = "\u001B[0m";
    public static final String TEXT_ATTENTION = "\u001B[31m";
    public static final String TEXT_SOLUTION = "\u001B[32m";
    
    public PropertyNotFoundException(String msg) {
        super(msg);
    }

    public static String attention(final String text) {
        return TEXT_ATTENTION+text+TEXT_RESET;
    }

    public static String solution(final String text) {
        return TEXT_SOLUTION+text+TEXT_RESET;
    }

    @Override
    public void printStackTrace() {
        super.printStackTrace();
    }

}
