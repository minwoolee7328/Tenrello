const jwtToken = getJwtFromCookie();
$(document).ready(function () {
    console.log("(document).ready");

})

// 컬럼 삭제
function DeleteColumnBtn(id) {
    $.ajax({
        url: `/api/columns/${id}`, // 요청을 보낼 서버의 URL
        method: 'DELETE', // 요청 메소드 (GET, POST 등)
        headers: {
            "Authorization": jwtToken,
        },
        success: function (response) {
            alert("컬럼 삭제 완료")
            showSelectedBoard();
        },
        error: function (xhr, status, error) {
            alert("컬럼 삭제 실패")
            console.log(xhr);
        }
    });

}

// 컬럼 수정
function modifyColumnBtn(id) {

    const modifyDiv = document.getElementById("columnModify");
    console.log(modifyDiv);
    localStorage.setItem("modifyColumnId", id);

    modifyDiv.style.display = 'block';

}

function getJwtFromCookie() {
    const cookieName = 'Authorization'; // JWT가 저장된 쿠키의 이름
    const cookies = document.cookie.split(';');

    for (let i = 0; i < cookies.length; i++) {
        const cookie = cookies[i].trim();

        if (cookie.startsWith(`${cookieName} = `)) {
            const jwtCookie = cookie.substring(cookieName.length + 1);
            return jwtCookie;
        }
    }

    return null; // JWT가 존재하지 않는 경우 null 반환
}

//더미 데이터로 들어오는 컬럼 혹은 카드들을 움직이게 이벤트 설정 해주는 코드
document.addEventListener("DOMContentLoaded", function () {
    var offcanvasElement = document.querySelector("#offcanvasScrolling");
    var offcanvas = new bootstrap.Offcanvas(offcanvasElement);

    var btn = document.querySelector('[data-bs-toggle="offcanvas"]');
    btn.addEventListener("click", function () {
        offcanvas.show();
    });
});

const lists = document.querySelectorAll('.list');
const addListButton = document.getElementById('addList');
let draggedItem = null;

lists.forEach(list => {
    list.addEventListener('dragstart', e => {
        draggedItem = e.target;
        setTimeout(() => {
            e.target.style.opacity = '0.5';
        }, 0);
    });

    list.addEventListener('dragend', e => {
        setTimeout(() => {
            e.target.style.opacity = '1';
            draggedItem = null;
        }, 0);
    });

    list.addEventListener('dragover', e => {
        e.preventDefault();
    });

    list.addEventListener('dragenter', e => {
        e.preventDefault();
        list.classList.add('highlight');
    });

    list.addEventListener('dragleave', e => {
        list.classList.remove('highlight');
    });

    list.addEventListener('drop', e => {
        if (draggedItem) {
            const container = document.getElementById('listContainer');
            if (draggedItem.classList.contains('card')) {
                if (list.contains(draggedItem)) {
                    return;
                }
                list.appendChild(draggedItem);
            } else if (draggedItem.classList.contains('list')) {
                if (draggedItem.contains(list)) {
                    return;
                }
                if (list.nextSibling === draggedItem) {
                    container.insertBefore(draggedItem, list);
                } else {
                    container.insertBefore(draggedItem, list.nextSibling);
                }
            }
            list.classList.remove('highlight');
        }
    });
});


const closeNicknameModalButton = document.getElementById('closeNicknameModal');
const registerNicknameModal = document.getElementById('registerNicknameModal');
const nicknameModalOverlay = document.getElementById('nicknameModalOverlay');
addListButton.addEventListener('click', () => {

    registerNicknameModal.style.display = 'block';
    nicknameModalOverlay.style.display = 'block';
});
closeNicknameModalButton.addEventListener('click', function () {
    registerNicknameModal.style.display = 'none';
    nicknameModalOverlay.style.display = 'none';
});

// 보드 속 컬럼이 존재하는지 여부에 따라 보이는 내용이 다르도록
function toggleElements(responseExists) {
    if (responseExists) {
        // 컬럼이 있을 때
        modalBoard.style.display = "block";
        $("#deleteThisBoard").show();
    } else {
        // 컬럼이 없을 때 버튼만 보이도록
        modalBoard.style.display = "block";
        $("#listContainer").hide();
        $("#deleteThisBoard").show();
    }
}

