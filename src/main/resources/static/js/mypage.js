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
        error: function () {
            console.log('닉네임 변경 실패');
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

    $.ajax({
        type: "PUT",
        url: `/api/users/password`,
        contentType: "application/json",
        data: JSON.stringify(data),
        success: function (xhr) {
            console.log(xhr);
            alert(xhr.msg + '\n 다시 로그인 하세요.');

            $.ajax({
                type: "POST",
                url: `/api/logout`,
                contentType: "application/json",
                success: function (xhr) {
                    console.log(xhr.msg);
                    Cookies.remove('Authorization', {path: '/'});
                    window.location.href = "/view/main";
                },
                error: function() {
                    console.log('변경 후 로그아웃 실패');
                    location.reload();
                }
            })
        },
        error: function (xhr) {
            console.log(xhr.msg);
            alert(xhr.msg);
        }
    })
}