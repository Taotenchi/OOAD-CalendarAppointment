import java.time.LocalDateTime;

/**
 * Control: Điều khiển logic thêm cuộc hẹn
 * Theo đúng thuật toán trong Sequence Diagram (Bước 7-24)
 */
public class AppointmentController {
    private CalendarModel calendar;
    private User currentUser;
    private CalendarUI calendarUI;

    public AppointmentController(CalendarModel calendar, User currentUser) {
        this.calendar = calendar;
        this.currentUser = currentUser;
    }

    /** Gắn CalendarUI để controller có thể callback hiển thị cảnh báo */
    public void setCalendarUI(CalendarUI calendarUI) {
        this.calendarUI = calendarUI;
    }

    public CalendarModel getCalendar() { return calendar; }
    public User getCurrentUser() { return currentUser; }

    /**
     * THUẬT TOÁN CHÍNH: Thêm cuộc hẹn (Bước 7 trong Sequence)
     * 
     * @param name         Tên cuộc hẹn
     * @param location     Địa điểm
     * @param startTime    Thời gian bắt đầu
     * @param endTime      Thời gian kết thúc
     * @param addReminder  Có thêm lời nhắc không
     * @return true nếu thêm thành công, false nếu thất bại
     */
    public boolean addAppointment(String name, String location,
                                   LocalDateTime startTime, LocalDateTime endTime,
                                   boolean addReminder) {

        // ===== BƯỚC 8: Tạo đối tượng Appointment trong bộ nhớ (bản nháp) =====
        Appointment newApt = new Appointment(name, location, startTime, endTime);

        // ===== BƯỚC 9: Kiểm tra trùng lịch =====
        Appointment conflict = calendar.checkConflict(newApt);

        // ===== BƯỚC 10-13: ALT - Xử lý trùng lịch =====
        if (conflict != null) {
            // Bước 10: Hiển thị cảnh báo trùng lịch
            // Bước 11: User chọn hành động
            int choice = calendarUI.showConflictWarning(conflict);

            if (choice == CalendarUI.REPLACE) {
                // Bước 13: Thay thế - xóa cuộc hẹn cũ
                calendar.removeAppointment(conflict);
                currentUser.removeAppointment(conflict);
            } else {
                // User chọn đổi giờ → hủy thao tác, quay lại
                return false;
            }
        }

        // ===== BƯỚC 14: Tìm cuộc họp nhóm trùng tên và thời lượng =====
        GroupMeeting matchingMeeting = calendar.findMatchingGroupMeeting(name, newApt.getDuration());

        // ===== BƯỚC 15-18: ALT - Xử lý cuộc họp nhóm =====
        if (matchingMeeting != null) {
            // Bước 15: Hỏi user có muốn tham gia cuộc họp nhóm không
            // Bước 16: User xác nhận
            boolean joinMeeting = calendarUI.showGroupMeetingPrompt(matchingMeeting);

            if (joinMeeting) {
                // Bước 18: Thêm user vào danh sách người tham gia
                matchingMeeting.addParticipant(currentUser);
                // Bản nháp appointment bị BỎ ĐI, không lưu
                // Bước 24: Refresh giao diện
                calendarUI.refreshView();
                return true;
            }
        }

        // ===== BƯỚC 19: Lưu cuộc hẹn vào Calendar =====
        calendar.addAppointment(newApt);

        // ===== BƯỚC 20: Lưu cuộc hẹn vào danh sách của User =====
        currentUser.addAppointment(newApt);

        // ===== BƯỚC 21-22: OPT - Thêm lời nhắc nếu user chọn =====
        if (addReminder) {
            // Bước 21: Tạo Reminder mới (nhắc trước 15 phút)
            LocalDateTime reminderTime = startTime.minusMinutes(15);
            Reminder reminder = new Reminder(newApt, reminderTime,
                    "Nhắc nhở: " + name + " sắp bắt đầu lúc " + startTime.toLocalTime());

            // Bước 22: Thêm reminder vào danh sách của User
            currentUser.addReminder(reminder);
        }

        // ===== BƯỚC 23: Trả về success =====
        // ===== BƯỚC 24: Refresh giao diện =====
        calendarUI.refreshView();
        return true;
    }
}