// 선택한 보드 보기
function showSelectedBoard(boardId, boardTitle) {

    var clickedBoardId = localStorage.getItem("boardId");

    var clickedBoardName = localStorage.getItem("boardTitle");

    console.log('Clicked Board ID:', clickedBoardId);
    console.log('Clicked Board Name:', clickedBoardName);

    $('#deleteThisBoard').attr('data-board-id', clickedBoardId);
    $('#deleteThisBoard').attr('data-board-name', clickedBoardName);

    $.ajax({
        url: 'http://localhost:8080/api/boards/' + clickedBoardId + '/columns',
        type: 'GET',
        contentType: 'application/json',
        headers: {
            'Authorization': document.cookie // 클라이언트 쿠키의 값을 전달
        },
        success: function (response) {
            console.log(response);

            toggleElements(true);

            let temp_htmls = '';
            $('#listContainer').empty();

            var columnSettingArr = new Array();     //조회하는 컬럼 id를 순서대로 저장
            var i = 0;                              //response.forEach순서 기정

            response.forEach((a) => {
                let columnId = a['id'];
                let columnTitle = a['title'];
                // let cardTitles = a['cardList'];
                let positionSortTitles = a['posisionCardList'];

                console.log("positionSortTitles",positionSortTitles[0].title);
                columnSettingArr[i] = columnId;
                i += 1;

                let temp_html = `
                        <div class="list" id="${columnId}" draggable="true">
                            <div class="list-header">${columnTitle}
                                   <button onclick="DeleteColumnBtn(${columnId})" style="float: right; margin-right: 5px;" class="bi bi-trash3 fs-20"></button>
                                   <button onclick="modifyColumnBtn(${columnId})" style="float: right; margin-right: 5px;" class="bi bi-pencil fs-20"></button>
                            </div>
                    `;

                // position 으로 정렬된 title을 넣어주기위한 값
                // let positionSortTitlesTemp = 0;

                positionSortTitles.forEach((a)=>{
                    let card_html =`
                                            
                                            <div id="card-${a['id']}" class="card" draggable="true">
                                                <div id="miniCardDiv-${a['id']}" class="miniCardDiv">
                                                    <span id="cardTitleSpan-${a['id']}" onclick="model(${a['id']})">${a['title']}</span>
                                                    <button id="cardDeleteBtn" class="cardDeleteBtn" onclick="cardUpdate(${a['id']})">수정</button>
                                                </div>
                                                <div id="updateCard-${a['id']}" class="updateCard" style="display: none">
                                                    <input id="updateCardInput-${a['id']}" type="text" value="${a['title']}">
                                                    <button onclick="updateCard(${a['id']})">수정</button>
                                                    <button onclick="updateCardCancle(${a['id']})">취소</button>
                                                </div>
                                            </div>`;

                    temp_html += card_html;
                })




                temp_htmls += temp_html + `
                                             <div id="cardInsertDiv-${columnId}" class="insertDid" style="display: none">
                                             <input id="cardInsert-${columnId}"  type="text"/>
                                             <button id="insert" onclick="insertData(${columnId})">확인</button>
                                             <button id="cancle" onclick="cancleBtn(${columnId})">취소</button>
                                             </div>

                                             <button id="createBtn-${columnId}" class="createBtn" onclick="createBtn(${columnId})">카드 추가하기</button>

                                       </div>`;

            });
            console.log('forEach문 끝');
            $('#listContainer').append(temp_htmls);

            showMembersOfBoard(clickedBoardId);
            modalBoard.style.display = "block";

            columnSettingArr.forEach((col) => {       //조회된 컬럼들 순서대로 드래그 자리 바꾸기 가능하게 설정
                const newList = document.getElementById(col);
                newList.className = 'list';
                newList.draggable = true;
                //
                newList.addEventListener('dragstart', e => {
                    draggedItem = e.target;
                    setTimeout(() => {
                        e.target.style.opacity = '0.5';
                    }, 0);
                });

                newList.addEventListener('dragend', e => {
                    setTimeout(() => {
                        e.target.style.opacity = '1';
                        draggedItem = null;
                    }, 0);
                });

                newList.addEventListener('dragover', e => {
                    e.preventDefault();
                });

                newList.addEventListener('dragenter', e => {
                    e.preventDefault();
                    newList.classList.add('highlight');
                });

                newList.addEventListener('dragleave', e => {
                    newList.classList.remove('highlight');
                });

                newList.addEventListener('drop', e => {
                    if (draggedItem) {
                        const container = document.getElementById('listContainer');
                        if (newList.nextSibling === draggedItem.nextSibling) {
                            container.insertBefore(draggedItem, newList);
                            console.log("if");
                        } else {
                            var item = draggedItem;
                            while (true) {

                                if (item === null || item.id === newList.id)
                                    break;
                                if (item.nextSibling === null)
                                    item = null;
                                else
                                    item = item.nextSibling;

                            }
                            if (item === newList) {
                                console.log("오른이동");
                                // console.log("draggedItem"+draggedItem);
                                // console.log("newList"+newList);
                                container.insertBefore(draggedItem, newList.nextSibling);
                            } else if (item === null) {
                                // console.log("왼쪽이동");
                                container.insertBefore(draggedItem, newList);
                            }
                        }

                        // 이동한 열의 위치를 변경한 후, 리스트 순서를 업데이트합니다.
                        lists.forEach((list, index) => {
                            container.appendChild(list);
                        });
                        console.log("함수전");
                        moveColumn(draggedItem.id, newList.id);
                        newList.classList.remove('highlight');
                    }
                });
            });
            //
        },
        error: function () {
            // 보드 속 컬럼이 없을 때
            console.log('보드 속 컬럼 AJAX 요청 실패');

            toggleElements(false);
            showMembersOfBoard(clickedBoardId);
        }
    });

    modalBoard.style.display = "block";
}

function moveColumn(dragColumn, targetColumn) {
    console.log("moveColumn");
    $.ajax({
        type: "PUT",
        url: `/api/columns/${dragColumn}/position/${targetColumn}`,
        contentType: "application/json",
        success: function (response) {
            console.log(response['msg']);
        },
        error: function (xhr, status, error) {
            console.error(error);
            console(xhr);
            alert("위치이동을 DB에 업데이트하는 과정에서 오류발생");
        }
    })
}

function logout() {
    Cookies.remove('Authorization', {path: '/'});
    window.location.href = "/view/main";
}

//카드추가 버튼
function createBtn(id) {
    //추가하기 버튼 해당 컬럼 id 불러오기
    console.log("id", id);

    $(`#listContainer`).find($(`div[class^='insertDid']`)).hide();
    $(`#listContainer`).find($(`button[class^='createBtn']`)).show();

    // 카드 부분에 insert 창 만들기

    // 카드 추가하기 버튼 숨기기
    $(`#createBtn-${id}`).hide();

    //insert 창 만들기 show (컬럼에 해당하는)
    $(`#cardInsertDiv-${id}`).show();

}


// 카드추가 취소
function cancleBtn(id) {
    // 카드 추가하기 버튼 나타내기
    $(`#createBtn-${id}`).show();

    //컬럼에 해당하는 insert창 숨기기
    $(`#cardInsertDiv-${id}`).hide();
}

