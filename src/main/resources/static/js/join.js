document.addEventListener("DOMContentLoaded", () => {
    const emailCheckButton = document.querySelector("#email-check-button");
    let isEmail = false;
    let isNickname = false;

    emailCheckButton.addEventListener("click", () => {
        const userEmail = document.querySelector("#userEmail");
         console.log(userEmail);
        $.ajax({
            url: "/user/email/check",
            method: "post",
            data: memberDTO = {
                userEmail: userEmail.value
            },
            success: function (responseData) {
                isEmail = true;
                alert(responseData)
            },
            error: function (error) {
                userEmail.value = '';
                alert(error.responseText);
            }
        })
    })

    const nicknameCheckButton = document.querySelector("#nickname-check-button");
    nicknameCheckButton.addEventListener("click", () => {
        const nickname = document.querySelector("#nickname");
        console.log(nickname);
        $.ajax({
            url: "/user/nickname/check",
            method: "post",
            data: memberDTO = {
                nickname : nickname.value
            },
            success: function (responseData) {
                isNickname = true;
                alert(responseData)
            },
            error: function (error) {
                nickname.value = '';
                alert(error.responseText);
            }
        })
    })

    const password = document.querySelector("#password");
    const passwordCheck = document.querySelector("#password-check");

    passwordCheck.addEventListener("blur",()=>{
        if(password.value === passwordCheck.value) {
            password.style.color = "green";
            passwordCheck.style.color = "green";
        }else{
            password.style.color = "red";
            passwordCheck.style.color = "red";
        }
    })

    const form = document.querySelector(".join-form");
    form.addEventListener("submit", function (event) {
        if (password.value !== passwordCheck.value) {
            alert("비밀번호가 일치하지 않습니다.");
            event.preventDefault(); // 제출 중단
        }

        if(isEmail !== true && isNickname !== true) {
            alert('중복체크를 하지 않았습니다');
            event.preventDefault();
        }
    });



})