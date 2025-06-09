// Hàm khởi tạo và cập nhật vị trí của underline
function initializeUnderline(tabsSelector, underlineSelector) {
    const tabs = document.querySelector(tabsSelector);
    const underline = document.querySelector(underlineSelector);
    const activeTab = tabs.querySelector('.active');

    // Cập nhật vị trí underline cho tab đang chọn
    const updateUnderline = (tab) => {
        if (tab && underline) {
            underline.style.width = `${tab.offsetWidth}px`;
            underline.style.left = `${tab.offsetLeft}px`;
        }
    };

    // Khởi tạo vị trí ban đầu
    if (activeTab) {
        updateUnderline(activeTab);
    }

    // Thêm sự kiện click cho các tab
    tabs.querySelectorAll('span').forEach(tab => {
        tab.addEventListener('click', () => {
            tabs.querySelector('.active').classList.remove('active');
            tab.classList.add('active');
            updateUnderline(tab);
        });
    });

    // Cập nhật lại vị trí khi resize cửa sổ
    window.addEventListener('resize', () => {
        const currentActiveTab = tabs.querySelector('.active');
        updateUnderline(currentActiveTab);
    });
}

// Khởi tạo underline cho navbar-primary
initializeUnderline('.navbar-primary .tabs', '.navbar-primary .tabs .underline');
// Chọn tất cả các phần tử kích hoạt dropdown
const dropdownTriggers = document.querySelectorAll('.tl-dropdown');

// Thêm sự kiện click cho từng phần tử kích hoạt
dropdownTriggers.forEach(trigger => {
    trigger.addEventListener('click', (e) => {
        e.preventDefault();
        // Tìm abs-dropdown tương ứng trong cùng rel-dropdown
        const parentDropdown = trigger.closest('.rel-dropdown');
        const absDropdown = parentDropdown.querySelector('.abs-dropdown');
        
        // Đóng tất cả các dropdown khác
        document.querySelectorAll('.abs-dropdown').forEach(dropdown => {
            if (dropdown !== absDropdown) {
                dropdown.classList.remove('show');
            }
        });
        
        // Bật/tắt dropdown hiện tại
        absDropdown.classList.toggle('show');
    });
});

// Đóng dropdown khi nhấp ra ngoài
document.addEventListener('click', (e) => {
    if (!e.target.closest('.rel-dropdown')) {
        document.querySelectorAll('.abs-dropdown').forEach(dropdown => {
            dropdown.classList.remove('show');
        });
    }
});

// Thêm sự kiện click cho từng mục trong dropdown
document.querySelectorAll('.abs-dropdown li').forEach(item => {
    item.addEventListener('click', () => {
        // Đóng dropdown chứa mục được nhấp
        const absDropdown = item.closest('.abs-dropdown');
        absDropdown.classList.remove('show');
        console.log(`Chọn: ${item.textContent}`);
    });
});


function toggleSubRows(row) {
    let next = row.nextElementSibling;
    while (next && next.classList.contains('child-row')) {
        next.style.display = (next.style.display === 'none') ? 'table-row' : 'none';
        next = next.nextElementSibling;
    }
}
document.addEventListener('DOMContentLoaded', function () {
    const tabs = document.querySelectorAll('.tabs span');
    const sections = {
      'Tất cả': document.querySelector('.product-overall'),
      'Đang hoạt động': document.querySelector('.product-active'),
      'Vi phạm': document.querySelector('.product-violation'),
      'Đợi duyệt': document.querySelector('.product-approve')
    };

    // Hàm ẩn tất cả các khối
    function hideAllSections() {
      Object.values(sections).forEach(section => section.style.display = 'none');
    }

    // Gán sự kiện click cho từng tab
    tabs.forEach(tab => {
      tab.addEventListener('click', function () {
        // Bỏ active cũ
        tabs.forEach(t => t.classList.remove('active'));
        this.classList.add('active');

        // Hiển thị đúng phần tương ứng
        const selected = this.childNodes[0].nodeValue.trim(); // Lấy text tab, bỏ span.count
        hideAllSections();

        if (sections[selected]) {
          sections[selected].style.display = 'block';
        }
      });
    });

    // Mặc định hiển thị "Tất cả"
    hideAllSections();
    sections["Tất cả"].style.display = 'block';
  });