// 카드추가 기능
function insertData(id){

    $.ajax({
        url: `/api/card/columns/${id}`,
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({title: $(`#cardInsert-${id}`).val()}),
        headers: {
            'Authorization': document.cookie // 클라이언트 쿠키의 값을 전달
        },
        success: function (response) {
            // 저장된 테이터 넣기
            // $(`<div id="card-${response.id}" class="card" draggable="true" onclick="model(${response.id})">${response.title}</div>`).insertBefore(`#cardInsertDiv-${id}`);

            let temp = ` <div id="card-${response.id}"  class="card cardColumn-${response.columnId}" draggable="true">
                                    <div id="miniCardDiv-${response.id}" class="miniCardDiv">
                                        <span id="cardTitleSpan-${response.id}" onClick="model(${response.id})">${response.title}</span>
                                        <button id="cardDeleteBtn" class="cardDeleteBtn" onClick="cardUpdate(${response.id})">수정</button>
                                    </div>
                                    <div id="updateCard-${response.id}" class="updateCard" style="display: none">
                                        <input id="updateCardInput-${response.id}" type="text" value="${response.title}">
                                            <button onClick="updateCard(${response.id})">수정</button>
                                            <button onClick="updateCardCancle(${response.id})">취소</button>
                                    </div>
                                </div>`;

            $(`${temp}`).insertBefore(`#cardInsertDiv-${id}`);



            // 추가버튼 / insert버튼 제어
            $(`#column-${id}`).find($(`div[id^='cardInsertDiv-${id}']`)).hide();
            $(`#column-${id}`).find($(`button[id^='createBtn-${id}']`)).show();

        },
        error: function () {

            console.log('카드생성 실패');
            toggleElements(false);
        }
    });
}

// 카드 모달창 띄우기
function model(id){
    $(".cardModal").fadeIn();

    // 카드 데이터 불러오기

    $.ajax({
        url: `/api/cards/${id}`,
        type: 'GET',
        contentType: 'application/json',
        headers: {
            'Authorization': document.cookie // 클라이언트 쿠키의 값을 전달
        },
        success: function (response) {
            console.log("cardData",response);
            $("#cardName").text(response.title);

            // 카드 내용 부분 생성
            if(response.content == null){
                $(`<div id="contentNull" class="contentNull" onClick="insertContentBtn(${id})"></div>`).prependTo(`#content`);
                $("#contentNull").text("내용을 입력하세요");
            }else {
                $(`<div id="contentNotNull" class="contentNotNull" onClick="updateContentBtn(${id})"></div>`).prependTo(`#content`);
                $("#contentNotNull").text(response.content);
            }

            //카드 메뉴 삭제버튼 생성
            $(`<button id="cardMenuDeleteBtn" onClick="cardDelete(${id})">카드삭제</button>`).prependTo(`#cardRight`);

            // 댓글 입력 생성
            // div 생성
            $(`<div id="insertCommentDiv" class="insertCommentDiv"></div>`).prependTo(`.cardComment`);
            // input 생성
            $(`<input id="insertCommentInput" class="insertCommentInput" type="text"/>`).appendTo(`#insertCommentDiv`);
            // 입력버튼 생성
            $(`<button id="insertCommentBtn" class="insertCommentBtn" onclick="insertComment(${id})">입력</button>`).appendTo(`#insertCommentDiv`);

            //댓글 들어갈 div 생성
            $(`<div id="cardCommentContainer" class="cardCommentContainer"></div>`).appendTo(`.cardComment`);

            // 댓글 div에 댓글추가
            for(let i =0; i<response.commetList.length;i++){

                // div 생성
                $(`<div id="viewCardComment-${response.commetList[i]['id']}" class="viewCardComment"></div>`).appendTo(`.cardCommentContainer`);
                // 이름 넣을곳
                $(`<div id="commentUserName">${response.commetList[i]['username']}</div>`).appendTo(`#viewCardComment-${response.commetList[i]['id']}`);
                // 댓글 내용 넣을곳
                $(`<div id="userComment">${response.commetList[i]['comment']}</div>`).appendTo(`#viewCardComment-${response.commetList[i]['id']}`);

                // 댓글 수정 버튼
                $(`<button id="CommentUpdateBtn-${response.commetList[i]['id']}" class="CommentUpdateBtn" onclick="commentUpdateBtn(${response.commetList[i]['id']})">수정</button>`).appendTo(`#viewCardComment-${response.commetList[i]['id']}`);
                // 댓글 삭제 버튼
                $(`<button id="CommentDeleteBtn-${response.commetList[i]['id']}" class="CommentDeleteBtn" onclick="commentDelete(${response.commetList[i]['id']})">삭제</button>`).appendTo(`#viewCardComment-${response.commetList[i]['id']}`);
            }

            // 사용자 할당 버튼 생성
            $(`<button id="cardAllotUsers" onClick="cardAllotUsersBtn(${id})">유저 추가</button>`).appendTo(`#cardRight`);

            // 어떤 사용자가 할당되었는지 이름 표시

            // 사용자 할당용 div 생성
            $(`<div id="cardAllotUsersViewDiv" class="cardAllotUsersViewDiv"><h2>Members</h2></div>`).appendTo(`.cardState`);

            // 카드에 할당된 인원 출력
            for(let i =0; i<response.allotCardUserList.length;i++){
                // 할당된 유저 네임 div
                $(`<div id="cardAllotUserViewDiv-${response.allotCardUserList[i]['userId']}" class="cardAllotUserViewDiv">${response.allotCardUserList[i]['userName']}</div>`).appendTo(`.cardAllotUsersViewDiv`);

            }

            // 시간 데이터 추가 버튼 생성
            $(`<button id="timeSetBtn" onClick="timeSetBtn(${id})">마감 시간</button>`).appendTo(`#cardRight`);

            // 시간 데이터 시각화 div
            $(`<div id="timeSetViewDiv" class="timeSetViewDiv"><h2>EndTime</h2></div>`).appendTo(`.cardState`);

            // 시간데이터 시각화  (시간 데이터가 없을때는 안보이도록)
            if(!(response.endTime == null)){
                if(response.startTime == null){
                    //시작시간이 없을 때
                    $(`<div id="setTimeViewZone" class="setTimeViewZone">${response.endTime} ${response.result}</div>`).appendTo(`.timeSetViewDiv`);
                }else {
                    //시작시간이 있을 때
                    $(`<div id="setTimeViewZone" class="setTimeViewZone">${response.startTime} ~ ${response.endTime} ${response.result}</div>`).appendTo(`.timeSetViewDiv`);
                }
            }

            // 카드 이동 버튼 생성
            $(`<button id="cardMoveBtn" onClick="cardMoveBtn(${id},${response.columnId})">카드 이동</button>`).appendTo(`#cardRight`);


        },
        error: function () {
            alert('카드데이터 불러오기 실패');
            toggleElements(false);
        }
    });

}

