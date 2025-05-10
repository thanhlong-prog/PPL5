function parseCurrency(text) {
    return parseInt(text.replace(/[^\d]/g, '')) || 0;
}

function updateTotalPerShop() {
    const shopTotals = document.querySelectorAll('.order-total');

    shopTotals.forEach(shopTotal => {
        const shopId = shopTotal.getAttribute('data-shop-id');
        const productsInShop = document.querySelectorAll(`.product-order[data-shop-id="${shopId}"] .total-price`);
        
        let shopTotalAmount = 0;
        let shopProductCount = 0;

        productsInShop.forEach(product => {
            const productTotalPrice = parseCurrency(product.textContent);
            shopTotalAmount += productTotalPrice;
            shopProductCount += 1;  
        });
        const priceElement = shopTotal.querySelector('.price');
        if (priceElement) {
            priceElement.textContent = `₫${shopTotalAmount.toLocaleString('vi-VN')}`;
        }
        const quantityElement = shopTotal.querySelector('.quantity-order');
        if (quantityElement) {
            quantityElement.textContent = `Tổng số tiền (${shopProductCount} sản phẩm):`;
        }
    });
}

function updateTotalAmount() {
    const shopTotals = document.querySelectorAll('.order-total .price');
    
    let totalAmount = 0;
    shopTotals.forEach(shopTotal => {
        const shopTotalAmount = parseCurrency(shopTotal.textContent);
        totalAmount += shopTotalAmount;
    });
    const totalAmountElement = document.querySelector('.p-m-list .price-inf');
    if (totalAmountElement) {
        totalAmountElement.textContent = `₫${totalAmount.toLocaleString('vi-VN')}`;
    }
}


function updateShippingCost() {
    const shippingCosts = document.querySelectorAll('.giaohang.row-1.row-end');
    
    let totalShippingCost = 0;
    shippingCosts.forEach(shippingCost => {
        const shippingCostAmount = parseCurrency(shippingCost.textContent);
        totalShippingCost += shippingCostAmount;
    });
    const shippingTotalElement = document.querySelector('.p-m-list.shipping-subtotal .price-inf');
    if (shippingTotalElement) {
        shippingTotalElement.textContent = `₫${totalShippingCost.toLocaleString('vi-VN')}`;
    }
}

function updateTotalPayment() {
    const merchandiseText = document.querySelector('.merchandise-subtotal .price-inf')?.textContent || '₫0';
    const shippingText = document.querySelector('.shipping-subtotal .price-inf')?.textContent || '₫0';

    const merchandise = parseCurrency(merchandiseText);
    const shipping = parseCurrency(shippingText);
    const total = merchandise + shipping;

    const totalDisplay = document.querySelector('.price-inf-tp');
    if (totalDisplay) {
        totalDisplay.textContent = `₫${total.toLocaleString('vi-VN')}`;
    }
}

window.onload = function () {
    updateTotalPerShop();
    updateTotalAmount();
    updateShippingCost();
    updateTotalPayment();
};
