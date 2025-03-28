
var normalBox = document.querySelector(".normal-box");
var loginKey = document.getElementById("username");

loginKey.addEventListener("focus", function () {
    normalBox.classList.add("box-focus");
});

loginKey.addEventListener("blur", function () {
    normalBox.classList.remove("box-focus");
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