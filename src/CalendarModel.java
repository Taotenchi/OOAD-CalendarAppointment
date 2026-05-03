import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Entity: Lịch - quản lý tất cả cuộc hẹn và cuộc họp nhóm
 * Đặt tên CalendarModel để tránh trùng với java.util.Calendar
 */
public class CalendarModel {
    private User owner;
    private List<Appointment> appointments;
    private List<GroupMeeting> groupMeetings;

    public CalendarModel(User owner) {
        this.owner = owner;
        this.appointments = new ArrayList<>();
        this.groupMeetings = new ArrayList<>();
    }

    public User getOwner() { return owner; }

    /** Thêm cuộc hẹn vào lịch */
    public boolean addAppointment(Appointment apt) {
        return appointments.add(apt);
    }

    /** Xóa cuộc hẹn khỏi lịch */
    public boolean removeAppointment(Appointment apt) {
        return appointments.remove(apt);
    }

    /** Lấy tất cả cuộc hẹn trong một ngày */
    public List<Appointment> getAppointmentsForDate(LocalDate date) {
        return appointments.stream()
                .filter(a -> a.getStartTime().toLocalDate().equals(date))
                .collect(Collectors.toList());
    }

    /** Lấy tất cả cuộc hẹn */
    public List<Appointment> getAllAppointments() {
        return new ArrayList<>(appointments);
    }

    /**
     * Bước 9 trong Sequence: Kiểm tra trùng lịch
     * Trả về cuộc hẹn bị trùng (nếu có), null nếu không trùng
     */
    public Appointment checkConflict(Appointment apt) {
        for (Appointment existing : appointments) {
            if (existing.conflictsWith(apt)) {
                return existing;
            }
        }
        return null;
    }

    /**
     * Bước 14 trong Sequence: Tìm cuộc họp nhóm trùng tên và thời lượng
     * Trả về GroupMeeting trùng khớp (nếu có), null nếu không
     */
    public GroupMeeting findMatchingGroupMeeting(String name, int duration) {
        for (GroupMeeting gm : groupMeetings) {
            if (gm.matchesNameAndDuration(name, duration)) {
                return gm;
            }
        }
        return null;
    }

    /** Thêm cuộc họp nhóm (dùng để tạo dữ liệu mẫu) */
    public void addGroupMeeting(GroupMeeting gm) {
        groupMeetings.add(gm);
    }

    /** Lấy danh sách cuộc họp nhóm */
    public List<GroupMeeting> getGroupMeetings() {
        return new ArrayList<>(groupMeetings);
    }
}
