(function () {
    let currentPage = 1;
    const prevBtn = document.querySelector(".form .space-footer .prev");
    const nextBtn = document.querySelector(".form .space-footer .next");
    const steps = document.querySelectorAll(".form .steps .step");
    const paginationNumbers = document.querySelectorAll(".form .pagination .number");
    const totalPages = steps.length;

    function movePage() {
        prevBtn.disabled = false;
        nextBtn.disabled = false;

        if (currentPage === 1) {
            prevBtn.disabled = true;
            prevBtn.classList.add("btn-disbled");
        }

        if (currentPage === totalPages) {
            nextBtn.disabled = true;
            nextBtn.classList.add("btn-disbled");
        }

        if(currentPage > 1 && currentPage < totalPages) {
            prevBtn.classList.remove("btn-disbled");
            nextBtn.classList.remove("btn-disbled");
        }
        const activeNumber = document.querySelector(".form .pagination .active");
        if (activeNumber) {
            activeNumber.classList.remove("active");
        }
        paginationNumbers[currentPage - 1].classList.add("active");

        const stepWidth = steps[0].offsetWidth;
        const newMargin = ((currentPage - 1) * stepWidth * -1) + "px";
        steps[0].parentNode.style.marginLeft = newMargin;
    }

    prevBtn.addEventListener("click", function () {
        if (currentPage > 1) {
            currentPage--;
            movePage();
        }
    });

    nextBtn.addEventListener("click", function () {
        if (currentPage < totalPages) {
            currentPage++;
            movePage();
        }
    });

    movePage();
})();

const codeInputs = document.querySelectorAll(".code-input");
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

const inputActs = document.querySelectorAll(".input-act");
document.addEventListener("DOMContentLoaded", function () {
    inputActs.forEach(inputAct => {
        if (inputAct) {
            inputAct.addEventListener("input", function () {
                if(inputAct.classList.contains("error-input")) {
                    inputAct.classList.remove("error-input");
                    let errorWarnName = inputAct.id + "-error-warn";
                    let errorWarn = document.getElementById(errorWarnName);
                    if (errorWarn) {
                        errorWarn.classList.add("hide");
                    }
                }
            });
        }
    });
});