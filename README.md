# Vua Hùng Kén Rể

> **Game đối kháng 2D dựa trên truyền thuyết dân gian Việt Nam "Sơn Tinh - Thủy Tinh".**

## Giới thiệu

**Vua Hùng Kén Rể** là một tựa game đối kháng 2D được xây dựng bằng ngôn ngữ Java, thuộc khuôn khổ bài tập lớn học phần **IT3100 - Lập trình hướng đối tượng**.

Trò chơi tái hiện lại trận chiến hào hùng và kịch tính giữa **Sơn Tinh** và **Thủy Tinh** để giành lấy trái tim của công chúa **Mị Nương**. Lấy cảm hứng từ lối chơi kinh điển của tựa game **Bleach Vs Naruto**, người chơi sẽ nhập vai vào nhân vật mình chọn, sử dụng các chiêu thức võ thuật, kỹ năng triệu hồi và tuyệt kỹ mạnh mẽ để đánh bại đối thủ trong một trận đấu đối kháng.

## Cài đặt & Hướng dẫn chạy

### Yêu cầu hệ thống

- **Java Runtime Environment (JRE):** Khuyến nghị phiên bản 8 trở lên.
- **Java Development Kit (JDK):** Khuyến nghị phiên bản 8 trở lên.
- **IDE (Tùy chọn):** IntelliJ IDEA, Eclipse hoặc NetBeans.

### Cách chạy game

#### Chạy từ mã nguồn

1.  **Tải mã nguồn:**

    ```bash
    git clone https://github.com/phucmap85/IT3100_VuaHungKenRe.git
    ```

2.  **Truy cập vào thư mục mã nguồn:**

    ```bash
    cd IT3100_VuaHungKenRe/src
    ```

3.  **Biên dịch:**

    ```bash
    javac main/Main.java
    ```

4.  **Chạy game:**
    ```bash
    java main.Main
    ```

#### Chạy từ file

1.  **Tải game:** Truy cập mục **Releases** trên repository này và tải file `VuaHungKenRe.jar` về máy.
2.  **Mở Terminal:** Mở Command Prompt (Windows) hoặc Terminal (macOS/Linux) tại thư mục chứa file vừa tải.
3.  **Chạy lệnh:**

    ```bash
    java -jar VuaHungKenRe.jar
    ```

## Cấu trúc Dự án

```
src/
├── entity/       # Xử lý thực thể nhân vật và các kỹ năng
├── gamestates/   # Quản lý trạng thái game (Menu, Đang chơi, Chọn map...)
├── image/        # Tài nguyên hình ảnh
├── inputs/       # Xử lý đầu vào từ bàn phím và chuột
├── main/         # Class chính (Main) và vòng lặp game (Game Loop)
├── map/          # Vẽ bản đồ và xử lý logic nền
├── sound/        # Hiệu ứng âm thanh và nhạc nền
├── ui/           # Các thành phần giao diện (Nút bấm, Thanh máu...)
└── utilz/        # Các hằng số và hàm tiện ích hỗ trợ
```

## Tác giả

- Lê Quang Phúc (202416316)
- Hồ Sỹ Toàn (202416368)
- Nguyễn Sỹ Thành (202416351)
- Nguyễn Nhật Minh (202416291)
- Hà Tiến Khiêm (202416246)
