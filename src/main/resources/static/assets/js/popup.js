document.querySelectorAll('.pop-up').forEach(popup => {
    const closeIcon = popup.querySelector('.close-icon');
    closeIcon.addEventListener('click', () => {
        popup.classList.add('hide');
    });
    const checkBox = popup.querySelector('.check-box');
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
});
