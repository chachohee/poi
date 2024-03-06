package com.example.poi;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.Objects;

public class readExcel {
    public static void main(String[] args) {
        try {
            FileInputStream file = new FileInputStream("/Users/babydoll/Downloads/sample2.xlsx");
            IOUtils.setByteArrayMaxOverride(Integer.MAX_VALUE);
            XSSFWorkbook workbook = new XSSFWorkbook(file);
            XSSFSheet sheet = workbook.getSheetAt(0);

            int rows = sheet.getPhysicalNumberOfRows(); // 행 개수
            String ENGTBNM = "";
            String preENGTBNM = "";
            String KRNTBNM = "";
            String preKRNTBNM = "";
            String[] rowArr = new String[rows];

            for (int i = 0; i < rows; i++) {
                Row row = sheet.getRow(i);
                String colName = "";
                String dataType = "";
                String pk = "PRIMARY KEY";
                String setNull = "NOT NULL";
                String def = "DEFAULT 'N'";
                String comment = "COMMENT '";

                int cells = row.getPhysicalNumberOfCells(); // 열 개수
                for (int j = 0; j < cells; j++) {
                    Cell cell = row.getCell(j);

                    if (j == 10) ENGTBNM = cell.getStringCellValue();
                    if (i == 0) {
                        preENGTBNM = ENGTBNM;
                    } else {
                        preENGTBNM = sheet.getRow(i-1).getCell(10).getStringCellValue();
                    }
                    if (j == 11) KRNTBNM = "COMMENT '" + cell.getStringCellValue() + "';";
                    if (i == 0) {
                        preKRNTBNM = KRNTBNM;
                    } else {
                        preKRNTBNM = sheet.getRow(i-1).getCell(11).getStringCellValue();
                    }
                    if (j == 13) comment = "COMMENT '" + cell.getStringCellValue() + "'";
                    if (j == 14) colName = cell.getStringCellValue();
                    if (j == 15) dataType = cell.getStringCellValue();
                    if (j == 16 && !cell.getBooleanCellValue()) pk = "";
                    if (j == 17 && !cell.getBooleanCellValue()) setNull = "NULL";
                    if (j == 19 && Objects.equals(cell.getStringCellValue(), "")) def = "";

                }

                boolean flag = false;
                String rowSet =  rowSetChange(flag, colName, dataType, setNull, pk, def, comment);

                if (i == 0) {
                    flag = true;
                    rowSet = "CREATE TABLE LMS." + ENGTBNM + " (" + "\n"
                            + rowSetChange(flag, colName, dataType, setNull, pk, def, comment);
                }

                if (!Objects.equals(ENGTBNM, preENGTBNM)) {
                    flag = true;
                    rowSet = ") COMMENT '" + preKRNTBNM + "';"
                            + "CREATE TABLE LMS." + ENGTBNM + " (" + "\n"
                            + rowSetChange(flag, colName, dataType, setNull, pk, def, comment);
                }
                if (i == rows-1) rowSet += "\n) " + KRNTBNM;

                rowArr[i] = rowSet;
            }

//            PrintQueryToConsole.printCreateQuery(rowArr); // 콘솔창에 쿼리 출력하기

            String totString = conRowArr(rowArr);

//            CreateFileInProject.createFile(totString); // 프로젝트 안에 파일 생성

            ExportFile.exportFile(totString); // 원하는 경로에 파일 생성

            file.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String rowSetChange(boolean flag, String colName, String dataType, String setNull, String pk, String def, String comment) {
        String rowSet = "";
        if (flag) {
            rowSet =  "\t" + colName + " " + dataType + " "
                    + setNull + " " + pk + " " + def + " " + comment;
        } else {
            rowSet =  "\t, " + colName + " " + dataType + " "
                    + setNull + " " + pk + " " + def + " " + comment;
        }
        return rowSet;
    }

    // rowArr 문자열로 합치기
    public static String conRowArr(String[] rowArr) {
        StringBuilder rowStr = new StringBuilder();
        for (int i = 0; i < rowArr.length; i++) {
            if (i == 0) {
                rowStr.append(rowArr[i]).append("\n");
            } else {
                rowStr.append(rowArr[i]).append("\n");
            }
        }
//        System.out.println("conRowArr >>\n" + rowStr);
        return rowStr.toString();
    }
}