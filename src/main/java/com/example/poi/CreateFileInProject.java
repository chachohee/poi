package com.example.poi;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CreateFileInProject {
    public static void createFile(String totString) throws IOException {
        String directoryName = "output"; // 내보낼 디렉토리 이름

        // 디렉토리 생성
        Path directoryPath = Paths.get(directoryName);
        Files.createDirectories(directoryPath);
        String extension = ".txt";

        String[] querys = totString.split(";");

        for (String query : querys) {
            if (query.startsWith("CREATE")) {
                String fileName = query.substring(17, 26);
                String filePath = directoryName + "/" + fileName + extension; // 파일 이름 생성
                FileWriter fileWriter = new FileWriter(filePath);
                fileWriter.write(query + ";"); // CREATE 부분을 파일에 쓰기
                fileWriter.close();
            }
        }

        System.out.println("CreateFileInProject completed.");
    }
}
