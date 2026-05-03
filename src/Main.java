import javax.swing.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Main: Khởi chạy ứng dụng Calendar
 * Tạo dữ liệu mẫu để demo các tình huống trong Sequence Diagram
 */
public class Main {
    public static void main(String[] args) {
        // Sử dụng Look and Feel đẹp hơn
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            // Fallback to default
        }

        SwingUtilities.invokeLater(() -> {
            // ===== Tạo dữ liệu mẫu =====

            // Tạo User hiện tại
            User currentUser = new User(1, "Nguyen Van A", "nguyenvana@email.com");

            // Tạo Calendar cho user
            CalendarModel calendar = new CalendarModel(currentUser);

            // Tạo vài cuộc hẹn có sẵn (để demo tình huống trùng lịch)
            LocalDate today = LocalDate.now();

            Appointment apt1 = new Appointment("Học OOAD", "Phòng 301",
                    LocalDateTime.of(today, LocalTime.of(8, 0)),
                    LocalDateTime.of(today, LocalTime.of(10, 0)));
            calendar.addAppointment(apt1);
            currentUser.addAppointment(apt1);

            Appointment apt2 = new Appointment("Ăn trưa", "Canteen",
                    LocalDateTime.of(today, LocalTime.of(11, 30)),
                    LocalDateTime.of(today, LocalTime.of(12, 30)));
            calendar.addAppointment(apt2);
            currentUser.addAppointment(apt2);

            Appointment apt3 = new Appointment("Học Tiếng Anh", "Phòng 205",
                    LocalDateTime.of(today, LocalTime.of(14, 0)),
                    LocalDateTime.of(today, LocalTime.of(16, 0)));
            calendar.addAppointment(apt3);
            currentUser.addAppointment(apt3);

            // Tạo cuộc họp nhóm có sẵn (để demo tình huống trùng group meeting)
            // GroupMeeting "Họp nhóm OOAD" 2 tiếng, có 2 người tham gia
            User member1 = new User(2, "Tran Van B", "tranvanb@email.com");
            User member2 = new User(3, "Le Thi C", "lethic@email.com");

            GroupMeeting groupMeeting = new GroupMeeting("Họp nhóm OOAD", "Phòng Lab",
                    LocalDateTime.of(today, LocalTime.of(18, 0)),
                    LocalDateTime.of(today, LocalTime.of(20, 0)));
            groupMeeting.addParticipant(member1);
            groupMeeting.addParticipant(member2);
            calendar.addGroupMeeting(groupMeeting);

            // ===== Khởi tạo Controller và UI =====
            AppointmentController controller = new AppointmentController(calendar, currentUser);
            CalendarUI ui = new CalendarUI(controller);
            ui.setVisible(true);

            // ===== Hướng dẫn demo =====
            System.out.println("=== CALENDAR APP - DEMO GUIDE ===");
            System.out.println();
            System.out.println("Du lieu mau da tao:");
            System.out.println("  - 3 cuoc hen hom nay (8-10h, 11:30-12:30, 14-16h)");
            System.out.println("  - 1 cuoc hop nhom 'Hop nhom OOAD' (18-20h, 120 phut)");
            System.out.println();
            System.out.println("Cach demo:");
            System.out.println("  1. BINH THUONG: Them cuoc hen 'Di cafe' luc 17:00-18:00");
            System.out.println("  2. TRUNG LICH:  Them cuoc hen luc 9:00-10:00 (trung voi 'Hoc OOAD')");
            System.out.println("  3. GROUP MEETING: Them cuoc hen ten 'Hop nhom OOAD', 120 phut (18:00-20:00)");
            System.out.println("  4. VALIDATION: Them cuoc hen ten trong hoac gio ket thuc truoc gio bat dau");
        });
    }
}
