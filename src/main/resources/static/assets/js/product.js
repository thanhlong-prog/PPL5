function scrollCarousel(direction, total, category, prev_btn, next_btn) {
    const carousel = document.querySelector(category);
    const prevBtn = document.querySelector(prev_btn);
    const nextBtn = document.querySelector(next_btn);
    const cateDiv = document.querySelector('.totalcate');
    const numOfCateg = cateDiv ? cateDiv.getAttribute('data-num') : 0;
    // const totalProduct = Math.ceil(numOfCateg/2);// tong san pham
    const totalProduct = 7;// tong san pham
    console.log(totalProduct);
    let currentIndex = parseInt(carousel.dataset.currentIndex) || 0;
    // total la so san pham lon nhat hien thi duoc tren 1 hang
    maxPage = Math.ceil(totalProduct / total);
    if(currentIndex === maxPage -2 && totalProduct % total > 0){
      widthOffset = totalProduct % total;
    }
    if (maxPage > 1 && currentIndex != maxPage-2 || totalProduct % total === 0 ) {
      widthOffset = 10;
    }
    console.log("currentIndex", currentIndex);
    console.log("maxPage",maxPage-2);
    console.log("widthOffset", widthOffset);
    let maxIndex =0;
    if(totalProduct/(total) > 0){
      maxIndex = Math.ceil(totalProduct / total) -1;
    }
    else{
      maxIndex = 0;
    }
    currentIndex += direction;
    if (currentIndex < 0) currentIndex = 0;
    if (currentIndex > maxIndex) currentIndex = maxIndex;
    const offset =currentIndex*-(widthOffset / total) * 100 + "%";
    carousel.style.transform = `translateX(${offset})`;
    carousel.dataset.currentIndex = currentIndex;
  
    if (currentIndex === 0) {
        prevBtn.style.display = "none"; 
    } else {
        prevBtn.style.display = "flex"; 
    }
  
    if (currentIndex === maxIndex) {
        nextBtn.style.display = "none";
    } else {
        nextBtn.style.display = "flex";
    }
  }
  function changeMainImage(thumbnail) {
    const mainImage = document.getElementById('main-image');
    mainImage.src = thumbnail.src;
    mainImage.alt = thumbnail.alt;
  }
  
let productQuantity = null;
let sendProductId = null;
let totalQuantity = null;

    const selector = document.querySelector(".quantity-selector");
    totalQuantity = parseInt(selector.dataset.totalQuantity);
    const productId = document.querySelector(".product-id");
    sendProductId = parseInt(productId.dataset.productId);
    console.log("Product ID:", sendProductId);
    console.log("Total quantity:", totalQuantity);
    productQuantity = totalQuantity;
    totalQuantity = productQuantity;
    document.getElementById("product-quantity").textContent = productQuantity;

    const minusBtn = selector.querySelectorAll(".quantity-btn")[0];
    const plusBtn = selector.querySelectorAll(".quantity-btn")[1];
    const input = selector.querySelector(".quantity-input");

    minusBtn.addEventListener("click", () => {
        let value = parseInt(input.value);
        if (value > 1) {
            input.value = value - 1;
        }
    });

    plusBtn.addEventListener("click", () => {
        let value = parseInt(input.value);
        if (value < totalQuantity) {
            input.value = value + 1;
        }
    });

    input.addEventListener("input", () => {
        let value = parseInt(input.value);
        if (isNaN(value) || value < 1) {
            input.value = 1;
        } else if (value > totalQuantity) {
            input.value = totalQuantity;
        }
    });


let selectedVersion = null;
let selectedColor = null;
let selectedSize = null;





    const variationGroups = document.querySelectorAll(".variation-options");
    const addToCartBtn = document.querySelector(".add-to-cart");
    const buyNowBtn = document.querySelector(".buy-now");
    variationGroups.forEach(group => {
        const options = group.querySelectorAll(".variation-option");

        options.forEach(option => {
            option.addEventListener("click", function () {
                options.forEach(opt => opt.classList.remove("active"));
                this.classList.add("active");
                const groupType = this.closest(".variation-options").dataset.type;
                const value = this.textContent.trim();
                if (groupType === "version") selectedVersion = value;
                if (groupType === "color") selectedColor = value;
                if (groupType === "size") selectedSize = value;
                checkSelections();
            });
        });
    });

    function checkSelections() {
      let allSelected = true;
  
      variationGroups.forEach(group => {
        const type = group.dataset.type;
        const activeOption = group.querySelector(".variation-option.active");

        if (!activeOption) {
            allSelected = false;
        } else {
            const text = activeOption.innerText.trim();
            if (type === "version") selectedVersion = text;
            if (type === "color") selectedColor = text;
            if (type === "size") selectedSize = text;
        }
    });

    const quantityBtns = document.querySelectorAll(".quantity-btn");

    if (allSelected) {
        addToCartBtn.classList.remove("cursor-banner");
        buyNowBtn.classList.remove("cursor-banner");
        quantityBtns.forEach(btn => btn.classList.remove("cursor-banner"));
        console.log(sendProductId);
        updateQuantity(selectedVersion, selectedColor, selectedSize, sendProductId);
    } else {
        addToCartBtn.classList.add("cursor-banner");
        buyNowBtn.classList.add("cursor-banner");
        quantityBtns.forEach(btn => btn.classList.add("cursor-banner"));
    }
  }
  

    checkSelections(); 


