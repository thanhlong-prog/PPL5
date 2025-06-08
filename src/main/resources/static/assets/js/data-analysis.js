let currentTab = "overview"; // overview, revenue, orders, products
let currentOffset = 0;
let currentMode = "week"; // week, month, quarter

const ctx = document.getElementById("chart").getContext("2d");
const pieCtx = document.getElementById("pieChart").getContext("2d");

// Cấu hình datasets cho từng tab
const tabConfigs = {
    overview: {
        title: "Doanh thu & Đơn hàng",
        datasets: [
            {
                label: 'Doanh thu (VNĐ)',
                yAxisID: 'y',
                borderColor: 'orange',
                backgroundColor: 'rgba(255,165,0,0.2)',
                tension: 0,
                fill: false,
                pointRadius: 4,
            },
            {
                label: 'Đơn hàng',
                yAxisID: 'y1',
                borderColor: 'black',
                backgroundColor: 'rgba(0,0,0,0.2)',
                tension: 0,
                fill: false,
                pointRadius: 4,
            }
        ],
        controls: [
            { label: "Doanh thu", color: "orange" },
            { label: "Đơn hàng", color: "black" }
        ]
    },
    revenue: {
        title: "Doanh thu",
        datasets: [
            {
                label: 'Doanh thu (VNĐ)',
                yAxisID: 'y',
                borderColor: 'orange',
                backgroundColor: 'rgba(255,165,0,0.2)',
                tension: 0,
                fill: false,
                pointRadius: 4,
            }
        ],
        controls: [
            { label: "Doanh thu", color: "orange" }
        ]
    },
    orders: {
        title: "Chi tiết đơn hàng",
        datasets: [
            {
                label: 'Tổng đơn hàng',
                yAxisID: 'y1',
                borderColor: 'black',
                backgroundColor: 'rgba(0,0,0,0.2)',
                tension: 0,
                fill: false,
                pointRadius: 4,
            },
            {
                label: 'Đơn thành công',
                yAxisID: 'y1',
                borderColor: '#28a745',
                backgroundColor: 'rgba(40,167,69,0.2)',
                tension: 0,
                fill: false,
                pointRadius: 4,
            },
            {
                label: 'Đơn huỷ',
                yAxisID: 'y1',
                borderColor: '#dc3545',
                backgroundColor: 'rgba(220,53,69,0.2)',
                tension: 0,
                fill: false,
                pointRadius: 4,
            },
            {
                label: 'Đơn không thành công',
                yAxisID: 'y1',
                borderColor: '#ffc107',
                backgroundColor: 'rgba(255,193,7,0.2)',
                tension: 0,
                fill: false,
                pointRadius: 4,
            }
        ],
        controls: [
            { label: "Tổng đơn hàng", color: "black" },
            { label: "Đơn thành công", color: "#28a745" },
            { label: "Đơn huỷ", color: "#dc3545" },
            { label: "Đơn không thành công", color: "#ffc107" }
        ]
    },
    products: {
        title: "Sản phẩm",
        datasets: [
            {
                label: 'Số lượng bán ra',
                yAxisID: 'y1',
                borderColor: '#007bff',
                backgroundColor: 'rgba(0,123,255,0.2)',
                tension: 0,
                fill: false,
                pointRadius: 4,
            },
            {
                label: 'Số lượng còn lại',
                yAxisID: 'y1',
                borderColor: '#28a745',
                backgroundColor: 'rgba(40,167,69,0.2)',
                tension: 0,
                fill: false,
                pointRadius: 4,
            },
            {
                label: 'Sản phẩm chờ duyệt',
                yAxisID: 'y1',
                borderColor: '#ffc107',
                backgroundColor: 'rgba(255,193,7,0.2)',
                tension: 0,
                fill: false,
                pointRadius: 4,
            }
        ],
        controls: [
            { label: "Số lượng bán ra", color: "#007bff" },
            { label: "Số lượng còn lại", color: "#28a745" },
            { label: "Sản phẩm chờ duyệt", color: "#ffc107" }
        ]
    }
};

document.addEventListener("DOMContentLoaded", function () {
    updateChart(); 
});


// Hàm tạo dữ liệu ảo
function randomData(min, max, count) {
    return Array.from({ length: count }, () => Math.floor(Math.random() * (max - min + 1)) + min);
}

let totalRevenueGlobal = [];
let cartCountGlobal = [];
let monthlyRevenueGlobal = [];
let monthlyCartCountGlobal = [];
let quarterlyRevenueGlobal = [];
let quarterlyCartCountGlobal = [];
let monthlySuccessCount = [];
let monthlyCancelCount = [];