// 카드 모달창 접기
function cardClose(){
    $("#contentNull").remove();
    $("#contentNotNull").remove();
    $("#cardMenuDeleteBtn").remove();
    $("#cardCommentContainer").remove();
    $("#insertCommentDiv").remove();
    $("#cardAllotUsers").remove();
    $("#cardAllotUsersViewDiv").remove();
    $("#timeSetBtn").remove();
    $("#cardMoveBtn").remove();
    $("#setTimeViewZone").remove();
    $("#timeSetViewDiv").remove();

    $(".cardModal").fadeOut();
}

function cardUpdate(id){
    //다른 업데이트 창 숨기기
    $(`.card`).find($(`div[class^='updateCard']`)).hide();
    $(`.card`).find($(`div[class^='miniCardDiv']`)).show();
    //카드 내용 숨기고 업데이트창 출력
    $(`#miniCardDiv-${id}`).hide();
    $(`#updateCard-${id}`).show();

}

function updateCardCancle(id){
    //업데이트 창 숨기고 카드 내용 출력
    $(`#updateCard-${id}`).hide();
    $(`#miniCardDiv-${id}`).show();
}

//카드 제목변경
function updateCard(id){

    $.ajax({
        url: `/api/card/cardTitles/${id}`,
        type: 'PUT',
        contentType: 'application/json',
        data: JSON.stringify({title: $(`#updateCardInput-${id}`).val()}),
        headers: {
            'Authorization': document.cookie // 클라이언트 쿠키의 값을 전달
        },
        success: function (response) {
            // 노드 title 값 변경
            $(`.miniCardDiv`).find($(`span[id^='cardTitleSpan-${id}']`)).text(response.title);

            //업데이트 창 숨기고 카드 내용 출력
            $(`#updateCard-${id}`).hide();
            $(`#miniCardDiv-${id}`).show();
        },
        error: function () {
            alert('카드데이터 불러오기 실패');
            toggleElements(false);
        }
    });
}
function insertContentBtn(id){
    // 내용입력 버튼 hide
    $(`#contentNull`).hide();

    // 내용입력창 생성
    // div 생성
    $(`<div id="insertContentDiv" class="insertContentDiv"></div>`).prependTo(`#content`);
    // input 생성
    $(`<input id="insertContentInput" class="insertContentInput" type="text"/>`).appendTo(`#insertContentDiv`);
    // 입력버튼 생성
    $(`<button id="insertContentBtn" class="insertContentBtn" onclick="insertContent(${id})">입력</button>`).appendTo(`#insertContentDiv`);
    // 취소버튼 생성
    $(`<button id="insertContentCancelBtn" class="insertContentCancelBtn" onclick="insertContentCancel()">취소</button>`).appendTo(`#insertContentDiv`);

}


function insertContent(id){
    // 내용 저장
    $.ajax({
        url: `/api/card/cardContents/${id}`,
        type: 'PUT',
        contentType: 'application/json',
        data: JSON.stringify({content: $(`#insertContentInput`).val()}),
        headers: {
            'Authorization': document.cookie // 클라이언트 쿠키의 값을 전달
        },
        success: function (response) {
            //입력창 삭제
            $(`#insertContentDiv`).remove();
            $(`#contentNull`).remove();

            // 데이터 입력 밑 보여주기
            $(`<div id="contentNotNull" class="contentNotNull" onClick="updateContentBtn(${id})"></div>`).prependTo(`#content`);
            $("#contentNotNull").text(response.content);
        },
        error: function () {
            alert('카드데이터 불러오기 실패');
            toggleElements(false);
        }
    });

}

function updateContentBtn(id){
    // 내용창 생성
    $(`#contentNotNull`).hide();

    // 내용입력창 생성
    // div 생성
    $(`<div id="insertContentDiv" class="insertContentDiv"></div>`).prependTo(`#content`);
    // input 생성
    $(`<input id="insertContentInput" class="insertContentInput" type="text"/>`).appendTo(`#insertContentDiv`);
    // 입력버튼 생성
    $(`<button id="insertContentBtn" class="insertContentBtn" onclick="insertContent(${id})">수정</button>`).appendTo(`#insertContentDiv`);
    // 취소버튼 생성
    $(`<button id="insertContentCancelBtn" class="insertContentCancelBtn" onclick="insertContentCancel()">취소</button>`).appendTo(`#insertContentDiv`);
}

function insertContentCancel(){
    $(`#contentNotNull`).show();
    $(`#contentNull`).show();
    $(`#insertContentDiv`).remove();
}

function cardDelete(id){
    // 카드 삭제시

    // 카드 삭제후 모달창 끄기
    $.ajax({
        url: `/api/cards/${id}`,
        type: 'DELETE',
        contentType: 'application/json',
        headers: {
            'Authorization': document.cookie // 클라이언트 쿠키의 값을 전달
        },
        success: function (response) {
            // 모달창 끄기
            $("#contentNull").remove();
            $("#contentNotNull").remove();
            $("#cardMenuDeleteBtn").remove();
            $("#cardCommentContainer").remove();
            $("#insertCommentDiv").remove();
            $("#cardAllotUsers").remove();
            $("#cardAllotUsersViewDiv").remove();
            $("#timeSetBtn").remove();
            $("#cardMoveBtn").remove();
            $("#setTimeViewZone").remove();
            $("#timeSetViewDiv").remove();

            $(".cardModal").fadeOut();

            // 보드 프론트에 반영하기
            // 컬럼에 헤댕하는 노드 삭제
            $(`#card-${id}`).remove();

        },
        error: function () {
            alert('카드데이터 불러오기 실패');
            toggleElements(false);
        }
    });

}

