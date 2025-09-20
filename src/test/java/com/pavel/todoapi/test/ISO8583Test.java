package com.pavel.todoapi.test;

import com.pavel.todoapi.service.ISO8583Parser;

public class ISO8583Test {
    public static void main(String[] args) {
        ISO8583Parser parser = new ISO8583Parser();
        String hexMessage = "08100220000002000000112309023307315600";
        
        System.out.println("=== ISO 8583 Message Test ===");
        System.out.println("Input: " + hexMessage);
        System.out.println();
        
        String result = parser.parseMessage(hexMessage);
        System.out.println("Result: " + result);
    }
}
