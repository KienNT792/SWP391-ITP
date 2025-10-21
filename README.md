# ITP - Item Trading Platform

## 📋 Mô tả dự án
Nền tảng giao dịch vật phẩm MMO được xây dựng bằng Spring Boot, Thymeleaf và MySQL.

## 🛠️ Công nghệ sử dụng
- **Backend**: Spring Boot 3.2.1, Java 21
- **Frontend**: Thymeleaf, Bootstrap 5.3
- **Database**: MySQL
- **Build Tool**: Maven
- **Security**: Spring Security (Password Encoding)

## 🚀 Cách chạy dự án

### Yêu cầu hệ thống
- Java 21
- MySQL 8.0+
- Maven 3.6+

### Bước 1: Chuẩn bị Database
1. Tạo database MySQL với tên `itp`
2. Cập nhật thông tin kết nối trong `application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/itp?createDatabaseIfNotExist=true&allowPublicKeyRetrieval=true&useSSL=false&characterEncoding=UTF-8&serverTimezone=Asia/Ho_Chi_Minh
spring.datasource.username=root
spring.datasource.password=your_password_here
```

### Bước 2: Chạy ứng dụng
```bash
mvn spring-boot:run
```

### Bước 3: Truy cập ứng dụng
- **Trang chủ**: http://localhost:8080/itp
- **Đăng ký**: http://localhost:8080/itp/auth/register
- **Đăng nhập**: http://localhost:8080/itp/auth/login

## 📁 Cấu trúc dự án

```
src/main/java/fpt/swp/springmvctt/itp/
├── config/          # Configuration classes
├── controller/      # Web Controllers
├── dto/            # Data Transfer Objects
├── entity/         # JPA Entities
├── repository/     # Data Access Layer
└── service/        # Business Logic Layer
```

## 🎯 Tính năng đã implement

### ✅ Authentication System
- [x] Đăng ký tài khoản mới
- [x] Đăng nhập với username/email
- [x] Mã hóa mật khẩu (BCrypt)
- [x] Validation form đăng ký/đăng nhập

### ✅ User Management
- [x] Tạo, đọc, cập nhật, xóa user
- [x] Quản lý role (USER, ADMIN, SELLER)
- [x] Soft delete (không xóa vĩnh viễn)

### ✅ UI/UX
- [x] Responsive design với Bootstrap
- [x] Dark theme design
- [x] Form validation với feedback

## 🔧 Tính năng cần phát triển

### 🚧 Chưa implement
- [ ] Spring Security configuration
- [ ] Session management
- [ ] Product management
- [ ] Shopping cart
- [ ] Order processing
- [ ] Payment integration (VNPay)
- [ ] Admin panel
- [ ] Chat system

## 📝 Hướng dẫn phát triển

### Thêm entity mới
1. Tạo entity trong package `entity`
2. Extend từ `BaseEntity` để có audit fields
3. Tạo repository tương ứng
4. Tạo service interface và implementation

### Thêm trang web mới
1. Tạo controller trong package `controller`
2. Tạo HTML template trong `templates`
3. Sử dụng fragments trong `Included` cho header/footer

### Database Migration
- Sử dụng `spring.jpa.hibernate.ddl-auto=update` cho development
- Production nên sử dụng `validate` và migration tools

## 🐛 Troubleshooting

### Lỗi thường gặp
1. **Bean not found**: Kiểm tra @Service, @Repository annotations
2. **Database connection**: Kiểm tra MySQL service và credentials
3. **Port 8080 occupied**: Thay đổi `server.port` trong application.properties

### Logs
- Application logs: Console output
- SQL logs: Enabled trong application.properties
- Error details: Check browser developer tools

## 👥 Team Members
- [Thêm tên thành viên team]

## 📄 License
Educational purpose only - FPT University
