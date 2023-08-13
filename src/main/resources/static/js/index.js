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
                let cardTitles = a['cardList'];

                columnSettingArr[i] = columnId;
                i += 1;

                let temp_html = `
                        <div class="list" id="${columnId}" draggable="true">
                            <div class="list-header">${columnTitle}
                                   <button onclick="DeleteColumnBtn(${columnId})" style="float: right; margin-right: 5px;" class="bi bi-trash3 fs-20"></button>
                                   <button onclick="modifyColumnBtn(${columnId})" style="float: right; margin-right: 5px;" class="bi bi-pencil fs-20"></button>
                            </div>
                    `;

                cardTitles.forEach((a) => {
                    let card_html = `<div id="card-${a['id']}" class="card" draggable="true" onclick="model(${a['id']})">${a['title']}</div>`;

                    temp_html += card_html;
                })

                temp_htmls += temp_html + `
                                             <div id="cardInsertDiv-${columnId}" class="insertDid" style="display: none">
                                             <input id="cardInsert-${columnId}"  type="text"/>
                                             <button id="insert" onclick="insertData(${columnId},${a['id']})">확인</button>
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
function insertData(id, cardid) {

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


function model(id) {
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

function cardClose() {
    $(".cardModal").fadeOut();
}

// 보드 모달 none, block
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
                memberLink.onclick = function() {
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
    const modal = document.getElementById('permissionModal');
    const changePermissionsButton = document.getElementById('changePermissions');

    changePermissionsButton.onclick = function() {
        modal.style.display = 'none';
        changeBoardPermissions(boardId, userId);
    };

    const closeModal = document.getElementById('closeModal');
    closeModal.onclick = function() {
        modal.style.display = 'none';
    };

    modal.style.display = 'block';
}

// 권한 변경
function changeBoardPermissions(boardId, userId) {
    console.log(boardId);
    console.log(userId);

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
            location.reload();
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

    if(confirm(clickedInviteUsername + '을(를) ' + currentBoardTitle + ' 보드에 초대하시겠습니까?')) {

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