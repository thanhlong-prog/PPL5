var quill = new Quill('#editor', {
    theme: 'snow',
    modules: {
        toolbar: '#editor-toolbar'
    }
});

function mergeTableCellsByText(columnIndex = 0) {
    const table = document.querySelector("table");
    const rows = Array.from(table.querySelectorAll("tbody tr"));

    let prevText = "";
    let rowspan = 1;
    let prevCell = null;

    rows.forEach((row, i) => {
        const cell = row.children[columnIndex];

        if (!cell) return;

        const text = cell.innerText.trim();

        if (text === prevText) {

            rowspan++;
            prevCell.rowSpan = rowspan;
            cell.remove();
        } else {

            prevText = text;
            rowspan = 1;
            prevCell = cell;
        }
    });
}


document.addEventListener("DOMContentLoaded", function () {
    mergeTableCellsByText(0);
});



let attributeList = [];


document.addEventListener('DOMContentLoaded', function () {
    const typeContainer = document.querySelector('.type-attribute');
    const btnAddAttribute = document.querySelector('.btn-add-attribute');
    const btnAddComplete = document.querySelector('.btn-add-complete');

    typeContainer.addEventListener('click', function (e) {
        if (e.target.classList.contains('btn-add-value')) {
            const container = e.target.closest('.add-attribute');
            const input = document.createElement('input');
            input.type = 'text';
            input.className = 'value-input';
            input.placeholder = 'Giá trị';
            container.insertBefore(input, e.target);
        }
    });

    btnAddAttribute.addEventListener('click', function () {
        const currentAttributes = document.querySelectorAll('.add-attribute').length;

        if (currentAttributes >= 2) {

            btnAddAttribute.style.display = 'none';
            return;
        }

        const newAttr = document.createElement('div');
        newAttr.className = 'add-attribute';
        newAttr.innerHTML = `
        <input type="text" class="type-input" placeholder="Tên thuộc tính">
        <input type="text" class="value-input" placeholder="Giá trị">
        <button class="btn-add-value" type="button">Thêm giá trị</button>
    `;

        typeContainer.insertBefore(newAttr, btnAddAttribute);

        if (currentAttributes + 1 >= 2) {
            btnAddAttribute.style.display = 'none';
        }
    });


    btnAddComplete.addEventListener('click', function () {
        attributeList = [];

        const attrBlocks = document.querySelectorAll('.add-attribute');
        attrBlocks.forEach(attr => {
            const type = attr.querySelector('.type-input').value.trim();
            const values = [...attr.querySelectorAll('.value-input')]
                .map(input => input.value.trim())
                .filter(Boolean);

            if (type && values.length > 0) {
                attributeList.push({ type, values });
            }
        });

        renderTable(attributeList);
    });
});

function renderTable(attributes) {
    const thead = document.querySelector('thead tr');
    const tbody = document.querySelector('tbody');
    thead.innerHTML = '';
    tbody.innerHTML = '';

    if (attributes.length === 0) return;

    attributes.forEach(attr => {
        const th = document.createElement('th');
        th.innerText = attr.type;
        thead.appendChild(th);
    });

    ['Giá (đ)', 'Số lượng', 'SKU phân loại'].forEach(label => {
        const th = document.createElement('th');
        th.innerText = label;
        thead.appendChild(th);
    });

    const combinations = generateCombinations(attributes.map(a => a.values));

    combinations.forEach(comb => {
        const tr = document.createElement('tr');
        comb.forEach(value => {
            const td = document.createElement('td');
            td.innerText = value;
            tr.appendChild(td);
        });

        ['price-option', 'quantity-option', 'sku-option'].forEach(cls => {
            const td = document.createElement('td');
            const input = document.createElement('input');
            input.type = 'text';
            input.className = cls;
            input.placeholder = cls === 'sku-option' ? 'Nhập vào' : '0';
            td.appendChild(input);
            tr.appendChild(td);
        });

        tbody.appendChild(tr);
    });
    mergeTableCellsByText(0);
}

function generateCombinations(arrays, prefix = []) {
    if (!arrays.length) return [prefix];
    const [first, ...rest] = arrays;
    return first.flatMap(value => generateCombinations(rest, [...prefix, value]));
}

const fileInput = document.getElementById("img-input");
const btnAdd = document.querySelector(".btn-add");
const imgList = document.querySelector(".img-prd-list");
const currentCount = document.querySelector(".current-count");
const maxCount = 9;

btnAdd.addEventListener("click", () => {
    if (parseInt(currentCount.innerText) >= maxCount) return;
    fileInput.click();
});

fileInput.addEventListener("change", () => {
    const files = fileInput.files;
    const current = parseInt(currentCount.innerText);
    const remain = maxCount - current;

    const displayFiles = Array.from(files).slice(0, remain);

    displayFiles.forEach(file => {
        const reader = new FileReader();
        reader.onload = e => {
            const imgWrapper = document.createElement("div");
            imgWrapper.className = "img-product";
            imgWrapper.innerHTML = `<img src="${e.target.result}" alt="">`;
            imgList.insertBefore(imgWrapper, btnAdd.parentElement);

            const newCount = parseInt(currentCount.innerText) + 1;
            currentCount.innerText = newCount;
            if (newCount >= maxCount) {
                btnAdd.style.display = "none";
            }
        };
        reader.readAsDataURL(file);
    });

    fileInput.value = "";
});



const videoInput = document.getElementById("video-input");
const btnAddVideo = document.querySelector(".btn-add-video");
const videoPreview = document.getElementById("video-preview");

btnAddVideo.addEventListener("click", () => {
    videoInput.click();
});

videoInput.addEventListener("change", () => {
    const file = videoInput.files[0];

    // Kiểm tra nếu có file
    if (file) {
        // Kiểm tra dung lượng video (giới hạn 30MB)
        if (file.size > 30 * 1024 * 1024) {
            alert("Dung lượng video vượt quá 30MB");
            videoInput.value = "";
            return;
        }

        const reader = new FileReader();
        reader.onload = e => {
            videoPreview.innerHTML = `
                <video controls width="100%">
                    <source src="${e.target.result}" type="${file.type}">
                    Trình duyệt không hỗ trợ video.
                </video>
            `;
        };
        reader.readAsDataURL(file);
    }

    videoInput.value = "";
});
