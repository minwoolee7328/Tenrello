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
https://documenter.getpostman.com/view/27929820/2s9Xy5MqVy
----------------------------------------------------------
<h2>협업 방식</h2>

1. 깃모지
2. 기능 단위 별 브랜치
    -> 기능 단위 별 브랜치 — 병합 → 디벨롭 브랜치 —최종병합 순으로 프로젝트 진행
3. 화면 공유로 다같이 확인하며 병합

<h2>프로젝트 기능 (📋 이런 기능을 구현했어요!)</h2>

1. Redis를 활용해 refresh token, 로그인 연장, 로그아웃 구현
- Redis에는 아래의 형태로 저장
    → “username” : “refresh token”
    → “access token” : “logout”
- api 요청이 들어올 때마다 access token 재발급

2. QueryDsl을 활용해 입력받은 keyword와 관련된 user 검색 구현
    - keyword가 username, 또는 nickname에 포함된 사용자 검색



<h2>🤯 어려웠던 점</h2>

1. access  token 만료되면 재발급 하도록 구현 
    (만료된 토큰에서 사용자 정보 꺼내기 불가능 → 재발급 불가능)
    → access token을 api 요청이 들어올 때마다 재발급 하도록 구현
    → 만료 됐을 때 자동 로그아웃 구현하려고 했지만 결국 실패
        
2. LinkedList이용한 컬럼이동, 좌우 구별
이동시 오른쪽 이동과 왼쪽이동이 다른방식으로 동작했어야함.
→한쪽 방향으로 head를 바꿔주며 타겟id를 만나는지 null을 만나는지 체크
→if로 분기해서 왼쪽,오른쪽 구별 후 코드 분기
→DB상에서 첫 컬럼과 마지막 컬럼의 이동을 LinkedList를 이용해 구현하니 따로 구현했어야했다.

3. 코드 병합 (특히 프론트)
    → id 규칙 정할 필요를 느낌
    
4. 컬럼 추가 -> ColumnServiceImpl 46 에서 오류 -> lastnode 1인 컬럼을 찾을 때 보드 아이디도 같이 확인해서 검색하도록 수정

5. 마감시간 설정 후 지정한 시간이 지나면 진행상황이 변경되는 기능 구현 에 대한 지식 부족 
→ 정보 검색 을 통해 spring batch & scheduler 를 사용
→ 구색만 맞췄을뿐 spring batch를 재대로 사용하지 못한것 같지만 추후 수정 예정

6. N+1 이 발생해 해결하는 과정에서 테스트 코드 작성이 미흡해 제대로 해결되었는지 확인하기 어려웠다.
   그래도 'JOIN FETCH'를 이용해 N+1 문제를 해결하였다. 
