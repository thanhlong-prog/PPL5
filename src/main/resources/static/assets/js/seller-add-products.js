var quill = new Quill('#editor', {
    theme: 'snow',
    modules: {
        toolbar: '#editor-toolbar'
    }
});

const form = document.querySelector('form');
form.addEventListener('submit', function () {
    saveVariantsToHiddenInput();
    const hiddenInput = document.getElementById('description-input');
    hiddenInput.value = quill.root.innerHTML;
    e.preventDefault();
    const variantJsonInput = document.getElementById('variant-json');
    variantJsonInput.value = JSON.stringify(getAllVariantData());

});

function getAllVariantData() {
    const rows = document.querySelectorAll('tbody tr');
    const data = [];

    rows.forEach(row => {
        const cells = row.querySelectorAll('td');
        const obj = {};

        attributeList.forEach((attr, i) => {
            obj[attr.type] = cells[i].innerText.trim();
        });

        obj.price = row.querySelector('.price-option').value.trim();
        obj.quantity = row.querySelector('.quantity-option').value.trim();
        obj.sku = row.querySelector('.sku-option').value.trim();

        data.push(obj);
    });

    return data;
}


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
            cell.style.display = "none"; 
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
        saveVariantsToHiddenInput();
    });
});

function saveVariantsToHiddenInput() {
    const rows = document.querySelectorAll('tbody tr');
    const data = [];

    rows.forEach(row => {
        const cells = row.querySelectorAll('td');
        const values = [];

        attributeList.forEach((attr, i) => {
            values.push(cells[i].innerText.trim());
        });

        const obj = {
            attributes: attributeList.map(a => a.type),
            values: values,
            price: row.querySelector('.price-option').value.trim(),
            quantity: row.querySelector('.quantity-option').value.trim(),
            sku: row.querySelector('.sku-option').value.trim()
        };

        data.push(obj);
    });

    const jsonString = JSON.stringify(data);
    console.log("JSON GỬI LÊN:", jsonString);

    document.getElementById('variant-json').value = JSON.stringify(data);
}

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

    ['Giá (đ)', 'Số lượng', 'SKU phân loại', 'Ảnh SKU'].forEach(label => {
        const th = document.createElement('th');
        th.innerText = label;
        thead.appendChild(th);
    });

    const combinations = generateCombinations(attributes.map(a => a.values));

    combinations.forEach((comb, rowIdx) => {
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

        // Thêm cột Ảnh SKU
        const tdImg = document.createElement('td');
        tdImg.style.verticalAlign = 'middle';
        tdImg.innerHTML = `
            <input type="file" accept="image/*" class="sku-img-input">
            <img class="sku-img-preview"/>
        `;
        // Xử lý hiển thị ảnh khi chọn
        const fileInput = tdImg.querySelector('.sku-img-input');
        const imgPreview = tdImg.querySelector('.sku-img-preview');
        fileInput.addEventListener('change', function () {
            const file = this.files[0];
            if (file) {
                const reader = new FileReader();
                reader.onload = function (e) {
                    imgPreview.src = e.target.result;
                    imgPreview.style.display = 'inline-block';
                };
                reader.readAsDataURL(file);
            } else {
                imgPreview.src = '';
                imgPreview.style.display = 'none';
            }
        });
        tr.appendChild(tdImg);

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

// ...existing code...
fileInput.addEventListener("change", () => {
    const files = fileInput.files;
    let current = parseInt(currentCount.innerText);
    const remain = maxCount - current;

    const displayFiles = Array.from(files).slice(0, remain);

    displayFiles.forEach(file => {
        const reader = new FileReader();
        reader.onload = e => {
            const imgWrapper = document.createElement("div");
            imgWrapper.className = "img-product";
            imgWrapper.style.position = "relative";
            imgWrapper.innerHTML = `
                <img src="${e.target.result}" alt="">
                <button type="button" class="btn-remove-img" style="
                    position:absolute;top:2px;right:2px;
                    background:#fff;border:none;border-radius:50%;
                    width:24px;height:24px;cursor:pointer;
                    box-shadow:0 1px 4px rgba(0,0,0,0.15);">
                    <i class='bx bx-x' style="color:#ee4d2d;font-size:18px;"></i>
                </button>
            `;
            imgList.insertBefore(imgWrapper, btnAdd.parentElement);

            current++;
            currentCount.innerText = current;
            if (current >= maxCount) {
                btnAdd.style.display = "none";
            }

            // Xử lý nút xoá
            imgWrapper.querySelector('.btn-remove-img').addEventListener('click', function () {
                imgWrapper.remove();
                current--;
                currentCount.innerText = current;
                if (current < maxCount) {
                    btnAdd.style.display = "";
                }
            });
        };
        reader.readAsDataURL(file);
    });

    // fileInput.value = "";
});
// ...existing code...  



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
