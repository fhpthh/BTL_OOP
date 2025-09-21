☕ FHCOFFEE – Hệ thống quản lý quán cà phê trực tuyến
📌 Giới thiệu

FHCOFFEE là một hệ thống quản lý và đặt hàng trực tuyến cho quán cà phê, được xây dựng trên nền tảng MERN Stack (MongoDB, Express.js, React.js, Node.js).
Hệ thống cho phép khách hàng đặt đồ uống online, theo dõi đơn hàng và thanh toán trực tuyến. Đồng thời, quản trị viên có thể quản lý sản phẩm, đơn hàng và người dùng thông qua giao diện Admin.

🚀 Tính năng
👤 Người dùng (User)
Đăng ký / Đăng nhập tài khoản
Xem danh sách và chi tiết sản phẩm
Thêm sản phẩm vào giỏ hàng, cập nhật hoặc xóa
Đặt hàng & thanh toán (VNPay hoặc COD)
Theo dõi trạng thái đơn hàng và xem lịch sử mua hàng

🛠️ Quản trị viên (Admin)
Đăng nhập hệ thống quản trị
Quản lý sản phẩm (Thêm, sửa, xóa)
Quản lý đơn hàng (Xem, cập nhật trạng thái)
Xem thống kê doanh thu, sản phẩm bán chạy

🔐 Bảo mật
Xác thực và phân quyền với JWT
Mã hóa mật khẩu bằng bcrypt
Middleware kiểm tra quyền truy cập
Cấu hình CORS an toàn

💳 Thanh toán
Tích hợp cổng thanh toán VNPay
Hỗ trợ COD (thanh toán khi nhận hàng)

🛠️ Công nghệ sử dụng
Frontend: React.js, React Router, Axios, CSS
Backend: Node.js, Express.js, JWT, Mongoose
Database: MongoDB

📂 Cấu trúc dự án.
├── backend/        # Xử lý API, logic nghiệp vụ, kết nối DB
├── frontend/       # Giao diện người dùng (User)
├── admin/          # Giao diện quản trị viên
└── README.md

⚙️ Cài đặt và chạy dự án
1. Clone project
git clone https://github.com/your-username/fhcoffee.git
cd fhcoffee
2. Cài đặt dependencies
# Backend
cd backend
npm install
# Frontend
cd ../frontend
npm install
# Admin
cd ../admin
npm install
3. Cấu hình CSDL
Cài đặt MongoDB Community
Tạo database fhcoffee
Cập nhật file .env trong thư mục backend với thông tin kết nối:

4. Chạy ứng dụng
# Backend
cd backend
npm run serve
# Frontend
cd ../frontend
npm run dev

# Admin
cd ../admin
npm run dev


Ứng dụng sẽ chạy tại:
Frontend: http://localhost:3000
Backend API: http://localhost:4000

Admin: http://localhost:3001
