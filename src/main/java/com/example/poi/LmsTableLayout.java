package com.example.poi;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * @Author    : admin
 * @Date    : 2024. 3. 4.
 * @see    : LMS 테이블 레이아웃 정의서 DDL 생성 프로그램
 */
public class LmsTableLayout {
    /**
     * @Author    : admin
     * @Date   : 2024. 3. 4.
     * @Method    : main
     * @Return    : void
     *
     */
    public static void main(String[] args) throws Exception{

//      String path = "C:\\Users\\arjja\\Desktop\\leadit\\02. 산출물\\01. 분석\\01. 테이블정의서\\LMS_테이블레이아웃정의서\\lms_20240227_SQL_DDL작업파일_ver.0.3.xlsx";
        String path = "/Users/babydoll/Downloads/lms_20240227_SQL_DDL작업파일_ver.0.3.xlsx";

        try {
            FileInputStream fi = new FileInputStream(path);
            XSSFWorkbook workbook = new XSSFWorkbook(fi);
            XSSFSheet sheet = workbook.getSheetAt(0);

            int sheets = sheet.getLastRowNum()+1; // 행 개수
            String[] rowSetArr = new String[sheets];

            List<String> column00List = new ArrayList<String>();
            ArrayList<String> column00ArrayList = new ArrayList<>();

            List<String> column01List = new ArrayList<String>();
            ArrayList<String> column01ArrayList = new ArrayList<>();

            String primaryKey = "";

            String notNull = "";
            String comment = "";

            String column00 = ""; //MLBP007TB
            String column01 = ""; //통합고객
            String column02 = ""; //1
            String column03 = ""; //고객아이디
            String column04 = ""; //CUST_ID
            String column05 = ""; //VARCHAR(8)
            String column06 = ""; //PK
            String column07 = ""; //NULL

            String setColumn00 = "";
            String setColumn01 = "";

            String rowSet = "";

            XSSFCell cell = null;
            List<String> cellList = null;


            int keyCnt = 0;

            for(int i=0; i<sheets; i++) {
                XSSFRow row = sheet.getRow(i); // 행



                if(row != null) {
                    cellList = new ArrayList<String>();
                    int cells = row.getPhysicalNumberOfCells(); // 열 개수

                    for(int j=0; j<cells; j++) {
                        cell = row.getCell(j);
                        if(cell != null) {
                            cellList.add(cellReader(cell)); //셀을 읽어와서 List에 추가
                        }
                    }

                    column00 = cellList.get(0); //MLBP007TB
                    column01 = cellList.get(1); //통합고객
                    column02 = cellList.get(2); //1
                    column03 = cellList.get(3); //고객아이디
                    column04 = cellList.get(4); //CUST_ID
                    column05 = cellList.get(5); //VARCHAR(8)
                    column06 = cellList.get(6); //PK
                    column07 = cellList.get(7); //NOT NULL

                    setColumn00 = "";
                    if ( ! "".equals(column00)) { // 테이블영문명이 있으면
                        setColumn00 += column00; // setColumn00에 테이블영문명 저장
                    }

                    setColumn01 = "";
                    if ( ! "".equals(column01)) { // 테이블한글명이 있으면
                        setColumn01 += column01; // setColumn01에 테이블한글명 저장
                    }

                    //not null 체크
                    if ( "true".equals(column07)) {
                        notNull = "NOT NULL";
                    } else {
                        notNull = "NULL";
                    }

                    //PK여부 체크
                    if ( "true".equals(column06)) {
//                  System.out.println(column02);
                        if ( "1.0".equals(column02)) { // 컬럼 번호가 1이면
                            keyCnt ++;
                        }
                        primaryKey += column04+keyCnt+ ","; // primaryKey에 PK 여러개 담아주기 CUST_ID1,칼럼1,칼럼2...
                    }

                    //DEFAULT 체크
                    if ( "DEL_YN".equals(column04)) { // 컬럼명이 DEL_YN이면
                        comment = "DEFAULT 'N' COMMENT '" + column03 + "',";
                    } else {
                        comment = "COMMENT '" + column03 + "',";
                    }
                } // end 셀값 가져오기

                rowSet = column00 +":"+column04 + " " + column05 + " "
                        + notNull + " " + comment; // MLBP007TB:CUST_ID VARCHAR(8) NOT NULL COMMENT '고객아이디'
                // : 기준으로 split

                rowSetArr[i] = rowSet;

                //중복제거를 위해 리스트 담기
                column00List.add(setColumn00); // 테이블영문
                column01List.add(setColumn01); //테이블한글
            } //end for
//         System.out.println(primaryKey);

            //영문테이블 중복제거
            for(String item : column00List){
                if(!column00ArrayList.contains(item)) {
                    column00ArrayList.add(item);
                }
            }
            //국문테이블 중복제거
            for(String item : column01List){
                if(!column01ArrayList.contains(item)) {
                    column01ArrayList.add(item);
                }
            }

            //테이블 수 만큼 저장하기 위해 루프처리
//         System.out.println(primaryKey);
            for (int k=0; k<column00ArrayList.size(); k++) {
                printCreateQuery(rowSetArr, column00ArrayList.get(k), column01ArrayList.get(k), primaryKey);
            }
            System.out.println();

        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    // 위에서 세팅한 값으로 쿼리 만들기
    public static void printCreateQuery(String[] rowSetArr, String engTbNm, String krnTbNm, String primaryKey) {
        StringBuilder output = new StringBuilder();

        String splitCol00 = "";
        String splitCol01 = "";
        String spaceCol = "";

        //생성쿼리 조합
        output.append("CREATE TABLE LMS.").append(engTbNm).append(" (\n");

        for (int i=0; i<rowSetArr.length; i++) {
            splitCol00 = rowSetArr[i].split(":")[0]; // 테이블영문
            if (splitCol00.contains(engTbNm)) {
                splitCol01 = rowSetArr[i].split(":")[1];
                output.append("\t").append(splitCol01).append("\n");

                if (splitCol01.contains("NOT")) {
                    spaceCol += rowSetArr[i].split(":")[1].split(" ")[0] + ",";
                }
            }
        }

        output.append("\tPRIMARY KEY (")
                .append(spaceCol.replaceAll(",$", ""))
                .append(")\n")
                .append(") COMMENT ").append("'").append(krnTbNm).append("';\n");

        System.out.println(output);

        exportFile(String.valueOf(output));
    }

    private static String cellReader(XSSFCell cell) {

        String value = "";
        CellType ct = cell.getCellType();

        if(ct != null) {
            switch(cell.getCellType()) {
                case FORMULA:
                    value = cell.getCellFormula();
                    break;
                case NUMERIC:
                    cell.setCellType(CellType.STRING);
                    value = cell.getStringCellValue();
//             value = cell.getNumericCellValue()+"";
                    break;
                case STRING:
                    value = cell.getStringCellValue()+"";
                    break;
                case BOOLEAN:
                    value = cell.getBooleanCellValue()+"";
                    break;
                case ERROR:
                    value = cell.getErrorCellValue()+"";
                    break;
                case BLANK:
                    value = "";
                    break;
            }
        }

        return value;
    }

    public static void exportFile(String totString) {
        try {
            String directoryName = "/Users/babydoll/IdeaProjects/totxt/";
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
            System.out.println();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}