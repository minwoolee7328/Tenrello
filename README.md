![header](https://capsule-render.vercel.app/api?type=wave&color=auto&height=300&section=header&text=Tenrello&fontSize=90)


## 🔧 사용한 Tool
<div style="display: flex; justify-content: center;">
  <img src="https://img.shields.io/badge/Java-007396?&style=flat&logo=Java&logoColor=white" style="margin-right: 10px;">
  <img src="https://img.shields.io/badge/HTML5-E34F26?style=flat&logo=HTML5&logoColor=white" style="margin-right: 10px;"/>
	<img src="https://img.shields.io/badge/CSS3-1572B6?style=flat&logo=CSS3&logoColor=white" style="margin-right: 10px;" />
  <img src="https://img.shields.io/badge/JavaScript-F7DF1E?style=flat&logo=javascript&logoColor=white" />
</div>

<div style="display: flex; justify-content: center;">
  <img src="https://img.shields.io/badge/Spring-6DB33F?&style=flat&logo=spring&logoColor=white" style="margin-right: 10px;">
  <img src="https://img.shields.io/badge/MySQL-4479A1?style=flat&logo=mysql&logoColor=white" style="margin-right: 10px;"/>
  <img src="https://img.shields.io/badge/ApachetTomcat-F8DC75?style=flat&logo=apachetomcat&logoColor=white"/>
</div>

<div style="display: flex; justify-content: center;">
  <img src="https://img.shields.io/badge/Git-F05032?style=flat&logo=git&logoColor=white" style="margin-right: 10px;">
  <img src="https://img.shields.io/badge/Github-181717?style=flat&logo=github&logoColor=white" style="margin-right: 10px;">
  <img src="https://img.shields.io/badge/Intellijidea-000000?style=flat&logo=intellijidea&logoColor=white" style="margin-right: 10px;">
  <img src="https://img.shields.io/badge/Postman-FF6C37?style=flat&logo=postman&logoColor=white" style="margin-right: 10px;">
</div>

---------------------------------------------------------------
<h2>Tenrello 기능</h2>
- **사용자 관리 기능**
    - [x]  로그인 / 회원가입 기능
    - [x]  사용자 정보 수정
        - [x]  닉네임
        - [x]  비밀번호
    - [x]  삭제 기능
    - [x]  refresh token, 로그아웃(redis)
        
- **보드 관리 기능**
    - [x]  보드 생성
    - [x]  보드 조회
        - 사용자가 속한 보드
    - [x]  보드 수정
        - 보드 이름
        - 배경 색상
        - 설명
    - [x]  보드 수정
        - 보드 이름
    - [x]  보드 삭제
        - 생성한 사용자만 삭제를 할 수 있습니다.
    - [x]  회원 권한 부여
        - MEMBER → ADMIN, ADMIN → MEMBER
    - [x]  보드 초대
        - 특정 사용자들을 해당 보드에 초대시켜 협업을 할 수 있어야 합니다.
          
- **컬럼 관리 기능**
    - [x]  컬럼 생성
        - 보드 내부에 컬럼을 생성할 수 있어야 합니다.
        - 컬럼이란 위 사진에서 Backlog, In Progress와 같은 것을 의미해요.
    - [x]  컬럼 이름 수정
    - [x]  컬럼 삭제
    - [x]  컬럼 순서 이동
        - 컬럼 순서는 자유롭게 변경될 수 있어야 합니다.
            - e.g. Backlog, In Progress, Done → Backlog, Done, In Progress
              
- **카드 관리 기능**
    - [x]  카드 생성
        - 컬럼 내부에 카드를 생성할 수 있어야 합니다.
    - [x]  카드 수정
        - 카드 이름
        - 카드 설명
        - 카드 색상
        - 작업자 할당
        - 작업자 변경
    - [x]  카드 삭제
    - [x]  카드 이동
        - 같은 컬럼 내에서 카드의 위치를 변경할 수 있어야 합니다.
        - 카드를 다른 컬럼으로 이동할 수 있어야 합니다.
- **카드 상세 기능**
    - [x]  댓글 달기
	- 협업하는 사람들끼리 카드에 대한 토론이 이루어질 수 있어야 합니다.
    - [x]  날짜 지정
	- 카드에 마감일을 설정하고 관리할 수 있어야 합니다.

---------------------------------------------------------
<h2>ERD</h2>
<img width="787" alt="스크린샷 2023-08-13 215445" src="https://github.com/minwoolee7328/Tenrello/assets/130745679/ac35d739-165c-4f40-bf8e-fdc4b7eba375">

<h2>API</h2>
https://documenter.getpostman.com/view/27971774/2s9Xy5MqRa
