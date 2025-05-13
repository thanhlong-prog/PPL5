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

