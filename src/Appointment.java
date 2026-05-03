import java.time.LocalDateTime;
import java.time.Duration;

/**
 * Entity: Cuộc hẹn trong lịch
 */
public class Appointment {
    private static int nextId = 1;
    private int appointmentId;
    private String name;
    private String location;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public Appointment(String name, String location, LocalDateTime startTime, LocalDateTime endTime) {
        this.appointmentId = nextId++;
        this.name = name;
        this.location = location;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getAppointmentId() { return appointmentId; }
    public String getName() { return name; }
    public String getLocation() { return location; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }

    /** Tính thời lượng cuộc hẹn (phút) */
    public int getDuration() {
        return (int) Duration.between(startTime, endTime).toMinutes();
    }

    /** Kiểm tra cuộc hẹn hợp lệ: tên không trống, thời lượng dương */
    public boolean isValid() {
        return name != null && !name.trim().isEmpty() && getDuration() > 0;
    }

    /** Kiểm tra có trùng thời gian với cuộc hẹn khác không */
    public boolean conflictsWith(Appointment other) {
        // Hai khoảng thời gian trùng nếu: start1 < end2 AND start2 < end1
        return this.startTime.isBefore(other.endTime) && other.startTime.isBefore(this.endTime);
    }

    @Override
    public String toString() {
        return name + " @ " + location + " (" + startTime.toLocalTime() + " - " + endTime.toLocalTime() + ")";
    }
}
