
var normalBox = document.querySelector(".normal-box");
var loginKey = document.getElementById("username");

loginKey.addEventListener("focus", function () {
    normalBox.classList.add("box-focus");
});

loginKey.addEventListener("blur", function () {
    normalBox.classList.remove("box-focus");
});


var normalBoxs = document.querySelectorAll(".normal-box");
document.addEventListener("DOMContentLoaded", function () {
    normalBoxs.forEach(box => {
        let username = box.querySelector("#username");
        if (username) {
            username.addEventListener("input", function () {
                authenUsernameInput = document.getElementById("authen-username-input");
                passwordCheck = document.getElementById("password");
                rePasswordCheck = document.getElementById("retype-pass");
                loginBtn = document.getElementById("login-btn");
                if (this.value.trim() === "") {
                    authenUsernameInput.classList.remove("hide");
                    this.classList.add("error-bgr");
                    box.classList.add("error-input");
                    loginBtn.setAttribute("disabled", "true");
                    loginBtn.classList.add("btn-disbled");
                } else {
                    authenUsernameInput.classList.add("hide");
                    this.classList.remove("error-bgr");
                    box.classList.remove("error-input");
                    if (passwordCheck.value.trim() !== "" && rePasswordCheck.value.trim() !== "") {
                        loginBtn.removeAttribute("disabled")
                        loginBtn.classList.remove("btn-disbled");
                    }
                }
            });
        }
    });
});

var passBoxs = document.querySelectorAll(".pass-box");
document.addEventListener("DOMContentLoaded", function () {
    passBoxs.forEach(box => {
        let password = box.querySelector("#password");
        if (password) {
            password.addEventListener("input", function () {
                authenPasswordInput = document.getElementById("authen-password-input");
                usernameCheck = document.getElementById("username");
                rePasswordCheck = document.getElementById("retype-pass");
                loginBtn = document.getElementById("login-btn");
                if (this.value.trim() === "") {
                    authenPasswordInput.classList.remove("hide");
                    this.classList.add("error-bgr");
                    box.classList.add("error-input");
                    loginBtn.setAttribute("disabled", "true");
                    loginBtn.classList.add("btn-disbled");
                } else {
                    authenPasswordInput.classList.add("hide");
                    this.classList.remove("error-bgr");
                    box.classList.remove("error-input");
                    if (usernameCheck.value.trim() !== "" && rePasswordCheck.value.trim() !== "") {
                        loginBtn.removeAttribute("disabled")
                        loginBtn.classList.remove("btn-disbled");
                    }
                }
            });
        }

        let retypePassword = box.querySelector("#retype-pass");
        if (retypePassword) {
            retypePassword.addEventListener("input", function () {
                authenRepasswordInput = document.getElementById("authen-retypepass-input");
                passwordCheck = document.getElementById("password");
                usernameCheck = document.getElementById("username");
                loginBtn = document.getElementById("login-btn");
                if (this.value.trim() === "") {
                    authenRepasswordInput.classList.remove("hide");
                    this.classList.add("error-bgr");
                    box.classList.add("error-input");
                    loginBtn.setAttribute("disabled", "true");
                    loginBtn.classList.add("btn-disbled");
                } else {
                    authenRepasswordInput.classList.add("hide");
                    this.classList.remove("error-bgr");
                    box.classList.remove("error-input");
                    if (passwordCheck.value.trim() !== "" && usernameCheck.value.trim() !== "") {
                        loginBtn.removeAttribute("disabled")
                        loginBtn.classList.remove("btn-disbled");
                    }
                    if(passwordCheck.value.trim() !== retypePassword.value.trim()) {
                        authenRepasswordInput.classList.remove("hide");
                        authenRepasswordInput.innerHTML = "Nhập sai mật khẩu";
                        this.classList.add("error-bgr");
                        box.classList.add("error-input");
                        loginBtn.removeAttribute("disabled")
                        loginBtn.classList.add("btn-disbled");
                    }
                }
            });
        }
    });
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

var retypePass = document.getElementById("retype-pass");
var passHideBtn1 = document.getElementById("pass-hide-btn-1");
passHideBtn1.addEventListener("click", function() {
    if(retypePass.type === "password") {
        retypePass.type = "text";
        passHideBtn1.querySelector("i").classList.remove("fa-eye-slash");
        passHideBtn1.querySelector("i").classList.add("fa-eye");
    }
    else {
        retypePass.type = "password";
        passHideBtn1.querySelector("i").classList.remove("fa-eye");
        passHideBtn1.querySelector("i").classList.add("fa-eye-slash");
    }
});