// Hàm lấy nhãn và tiêu đề theo mode
function getLabelsAndTitle(mode, offset) {
    const now = new Date();
    let labels = [];
    let title = "";

    if (mode === "week") {
        const end = new Date(now);
        end.setDate(end.getDate() - offset * 7);
        const start = new Date(end);
        start.setDate(end.getDate() - 6);
        for (let i = 0; i < 7; i++) {
            const d = new Date(start);
            d.setDate(start.getDate() + i);
            labels.push(`${d.getDate()}/${d.getMonth() + 1}`);
        }
        title = `Tuần từ ${labels[0]} đến ${labels[6]}`;

        const startDate = start.toISOString().split('T')[0]; 
        const endDate = end.toISOString().split('T')[0];     

        $.ajax({
            url: '/seller/get-revenue',
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({
                startDate: startDate,
                endDate: endDate
            }),
            success: function (response) {
                console.log("Doanh thu:", response.dailyRevenue);
                console.log("Số lượng cart:", response.dailyCartCount);
                console.log("Số lượng thành công:", response.dailySuccessCount);
                console.log("Số lượng huỷ:", response.dailyCancelCount);
                totalRevenueGlobal = response.dailyRevenue;
                cartCountGlobal = response.dailyCartCount;
                monthlySuccessCount = response.dailySuccessCount;
                monthlyCancelCount = response.dailyCancelCount;
            },
            error: function (xhr) {
                alert("Lỗi khi lấy doanh thu.");
                console.error(xhr);
            }
        });
    } else if (mode === "month") {
        const ref = new Date(now.getFullYear(), now.getMonth() - offset, 1);
        const month = ref.getMonth();
        const year = ref.getFullYear();
        const daysInMonth = new Date(year, month + 1, 0).getDate();
        for (let i = 1; i <= daysInMonth; i++) {
            labels.push(`${i}/${month + 1}`);
        }
        title = `Tháng ${month + 1}/${year}`;

        $.ajax({
            url: '/seller/get-revenue-month',
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({
                year: year,
                month: month
            }),
            success: function (response) {
            totalRevenueGlobal = response.dailyRevenue;
            cartCountGlobal = response.dailyCartCount;
            monthlySuccessCount = response.dailySuccessCount;
            monthlyCancelCount = response.dailyCancelCount;
            console.log("Doanh thu tháng:", monthlyRevenueGlobal);
            console.log("Số lượng cart tháng:", monthlyCartCountGlobal);
            console.log("Số lượng thành công tháng:", monthlySuccessCount);
            console.log("Số lượng huỷ tháng:", monthlyCancelCount);
            },
            error: function (xhr) {
                alert("Lỗi khi lấy doanh thu tháng.");
                console.error(xhr);
            }
        });
    } else if (mode === "quarter") {
        const ref = new Date(now.getFullYear(), now.getMonth() - offset * 3, 1);
        const quarter = Math.floor(ref.getMonth() / 3) + 1;
        const startMonth = (quarter - 1) * 3;
        const year = ref.getFullYear();
        const labelsList = ["Tháng 1", "Tháng 2", "Tháng 3", "Tháng 4", "Tháng 5", "Tháng 6", "Tháng 7", "Tháng 8", "Tháng 9", "Tháng 10", "Tháng 11", "Tháng 12"];
        labels = labelsList.slice(startMonth, startMonth + 3);
        title = `Quý ${quarter} năm ${year}`;

         $.ajax({
            url: '/seller/get-revenue-quarter',
            method: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({
                year: year,
                quarter: quarter
            }),
            success: function (response) {
                totalRevenueGlobal = response.monthlyRevenue;
                cartCountGlobal = response.monthlyCartCount;
                monthlySuccessCount = response.monthlySuccessCount;
                monthlyCancelCount = response.monthlyCancelCount;
                console.log("Doanh thu quý:", quarterlyRevenueGlobal);
                console.log("Số lượng cart quý:", quarterlyCartCountGlobal);
                console.log("Số lượng thành công quý:", monthlySuccessCount);
                console.log("Số lượng huỷ quý:", monthlyCancelCount);
            },
            error: function (xhr) {
                alert("Lỗi khi lấy doanh thu quý.");
                console.error(xhr);
            }
        });
    }

    return { labels, title };
}

// Khởi tạo biểu đồ đường
let chart = new Chart(ctx, {
    type: 'line',
    data: {
        labels: [],
        datasets: []
    },
    options: {
        responsive: true,
        interaction: {
            mode: 'index',
            intersect: false
        },
        plugins: {
            tooltip: { mode: 'index', intersect: false },
            legend: { display: false }
        },
        scales: {
            x: { title: { display: true, text: 'Thời gian' } },
            y: {
                type: 'linear',
                position: 'left',
                title: { display: true, text: 'Doanh thu (VNĐ)' },
                beginAtZero: true,
                display: true
            },
            y1: {
                type: 'linear',
                position: 'right',
                title: { display: true, text: 'Số lượng' },
                beginAtZero: true,
                grid: { drawOnChartArea: false },
                display: true
            }
        }
    }
});

