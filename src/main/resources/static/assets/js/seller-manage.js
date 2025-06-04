const titles = document.querySelectorAll('.title');

titles.forEach(title => {
    const dropdown = title.nextElementSibling;
    const chevron = title.querySelector('[class*="bx-chevron"]');

    // Thêm thuộc tính cho khả năng truy cập
    title.setAttribute('tabindex', '0');
    title.setAttribute('aria-expanded', 'false');

    title.addEventListener('click', (e) => {
        e.stopPropagation(); // Ngăn sự kiện lan ra document

        // Bật/tắt dropdown hiện tại
        const isOpen = dropdown.classList.toggle('show');
        chevron.classList.toggle('bx-chevron-down', !isOpen);
        chevron.classList.toggle('bx-chevron-up', isOpen);
        title.setAttribute('aria-expanded', isOpen);
    });

    // Hỗ trợ bàn phím (Enter hoặc Space)
    title.addEventListener('keydown', (e) => {
        if (e.key === 'Enter' || e.key === ' ') {
            e.preventDefault();
            title.click();
        }
    });
});

// Đóng tất cả dropdown khi nhấp bên ngoài
document.addEventListener('click', (e) => {
    if (!e.target.closest('.mn')) {
        document.querySelectorAll('.dropdown.show').forEach(dropdown => {
            dropdown.classList.remove('show');
            const chevron = dropdown.previousElementSibling.querySelector('[class*="bx-chevron"]');
            if (chevron) {
                chevron.classList.add('bx-chevron-down');
                chevron.classList.remove('bx-chevron-up');
            }
            dropdown.previousElementSibling.setAttribute('aria-expanded', 'false');
        });
    }
    
});