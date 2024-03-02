package com.example.poi;

public class PrintQueryToConsole {
    public static void printCreateQuery(String[] rowArr) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < rowArr.length; i++) {
            if(rowArr[i].contains("CREATE")) {
                rowArr[i] = addNewLineBeforeCreate(rowArr[i]);
            }
            output.append(rowArr[i]).append("\n");
        }
        System.out.println("PrintQueryToConsole >>" + output);
    }

    public static String addNewLineBeforeCreate(String input) {
        String[] parts = input.split("CREATE");

        // 새로운 문자열을 만들어서 "CREATE" 앞에 개행 문자 추가
        StringBuilder resultBuilder = new StringBuilder(parts[0]);
        for (int i = 1; i < parts.length; i++) {
            resultBuilder.append("\n\nCREATE").append(parts[i]);
        }

        return resultBuilder.toString();
    }
}