// Khởi tạo biểu đồ tròn
let pieChart = new Chart(pieCtx, {
    type: 'pie',
    data: {
        labels: ['Đơn thành công', 'Đơn huỷ', 'Đơn không thành công'],
        datasets: [{
            data: [0, 0, 0],
            backgroundColor: ['#28a745', '#dc3545', '#ffc107'],
            borderWidth: 1
        }]
    },
    options: {
        responsive: true,
        plugins: {
            legend: { position: 'bottom' }
        }
    }
});

// Cập nhật dữ liệu cho pie chart (chỉ ở tab Đơn hàng)
function updatePieChart() {
    if (currentTab === "orders") {
        let ds = chart.data.datasets;
        let success = ds[1]?.data?.reduce((a, b) => a + b, 0) || 0;
        let cancel = ds[2]?.data?.reduce((a, b) => a + b, 0) || 0;
        let fail = ds[3]?.data?.reduce((a, b) => a + b, 0) || 0;
        let data = [success, cancel, fail];
        pieChart.data.datasets[0].data = data;
        pieChart.update();
        document.getElementById("pieChart").style.display = "block";
    } else {
        document.getElementById("pieChart").style.display = "none";
    }
}

// Cập nhật biểu đồ và controls theo tab
function updateChart() {
    const config = tabConfigs[currentTab];
    const { labels, title } = getLabelsAndTitle(currentMode, currentOffset);

    // Tạo dữ liệu ảo cho từng dataset
    let datasets = config.datasets.map((ds, idx) => {
        let data;
        if (currentTab === "overview") {
            data = idx === 0
                ? totalRevenueGlobal
                : cartCountGlobal;
        } else if (currentTab === "revenue") {
            data = totalRevenueGlobal;
        } else if (currentTab === "orders") {
            if (idx === 0) data = cartCountGlobal; // Tổng đơn
            else if (idx === 1) data = monthlySuccessCount; // Thành công
            else if (idx === 2) data = monthlyCancelCount; // Huỷ
            // else data = randomData(1, 5, labels.length); // Không thành công
        } else if (currentTab === "products") {
            if (idx === 0) data = randomData(10, 100, labels.length); // Bán ra
            else if (idx === 1) data = randomData(5, 50, labels.length); // Còn lại
            else data = randomData(1, 10, labels.length); // Chờ duyệt
        }
        return { ...ds, data, hidden: false };
    });

    // Cập nhật dữ liệu biểu đồ
    chart.data.labels = labels;
    chart.data.datasets = datasets;
    document.getElementById("dateLabel").textContent = title;

    // Cập nhật tiêu đề
    document.querySelector(".chart-container h2").textContent = config.title;

    // Cập nhật controls
    const controls = document.querySelector('.controls');
    controls.innerHTML = "";
    config.controls.forEach((ctrl, idx) => {
        const label = document.createElement("label");
        label.className = "checkbox-label";
        label.style.color = ctrl.color;
        label.innerHTML = `
            <input type="checkbox" checked onchange="toggleDataset(${idx})"> ${ctrl.label}
        `;
        controls.appendChild(label);
    });
    controls.style.display = "flex";

    // Hiển thị trục phù hợp
    if (currentTab === "overview") {
        chart.options.scales.y.display = true;
        chart.options.scales.y1.display = true;
    } else {
        chart.options.scales.y.display = true;
        chart.options.scales.y1.display = false;
    }
    // ...existing code...

    // Ẩn/hiện nút tiến tới
    const nextBtn = document.getElementById("nextBtn");
    if (currentOffset === 0) {
        nextBtn.style.visibility = "hidden";
    } else {
        nextBtn.style.visibility = "visible";
    }

    chart.update();
    updatePieChart();
}

// Đổi mode tuần/tháng/quý
document.getElementById("overview-select").addEventListener("change", function () {
    const val = this.value;
    currentOffset = 0;
    if (val === "1") currentMode = "week";
    else if (val === "2") currentMode = "month";
    else if (val === "3") currentMode = "quarter";
    updateChart();
});

// Điều hướng thời gian
function changeOffset(direction) {
    // Không cho vượt quá ngày hiện tại (currentOffset <= 0)
    if (direction > 0 && currentOffset === 0) return;
    currentOffset -= direction;
    updateChart();
}

// Ẩn/hiện dataset
function toggleDataset(index) {
    const meta = chart.getDatasetMeta(index);
    meta.hidden = meta.hidden === null ? !chart.data.datasets[index].hidden : null;
    chart.update();
    updatePieChart();
}

// Tabs
document.querySelectorAll(".tabs span").forEach((tab, idx) => {
    tab.addEventListener("click", () => {
        document.querySelectorAll(".tabs span").forEach(t => t.classList.remove("active"));
        tab.classList.add("active");
        if (idx === 0) currentTab = "overview";
        else if (idx === 1) currentTab = "revenue";
        else if (idx === 2) currentTab = "orders";
        else if (idx === 3) currentTab = "products";
        currentOffset = 0;
        updateChart();
    });
});

updateChart();