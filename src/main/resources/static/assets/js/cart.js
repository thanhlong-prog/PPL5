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
        const input = this.nextElementSibling; 
        let quantity = parseInt(input.value); 
        const cartId = this.closest('.cart-item').getAttribute('data-id'); 

        if (quantity > 1) { 
            input.value = quantity - 1;
            updateCartItem(cartId, input.value);
        } else {
            alert("Số lượng không thể giảm nữa.");
        }
    });
});

document.querySelectorAll('.plus').forEach(button => {
    button.addEventListener('click', function() {
        const input = this.previousElementSibling; 
        let quantity = parseInt(input.value); 
        const cartId = this.closest('.cart-item').getAttribute('data-id'); 

        input.value = quantity + 1;
        updateCartItem(cartId, input.value); 
    });
});

const checkedId = document.querySelector('div[data-id-checked]').getAttribute('data-id-checked');
    
    if (checkedId != 0) {
        const cartItems = document.querySelectorAll('.cart-item');

        cartItems.forEach(item => {
            const itemId = item.getAttribute('data-id');

            if (itemId === checkedId) {
                const checkBox = item.querySelector('.check-item');
                if (checkBox) {
                    checkBox.checked = true;
                    updateTotalChecked();
                    updateTotalPriceChecked();
                }
            }
        });
    }
