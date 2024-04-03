# 💥 프로젝트 설명
- 엑셀 파일을 읽어서 테이블마다 CREATAE 쿼리를 생성하여 텍스트 파일로 내보내고, 폴더 안의 CREATE문이 담긴 파일들을 읽어서 엑셀 파일로 내보냅니다.
# 💥 주요 클래스
- LMSTableLayout : 엑셀 파일을 읽어서 테이블마다 CREATAE 쿼리를 생성하여 지정한 폴더에 텍스트 파일을 생성한다.
- toExcel : LMSTableLayout 실행 후 생성한 텍스트 파일들의 컬럼 정보를 추출해서 엑셀 파일을 생성한다.

# 💥 프로그램 실행 방법
### 0. 엑셀 파일 양식
- 영문테이블명 | 한글테이블명 | 컬럼번호 | 한글컬럼명 | 영문컬럼명 | 컬럼타입 | PK 여부 | NULL 여부 | 비고
<img width="1164" alt="스크린샷 2024-04-03 오후 4 02 35" src="https://github.com/chachohee/poi/assets/83406032/03d77426-fb2b-48df-9292-84e1a82067bc">

###  1. LMSTableLayout 수정 후 실행
- main 메서드의 path 수정(읽을 엑셀 파일 경로)
- exportFile 메서드의 directoryName 수정(파일 생성될 폴더 경로)
### 2. toExcel 수정 후 실행
- dirPath 수정(위애서 생성된 .txt파일들이 저장된 폴더 경로)
- filOut 수정(내보낼 엑셀파일의 경로 (파일명까지 지정해야함))
