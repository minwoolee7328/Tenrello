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
            window.location.href = "/view/index";
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
                    console.log("if이동");
                } else {
                    if (list.contains(draggedItem.nextSibling)) {
                        container.insertBefore(draggedItem, list);
                        console.log("한 열 더 이동");
                    } else {
                        container.insertBefore(draggedItem, list.previousSibling);

                    }
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