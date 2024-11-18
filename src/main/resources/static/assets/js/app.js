fetch('donor/profile/1')
    .then(response => response.json())
    .then(data => {
        console.log("Get profile success");
        document.querySelectorAll(".firstName").forEach(el => el.innerText = data.firstname || "First Name");
        document.querySelectorAll(".lastName").forEach(el => el.innerText = data.lastname || "Last Name");
        document.getElementById("sex").innerText = data.sex;
        document.getElementById("phone").innerText = data.phone;
        document.getElementById("totalBloodDonated").innerText = data.totalBloodDonated;
        document.getElementById("bloodGroup").innerText = data.bloodGroup;
        document.getElementById("birthday").innerText = data.birthday;
        document.getElementById("address").innerText = data.address;
        document.getElementById("email").innerText = data.email;
    })
    .catch(error => console.log('Error: ', error));



fetch('https://provinces.open-api.vn/api/?depth=1')
    .then(response => response.json())
    .then(cities => {
        const citySelect = document.getElementById('city');
        citySelect.innerHTML = '<option value="">Chọn TP</option>'; // Initial placeholder

        cities.forEach(city => {
            const option = document.createElement('option');
            option.value = city.code;
            option.innerText = city.name;
            citySelect.appendChild(option);
        });

        // Listen for city selection to fetch districts
        citySelect.addEventListener('change', function () {
            const cityCode = citySelect.value;
            if (cityCode) {
                fetchDistricts(cityCode);
            } else {
                resetSelect(document.getElementById('district'), 'Chọn Quận/Huyện');
                resetSelect(document.getElementById('ward'), 'Chọn Xã/Phường');
            }
        });
    })
    .catch(error => console.log('Error fetching cities:', error));

// Function to fetch and populate districts based on the selected city
function fetchDistricts(cityCode) {
    fetch(`https://provinces.open-api.vn/api/p/${cityCode}?depth=2`)
        .then(response => response.json())
        .then(cityData => {
            const districtSelect = document.getElementById('district');
            resetSelect(districtSelect, 'Chọn Quận/Huyện'); // Clear existing options
            cityData.districts.forEach(district => {
                const option = document.createElement('option');
                option.value = district.code;
                option.innerText = district.name;
                districtSelect.appendChild(option);
            });

            // Listen for district selection to fetch wards
            districtSelect.addEventListener('change', function () {
                const districtCode = districtSelect.value;
                if (districtCode) {
                    fetchWards(districtCode);
                } else {
                    resetSelect(document.getElementById('ward'), 'Chọn Xã/Phường');
                }
            });
        })
        .catch(error => console.log('Error fetching districts:', error));
}

// Function to fetch and populate wards based on the selected district
function fetchWards(districtCode) {
    fetch(`https://provinces.open-api.vn/api/d/${districtCode}?depth=2`)
        .then(response => response.json())
        .then(districtData => {
            const wardSelect = document.getElementById('ward');
            resetSelect(wardSelect, 'Chọn Xã/Phường'); // Clear existing options
            districtData.wards.forEach(ward => {
                const option = document.createElement('option');
                option.value = ward.code;
                option.innerText = ward.name;
                wardSelect.appendChild(option);
            });
        })
        .catch(error => console.log('Error fetching wards:', error));
}

// Utility function to reset a select element with a placeholder
function resetSelect(selectElement, placeholderText) {
    selectElement.innerHTML = `<option value="">${placeholderText}</option>`;
}


let selectedHospitalId = null; // Biến lưu hospitalId được chọn

// Lấy danh sách bệnh viện từ API và hiển thị
fetch('/api/all_hospitals')
    .then(response => response.json())
    .then(data => {
        console.log("Fetched hospital data successfully.");
        const hospitalList = document.getElementById("hospital-list");
        if (data.length > 0) {
            hospitalList.innerHTML = data.map(hospital => `
                <div class="hospital">
                    <h3>${hospital.name}</h3>
                    <p>Địa chỉ: ${hospital.address}</p>
                    <button class="register-btn" onclick="selectHospital(${hospital.id})">Đăng ký hiến máu</button>
                </div>
            `).join('');
        } else {
            hospitalList.innerHTML = '<p>Không có bệnh viện nào trong danh sách.</p>';
        }
    })
    .catch(error => {
        console.error("Error fetching hospital data:", error);
        document.getElementById("hospital-list").innerHTML = '<p>Không thể tải danh sách bệnh viện.</p>';
    });

// Khi người dùng nhấp vào "Đăng ký hiến máu"
function selectHospital(hospitalId) {
    // Lưu hospitalId vào sessionStorage
    sessionStorage.setItem('selectedHospitalId', hospitalId);

    // Chuyển hướng người dùng đến trang đăng ký
    redirectToRegister(hospitalId);

    // Hiển thị form đăng ký
    document.getElementById('registrationForm').classList.remove('hidden');
}

// Chuyển hướng đến trang đăng ký với hospitalId
function redirectToRegister(hospitalId) {
    window.location.href = `/register?hospitalId=${hospitalId}`;
}

