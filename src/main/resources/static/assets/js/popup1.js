document.querySelectorAll('.pop-up').forEach(popup => {
    const checkBox = popup.querySelector('.check-box');
    const btnBack = popup.querySelector('.btn-back');
    document.addEventListener('click', (event) => {
        if (!popup.classList.contains('hide')) {
            if (!checkBox.contains(event.target)) {
                popup.classList.add('hide');
            }
        }
    });
    checkBox.addEventListener('click', (event) => {
        event.stopPropagation();
    });

    if (btnBack) {
        btnBack.addEventListener('click', () => {
            popup.classList.add('hide');
        });
    }
});
