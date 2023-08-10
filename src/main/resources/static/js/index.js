<!--  offcanvas  -->


const jwtToken = getJwtFromCookie();
$(document).ready(function () {
    console.log("(document).ready");

    getColumns(1);//임시 나중에 board연결시 클릭시 API로 boarId값 받아서 넣어주기
})

function getColumns(boardId){
    $.ajax({
        url: `/api/boards/${boardId}/columns`,
        method: 'GET',
        headers: {
            "Authorization": jwtToken,
        },
        contentType: 'application/json',
        success: function (response) {
            $('.list-container').empty();
            console.log(response)
            response.forEach(function (column) {
                var temp_html = `<div class="list" id="columnList" draggable="true">
                                <div class="list-header">${column.title}
                                    <button onclick="DeleteColumnBtn(${column.id})" style="float: right; margin-right: 5px;" class="bi bi-trash3 fs-20"></button>
                                    <button onclick="modifyColumnBtn(${column.id})" style="float: right; margin-right: 5px;" class="bi bi-pencil fs-20"></button>
                                </div>
<!--                                이쪽에 card들이 들어가면 됩니다.-->
                                
                                </div>`
                $('.list-container').append(temp_html);
            })
        }
    })
}
function DeleteColumnBtn(id){
    $.ajax({
        url: `/api/columns/${id}`, // 요청을 보낼 서버의 URL
        method: 'DELETE', // 요청 메소드 (GET, POST 등)
        headers: {
            "Authorization": jwtToken,
        },
        success: function (response) {
            alert("메뉴 삭제 완료")
            window.location.href = "/index";
        },
        error: function (xhr, status, error) {
            alert("메뉴 삭제 실패")
            console.log(xhr);
        }
    });

}
function modifyColumnBtn(){

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

addListButton.addEventListener('click', () => {
    const container = document.getElementById('listContainer');
    const newList = document.createElement('div');
    newList.className = 'list';
    newList.draggable = true;
    newList.innerHTML = `
    <div class="list-header">New List</div>
    <div class="card" draggable="true">New Card</div>
    `;

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

    container.appendChild(newList);
});