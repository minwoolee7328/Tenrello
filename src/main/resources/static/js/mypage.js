document.addEventListener("DOMContentLoaded", function () {
    const goBackButton = document.getElementById('goBack');
    const backToTopButton = document.getElementById('backToTop');

    const changeNicknameButton = document.getElementById('changeNickname');
    const changePasswordButton = document.getElementById('changePassword');
    const deleteUserButton = document.getElementById('deleteUserBtn');

    const closeNicknameModalButton = document.getElementById('closeNicknameModal');
    const closePasswordModalButton = document.getElementById('closePasswordModal');
    const closeDeleteUserModalButton = document.getElementById('closeDeleteUserModal');


    goBackButton.addEventListener('click', function () {
        window.history.back();
    });

    backToTopButton.addEventListener('click', function () {
        window.scrollTo({top: 0, behavior: 'smooth'});
    });

    window.addEventListener('scroll', function () {
        backToTopButton.style.display = (window.scrollY > 20) ? 'block' : 'none';
    });

    // 모달 창 띄우기
    changeNicknameButton.addEventListener('click', showModal.bind(null, 'registerNicknameModal', 'nicknameModalOverlay'));
    changePasswordButton.addEventListener('click', showModal.bind(null, 'registerPasswordModal', 'passwordModalOverlay'));
    deleteUserButton.addEventListener('click', showModal.bind(null, 'registerDeleteUserModal', 'deleteUserModalOverlay'));

    // 모달 창 닫기
    closeNicknameModalButton.addEventListener('click', closeModal.bind(null, 'registerNicknameModal', 'nicknameModalOverlay'));
    closePasswordModalButton.addEventListener('click', closeModal.bind(null, 'registerPasswordModal', 'passwordModalOverlay'));
    closeDeleteUserModalButton.addEventListener('click', closeModal.bind(null, 'registerDeleteUserModal', 'deleteUserModalOverlay'));

    // 닉네임 변경하기
    $(document).on("click", "#submitNicknameModal", changeNickname);

    // 비밀번호 변경하기
    $(document).on("click", "#submitPasswordModal", changePassword);

    // 회원 탈퇴하기
    $(document).on("click", "#submitDeleteUserModal", deleteUser);
});

function showModal(modalId, overlayId) {
    const modal = document.getElementById(modalId);
    const overlay = document.getElementById(overlayId);
    modal.style.display = 'block';
    overlay.style.display = 'block';
}

function closeModal(modalId, overlayId) {
    const modal = document.getElementById(modalId);
    const overlay = document.getElementById(overlayId);
    modal.style.display = 'none';
    overlay.style.display = 'none';
}

function changeNickname() {
    let nicknameInput = $('#nicknameInput').val();

    if (nicknameInput.trim() === '') {
        alert('새로운 닉네임을 입력하세요');
        return;
    }

    let data = {
        newNickname:nicknameInput
    };

    $.ajax({
        type: "PUT",
        url: `/api/users/nickname`,
        contentType: "application/json",
        data: JSON.stringify(data),
        success: function (xhr) {       // xhr : response로 받은 데이터
            console.log(xhr);

            alert('이전 닉네임 : ' + xhr.nicknameBefore +
                '\n' + '새로운 닉네임 : ' + xhr.nicknameAfter);                 // ResponseEntity<ApiResponseDto> 의 메세지
            location.reload();
        },
        error: function (response) {
            console.log('닉네임 변경 실패');
            console.log(response.responseJSON.msg);
        }
    })
}

function changePassword() {
    let passwordInput = $('#passwordInput').val();
    let newPasswordInput = $('#newPasswordInput').val();

    if (passwordInput.trim() === '') {
        alert('현재 비밀번호를 입력하세요');
        return;
    }
    if (newPasswordInput.trim() === '') {
        alert('새로운 비밀번호를 입력하세요');
        return;
    }

    let data = {
        password:passwordInput,
        newPassword:newPasswordInput
    };

    // 비밀번호 변경 ajax 요청
    $.ajax({
        type: "PUT",
        url: `/api/users/password`,
        contentType: "application/json",
        data: JSON.stringify(data),
        success: function (xhr) {
            console.log(xhr);
            alert(xhr.msg + '\n다시 로그인 하세요.');

            // 변경 성공 > 로그아웃 ajax 요청
            $.ajax({
                type: "POST",
                url: `/api/logout`,
                contentType: "application/json",
                success: function (xhr) {
                    console.log(xhr.msg);
                    logout();
                },
                error: function(response) {
                    console.log('변경 후 로그아웃 실패');
                    console.log(response.responseJSON.msg);
                    location.reload();
                }
            })
        },
        error: function (response) {
            console.log(response.responseJSON.msg);
            alert(response.responseJSON.msg);
        }
    })
}

function deleteUser() {
    let deleteUserInput = $('#deleteUserInput').val();

    if (deleteUserInput.trim() === '') {
        alert('비밀번호를 입력하세요');
        return;
    }

    let data = {
        password:deleteUserInput
    };

    $.ajax({
        type: "DELETE",
        url: `/api/users`,
        contentType: "application/json",
        data: JSON.stringify(data),
        success: function (xhr) {
            console.log(xhr);
            alert(xhr.msg + '\n로그인 화면으로 돌아갑니다');     // 탈퇴 완료
            logout();
        },
        error: function (response) {
            console.log('회원 탈퇴 실패');
            console.log(response.responseJSON.msg);         // 비밀번호가 일치하지 않습니다.
            alert(response.responseJSON.msg);               // 비밀번호가 일치하지 않습니다.
        }
    })
}

function logout() {
    Cookies.remove('Authorization', {path: '/'});
    window.location.href = "/view/main";
}