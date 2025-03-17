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

function scrollCarousel(direction, totalWidth, widthNext, category, prev_btn, next_btn) {
  const carousel = document.querySelector(category);
  const prevBtn = document.querySelector(prev_btn);
  const nextBtn = document.querySelector(next_btn);

  let currentIndex = parseInt(carousel.dataset.currentIndex) || 0;
  const maxIndex = 1;
  const total = totalWidth;
  const widthOffset = widthNext;

  currentIndex += direction;
  if (currentIndex < 0) currentIndex = 0;
  if (currentIndex > maxIndex) currentIndex = maxIndex;

  const offset = currentIndex * -(widthOffset / total) * 100 + "%"; 
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
