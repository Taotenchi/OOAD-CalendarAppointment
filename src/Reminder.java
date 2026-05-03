import java.time.LocalDateTime;

/**
 * Entity: Lời nhắc cho cuộc hẹn
 */
public class Reminder {
    private static int nextId = 1;
    private int reminderId;
    private Appointment appointment;
    private LocalDateTime reminderTime;
    private String message;

    public Reminder(Appointment appointment, LocalDateTime reminderTime, String message) {
        this.reminderId = nextId++;
        this.appointment = appointment;
        this.reminderTime = reminderTime;
        this.message = message;
    }

    public int getReminderId() { return reminderId; }
    public Appointment getAppointment() { return appointment; }
    public LocalDateTime getReminderTime() { return reminderTime; }
    public String getMessage() { return message; }

    /** Kích hoạt lời nhắc */
    public void trigger() {
        System.out.println("REMINDER: " + message + " at " + reminderTime);
    }

    @Override
    public String toString() {
        return "Reminder: " + message + " (" + reminderTime.toLocalTime() + ")";
    }
}
