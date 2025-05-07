document.querySelectorAll('.input-count').forEach(function (input) {
    const container = input.parentElement.querySelector('.charCount');
    const currentCount = container.querySelector('.currentCount');
    const maxCount = container.querySelector('.maxCount');

    function updateCharCount() {
        currentCount.textContent = input.value.length;
    }

    input.addEventListener('input', updateCharCount);
    updateCharCount();
});

document.addEventListener("DOMContentLoaded", () => {
    const pages = document.querySelectorAll(".form-register .page");
    const stepBarItems = document.querySelectorAll(".step-bar .step"); 
    let currentStep = 0;

    const btnNext = document.querySelector(".btn-seller-register .btn-next");
    const btnPrev = document.querySelector(".btn-seller-register .btn-prev");
    const btnComplete = document.querySelector(".btn-seller-register .btn-complete");
    showStep(currentStep);

    btnNext.addEventListener("click", () => {
        if (currentStep < pages.length - 1) {
            currentStep++;
            showStep(currentStep);
        }
    });
    btnComplete.addEventListener("click", () => {
        if (currentStep < pages.length - 1) {
            currentStep++;
            showStep(currentStep);
        }
    });

    btnPrev.addEventListener("click", () => {
        if (currentStep > 0) {
            currentStep--;
            showStep(currentStep);
        }
    });

    function showStep(index) {
        pages.forEach((page, i) => {
            page.classList.toggle("active", i === index);
        });
        stepBarItems.forEach((item, i) => {
            if (i < index) {
                item.classList.add("complete");
                item.classList.remove("active");
            } else if (i === index) {
                item.classList.add("active");
                item.classList.remove("complete");
            } else {
                item.classList.remove("active", "complete");
            }
        });
        const btnPrev = document.querySelector(".btn-prev");
        const btnNext = document.querySelector(".btn-next");
        const btnComplete = document.querySelector(".btn-complete");
        const btnContainer = document.querySelector(".btn-seller-register");
        btnPrev.style.display = index === 0 ? "none" : "inline-block";
        const isIdentityInf = pages[index].classList.contains("identity-information");
        btnNext.style.display = isIdentityInf ? "none" : "inline-block";
        btnComplete.style.display = isIdentityInf ? "inline-block" : "none";
        btnContainer.style.display = index === pages.length - 1 ? "none" : "flex";
    }
});
document.addEventListener("DOMContentLoaded", function () {
    const individualRadio = document.getElementById("individual");
    const householdRadio = document.getElementById("household-business");
    const companyRadio = document.getElementById("company");
    const companyNameSection = document.getElementById("company-name-section");
    function toggleCompanyNameInput() {
        if (householdRadio.checked || companyRadio.checked) {
            companyNameSection.style.display = "flex"; 
        } else {
            companyNameSection.style.display = "none";
        }
    }
    toggleCompanyNameInput();
    individualRadio.addEventListener("change", toggleCompanyNameInput);
    householdRadio.addEventListener("change", toggleCompanyNameInput);
    companyRadio.addEventListener("change", toggleCompanyNameInput);
});
