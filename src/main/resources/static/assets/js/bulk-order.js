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

function confirmBulkOrder() {
    const selectedIds = [];
    const checkboxes = document.querySelectorAll('tbody.order-list input[type="checkbox"]:checked');
    
    checkboxes.forEach(cb => {
        const id = cb.getAttribute("data-id");
        if (id) {
            selectedIds.push(parseInt(id));
        }
    });

    if (selectedIds.length === 0) {
        alert("Bạn chưa chọn đơn hàng nào.");
        return;
    }

    fetch("/seller/confirm-delivery", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ transactionIds: selectedIds })
    })
    .then(res => {
        if (!res.ok) throw new Error("Lỗi khi gửi dữ liệu");
        return res.json();
    })
    .then(data => {
        alert("Xác nhận giao hàng thành công!");
        location.reload(); 
    })
    .catch(err => {
        console.error(err);
        alert("Đã xảy ra lỗi.");
    });
}
