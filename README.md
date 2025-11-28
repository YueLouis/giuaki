📱 EatAndOrder – Mobile Application (Android + Spring Boot API)

Ứng dụng Android dành cho bài tập lớn môn lập trình di động.
App kết nối với backend Spring Boot (shop-api) để thực hiện:

Đăng ký (OTP demo 6 số)

Đăng nhập bằng username/password

Lấy danh sách Categories

Lấy danh sách Product theo Category

Hiển thị thông tin người dùng sau khi đăng nhập

Điều hướng bằng Bottom Navigation

1. Công nghệ sử dụng

Android Studio Giraffe / Ladybug

Java (hoặc Kotlin — nhóm dùng Java)

Retrofit2 + OkHttp – gọi API

Gson – parse JSON

ViewBinding

RecyclerView – hiển thị danh sách

Spring Boot shop-api chạy cổng 8081 – backend của dự án

2. Cấu trúc project chính
EatAndOrder/
├── app/
│   └── src/main/java/vn/hcmute/eatandorder
│       ├── ui
│       │   ├── intro
│       │   ├── login
│       │   ├── register
│       │   ├── main
│       │   └── product
│       ├── data
│       │   ├── api
│       │   │   ├── ApiService.java
│       │   │   ├── RetrofitClient.java
│       │   └── model
│       │       ├── Category.java
│       │       └── Product.java
│       └── util
│           └── PrefManager.java
│
└── res/
    ├── layout/
    ├── drawable/
    └── mipmap/

3. Kết nối API (Retrofit)

File cấu hình chính:

public class RetrofitClient {
    private static final String BASE_URL = "http://10.0.2.2:8081/api/";

    private static Retrofit retrofit;

    public static ApiService getApiService() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(ApiService.class);
    }
}


Ghi chú:

10.0.2.2 = localhost của máy Windows khi chạy emulator

shop-api phải chạy trước bằng Spring Boot

4. Các màn hình chính
4.1. Intro → Login → Register

Nếu chưa có tài khoản → đi tới màn Register

OTP demo: 123456

Sau khi đăng ký thành công quay lại Login

4.2. Trang Main

Hiển thị lời chào người dùng

Lấy danh sách category từ API

Hiển thị ngang bằng RecyclerView

4.3. Trang Product theo Category

Gọi API:

GET /api/categories/{id}/products


Hiển thị danh sách sản phẩm theo category.

4.4. Bottom Navigation

Có 3 tab (ví dụ):

Home

Profile

Settings

5. Giao tiếp API (ví dụ)
Lấy toàn bộ Categories
apiService.getCategories().enqueue(new Callback<List<Category>>() {
    @Override
    public void onResponse(Call<List<Category>> call, Response<List<Category>> response) {
        List<Category> list = response.body();
        adapter = new CategoryAdapter(list);
        binding.rvCategory.setAdapter(adapter);
    }

    @Override
    public void onFailure(Call<List<Category>> call, Throwable t) {
        Toast.makeText(MainActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
    }
});

6. Một số màn hình minh họa

(Em chỉ cần thêm folder docs/screenshots/ và bỏ ảnh vào)
README tự hiển thị đẹp

![Login](docs/screenshots/login.png)
![Register](docs/screenshots/register.png)
![Main](docs/screenshots/main.png)
![Category](docs/screenshots/category.png)
![Product](docs/screenshots/product.png)

7. Quy trình chạy project
7.1. Chạy backend (shop-api)
git clone https://github.com/YueLouis/shop-api.git
cd shop-api
./gradlew bootRun  # hoặc Run ShopApiApplication trong Android Studio


Server chạy tại:

http://localhost:8081/api

7.2. Chạy app EatAndOrder

Mở thư mục EatAndOrder bằng Android Studio

Bật Internet permission

<uses-permission android:name="android.permission.INTERNET" />
<application android:usesCleartextTraffic="true">


Run emulator

Mở app

Đăng ký → OTP 123456 → Đăng nhập → Main → Load category

8. Kết quả hoàn thành

Nhóm đã hoàn thiện các yêu cầu:

✔ Intro → Login → Register + OTP

✔ Đăng nhập username/password

✔ Trang Main hiển thị category

✔ Xem danh sách product theo category

✔ Lấy API từ shop-api

✔ Bottom navigation

✔ Lưu trạng thái user bằng SharedPreferences

✔ Up 2 repo GitHub riêng:

Backend: https://github.com/YueLouis/shop-api

Mobile: https://github.com/YueLouis/giuaki

9. Ghi chú

Nếu emulator không kết nối API → kiểm tra:

Backend có chạy chưa?

Base URL có đúng 10.0.2.2 chưa?

Có bật usesCleartextTraffic chưa?

Nếu đổi wifi / IP → backend phải chạy lại đúng cổng 8081.

10. License

MIT License – dùng cho mục đích học tập.