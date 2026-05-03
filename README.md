# Add Calendar Appointment - OOAD Project

## Mô tả
Ứng dụng Java Swing thực hiện chức năng **Add Calendar Appointment**, phát triển theo mô hình BCE (Boundary-Control-Entity) và tuân thủ thuật toán được trình bày trong Sequence Diagram.

## Chức năng
- Xem danh sách cuộc hẹn theo ngày
- Thêm cuộc hẹn mới (Add Calendar Appointment)
- Kiểm tra trùng lịch (Conflict Detection)
- Phát hiện cuộc họp nhóm trùng khớp (Group Meeting Matching)
- Thêm lời nhắc (Reminder)

## Kiến trúc BCE

| Tầng | Lớp | Vai trò |
|------|-----|---------|
| **Boundary** | `CalendarUI` | Giao diện chính |
| **Boundary** | `AddAppointmentDialog` | Dialog thêm cuộc hẹn |
| **Control** | `AppointmentController` | Logic xử lý chính |
| **Entity** | `Appointment` | Cuộc hẹn |
| **Entity** | `GroupMeeting` | Cuộc họp nhóm (kế thừa Appointment) |
| **Entity** | `Reminder` | Lời nhắc |
| **Entity** | `User` | Người dùng |
| **Entity** | `CalendarModel` | Quản lý lịch |

## Cách chạy

### Yêu cầu
- Java JDK 8 trở lên

### Biên dịch và chạy
```bash
cd src
javac -encoding UTF-8 *.java
java Main
```

### Hướng dẫn demo
1. **Thêm bình thường**: Thêm cuộc hẹn "Đi cafe" lúc 17:00-18:00
2. **Trùng lịch**: Thêm cuộc hẹn lúc 9:00-10:00 (trùng với "Học OOAD")
3. **Group Meeting**: Thêm cuộc hẹn tên "Họp nhóm OOAD", 120 phút (18:00-20:00)
4. **Validation**: Thêm cuộc hẹn tên trống hoặc giờ kết thúc trước giờ bắt đầu

## Sơ đồ
- `ClassDiagram_AddCalendarAppointment.xml` - Mở bằng [draw.io](https://app.diagrams.net)
- `SequenceDiagram_AddCalendarAppointment.xml` - Mở bằng [draw.io](https://app.diagrams.net)

## Thành viên nhóm
- Nguyễn Tấn Quốc
- Nguyễn Nhật Anh Hào
- Phạm Ngọc Hiệp
