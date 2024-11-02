fetch('donor/profile/3')
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