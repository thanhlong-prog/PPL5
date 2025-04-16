
var normalBox = document.querySelector(".normal-box");
var loginKey = document.getElementById("username");

loginKey.addEventListener("focus", function () {
    normalBox.classList.add("box-focus");
});

loginKey.addEventListener("blur", function () {
    normalBox.classList.remove("box-focus");
});


document.addEventListener("DOMContentLoaded", function () {
    const fields = {
        username: "#username",
        name: "#name",
        password: "#password",
        retypePass: "#retype-pass",
        gmail: "#gmail",
        phone: "#phone",
        codeMail: "#code-mail",
        codePhone: "#code-phone"
    };
    const loginBtn = document.getElementById("login-btn");

    function updateLoginBtn() {
        const allFilled = Object.values(fields).every(selector => {
            const el = document.querySelector(selector);
            return el && el.value.trim() !== "";
        });

        const passwordEl = document.querySelector(fields.password);
        const retypePassEl = document.querySelector(fields.retypePass);

        const isPasswordMatch = passwordEl.value === retypePassEl.value;

        if (allFilled && isPasswordMatch) {
            loginBtn.removeAttribute("disabled");
            loginBtn.classList.remove("btn-disbled");
        } else {
            loginBtn.setAttribute("disabled", "true");
            loginBtn.classList.add("btn-disbled");
        }
    }

    function validateField(inputEl, errorElId, boxEl) {
        const errorEl = document.getElementById(errorElId);
        inputEl.addEventListener("input", function () {
            const isEmpty = this.value.trim() === "";

            if (isEmpty) {
                errorEl.classList.remove("hide");
                this.classList.add("error-bgr");
                boxEl.classList.add("error-input");
            } else {
                errorEl.classList.add("hide");
                this.classList.remove("error-bgr");
                boxEl.classList.remove("error-input");
            }

            if (this.id === "retype-pass") {
                const passwordEl = document.querySelector(fields.password);
                if (this.value !== passwordEl.value) {
                    errorEl.classList.remove("hide");
                    errorEl.innerHTML = "Nhập sai mật khẩu";
                    this.classList.add("error-bgr");
                    boxEl.classList.add("error-input");
                }
            }

            updateLoginBtn();
        });
    }
    validateField(
        document.getElementById("username"),
        "authen-username-input",
        document.querySelector(".normal-box")
    );

    validateField(
        document.getElementById("password"),
        "authen-password-input",
        document.querySelector(".pass-box")
    );

    validateField(
        document.getElementById("retype-pass"),
        "authen-retypepass-input",
        document.querySelector(".pass-box.retype")
    );
    validateField(
        document.getElementById("name"),
        "authen-name-input",
        document.querySelector(".normal-box")
    );

    validateField(
        document.getElementById("gmail"),
        "authen-gmail-input",
        document.querySelector(".normal-box")
    );

    validateField(
        document.getElementById("phone"),
        "authen-phone-input",
        document.querySelector(".normal-box")
    );

    validateField(
        document.getElementById("code-mail"),
        "authen-codemail-input",
        document.querySelector(".code-box")
    );

    validateField(
        document.getElementById("code-phone"),
        "authen-codephone-input",
        document.querySelector(".code-box")
    );

    updateLoginBtn();
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

const codeInputs = document.querySelectorAll(".code-box-input");
document.addEventListener("DOMContentLoaded", function () {
    codeInputs.forEach(codeInput => {
        if (codeInput) {
            codeInput.addEventListener("keydown", function (e) {
                if (!/^\d$/.test(e.key) && e.key !== 'Backspace' && e.key !== 'ArrowLeft' && e.key !== 'ArrowRight') {
                    e.preventDefault();  
                }
            });
        }
    });
});