// Gửi form với dữ liệu bổ sung hospitalId
document.addEventListener('DOMContentLoaded', function () {
    document.getElementById('registrationForm').addEventListener('submit', function (event) {
        event.preventDefault(); // Ngăn hành động gửi form mặc định

        const formData = new FormData(this);

        // Lấy hospitalId từ sessionStorage
        const selectedHospitalId = sessionStorage.getItem('selectedHospitalId');

        // Thêm hospitalId vào form data
        if (selectedHospitalId) {
            formData.append('hospitalId', selectedHospitalId);
        } else {
            alert('Vui lòng chọn một bệnh viện trước khi đăng ký!');
            return;
        }

        // Gửi dữ liệu đến server
        fetch('/donation/create', {
            method: 'POST',
            body: formData,
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('authToken')}`
            }
        })
            .then(response => response.json())
            .then(data => {
                if (data.message === "success") {
                    showPopup('Đăng ký thành công');
                    this.reset(); // Reset form
                } else {
                    showPopup('Đăng ký không thành công, vui lòng thử lại!');
                }
            })
            .catch(error => {
                console.error('Lỗi khi gửi form:', error);
                alert('Có lỗi xảy ra. Vui lòng thử lại.');
            });
    });
});

function showPopup(message) {
    const popup = document.getElementById('popupNotification');
    const popupMessage = document.getElementById('popupMessage');
    popupMessage.textContent = message; // Cập nhật nội dung popup
    popup.style.display = 'block'; // Hiển thị popup
}

document.addEventListener('DOMContentLoaded', function () {
    // Lắng nghe sự kiện submit trên form đăng nhập
    document.getElementById('loginFormSubmit').addEventListener('submit', function (event) {
        event.preventDefault(); // Ngăn hành động gửi form mặc định

        // Lấy dữ liệu từ form dưới dạng FormData
        const formData = new FormData(this);
        const usernameOrEmail = formData.get('usernameOrEmail');
        const password = formData.get('password');

        // Tạo payload cho sign-up
        const data = {
            usernameOrEmail, // Ensure this matches the BE DTO
            password,
        };

        // Gửi yêu cầu đăng nhập đến server
        fetch('/api/auth/signin', {
            method: 'POST',
            body: JSON.stringify(data),
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Đăng nhập không thành công');
                }
                return response.json(); // Chuyển đổi phản hồi thành JSON
            })
            .then(data => {
                if (data.roles && data.roles.includes("ROLE_USER")) {
                    localStorage.setItem('authToken', data.token);
                    showPopup('Đăng nhập thành công!');
                    setTimeout(() => {
                        window.location.href = '/home/donor';
                    }, 500);
                }
                else if (data.roles && data.roles.includes("ROLE_HOSPITAL"))
                {
                    localStorage.setItem('authToken', data.token);

                    showPopup('Đăng nhập thành công!');
                    setTimeout(() => {
                        window.location.href = '/home/hospital';
                    }, 500);
                }
                else {
                    showPopup('Bạn không có quyền truy cập. Vui lòng thử lại.');
                }
            })
            .catch(error => {
                console.error('Lỗi đăng nhập:', error);
                showPopup('Có lỗi xảy ra. Vui lòng thử lại.');
            });
    });
});

// Hàm hiển thị popup
function showPopup(message) {
    alert(message); // Có thể thay thế bằng cách hiển thị một popup tùy chỉnh
}
document.addEventListener('DOMContentLoaded', function () {
    document.getElementById('signUpFormSubmit').addEventListener('submit', function (event) {
        event.preventDefault();

        const formData = new FormData(this);
        const name = formData.get('name');
        const username = formData.get('username');
        const email = formData.get('email');
        const password = formData.get('password');
        const role = formData.get('role');

        // Tạo payload cho sign-up
        const data = {
            name,
            username,
            email,
            password,
            role
        };

        // Gửi yêu cầu đăng ký
        fetch('/api/auth/signup', {
            method: 'POST',
            body: JSON.stringify(data),
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(response => response.json())
            .then(data => {
                if (data.message === "success") {
                    showPopup('Đăng ký thành công');
                } else {
                    showPopup('Đăng ký không thành công, vui lòng thử lại');
                }
            })
            .catch(error => {
                console.error('Lỗi đăng ký:', error);
                showPopup('Có lỗi xảy ra. Vui lòng thử lại.');
            });
    });
});

// Show the popup message
function showPopupLogin(message) {
    document.getElementById('popupMessage').innerText = message;
    document.getElementById('popupNotification').style.display = 'block';
    setTimeout(() => {
        document.getElementById('popupNotification').style.display = 'none';
    }, 3000);
}

const loginForm = document.getElementById('loginForm');
const signUpForm = document.getElementById('signUpForm');
const showSignUpLink = document.getElementById('showSignUp');
const showLoginLink = document.getElementById('showLogin');

// Show the sign-up form and hide the login form when "Sign up here" is clicked
showSignUpLink.addEventListener('click', function() {
    loginForm.style.display = 'none';
    signUpForm.style.display = 'block';
});

// Show the login form and hide the sign-up form when "Login here" is clicked
showLoginLink.addEventListener('click', function() {
    signUpForm.style.display = 'none';
    loginForm.style.display = 'block';
});


