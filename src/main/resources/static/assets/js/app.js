const roleUser = localStorage.getItem('role');
    // Get the role from localStorage

document.addEventListener("DOMContentLoaded", function() {
    const loginForm = document.getElementById('loginForm');
    const signUpForm = document.getElementById('signUpForm');
    const showSignUpLink = document.getElementById('showSignUp');
    const showLoginLink = document.getElementById('showLogin');

    // Add event listeners to show/hide the forms
    if (showSignUpLink && showLoginLink) {
        showSignUpLink.addEventListener('click', function () {
            loginForm.style.display = 'none';
            signUpForm.style.display = 'block';
        });

        showLoginLink.addEventListener('click', function () {
            signUpForm.style.display = 'none';
            loginForm.style.display = 'block';
        });
    }
});

document.addEventListener("DOMContentLoaded", function() {
    // Get the role from localStorage// Assume role is stored as 'ROLE_USER' or 'ROLE_HOSPITAL'

    // Get the home button element
    const homeButton = document.getElementById("homeLink");

    // Add a click event listener to the button
    homeButton.addEventListener("click", function () {
    if (roleUser === "ROLE_USER") {
    window.location.href = "/home/user"; // Redirect to the user home page
}    else if (roleUser === "ROLE_HOSPITAL") {
    window.location.href = "/home/hospital"; // Redirect to the hospital home page
} else {
    window.location.href = "/home"; // Default redirect, for guests or unauthenticated users
}
});
});

document.addEventListener("DOMContentLoaded", function() {
    const logoutButton = document.getElementById("logoutBtn")

    logoutButton.addEventListener("click", function () {
        // Gửi yêu cầu POST đến API logout
        fetch("/api/auth/logout", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                // Thêm token vào header nếu cần (nếu bạn sử dụng JWT token lưu trong localStorage)
                "Authorization": "Bearer " + localStorage.getItem("authToken") // Lấy token từ localStorage (nếu có)
            }
        })
            .then(response => response.json())
            .then(data => {
                if (data.message === "Logout thành công") {
                    // Xóa token khỏi localStorage hoặc sessionStorage sau khi logout
                    localStorage.removeItem("authToken"); // Hoặc sessionStorage.removeItem("token");
                    localStorage.removeItem("idToken");
                    localStorage.removeItem("role");
                    localStorage.removeItem("selectedHospitalId");
                    // Chuyển hướng đến trang đăng nhập hoặc trang chủ
                    window.location.href = "/";  // Chuyển hướng đến trang đăng nhập
                } else {
                    // Nếu có lỗi khi logout, bạn có thể xử lý ở đây
                    alert("Có lỗi khi logout: " + data.message);
                }
            })
            .catch(error => {
                console.error("Lỗi khi logout:", error);
                alert("Có lỗi khi logout.");
            });
    });
});

// Kiểm tra xem idUser có tồn tại không
if (roleUser) {
    // Kiểm tra nếu người dùng có vai trò 'ROLE_USER'
    if (roleUser === 'ROLE_USER') {
        // Fetch dữ liệu của donor khi người dùng có vai trò ROLE_USER
        fetch(`/api/donor/${localStorage.getItem('idToken')}`)
            .then(response => response.json())
            .then(data => {
                console.log("Get profile success");

                // Cập nhật giao diện với dữ liệu từ API
                document.querySelectorAll(".firstName").forEach(el => el.innerText = data.name || "First Name");
                document.getElementById("sex").innerText = data.sex;
                document.getElementById("phone").innerText = data.phone;
                document.getElementById("totalBloodDonated").innerText = data.totalBloodDonated;
                document.getElementById("bloodGroup").innerText = data.bloodGroup;
                document.getElementById("birthday").innerText = data.birthday;
                document.getElementById("address").innerText = data.address;
                document.getElementById("email").innerText = data.email;
            })
            .catch(error => console.log('Error fetching blood donor profile: ', error));
    }
    // Kiểm tra nếu người dùng có vai trò 'ROLE_HOSPITAL'
    else if (roleUser === 'ROLE_HOSPITAL') {
        // Fetch dữ liệu của hospital khi người dùng có vai trò ROLE_HOSPITAL
        fetch(`/api/hospitals/${localStorage.getItem('idToken')}`)
            .then(response => response.json())
            .then(hospitalData => {
                console.log("Get hospital profile success");

                // Cập nhật giao diện với dữ liệu từ API của bệnh viện
                document.querySelectorAll(".hospitalName").forEach(el => el.innerText = hospitalData.name || "Hospital Name");
                document.getElementById("hospitalAddress").innerText = hospitalData.address;
                document.getElementById("hospitalPhone").innerText = hospitalData.phone;
                document.getElementById("hospitalEmail").innerText = hospitalData.email;

            })
            .catch(error => console.log('Error fetching hospital profile: ', error));

    }
}

