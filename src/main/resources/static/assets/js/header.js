const wOptionAccount = document.querySelector(".w-option.account");
const dropDownBox = document.querySelector(".w-option.account .drop-down-box.account");
wOptionAccount.addEventListener("mouseenter", function() {
    dropDownBox.classList.remove("hide");
});
wOptionAccount.addEventListener("mouseleave", function() {
    dropDownBox.classList.add("hide");
});

const notification = document.querySelector(".notification");
const notificationBox = document.querySelector(".notification .notification-box");
notification.addEventListener("mouseenter", function() {
    notificationBox.classList.remove("hide");
});
notification.addEventListener("mouseleave", function() {
    notificationBox.classList.add("hide");
});