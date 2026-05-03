import java.util.ArrayList;
import java.util.List;

/**
 * Entity: Người dùng
 */
public class User {
    private int userId;
    private String name;
    private String email;
    private List<Appointment> appointments;
    private List<Reminder> reminders;

    public User(int userId, String name, String email) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.appointments = new ArrayList<>();
        this.reminders = new ArrayList<>();
    }

    public int getUserId() { return userId; }
    public String getName() { return name; }
    public String getEmail() { return email; }

    /** Lấy danh sách cuộc hẹn */
    public List<Appointment> getAppointments() {
        return new ArrayList<>(appointments);
    }

    /** Thêm cuộc hẹn vào danh sách của user */
    public void addAppointment(Appointment apt) {
        appointments.add(apt);
    }

    /** Xóa cuộc hẹn khỏi danh sách */
    public void removeAppointment(Appointment apt) {
        appointments.remove(apt);
    }

    /** Lấy danh sách lời nhắc */
    public List<Reminder> getReminders() {
        return new ArrayList<>(reminders);
    }

    /** Thêm lời nhắc */
    public void addReminder(Reminder reminder) {
        reminders.add(reminder);
    }

    @Override
    public String toString() {
        return name + " (" + email + ")";
    }
}