if (roleUser === 'ROLE_USER') {
    document.addEventListener('DOMContentLoaded', () => {
        const donationId = localStorage.getItem('idToken');

        // Kiểm tra xem donationId có hợp lệ không
        if (!donationId) {
            console.error("Không tìm thấy donationId trong localStorage.");
            return;
        }

        fetch(`/api/donation/donor/${donationId}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Không thể lấy thông tin đơn hiến máu.');
                }
                return response.json();
            })
            .then(data => {
                const donationList = document.getElementById('donation-list');

                if (!donationList) {
                    console.error('Không tìm thấy phần tử #donation-list trong DOM.');
                    return;
                }

                // Clear any existing items
                donationList.innerHTML = '';

                // Kiểm tra nếu dữ liệu trả về là mảng hợp lệ
                if (Array.isArray(data.data) && data.data.length > 0) {
                    data.data.forEach(donation => {
                        const listItem = document.createElement('li');
                        listItem.classList.add('donation-item', 'p-4', 'bg-white', 'mb-4', 'rounded-lg', 'shadow-md');
                        const statusColor = donation.status === "Đã duyệt" ? "text-green font-bold" : "text-red font-bold";
                        // Kiểm tra giá trị hospitalId hợp lệ
                        if (donation.hospitalId) {
                            fetch(`/api/hospital/${donation.hospitalId}`)
                                .then(hospitalResponse => {
                                    if (!hospitalResponse.ok) {
                                        throw new Error('Không thể lấy thông tin bệnh viện.');
                                    }
                                    return hospitalResponse.json();
                                })
                                .then(hospital => {
                                    listItem.innerHTML = `
                                        <p><strong>Đơn hiến máu của bạn tại:</strong> ${hospital.name || 'Không xác định'}</p>
                                        <p><strong>Người đăng ký hiến:</strong> ${donation.name}</p>
                                        <p><strong>Trạng thái:</strong> <span class="${statusColor}">${donation.status}</span></p>
                                    `;
                                    donationList.appendChild(listItem);
                                })
                                .catch(error => {
                                    console.error('Error fetching hospital data:', error);
                                    listItem.innerHTML = `
                                        <p><strong>Đơn hiến máu của bạn tại:</strong> Không xác định</p>
                                        <p><strong>Người đăng ký hiến:</strong> ${donation.name}</p>
                                        <p><strong>Trạng thái:</strong> <span class="${statusColor}">${donation.status}</span></p>
                                    `;
                                    donationList.appendChild(listItem);
                                });
                        } else {
                            // Nếu không có hospitalId, chỉ hiển thị thông tin đơn đăng ký
                            listItem.innerHTML = `
                                <p><strong>Đơn hiến máu của bạn tại:</strong> Không xác định</p>
                                <p><strong>Người đăng ký hiến:</strong> ${donation.name}</p>
                                <p><strong>Trạng thái:</strong> <span class="${statusColor}">${donation.status}</span></p>
                            `;
                            donationList.appendChild(listItem);
                        }
                    });
                } else {
                    donationList.innerHTML = '<li class="text-center">Không có đơn đăng ký nào.</li>';
                }
            })
            .catch(error => {
                console.error('Error fetching donation data:', error);
                const donationList = document.getElementById('donation-list');
                if (donationList) {
                    donationList.innerHTML = '<li class="text-center text-red-500">Có lỗi khi lấy dữ liệu đơn hiến máu.</li>';
                }
            });
    });
}





function displayDonations(donations) {
    const donationsContainer = document.getElementById('donationsContainer');

    if (!donationsContainer) {
        console.error('Donations container not found');
        return;
    }

    donationsContainer.innerHTML = '';

    donations.forEach(donation => {
        const donationRow = document.createElement('tr');

        const nameCell = document.createElement('td');
        nameCell.textContent = donation.name;

        const emailCell = document.createElement('td');
        emailCell.textContent = donation.email;

        const bloodTypeCell = document.createElement('td');
        bloodTypeCell.textContent = donation.bloodType;

        const actionCell = document.createElement('td');
        const requestButton = document.createElement('button');
        requestButton.className = 'request-button';
        requestButton.textContent = 'Xem chi tiết';

        // Sự kiện click để hiển thị popup chi tiết
        requestButton.addEventListener('click', () => {
            fetch(`/api/donation/${donation.id}`)
                .then(response => response.json())
                .then(data => {
                    if (data && data.message === 'success') {
                        showPopupDonation(data.data); // Hiển thị popup với dữ liệu chi tiết
                    } else {
                        alert('Không thể lấy thông tin chi tiết');
                    }
                })
                .catch(error => console.error('Error fetching donation detail:', error));
        });

        actionCell.appendChild(requestButton);

        donationRow.appendChild(nameCell);
        donationRow.appendChild(emailCell);
        donationRow.appendChild(bloodTypeCell);
        donationRow.appendChild(actionCell);

        donationsContainer.appendChild(donationRow);
    });
}

// Hàm hiển thị popup
function showPopupDonation(donationDetail) {
    const popup = document.getElementById('popupDetail');
    const donationDetailsContainer = document.getElementById('donationDetails');
    const statusColor = donationDetail.status === "Đã duyệt" ? "text-green" : "text-red";

    // Điền dữ liệu chi tiết vào popup
    donationDetailsContainer.innerHTML = `
            <p class="text-red font-bold">Thông tin cá nhân:</p>
            <p><strong>Họ và tên:</strong> ${donationDetail.name}</p>
            <p><strong>Giới tính:</strong> ${donationDetail.gender === "Male" ? "Nam" : "Nữ"}</p>
            <p><strong>Năm sinh:</strong> ${donationDetail.city}</p>
            <p><strong>Email:</strong> ${donationDetail.email}</p>
            <p><strong>Số điện thoại:</strong> ${donationDetail.phone}</p>
            <p><strong>Nhóm máu:</strong> ${donationDetail.bloodType}</p>
            <p class="text-red font-bold">Địa chỉ:</p>
            <p><strong>Tỉnh/Thành phố:</strong> ${donationDetail.city}</p>
            <p><strong>Huyện/Quận:</strong> ${donationDetail.district}</p>
            <p><strong>Xã/ Phường:</strong> ${donationDetail.ward}</p>
            <p><strong>Địa chỉ cụ thể:</strong> ${donationDetail.address}</p>
            <p class="text-red font-bold">Thông tin sức khoẻ:</p>
            <p><strong>Đã tiêm Vaccine Covid:</strong> ${donationDetail.vaccineStatus ? "Đã tiêm" : "Chưa tiêm"}</p>
            <p><strong>Chiều cao:</strong> ${donationDetail.height}</p>
            <p><strong>Cân nặng:</strong> ${donationDetail.weight}</p>
            <p><strong>BMI:</strong> ${donationDetail.bmi.toFixed(2)}</p>
            <p class="text-red font-bold"><strong>Trạng thái đơn:</strong> <span class="${statusColor}">${donationDetail.status}</span></p>
            <button class="bg-blue" id="approveBtn">Duyệt</button>
            <button id="rejectBtn">Không duyệt</button>
        `;

    // Hiển thị popup
    popup.classList.remove('hidden');

    // Đảm bảo sự kiện đóng chỉ được gắn một lần
    const closePopup = document.getElementById('closePopup');
    closePopup.onclick = () => {
        popup.classList.add('hidden');
    };

    // Handle "Duyệt" button click
    const approveBtn = document.getElementById('approveBtn');
    approveBtn.onclick = () => {
        // Update status in the database
        updateDonationStatus(donationDetail.id, "Đã duyệt");
        popup.classList.add('hidden'); // Close the popup
    };

    // Handle "Không duyệt" button click
    const rejectBtn = document.getElementById('rejectBtn');
    rejectBtn.onclick = () => {
        // Close the popup without making any changes
        popup.classList.add('hidden');
    };
}

function updateDonationStatus(donationId, status) {
    const apiUrl = `/api/donation/update-status/${donationId}`;

    // Create the request body (optional, depending on the API)
    const requestBody = {
        status: status
    };

    // Make the API call using fetch
    fetch(apiUrl, {
        method: 'PUT',  // The HTTP method for updating the resource
        headers: {
            'Content-Type': 'application/json', // Set the content type to JSON
            // Add any other necessary headers (e.g., authorization token)
        },
        body: JSON.stringify(requestBody)  // Convert the request body to JSON
    })
        .then(response => response.json())  // Parse the response as JSON
        .then(data => {
            if (data.status === 'success') {
                alert('Donation status updated successfully.');
            } else {
                alert('Failed to update donation status: ' + data.message);
            }
        })
        .catch(error => {
            console.error('Error updating donation status:', error);
            alert('An error occurred while updating donation status.');
        });
}


// Call the fetch function when the page loads (or when hospitalId is available)
if(roleUser === 'ROLE_HOSPITAL') {
    fetch(`/api/donation/hospital/${localStorage.getItem('idToken')}`)
        .then(response => response.json())
        .then(data => {
            if (data.message === 'success' && Array.isArray(data.data)) {
                console.log("Donations fetched successfully", data.data);
                displayDonations(data.data);
            } else {
                console.log('No donations found or error in response');
            }
        })
        .catch(error => console.log('Error fetching donations: ', error));
}

fetch('https://provinces.open-api.vn/api/?depth=1')
    .then(response => response.json())
    .then(cities => {
        const citySelect = document.getElementById('city');
        citySelect.innerHTML = '<option id="" value="" data-name="">Chọn TP</option>'; // Initial placeholder

        cities.forEach(city => {
            const option = document.createElement('option');
            option.value = city.code;
            option.innerText = city.name;
            option.setAttribute('data-name', city.name);
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
                option.setAttribute('data-name', district.name);
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
                option.setAttribute('data-name', ward.name);
                wardSelect.appendChild(option);
            });
        })
        .catch(error => console.log('Error fetching wards:', error));
}

// Utility function to reset a select element with a placeholder
function resetSelect(selectElement, placeholderText) {
    selectElement.innerHTML = `<option value="">${placeholderText}</option>`;
}

if (roleUser === 'ROLE_HOSPITAL') {
    document.addEventListener('DOMContentLoaded', function () {
        document.getElementById('bloodRequestForm').addEventListener('submit', function (event) {
            event.preventDefault(); // Prevent the default form submission behavior

            // Get form data
            const formData = new FormData(this);
            const bloodGroupId = formData.get('bloodGroupId');
            const quantityRequested = formData.get('quantityRequested');

            // Retrieve hospitalId from local storage
            const selectedHospitalId = parseInt(localStorage.getItem('idToken'));

            // Create the data object to send
            const data = {
                bloodGroupId,
                quantityRequested,
                hospitalId: selectedHospitalId
            };

            // Send the data as JSON in the request body
            fetch('/api/request/regis', {
                method: 'POST', // Ensure the method is POST
                headers: {
                    'Content-Type': 'application/json' // Set the correct content type
                },
                body: JSON.stringify(data), // Send JSON data
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Network response was not ok');
                    }
                    return response.json();
                })
                .then(data => {
                    if (data.message === "Blood request created") {
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
}

if(localStorage.getItem('authToken')) {
    document.addEventListener("DOMContentLoaded", async function () {
        const bloodRequestsList = document.getElementById("blood-requests-list");

        try {
            // Gọi API để lấy danh sách yêu cầu hiến máu
            const requestResponse = await fetch("/api/request");
            const requestData = await requestResponse.json();

            if (requestData && requestData.message === "Success") {
                const bloodRequests = requestData.data;

                if (bloodRequests.length === 0) {
                    bloodRequestsList.innerHTML = `<p class="text-center text-gray-500">Hiện tại không có yêu cầu hiến máu.</p>`;
                } else {
                    // Lấy danh sách hospitalId duy nhất
                    const hospitalIds = [...new Set(bloodRequests.map((request) => request.hospitalId))];

                    // Gọi API để lấy danh sách người dùng (tất cả hospitalId một lần)
                    const usersResponse = await fetch(`/api/auth/users?ids=${hospitalIds.join(",")}`);
                    const usersData = await usersResponse.json();

                    // Tạo ánh xạ hospitalId -> hospitalName
                    const hospitalMap = usersData.reduce((map, user) => {
                        map[user.id] = user.name;
                        return map;
                    }, {});

                    // Tạo và hiển thị danh sách yêu cầu hiến máu
                    bloodRequests.forEach((request) => {
                        const hospitalName = hospitalMap[request.hospitalId] || "Không xác định";

                        const card = document.createElement("div");
                        card.className = "blood-request-card";

                        card.innerHTML = `
                        <h3 class="font-bold text-lg text-red-600">Bệnh viện: ${hospitalName}</h3>
                        <p class="text-gray-700">Nhóm máu cần: 
                           <span class="blood-group">${request.bloodGroupId}</span>
                        </p>
                        <p class="text-gray-700">Số lượng: <span class="font-medium">${request.quantityRequested} ml</span></p>
                    `;
                        bloodRequestsList.appendChild(card);
                    });
                }
            } else {
                bloodRequestsList.innerHTML = `<p class="text-center text-red-500">Đã xảy ra lỗi khi tải dữ liệu.</p>`;
            }
        } catch (error) {
            bloodRequestsList.innerHTML = `<p class="text-center text-red-500">Không thể kết nối tới API.</p>`;
            console.error("Error fetching blood requests:", error);
        }
    });
}

if(localStorage.getItem('authToken')) {
    document.addEventListener('DOMContentLoaded', function () {
        // Lấy ID của bệnh viện từ localStorage
        const hospitalId = localStorage.getItem('idToken');

        if (!hospitalId) {
            console.error('Không tìm thấy ID bệnh viện trong localStorage.');
            return;
        }

        // Gọi API để lấy danh sách yêu cầu
        fetch(`/api/request/${hospitalId}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Lỗi khi lấy dữ liệu từ API.');
                }
                return response.json();
            })
            .then(data => {
                const requests = data.data; // Đảm bảo `data.data` chứa danh sách yêu cầu
                const requestsContainer = document.getElementById('requestsContainer');

                if (requests.length === 0) {
                    requestsContainer.innerHTML = '<tr><td colspan="3">Không có yêu cầu nào.</td></tr>';
                    return;
                }

                // Hiển thị danh sách yêu cầu
                requests.forEach(request => {
                    const row = document.createElement('tr');
                    row.innerHTML = `
                        <td>${request.bloodGroupId}</td>
                        <td>${request.quantityRequested}</td>
                        <td><button class="delete-btn" data-id="${request.id}">XOÁ</button></td>
                    `;
                    requestsContainer.appendChild(row);
                });
                document.querySelectorAll('.delete-btn').forEach(button => {
                    button.addEventListener('click', function () {
                        const requestId = this.getAttribute('data-id'); // Lấy ID từ data-id
                        if (confirm('Bạn có chắc chắn muốn xóa yêu cầu này không?')) {
                            // Gửi yêu cầu DELETE đến API
                            fetch(`/api/request/delete/${requestId}`, {
                                method: 'DELETE',
                            })
                                .then(response => {
                                    if (!response.ok) {
                                        throw new Error('Không thể xóa yêu cầu.');
                                    }
                                    return response.json();
                                })
                                .then(data => {
                                    alert(data.message || 'Đã xóa yêu cầu thành công!');
                                    location.reload(); // Reload lại trang
                                })
                                .catch(error => {
                                    console.error(error);
                                    alert('Có lỗi xảy ra khi xóa yêu cầu.');
                                });
                        }
                    });
                });
            })
            .catch(error => {
                console.error('Lỗi:', error);
            });
    });
}


