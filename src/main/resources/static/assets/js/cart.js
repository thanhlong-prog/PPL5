const checkAll = document.getElementById('check-all-products');

const itemCheckboxes = document.querySelectorAll('.check-item');

checkAll.addEventListener('change', function () {
    itemCheckboxes.forEach(function (checkbox) {
        checkbox.checked = checkAll.checked;
        updateTotalChecked();
        updateTotalPriceChecked();
    });
});

itemCheckboxes.forEach(function (checkbox) {
    checkbox.addEventListener('change', function () {
        const allChecked = [...itemCheckboxes].every(cb => cb.checked);
        checkAll.checked = allChecked;
        updateTotalChecked();
        updateTotalPriceChecked();
    });
});

let cartItems;
let totalItemCount;
cartItems = document.querySelectorAll(".cart-item");
totalItemCount = cartItems.length;
console.log("Số sản phẩm trong giỏ hàng:", totalItemCount);
document.getElementById("num-products").textContent = "("+totalItemCount+")";

function updateTotalChecked() {
    const count = document.querySelectorAll(".check-item:checked").length;
    document.getElementById("total-checked").textContent = `(${count} sản phẩm):`;
}


document.querySelector(".delete").addEventListener("click", function () {
    const checkedBoxes = document.querySelectorAll(".check-item:checked");
    const cartIds = [];

    checkedBoxes.forEach(box => {
        const cartItem = box.closest(".cart-item");
        const cartId = cartItem.getAttribute("data-id");
        cartIds.push(cartId);
    });
    cartIds.forEach(cartId => {
        delCartItem(cartId);
    });
});

function updateTotalPriceChecked() {
    const checkedItems = document.querySelectorAll(".check-item:checked");
    let total = 0;

    checkedItems.forEach(cb => {
        const cartItem = cb.closest(".cart-item");
        const priceSpan = cartItem.querySelector(".total-price span");
        if (priceSpan) {
            const priceText = priceSpan.textContent.replace(/[^\d]/g, '');
            const price = parseInt(priceText);
            if (!isNaN(price)) total += price;
        }
    });

    const totalPriceDisplay = document.getElementById("total-price-checked");
    if (totalPriceDisplay) {
        totalPriceDisplay.textContent = `₫${total.toLocaleString('vi-VN')}`;
    }
}

document.querySelectorAll('.minus').forEach(button => {
    button.addEventListener('click', function() {
        const input = this.nextElementSibling; // Lấy thẻ input kế tiếp nút trừ
        let quantity = parseInt(input.value); // Lấy giá trị trong input
        const cartId = this.closest('.cart-item').getAttribute('data-id'); // Lấy cartId từ attribute data-id

        if (quantity > 1) { // Nếu số lượng lớn hơn 1 thì giảm
            input.value = quantity - 1;
            updateCartItem(cartId, input.value); // Gửi AJAX để cập nhật
        } else {
            // Nếu số lượng = 1 thì không giảm nữa
            alert("Số lượng không thể giảm nữa.");
        }
    });
});

document.querySelectorAll('.plus').forEach(button => {
    button.addEventListener('click', function() {
        const input = this.previousElementSibling; // Lấy thẻ input trước nút cộng
        let quantity = parseInt(input.value); // Lấy giá trị trong input
        const cartId = this.closest('.cart-item').getAttribute('data-id'); // Lấy cartId từ attribute data-id

        input.value = quantity + 1;
        updateCartItem(cartId, input.value); // Gửi AJAX để cập nhật
    });
});
