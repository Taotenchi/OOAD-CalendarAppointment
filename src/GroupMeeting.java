import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity: Cuộc họp nhóm - kế thừa từ Appointment
 * Thêm danh sách người tham gia (participants)
 */
public class GroupMeeting extends Appointment {
    private int meetingId;
    private List<User> participants;
    private static int nextMeetingId = 1;

    public GroupMeeting(String name, String location, LocalDateTime startTime, LocalDateTime endTime) {
        super(name, location, startTime, endTime);
        this.meetingId = nextMeetingId++;
        this.participants = new ArrayList<>();
    }

    public int getMeetingId() { return meetingId; }

    /** Thêm người tham gia vào cuộc họp nhóm */
    public void addParticipant(User user) {
        if (!participants.contains(user)) {
            participants.add(user);
        }
    }

    /** Xóa người tham gia */
    public void removeParticipant(User user) {
        participants.remove(user);
    }

    /** Lấy danh sách người tham gia */
    public List<User> getParticipants() {
        return new ArrayList<>(participants);
    }

    /** Kiểm tra cuộc hẹn có trùng tên và thời lượng với group meeting này không */
    public boolean matchesNameAndDuration(String name, int duration) {
        return this.getName().equalsIgnoreCase(name) && this.getDuration() == duration;
    }

    @Override
    public String toString() {
        return "[Group] " + super.toString() + " - " + participants.size() + " participants";
    }
}
