document.addEventListener('DOMContentLoaded', function () {
    const menus = document.querySelectorAll('.wrapper-seller-manage .menu .mn');
    let currentMode = null;
    try {
        currentMode = /*[[${mode}]]*/ '';
    } catch (e) {
        currentMode = '';
    }

    menus.forEach(mn => {
        const title = mn.querySelector('.title');
        const dropdown = mn.querySelector('.mn-dropdown');
        const chevron = title.querySelector('[class*="bx-chevron"]');
        const mode = mn.getAttribute('data-mode');
        const links = dropdown ? dropdown.querySelectorAll('a') : [];
        let hasActiveLink = false;
        links.forEach(link => {
            if (link.href && window.location.pathname + window.location.search === link.pathname + link.search) {
                link.classList.add('active');
                hasActiveLink = true;
            }
        });
        if (hasActiveLink || (mode && currentMode && currentMode === mode)) {
            dropdown.classList.add('show');
            title.classList.add('active');
            if (chevron) {
                chevron.classList.add('bx-chevron-up');
                chevron.classList.remove('bx-chevron-down');
            }
            title.setAttribute('aria-expanded', 'true');
        } else {
            title.setAttribute('aria-expanded', 'false');
        }

        title.setAttribute('tabindex', '0');

        title.addEventListener('click', function (e) {
            e.stopPropagation();
            if (hasActiveLink || (mode && currentMode && currentMode === mode)) return;

            const isOpen = dropdown.classList.toggle('show');
            title.classList.toggle('active', isOpen);
            if (chevron) {
                chevron.classList.toggle('bx-chevron-down', !isOpen);
                chevron.classList.toggle('bx-chevron-up', isOpen);
            }
            title.setAttribute('aria-expanded', isOpen);
        });

        title.addEventListener('keydown', (e) => {
            if (e.key === 'Enter' || e.key === ' ') {
                e.preventDefault();
                title.click();
            }
        });
    });
    document.addEventListener('click', (e) => {
        if (!e.target.closest('.mn')) {
            menus.forEach(mn => {
                const dropdown = mn.querySelector('.mn-dropdown');
                const title = mn.querySelector('.title');
                const chevron = title.querySelector('[class*="bx-chevron"]');
                const mode = mn.getAttribute('data-mode');
                const links = dropdown ? dropdown.querySelectorAll('a') : [];
                let hasActiveLink = false;
                links.forEach(link => {
                    if (link.classList.contains('active')) hasActiveLink = true;
                });
                if (hasActiveLink || (mode && currentMode && currentMode === mode)) return;
                dropdown.classList.remove('show');
                title.classList.remove('active');
                if (chevron) {
                    chevron.classList.add('bx-chevron-down');
                    chevron.classList.remove('bx-chevron-up');
                }
                title.setAttribute('aria-expanded', 'false');
            });
        }
    });
});