function insertComment(id){

    $.ajax({
        url: `/api/comment/cards/${id}`,
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({content: $(`#insertCommentInput`).val()}),
        headers: {
            'Authorization': document.cookie // 클라이언트 쿠키의 값을 전달
        },
        success: function (response) {
          // 댓글창에 추가
            // // div 생성
            $(`<div id="viewCardComment-${response.id}" class="viewCardComment"></div>`).appendTo(`.cardCommentContainer`);
            // // 이름 넣을곳
            $(`<div id="commentUserName">${response.username}</div>`).appendTo(`#viewCardComment-${response.id}`);
            // // 댓글 내용 넣을곳
            $(`<div id="userComment">${response.comment}</div>`).appendTo(`#viewCardComment-${response.id}`);
            // 댓글 수정 버튼
            $(`<button id="CommentUpdateBtn-${response.id}" class="CommentUpdateBtn" onclick="commentUpdateBtn(${response.id})">수정</button>`).appendTo(`#viewCardComment-${response.id}`);
            // 댓글 삭제 버튼
            $(`<button id="CommentDeleteBtn-${response.id}" class="CommentDeleteBtn" onclick="commentDelete(${response.id})">삭제</button>`).appendTo(`#viewCardComment-${response.id}`);

        },
        error: function () {
            alert('카드데이터 불러오기 실패');
            toggleElements(false);
        }
    });
}

function commentUpdateBtn(id){
    $(`.cardCommentContainer`).find($(`button[class^='CommentUpdateBtn']`)).show();
    $(`.cardCommentContainer`).find($(`button[class^='CommentDeleteBtn']`)).show();

    $(`#updateCommentDiv`).remove();

    // 수정 삭제 버튼 숨기기
    $(`#CommentUpdateBtn-${id}`).hide();
    $(`#CommentDeleteBtn-${id}`).hide();

    // 댓글 수정창 생성
    // div 생성
    $(`<div id="updateCommentDiv" class="updateCommentDiv"></div>`).appendTo(`#viewCardComment-${id}`);
    // input 생성
    $(`<input id="updateCommentInput" class="updateCommentInput" type="text"/>`).appendTo(`#updateCommentDiv`);
    // 입력버튼 생성
    $(`<button id="updateCommentBtn" class="updateCommentBtn" onclick="commentUpdate(${id})">수정</button>`).appendTo(`#updateCommentDiv`);
    // 취소버튼 생성
    $(`<button id="updateCommentCancelBtn" class="updateCommentCancelBtn" onclick="updateCommentCancelBtn(${id})">취소</button>`).appendTo(`#updateCommentDiv`);

}
function commentUpdate(id){

    $.ajax({
        url: `/api/comments/${id}`,
        type: 'PUT',
        contentType: 'application/json',
        data: JSON.stringify({content: $(`#updateCommentInput`).val()}),
        headers: {
            'Authorization': document.cookie // 클라이언트 쿠키의 값을 전달
        },
        success: function (response) {
            // 댓글 수정창 삭제
            $(`#updateCommentDiv`).remove();

            $(`.cardCommentContainer`).find($(`button[class^='CommentUpdateBtn']`)).show();
            $(`.cardCommentContainer`).find($(`button[class^='CommentDeleteBtn']`)).show();

            // 해당 댓글 내용 수정
            $(`#viewCardComment-${id}`).find($(`div[id^='userComment']`)).text(response.content);

        },
        error: function () {
            alert('댓글 작성자가 아닙니다.');

            $(`#updateCommentDiv`).remove();

            $(`.cardCommentContainer`).find($(`button[class^='CommentUpdateBtn']`)).show();
            $(`.cardCommentContainer`).find($(`button[class^='CommentDeleteBtn']`)).show();

            toggleElements(false);
        }
    });
}

function updateCommentCancelBtn(id){
    $(`#updateCommentDiv`).remove();

    $(`#CommentUpdateBtn-${id}`).show();
    $(`#CommentDeleteBtn-${id}`).show();
}

function commentDelete(id){

    $.ajax({
        url: `/api/comments/${id}`,
        type: 'DELETE',
        contentType: 'application/json',
        headers: {
            'Authorization': document.cookie // 클라이언트 쿠키의 값을 전달
        },
        success: function (response) {
         // 해당 DIV 삭제
            $(`#viewCardComment-${id}`).remove();
        },
        error: function () {
            alert('댓글 작성자가 아닙니다.');
            toggleElements(false);
        }
    });

}

// 보드 사용자 띄우기
function cardAllotUsersBtn(id){

    $.ajax({
        url: `/api/cards/${id}/user`,
        type: 'POST',
        contentType: 'application/json',
        headers: {
            'Authorization': document.cookie // 클라이언트 쿠키의 값을 전달
        },
        success: function (response) {
            //유저 모달창 띄우기
            $(".cardAllotUsersModal").fadeIn();
            // 유저조회 모달창 닫기버튼 생성
            $(`<button id="cardAllotUsersModalCancel" class="cardAllotUsersModalCancel" onclick="cardAllotUsersModalCancel(${id})">나가기</button>`).appendTo(`.cardAllotUsersContentTop`);

            // 유저를 담을 div
            $(`<div id="cardAllotUsersDiv" class="cardAllotUsersDiv"></div>`).appendTo(`.cardAllotUsersContentBottom`);

            // 불러올때 이미 할당 유저면 추가 버튼 다른걸로 바꾸기 allotCardUserList (1)

            for(let i = 0; i<response.userList.length;i++){

                //유저 div
                $(`<div id="cardAllotUserDiv-${response.userList[i].userId}" class="cardAllotUserDiv"></div>`).appendTo(`#cardAllotUsersDiv`);

                // 유저 이름
                $(`<div id="cardAllotUserName" class="cardAllotUserName">${response.userList[i].username}</div>`).appendTo(`#cardAllotUserDiv-${response.userList[i].userId}`);

                // 추가 버튼
                $(`<button id="cardAllotUserBtn" class="cardAllotUserBtn" onclick="cardAllotUsers(${id}, ${response.userList[i].userId})">추가</button>`).appendTo(`#cardAllotUserDiv-${response.userList[i].userId}`);

            }

            // (1)
            for(let i = 0; i<response.allotCardUserList.length;i++){
                //추가버튼 삭제
                $(`#cardAllotUsersDiv`).find($(`div[id^='cardAllotUserDiv-${response.allotCardUserList[i].userId}']`)).find($(`button[id^='cardAllotUserBtn']`)).remove();

                let temp_html =
                                    `
                                        <button id="cardAllotUserBtn" class="cardAllotUserBtn" onclick="cardAllotCancelUsers(${response.allotCardUserList[i].cardId},${response.allotCardUserList[i].userId} )">빼기</button>
                                    `;
                //빼기 버튼 추가
                $(`#cardAllotUsersDiv`).find($(`div[id^='cardAllotUserDiv-${response.allotCardUserList[i].userId}']`)).append(temp_html);
            }


            // console.log(response);
        },
        error: function () {
            alert('댓글 작성자가 아닙니다.');
            toggleElements(false);
        }
    });
}

