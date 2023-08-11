<!--  offcanvas  -->


const jwtToken = getJwtFromCookie();
$(document).ready(function () {
    console.log("(document).ready");

})


function DeleteColumnBtn(id){
    $.ajax({
        url: `/api/columns/${id}`, // 요청을 보낼 서버의 URL
        method: 'DELETE', // 요청 메소드 (GET, POST 등)
        headers: {
            "Authorization": jwtToken,
        },
        success: function (response) {
            alert("메뉴 삭제 완료")
            showSelectedBoard();
        },
        error: function (xhr, status, error) {
            alert("메뉴 삭제 실패")
            console.log(xhr);
        }
    });

}
function modifyColumnBtn(id){

    const modifyDiv = document.getElementById("columnModify");
    console.log(modifyDiv);
    localStorage.setItem("modifyColumnId",id);

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

function showSelectedBoard(boardId) {

    var clickedBoardId = localStorage.getItem("boardId");

    var clickedBoardName = $(this).text();

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

                columnSettingArr[i] = columnId;
                i+=1;

                let temp_html = `
                        <div class="list" id="${columnId}" draggable="true">
                            <div class="list-header">${columnTitle}
                                   <button onclick="DeleteColumnBtn(${columnId})" style="float: right; margin-right: 5px;" class="bi bi-trash3 fs-20"></button>
                                   <button onclick="modifyColumnBtn(${columnId})" style="float: right; margin-right: 5px;" class="bi bi-pencil fs-20"></button>
                            </div>
<!--                            카드-->
                        </div>
                    `;

                temp_htmls += temp_html;
            });
            console.log('forEach문 끝');
            $('#listContainer').append(temp_htmls);
            modalBoard.style.display = "block";

            columnSettingArr.forEach((col)=>{       //조회된 컬럼들 순서대로 드래그 자리 바꾸기 가능하게 설정
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
                        if (draggedItem.classList.contains('card')) {
                            if (newList.contains(draggedItem)) {
                                return;
                            }
                            newList.appendChild(draggedItem);
                        } else if (draggedItem.classList.contains('list')) {
                            if (newList.contains(draggedItem)) {
                                return;
                            }
                            if (newList.nextSibling === draggedItem) {
                                container.insertBefore(draggedItem, newList);
                            } else {
                                container.insertBefore(draggedItem, newList.nextSibling);
                            }
                        }
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
        }
    });

    modalBoard.style.display = "block";
}

function logout() {
    Cookies.remove('Authorization', {path: '/'});
    window.location.href = "/view/main";
}

//카드추가 버튼
function createBtn(id){
    //추가하기 버튼 해당 컬럼 id 불러오기
    console.log("id",id);

    $(`#listContainer`).find($(`div[class^='insertDid']`)).hide();
    $(`#listContainer`).find($(`button[class^='createBtn']`)).show();

    // 카드 부분에 insert 창 만들기

    // 카드 추가하기 버튼 숨기기
    $(`#createBtn-${id}`).hide();

    //insert 창 만들기 show (컬럼에 해당하는)
    $(`#cardInsertDiv-${id}`).show();

}



// 카드추가 취소
function cancleBtn(id){
    // 카드 추가하기 버튼 나타내기
    $(`#createBtn-${id}`).show();

    //컬럼에 해당하는 insert창 숨기기
    $(`#cardInsertDiv-${id}`).hide();
}

// 카드추가 기능
function insertData(id, cardid){

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
            $(`<div id="card-${cardid}" class="card" draggable="true" onclick="model(${cardid})">${response.title}</div>`).insertBefore(`#cardInsertDiv-${id}`);

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

$("#cardName")


function model(id){
    $(".cardModal").fadeIn();

    $("#cardName").text("카드이름");
    // 카드 데이터 불러오기

    $.ajax({
        url: `/api/cards/${id}`,
        type: 'GET',
        contentType: 'application/json',
        headers: {
            'Authorization': document.cookie // 클라이언트 쿠키의 값을 전달
        },
        success: function (response) {
            $("#cardName").text(response.title);

        },
        error: function () {
            alert('카드데이터 불러오기 실패');
            toggleElements(false);
        }
    });

}

function cardClose(){
    $(".cardModal").fadeOut();
}

