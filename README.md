# EatAndOrder – Mobile Application (Android + Java)

Ứng dụng Android phục vụ bài tập lớn môn Lập trình Mobile.  
App mô phỏng hệ thống đặt đồ ăn, với các chức năng:

- Đăng ký tài khoản (OTP 6 số – demo: 123456)
- Đăng nhập bằng username/password
- Lấy danh sách Category từ API
- Lấy danh sách Product theo Category
- Hiển thị thông tin người dùng sau khi đăng nhập
- Điều hướng bằng Bottom Navigation
- Kết nối trực tiếp với API Spring Boot: **shop-api**

---

# 1. Công nghệ sử dụng

- **Android Studio Ladybug | Java**
- **Retrofit2** – gọi API
- **Gson** – parse JSON
- **ViewBinding**
- **RecyclerView**
- **SharedPreferences** (lưu token + user info)
- API server: **Spring Boot (shop-api)** chạy tại `http://10.0.2.2:8081/`

---

# 2. Cấu trúc thư mục chính

```text
EatAndOrder
├── app
│   ├── src/main/java/vn/hcmute/eatandorder
│   │   ├── data
│   │   │   ├── api
│   │   │   │   ├── ApiService.java
│   │   │   │   ├── RetrofitClient.java
│   │   │   └── model
│   │   │       ├── Category.java
│   │   │       ├── Product.java
│   │   │       ├── LoginRequest.java
│   │   │       ├── LoginResponse.java
│   │   │       └── RegisterRequest.java
│   │   ├── ui
│   │   │   ├── intro
│   │   │   ├── login
│   │   │   ├── register
│   │   │   ├── otp
│   │   │   ├── main
│   │   │   │   ├── MainActivity.java
│   │   │   │   ├── CategoryAdapter.java
│   │   │   │   └── ProductAdapter.java
│   │   │   └── profile
│   │   └── util
│   │       └── PrefManager.java
│   ├── res/layout
│   │   ├── activity_intro.xml
│   │   ├── activity_login.xml
│   │   ├── activity_register.xml
│   │   ├── activity_otp.xml
│   │   ├── activity_main.xml
│   │   ├── item_category.xml
│   │   └── item_product.xml
│   └── AndroidManifest.xml
└── README.md
```

---

# 3. Hướng dẫn cài đặt & chạy ứng dụng

## 3.1. Clone project
```
git clone https://github.com/YueLouis/giuaki.git
cd giuaki
```

## 3.2. Cấu hình API base URL

Trong RetrofitClient.java:

```
private static final String BASE_URL = "http://10.0.2.2:8081/";
```

10.0.2.2 = địa chỉ truy cập localhost của máy thật từ Android Emulator.

## 3.3. Cấp quyền Internet

Trong AndroidManifest.xml:

```
<uses-permission android:name="android.permission.INTERNET" />

<application
    android:usesCleartextTraffic="true"
    ... >
```

## 3.4. Chạy app

Open project bằng Android Studio → chọn emulator → bấm Run ▶.

Ứng dụng sẽ:

Mở màn hình Intro → Login → nếu chưa có tài khoản → Register → OTP

Sau khi Login thành công → chuyển sang MainActivity

Gọi API để lấy Category & Product

---

# 4. Chức năng chính trong bài

## 4.1. Đăng ký tài khoản (Register API)

Body gửi đến API:

{
  "username": "tin123",
  "password": "123456",
  "otp": "123456"
}


OTP demo cố định = 123456.

## 4.2. Đăng nhập (Login API)

Ví dụ body:

{
  "username": "tin123",
  "password": "123456"
}


Nhận về token JWT → lưu SharedPreferences.

## 4.3. Lấy danh sách Categories
apiService.getCategories().enqueue(...)


Hiển thị bằng RecyclerView dạng ngang.

## 4.4. Lấy danh sách Product theo Category
@GET("api/categories/{id}/products")
Call<List<Product>> getProducts(@Path("id") int id);

## 4.5. Hiển thị thông tin user sau đăng nhập

Tên user được lấy từ LoginResponse và lưu bằng:

PrefManager.saveUserName(...)

## 4.6. Bottom Navigation

Điều hướng:

Home

Orders (demo)

Profile

---

# 5. API server yêu cầu để chạy app

App cần backend chạy độc lập:

👉 Repo: https://github.com/YueLouis/shop-api

👉 Port backend: 8081

Run backend bằng:

gradlew bootRun


Hoặc run class ShopApiApplication.java.

# 6. Minh hoạ giao diện (thêm hình sau)

```
docs/screenshots/
├── intro.png
├── login.png
├── register.png
├── main_categories.png
├── products_by_category.png
└── profile.png
```

---

# 7. Quy trình thực hiện bài (tóm tắt)

Thiết kế UI: Intro → Login → Register → OTP → Main

Cài Retrofit + tạo RetrofitClient

Tạo ApiService → mapping các endpoint:

```
/auth/register

/auth/login

/api/categories

/api/categories/{id}/products
```

Model hoá Category, Product, User

Hoàn thiện màn hình Main với RecyclerView

Gọi API thật từ Spring Boot

Lưu token, username vào SharedPreferences

Hoàn thiện Bottom Navigation

Kiểm thử toàn bộ flow → chụp screenshot → đưa vào README

---

# 8. Ghi chú

App chỉ chạy với backend shop-api chạy tại 10.0.2.2:8081.

Vì dùng H2 database nên backend reset dữ liệu mỗi lần restart.

Các API trong bài được đơn giản hoá phục vụ mục tiêu demo.