//유저조회 모달창 닫기
function cardAllotUsersModalCancel() {

    $("#cardAllotUsersModalCancel").remove();
    $("#cardAllotUsersDiv").remove();

    $(".cardAllotUsersModal").fadeOut();
}

// 유저 할당 테이블 데이터 넣기
function cardAllotUsers(id, userid){

    $.ajax({
        url: `/api/cards/${id}/users/${userid}`,
        type: 'POST',
        contentType: 'application/json',
        headers: {
            'Authorization': document.cookie // 클라이언트 쿠키의 값을 전달
        },
        success: function (response) {
            console.log("users :",response)
            // 추가버튼 지우기
            $(`#cardAllotUsersDiv`).find($(`div[id^='cardAllotUserDiv-${userid}']`)).find($(`button[id^='cardAllotUserBtn']`)).remove();
            // 빼기버튼 추가
            let temp_html =
                                    `
                                        <button id="cardAllotUserBtn" class="cardAllotUserBtn" onclick="cardAllotCancelUsers(${id}, ${userid})">빼기</button>
                                    `;

            $(`#cardAllotUsersDiv`).find($(`div[id^='cardAllotUserDiv-${userid}']`)).append(temp_html);

            let temp_cardHtml =
                                `
                                <div id="cardAllotUserViewDiv-${userid}" class="cardAllotUserViewDiv">${response.userName}</div>
                                `;

            // 카드 모달창에 표시하기
            $(`#cardAllotUsersViewDiv`).append(temp_cardHtml);


        },
        error: function () {
            alert('이미 할당된 유저 입니다.');
            toggleElements(false);
        }
    });
}
function cardAllotCancelUsers(id, userid){
     // console.log(id);
    $.ajax({
        url: `/api/cards/${id}/users/${userid}`,
        type: 'DELETE',
        contentType: 'application/json',
        headers: {
            'Authorization': document.cookie // 클라이언트 쿠키의 값을 전달
        },
        success: function (response) {
            // 빼기 버튼 삭제
            $(`#cardAllotUsersDiv`).find($(`div[id^='cardAllotUserDiv-${userid}']`)).find($(`button[id^='cardAllotUserBtn']`)).remove();

            let temp_html =
                `
                                        <button id="cardAllotUserBtn" class="cardAllotUserBtn" onclick="cardAllotUsers(${id}, ${userid})">추가</button>
                                    `;
            //  추가 버튼 추가
            $(`#cardAllotUsersDiv`).find($(`div[id^='cardAllotUserDiv-${userid}']`)).append(temp_html);

            //카드 모달창에 반영
            $(`#cardAllotUsersViewDiv`).find($(`div[id^='cardAllotUserViewDiv-${userid}']`)).remove();
        },
        error: function () {
            alert('삭제할 유저가 없습니다.');
            toggleElements(false);
        }
    });

}

function  timeSetBtn(id){
    // 시간 설정 모달창 띄우기
    $(".timeSetModal").fadeIn();
    // 시간 설정 모달창 닫기버튼 생성
    $(`<button id="timeSetModalCancel" class="timeSetModalCancel" onclick="timeSetModalCancel()">나가기</button>`).appendTo(`.timeSetModalTop`);

    // 시간 설정 div
    $(`<div id="endTimeSetViewDiv" class="endTimeSetViewDiv"></div>`).appendTo(`.timeSetModalBottom`);

    // 시작시간 div
    $(`<div id="startTimeSetDiv" class="startTimeSetDiv"></div>`).appendTo(`.endTimeSetViewDiv`);

    // 시작 시간 활성화 버튼
    $(`<button id="endTimeSetBtn" class="endTimeSetBtn" onclick="startTimeSet()">시작 시간</button>`).appendTo(`.startTimeSetDiv`);


    // 마감시간 div

    $(`<div id="endTimeSetDiv" class="endTimeSetDiv"></div>`).appendTo(`.endTimeSetViewDiv`);
    // 마감 시간
    $(`<input id="endTimeSetInput" type="datetime-local" name="starttime">`).appendTo(`.endTimeSetDiv`);
    // 시간 저장 버튼
    $(`<button id="endTimeSetBtn" class="endTimeSetBtn" onclick="endTimeSet(${id})">시간 저장</button>`).appendTo(`.endTimeSetDiv`);
}

// 시간 성정 모달창 닫기
function timeSetModalCancel(){
    $(".timeSetModalCancel").remove();
    $(".endTimeSetViewDiv").remove();

    $(".timeSetModal").fadeOut();
}

function endTimeSet(id){

    $.ajax({
        url: `/api/cards/${id}/time`,
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({startTime:$("#datepicker").val() , endTime: $(`#endTimeSetInput`).val()}),
        headers: {
            'Authorization': document.cookie // 클라이언트 쿠키의 값을 전달
        },
        success: function (response) {
            console.log(response);
            // 시간 데이터가 들어갔을때
            // 카드에 반영 하기


            if(response.startTime==null){
                //시작 시간이 없을시
                $("#setTimeViewZone").remove();
                $(`<div id="setTimeViewZone" class="setTimeViewZone">${response.endTime} ${response.result}</div>`).appendTo(`.timeSetViewDiv`);
            }else {
                //시작 시간이 있을시
                $("#setTimeViewZone").remove();
                $(`<div id="setTimeViewZone" class="setTimeViewZone">${response.startTime} ~ ${response.endTime} ${response.result}</div>`).appendTo(`.timeSetViewDiv`);
            }


        },
        error: function () {
            alert('삭제할 유저가 없습니다.');
            toggleElements(false);
        }
    });
}