// Function to fetch and populate wards based on the selected district



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
    if(localStorage.getItem('authToken'))
    {
        localStorage.setItem('selectedHospitalId', hospitalId);
        redirectToRegister(hospitalId);
        document.getElementById('registrationForm').classList.remove('hidden');
    }
    else
    {
        alert("Vui lòng đăng nhập để đăng ký hiến máu!");
        window.location.href='/'
    }
}

// Chuyển hướng đến trang đăng ký với hospitalId
function redirectToRegister(hospitalId) {
    window.location.href = `/register`;
}



// Gửi form với dữ liệu bổ sung hospitalId
document.addEventListener('DOMContentLoaded', function () {
    const heightInput = document.getElementById("height");
    const weightInput = document.getElementById("weight");
    const bmiInput = document.getElementById("bmi");

    heightInput.addEventListener("input", calculateBMI);
    weightInput.addEventListener("input", calculateBMI);

    function calculateBMI() {
        const height = parseFloat(heightInput.value) / 100; // Convert cm to meters
        const weight = parseFloat(weightInput.value);
        if (height > 0 && weight > 0) {
            const bmi = (weight / (height * height)).toFixed(2);
            bmiInput.value = bmi;
        } else {
            bmiInput.value = "";
        }
    }

    // Add event listener for form submission
    document.getElementById('registrationForm').addEventListener('submit', function (event) {
        event.preventDefault();

        const formData = new FormData(this);

        // Lấy các giá trị từ form
        const name = formData.get('name').trim();
        const birthYear = formData.get('birthYear').trim();
        const gender = formData.get('gender');
        const email = formData.get('email').trim();
        const phone = formData.get('phone').trim();
        const idNumber = formData.get('idNumber').trim();

// Lấy tên thành phố từ thuộc tính data-name trong phần tử select
        const cityElement = document.getElementById('city');
        const city = cityElement.options[cityElement.selectedIndex].getAttribute('data-name');

// Lấy tên quận từ thuộc tính data-name trong phần tử select
        const districtElement = document.getElementById('district');
        const district = districtElement.options[districtElement.selectedIndex].getAttribute('data-name');

// Lấy tên xã/phường từ thuộc tính data-name trong phần tử select
        const wardElement = document.getElementById('ward');
        const ward = wardElement.options[wardElement.selectedIndex].getAttribute('data-name');

        const address = formData.get('address').trim();
        const bloodType = formData.get('bloodType');
        const vaccineStatus = formData.get('vaccineStatus') === 'on'; // Checkbox field
        const height = parseInt(formData.get('height'));
        const weight = parseInt(formData.get('weight'));
        const bmi = parseFloat(formData.get('bmi'));

        // Kiểm tra các trường bắt buộc
        if (!name || !birthYear || !gender || !email || !phone || !idNumber || !city || !district || !ward || !address || !bloodType || isNaN(height) || isNaN(weight) || isNaN(bmi)) {
            showPopup('Vui lòng điền đầy đủ thông tin!');
            return;
        }

        // Lấy hospitalId từ sessionStorage
        const selectedHospitalId = parseInt(localStorage.getItem('selectedHospitalId'));
        const donationId = parseInt(localStorage.getItem('idToken'));

        // Kiểm tra xem hospitalId có hợp lệ không
        if (!selectedHospitalId) {
            showPopup('Vui lòng chọn một bệnh viện trước khi đăng ký!');
            return;
        }

        // Tạo payload cho form submission
        const data = {
            name,
            birthYear,
            gender,
            email,
            phone,
            idNumber,
            city,
            district,
            ward,
            address,
            bloodType,
            vaccineStatus,
            height,
            weight,
            bmi,
            hospitalId: selectedHospitalId,
            donorId: donationId,
            status: 'Chưa duyệt'
        };

        // Gửi yêu cầu đến server
        fetch('api/donation/registerdonation', {
            method: 'POST',
            body: JSON.stringify(data),
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${localStorage.getItem('authToken')}`
            }
        })
            .then(response => response.json())
            .then(data => {
                if (data.message === "Donation created successfully") {
                    showPopup('Đăng ký thành công');
                    localStorage.removeItem('selectedHospitalId');
                    this.reset(); // Reset form after success

                } else {
                    showPopup('Đăng ký không thành công, vui lòng thử lại!');
                }
            })
            .catch(error => {
                console.error('Lỗi khi gửi form:', error);
                showPopup('Có lỗi xảy ra. Vui lòng thử lại.');
            });
    });
});

// Function to display pop-up notifications
function showPopup(message) {
    const popup = document.getElementById('popupNotification');
    const popupMessage = document.getElementById('popupMessage');
    popupMessage.textContent = message;
    popup.style.display = 'block';

    setTimeout(() => {
        popup.style.display = 'none';
    }, 5000); // Hide after 5 seconds
}


function showPopup(message) {
    const popup = document.getElementById('popupNotification');
    const popupMessage = document.getElementById('popupMessage');
    popupMessage.textContent = message; // Cập nhật nội dung popup
    popup.style.display = 'block'; // Hiển thị popup
}

document.addEventListener('DOMContentLoaded', function () {
    document.getElementById('loginFormSubmit').onsubmit = function (event) {
        event.preventDefault();

        var usernameOrEmail = document.getElementById('usernameOrEmail').value;
        var password = document.getElementById('password').value;

        fetch('/api/auth/signin', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                usernameOrEmail: usernameOrEmail,
                password: password
            })
        })
            .then(response => response.json())
            .then(data => {
                if (data.token) {
                    // Store the JWT token in localStorage or sessionStorage
                    localStorage.setItem('authToken', data.token);
                    localStorage.setItem('idToken', data.id);
                    localStorage.setItem('role', data.roles[0]);
                    // Check user role and redirect accordingly
                    if (data.roles[0].includes('ROLE_USER')) {
                        window.location.href = '/home/user';
                    } else if (data.roles[0].includes('ROLE_HOSPITAL')) {
                        window.location.href = '/home/hospital';
                    }
                } else {
                    alert('Login failed: ' + data.message);
                }
            })
            .catch(error => {
                alert('Error during login: ' + error);
            });
    };
});


// Hàm hiển thị popup
function showPopup(message) {
    alert(message);
    location.reload();// Có thể thay thế bằng cách hiển thị một popup tùy chỉnh
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
                if (data.message === "User registered successfully") {
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
showSignUpLink.addEventListener('click', function () {
    loginForm.style.display = 'none';
    signUpForm.style.display = 'block';
});

// Show the login form and hide the sign-up form when "Login here" is clicked
showLoginLink.addEventListener('click', function () {
    signUpForm.style.display = 'none';
    loginForm.style.display = 'block';
});

