package com.example.poi;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ExportFile {
    public static void exportFile(String totString) {
        try {
            String directoryName = "/Users/babydoll/IdeaProjects/output/";
            String extension = ".txt";

            String[] querys = totString.split(";");

            for (String query : querys) {
                if (query.startsWith("CREATE")) {
                    String fileName = query.substring(17, 26);
                    String filePath = directoryName + fileName + extension;
                    FileWriter fileWriter = new FileWriter(filePath);
                    fileWriter.write(query + ";");
                    fileWriter.close();

                    // 파일이 생성되었는지 확인
                    File file = new File(filePath);
                    if (file.exists()) {
                        System.out.println("File created successfully at : " + file.getAbsolutePath());
                    } else {
                        System.out.println("Failed to create the file.");
                    }
                }
            }

            System.out.println("ExportFile completed.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
