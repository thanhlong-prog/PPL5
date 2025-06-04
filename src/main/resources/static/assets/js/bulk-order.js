function openDialog() {
    const dialog = document.getElementById("dialogOverlay");
    dialog.classList.remove("hidden");
    dialog.classList.add("visible");
}

function closeDialog() {
    const dialog = document.getElementById("dialogOverlay");
    dialog.classList.remove("visible");
    dialog.classList.add("hidden");
}

document.addEventListener("DOMContentLoaded", function () {
    const checkboxes = document.querySelectorAll('tbody.order-list input[type="checkbox"]');
    const bulkButton = document.querySelector('.btn-bulk-order');
    const quantityOrderDisplays = document.querySelectorAll('.quantity-order'); // Tất cả vị trí có class .quantity-order
    const checkAll = document.getElementById('check-all');

    function updateButtonState() {
        const checkedCount = Array.from(checkboxes).filter(checkbox => checkbox.checked).length;

        // Cập nhật tất cả vị trí hiển thị số đơn hàng
        quantityOrderDisplays.forEach(el => el.textContent = checkedCount);

        // Enable/disable nút tùy theo số lượng đơn được chọn
        bulkButton.disabled = checkedCount === 0;
    }

    // Gắn sự kiện cho từng checkbox đơn
    checkboxes.forEach(cb => {
        cb.addEventListener('change', updateButtonState);
    });

    // Gắn cho checkbox "Chọn tất cả"
    if (checkAll) {
        checkAll.addEventListener('change', function () {
            checkboxes.forEach(cb => cb.checked = checkAll.checked);
            updateButtonState();
        });
    }

    // Cập nhật ban đầu
    updateButtonState();
});
