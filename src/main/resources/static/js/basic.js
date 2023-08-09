function submitLogin() {
    let loginUser = $('#loginUser').val();
    let loginPass = $('#loginPass').val();

    console.log('로그인 시도 : ' + loginUser);
    console.log('비밀번호 : ' + loginPass);

    let data = {
        username: loginUser,
        password: loginPass
    };

    if (loginUser.trim() === '') {
        alert('아이디를 입력하세요');
        return;
    }
    if (loginPass.trim() === '') {
        alert('비밀번호를 입력하세요');
        return;
    }

    $.ajax({
        type: "POST",
        url: `/api/auth/login`,
        contentType: "application/json",
        data: JSON.stringify(data),
        success: function (xhr) {       // xhr : response로 받은 데이터
            console.log(xhr);
            alert(xhr.msg);                 // ResponseEntity<ApiResponseDto> 의 메세지
            window.location.href = "/next";
        },
        error: function () {
            console.log('실패');
        }
    })
}

function submitSignup() {
    let signupUser = $('#signupUser').val();
    let signupPass = $('#signupPass').val();

    console.log('로그인 시도 : ' + signupUser);
    console.log('비밀번호 : ' + signupPass);

    let data = {
        username: signupUser,
        password: signupPass
    };

    $.ajax({
        type: "POST",
        url: `/api/auth/signup`,
        contentType: "application/json",
        data: JSON.stringify(data),
        success: function (xhr) {       // xhr : response로 받은 데이터
            console.log(xhr);
            alert(xhr.msg);                 // ResponseEntity<ApiResponseDto> 의 메세지
            location.reload();
        },
        error: function () {
            console.log('회원가입 실패');
        }
    })
}