function startTimeSet(){

    $(`<input type="date" id="datepicker" name="datepicker" >`).prependTo(`.startTimeSetDiv`);

    var today = new Date().toISOString().substr(0, 10);
    document.getElementById("datepicker").value = today;

    //시작시간 버튼 지우기
    $("#endTimeSetBtn").remove();

    //시작시간 지우기 버튼
    $(`<button id="endTimeSetBtnDel" class="endTimeSetBtnDel" onclick="startTimeBtnDel()">빼기</button>`).appendTo(`.startTimeSetDiv`);

}

function startTimeBtnDel(){
    $("#datepicker").remove();
    $("#endTimeSetBtnDel").remove();

    //시작시간 버튼 생성
    $(`<button id="endTimeSetBtn" class="endTimeSetBtn" onclick="startTimeSet()">시작 시간</button>`).appendTo(`.startTimeSetDiv`);
}

// 카드 이동 모달 창 띄우기
function cardMoveBtn(id){
    $(".cardMoveModel").fadeIn();

    $(`<button id="cardMovBtnCancel" class="cardMovBtnCancel" onclick="cardMovBtnCancel()">닫기</button>`).appendTo(`.cardMoveModelTop`);

    $(`<button id="cardMoveDataSet" onClick="cardMoveDataSet(${id})">확인</button>`).appendTo(`.cardMoveModelContent`);

}

// 카드 이동 모달 창 닥기

function cardMovBtnCancel(){
    $(".cardMovBtnCancel").remove();
    $("#cardMoveDataSet").remove();

    $(".cardMoveModel").fadeOut();
}

// 선택한 값 input에 넣기
function testDiv(id){
    let temp = $(`#divb-${id}`).text();
    $("#choicePositionInput").attr('value',temp);

}
// 카드이동 데이터 set
function cardMoveDataSet(id, columnid){
        alert(id)
    $.ajax({
        url: `/api/cards/${id}`,
        type: 'PATCH',
        contentType: 'application/json',
        data: JSON.stringify({position:$("#choicePositionInput").val() , columnId: $(`#choiceColumnInput`).val()}),
        headers: {
            'Authorization': document.cookie // 클라이언트 쿠키의 값을 전달
        },
        success: function (response) {
            $("#cardMoveDataSet").remove();


            showSelectedBoard(1, 1);
        },
        error: function () {
            alert('삭제할 유저가 없습니다.');
            toggleElements(false);
        }
    });
}

const editBoardModal = document.getElementById('editBoardModal');
const editClose = document.getElementById('editClose');
const updateBoardButton = document.getElementById('updateBoard');

updateBoardButton.addEventListener('click', () => {
    editBoardModal.style.display = 'block';
})

editClose.addEventListener('click', () => {
    editBoardModal.style.display = 'none';
})

const createBoardButton = document.getElementById('createBoard');
const boardModalOverlay = document.getElementById('boardModalOverlay');
const registerBoardModal = document.getElementById('registerBoardModal');
const boardTitleInput = document.getElementById('boardTitle');
const boardDescriptionInput = document.getElementById('boardDescription');
const saveBoardButton = document.getElementById('saveBoard');
const closeButton = document.getElementById('close');

createBoardButton.addEventListener('click', () => {
    boardModalOverlay.style.display = 'block';
    registerBoardModal.style.display = 'block';
});

closeButton.addEventListener('click', () => {
    boardModalOverlay.style.display = 'none';
    registerBoardModal.style.display = 'none';
});

// 보드 삭제 함수
function deleteThisBoard() {
    // 지금 보이는 보드의 id, title
    var boardIdToDelete = $(this).attr('data-board-id');
    var boardNameToDelete = $(this).attr('data-board-name');

    console.log(boardIdToDelete);

    // alert 창으로 한 번 더 확인 후
    if (confirm("정말 " + boardNameToDelete + "을(를) 삭제하시겠습니까?")) {
        // 삭제 요청
        $.ajax({
            type: "DELETE",
            url: `/api/boards/` + boardIdToDelete,
            contentType: "application/json",
            success: function (xhr) {
                console.log(xhr.msg);                   // 삭제되었습니다.
                alert(xhr.msg);                   // 삭제되었습니다.
                location.reload();
            },
            error: function (response) {
                console.log('보드 삭제 실패');
                console.log(response.responseJSON.msg);
                alert(response.responseJSON.msg);       // 관리자만 ~ 있습니다.
            }
        })
    }
}

// 보드에 속한 멤버 보기
function showMembersOfBoard(clickedBoardId) {
    $.ajax({
        url: 'http://localhost:8080/api/boards/' + clickedBoardId + '/members',
        type: 'GET',
        contentType: 'application/json',
        headers: {
            'Authorization': document.cookie
        },
        success: function (response) {
            console.log(response);

            const memberDropdownList = document.getElementById('dropdown-menu');
            memberDropdownList.innerHTML = ''; // Clear any previous entries

            response.memberList.forEach(member => {
                const memberListItem = document.createElement('li');
                const memberLink = document.createElement('a'); // Create an anchor element
                memberLink.className = 'dropdown-item'; // Set the class name
                memberLink.textContent = `${member.username} (${member.role})`; // Set the username as text content
                memberLink.onclick = function () {
                    localStorage.setItem("clickedUserIdToChangeRole", member.userId);
                    openPermissionModal(clickedBoardId, member.userId); // Pass the necessary parameters
                };
                memberListItem.appendChild(memberLink); // Append the anchor element to the list item
                memberDropdownList.appendChild(memberListItem); // Append the list item to the dropdown menu
            });
        },
        error: function () {
            console.log('멤버 목록 불러오기 오류');
        }
    });
}

