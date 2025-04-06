const icons = {
    error: 'fa-solid fa-xmark',
    success: 'fa-solid fa-circle-check',
    warn: 'fa-solid fa-triangle-exclamation',
    info: 'fa-solid fa-info',

};

function toast({ title = '', content = '', type = 'success', duration = 2000 }) {
    const main = document.getElementById('toast');
    if (main) {
        const toast = document.createElement('div');
        const icon = icons[type];
        const delay = (duration/1000).toFixed(2);
        toast.classList.add('toast', `toast--${type}`);
        toast.style.animation = `slideInLeft ease 0.8s, fadeOut linear 1s ${delay}s forwards`;

        const autoClear = setTimeout(() => {
            main.removeChild(toast);
        }, duration + 1000);

        toast.onclick = function(e) {
            if(e.target.closest('.toast__close')) {
                console.log("keke");
                main.removeChild(toast);
                clearTimeout(autoClear);
            }
        }
        toast.innerHTML = `
            <div class="toast__icon toast__icon--${type}">
                <i class="${icon}"></i>
            </div>
            <div class="toast__body">
                <h2 class="toast__title">${title}</h2>
                <p class="toast__content">${content}</p>
            </div>
            <div class="toast__close">
                <i class="fa-solid fa-xmark"></i>
            </div>
        `;
        main.appendChild(toast);
        
    }
}