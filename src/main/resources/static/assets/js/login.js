
var normalBox = document.querySelector(".normal-box");
var loginKey = document.getElementById("username");

loginKey.addEventListener("focus", function () {
    normalBox.classList.add("box-focus");
});

loginKey.addEventListener("blur", function () {
    normalBox.classList.remove("box-focus");
});


document.addEventListener("DOMContentLoaded", function () {
    const loginBtn = document.getElementById("login-btn");
    const username = document.getElementById("username");
    const password = document.getElementById("password");

    const authenUsernameInput = document.getElementById("authen-username-input");
    const authenPasswordInput = document.getElementById("authen-password-input");

    const normalBox = document.querySelector(".normal-box");
    const passBox = document.querySelector(".pass-box");

    function updateLoginButton() {
        const isUsernameValid = username.value.trim() !== "";
        const isPasswordValid = password.value.trim() !== "";

        if (isUsernameValid && isPasswordValid) {
            loginBtn.removeAttribute("disabled");
            loginBtn.classList.remove("btn-disbled");
        } else {
            loginBtn.setAttribute("disabled", "true");
            loginBtn.classList.add("btn-disbled");
        }
    }

    username.addEventListener("input", function () {
        const isEmpty = this.value.trim() === "";
        if (isEmpty) {
            authenUsernameInput.classList.remove("hide");
            this.classList.add("error-bgr");
            normalBox.classList.add("error-input");
        } else {
            authenUsernameInput.classList.add("hide");
            this.classList.remove("error-bgr");
            normalBox.classList.remove("error-input");
        }
        updateLoginButton();
    });

    password.addEventListener("input", function () {
        const isEmpty = this.value.trim() === "";
        if (isEmpty) {
            authenPasswordInput.classList.remove("hide");
            this.classList.add("error-bgr");
            passBox.classList.add("error-input");
        } else {
            authenPasswordInput.classList.add("hide");
            this.classList.remove("error-bgr");
            passBox.classList.remove("error-input");
        }
        updateLoginButton();
    });
    updateLoginButton();
});


var passBox = document.querySelector(".pass-box");
var password = document.getElementById("password");

password.addEventListener("focus", function() {
    passBox.classList.add("box-focus");
});

password.addEventListener("blur", function() {
    passBox.classList.remove("box-focus");
});

var passHideBtn = document.getElementById("pass-hide-btn");
passHideBtn.addEventListener("click", function() {
    if(password.type === "password") {
        password.type = "text";
        passHideBtn.querySelector("i").classList.remove("fa-eye-slash");
        passHideBtn.querySelector("i").classList.add("fa-eye");
    }
    else {
        password.type = "password";
        passHideBtn.querySelector("i").classList.remove("fa-eye");
        passHideBtn.querySelector("i").classList.add("fa-eye-slash");
    }
});