// 권한 변경 모달 열고 닫기
function openPermissionModal(boardId, userId) {
    const modal = document.getElementById('registerPermissionChangeModal');
    const modalOverlay = document.getElementById('permissionChangeModalOverlay');
    const changePermissionsButton = document.getElementById('changePermissions');

    changePermissionsButton.onclick = function () {
        modal.style.display = 'none';
        modalOverlay.style.display = 'none';
        changeBoardPermissions(boardId, userId);
    };

    const closeModal = document.getElementById('closePermissionChange');
    closeModal.onclick = function () {
        modal.style.display = 'none';
        modalOverlay.style.display = 'none';
    };

    modal.style.display = 'block';
}

// 권한 변경
function changeBoardPermissions(boardId, userId) {
    console.log(boardId);
    console.log(userId);
    const boardTitle = localStorage.getItem('boardTitle');

    const url = `http://localhost:8080/api/boards/${boardId}/members/${userId}`;
    fetch(url, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': document.cookie
        }
    })
        .then(response => response.json())
        .then(data => {
            console.log(data.message);
            alert("권한 변경이 완료되었습니다.")
            showSelectedBoard(boardId, boardTitle);
        })
        .catch(error => {
            console.error('Error changing permissions:', error);
            alert("권한 변경 오류")
        });
}


// ======================================================================= 사용자 검색
$('#searchUserBtn').click(clickSearchUserIcon);

// 검색어 입력 후 검색 버튼 클릭 -> 모달 창에 검색 결과 출력
function clickSearchUserIcon() {
    console.log('사용자 검색 클릭');
    let userSearchKeyword = $('#searchKeywordInput').val();
    console.log(userSearchKeyword);

    if (userSearchKeyword.trim() === '') {
        alert('검색어를 입력하세요');
        return;
    }

    $.ajax({
        type: "GET",
        url: `/api/users/search?keyword=${userSearchKeyword}`,
        headers: {
            'Authorization': document.cookie
        },
        success: function (response) {
            console.log('검색 요청 성공');
            console.log(response);
            // 모달 보이기
            showModal('showSearchResultModal', 'searchResultModalOverlay');

            // 검색 결과 붙이기
            let temp_htmls = '';
            $('#searchedUserListUl').empty();

            response.searchUserResults.forEach((a) => {
                let searchedUserId = a['userId'];
                let searchedUsername = a['username'];
                let searchedUserNickname = a['nickname'];

                let temp_html = `
                        <li class="list-searched-user-item" list-search-result-id="${searchedUserId}">
                            <div class="search-result-username" id="list-search-result-username" style="font-weight: bold">${searchedUsername}</div>
                            <div class="search-result-nickname" id="list-search-result-nickname">${searchedUserNickname}</div>
                            <button class="invite-button">초대</button>
                            <div>-----------------</div>
                        </li>
                    `;

                temp_htmls += temp_html;
            });
            console.log('검색결과 forEach문 끝');
            $('#searchedUserListUl').append(temp_htmls);

        },
        error: function (response) {
            console.log('검색 요청 실패');
            console.log(response);
        }
    })
}

const closeSearchResultModalBtn = document.getElementById('closeSearchResultModal');

closeSearchResultModalBtn.addEventListener('click', closeModal.bind(null, 'showSearchResultModal', 'searchResultModalOverlay'));

// 모달 열기
function showModal(modalId, overlayId) {
    const modal = document.getElementById(modalId);
    const overlay = document.getElementById(overlayId);
    modal.style.display = 'block';
    overlay.style.display = 'block';
}

// 모달 닫기
function closeModal(modalId, overlayId) {
    const modal = document.getElementById(modalId);
    const overlay = document.getElementById(overlayId);

    modal.style.display = 'none';
    overlay.style.display = 'none';

    const boardId = localStorage.getItem('boardId');
    const boardTitle = localStorage.getItem('boardTitle');
    showSelectedBoard(boardId, boardTitle);
}

// 검색된 사용자 중 선택
let clickedUserIdToInvite;
let clickedUsernameToInvite;

$(document).on("click", ".invite-button", function () {
    clickedUserIdToInvite = $(this).closest(".list-searched-user-item").attr("list-search-result-id");
    localStorage.setItem("clickedUserIdToInvite", clickedUserIdToInvite);

    clickedUsernameToInvite = $(this).closest(".list-searched-user-item").find(".search-result-username").text();

    localStorage.setItem("clickedUsernameToInvite", clickedUsernameToInvite);
    console.log('저장 직전 clickedInviteUsername: ' + clickedUsernameToInvite);

    inviteUserThisBoard(clickedUserIdToInvite, clickedUsernameToInvite)
})

// 클릭한 사용자 초대
function inviteUserThisBoard(clickedUserIdToInvite, clickedUsernameToInvite) {
    let clickedInviteUserId = localStorage.getItem("clickedUserIdToInvite");
    let clickedInviteUsername = localStorage.getItem("clickedUsernameToInvite");
    let currentBoardId = localStorage.getItem("boardId");
    let currentBoardTitle = localStorage.getItem("boardTitle");

    console.log('clickedInviteUserId: ' + clickedInviteUserId);
    console.log('clickedInviteUsername: ' + clickedInviteUsername);
    console.log('currentBoardId: ' + currentBoardId);

    if (confirm(clickedInviteUsername + '을(를) ' + currentBoardTitle + ' 보드에 초대하시겠습니까?')) {

        $.ajax({
            type: "POST",
            url: `/api/boards/${currentBoardId}/invite/${clickedInviteUserId}`,
            contentType: "application/json",
            success: function (xhr) {
                console.log(xhr);
                console.log('보드에 초대 요청 성공');
                alert(xhr.msg);
            },
            error: function (response) {
                console.log('보드에 초대 요청 실패');
                console.log(response.responseJSON.msg);
                alert(response.responseJSON.msg);
            }
        })
    }

}