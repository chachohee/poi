package com.example.poi;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class toExcel3 {
    public static void main(String[] args) throws IOException {
        // 새로운 엑셀 파일 생성
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("테이블 컬럼 정보");

        // 열 너비 설정
        sheet.setColumnWidth(0, 5000); // 첫 번째 열의 너비 설정 (테이블영문)
        sheet.setColumnWidth(1, 8000); // 두 번째 열의 너비 설정 (테이블한글)
        sheet.setColumnWidth(2, 5000); // 세 번째 열의 너비 설정 (컬럼명)
        sheet.setColumnWidth(3, 5000); // 네 번째 열의 너비 설정 (데이터타입)
        sheet.setColumnWidth(4, 4000); // 다섯 번째 열의 너비 설정 (NULL 여부)
        sheet.setColumnWidth(5, 4000); // 여섯 번째 열의 너비 설정 (PK)
        sheet.setColumnWidth(6, 5000); // 일곱 번째 열의 너비 설정 (DEFAULT)
        sheet.setColumnWidth(7, 8000); // 여덟 번째 열의 너비 설정 (COMMENT)

        // 열 이름 설정
        XSSFRow headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("테이블영문");
        headerRow.createCell(1).setCellValue("테이블한글");
        headerRow.createCell(2).setCellValue("컬럼명");
        headerRow.createCell(3).setCellValue("데이터타입");
        headerRow.createCell(4).setCellValue("NULL 여부");
        headerRow.createCell(5).setCellValue("PK");
        headerRow.createCell(6).setCellValue("DEFAULT");
        headerRow.createCell(7).setCellValue("COMMENT");

        String dirPath = "/Users/babydoll/IdeaProjects/totxt/";
        File dir = new File(dirPath);
        String[] fileList = dir.list();

        int rowNum = 1;

        for (String fileName : fileList) {
            Scanner scanner = new Scanner(new File(dirPath + fileName));
            StringBuilder fileContents = new StringBuilder();

            while (scanner.hasNextLine()) {
                fileContents.append(scanner.nextLine());
            }
            scanner.close();
//            System.out.println(fileContents);

            String engTableName = fileName.substring(0,9);
            String[] cutForKrnTableName = String.valueOf(fileContents).replaceAll(";", "").split("'");
            String krnTableName = cutForKrnTableName[cutForKrnTableName.length-1];

            Pattern pattern = Pattern.compile("\\(.*\\)");
            Matcher matcher = pattern.matcher(fileContents);
            String columnLine = "";
            if (matcher.find()) {
                columnLine = matcher.group();
                columnLine = columnLine.replaceAll("\t", "");
                columnLine = columnLine.substring(1, columnLine.length()-1);

                // PK
                int idx = columnLine.indexOf("PRIMARY KEY");
                String primaryKeyColumns = null;
                if (idx != -1) {
                    // PRIMARY KEY 키워드 이후의 문자열 추출
                    String primaryKeyString = columnLine.substring(idx + "PRIMARY KEY".length()).trim();
                    // 괄호 안의 열(Column) 목록 추출
                    primaryKeyColumns = primaryKeyString.substring(primaryKeyString.indexOf("(") + 1, primaryKeyString.indexOf(")")).trim();
                }
                // PK 출력
                String[] primaryKeyColumnsArray = null;
                if (primaryKeyColumns != null) {
                    primaryKeyColumnsArray = primaryKeyColumns.split(",");
                } else {
                    System.out.println("No Primary Key found.");
                }

                String[] columns = columnLine.split(",");
                for (String column : columns) {
                    if (column.startsWith("PRIMARY")) break;
                    String[] columnInfo = column.trim().split("\\s+");
                    XSSFRow row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(engTableName);
                    row.createCell(1).setCellValue(krnTableName);
                    row.createCell(2).setCellValue(columnInfo[0]); // 컬럼명
                    row.createCell(3).setCellValue(columnInfo[1]); // 데이터타입
                    row.createCell(4).setCellValue(column.contains("NOT NULL")); // NULL 여부
                    row.createCell(5).setCellValue(Boolean.FALSE);
                    for (String pkColumn : primaryKeyColumnsArray) {
                        if (pkColumn.equals(columnInfo[0])) {
                            row.createCell(5).setCellValue(Boolean.TRUE);
                            break;
                        }
                    }
                    if (columnInfo[0].equals("DEL_YN")) row.createCell(6).setCellValue("N");
                    row.createCell(7).setCellValue(column.substring(column.indexOf("COMMENT") + 9).replaceAll("'", "").trim()); // COMMENT
                }
            }
//            System.out.println(columnLine);
        }
        // 엑셀 파일로 저장
        FileOutputStream fileOut = new FileOutputStream("/Users/babydoll/IdeaProjects/toxlsx/SQL_DDL작업파일.xlsx");
        workbook.write(fileOut);
        fileOut.close();
        workbook.close();
    }
}