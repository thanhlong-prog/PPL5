var slideIndex = 1;
showDivs(slideIndex);

function plusDivs(n) {
  showDivs(slideIndex += n);
}

function showDivs(n) {
  var i;
  var x = document.getElementsByClassName("mySlides");
  if (n > x.length) {slideIndex = 1}
  if (n < 1) {slideIndex = x.length}
  for (i = 0; i < x.length; i++) {
    x[i].style.display = "none";  
  }
  x[slideIndex - 1].style.display = "block";  
}
setInterval(function() {
  plusDivs(1);
}, 3000);

function scrollCarousel(direction, category, prev_btn, next_btn) {
  const carousel = document.querySelector(category);
  const prevBtn = document.querySelector(prev_btn);
  const nextBtn = document.querySelector(next_btn);
  const cateDiv = document.querySelector('.totalcate');
  const numOfCateg = cateDiv ? cateDiv.getAttribute('data-num') : 0;
  const totalProduct = Math.ceil(numOfCateg/2);// tong san pham
  let currentIndex = parseInt(carousel.dataset.currentIndex) || 0;
  const total = 10;//so san pham lon nhat hien thi duoc tren 1 hang
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
