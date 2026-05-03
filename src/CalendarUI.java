import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Boundary: Giao diện chính hiển thị lịch và danh sách cuộc hẹn
 * Bước 1, 10-11, 15-16, 23-24 trong Sequence Diagram
 */
public class CalendarUI extends JFrame {
    public static final int REPLACE = 0;
    public static final int FREE_TIME = 1;

    private LocalDate activeDate;
    private LocalTime activeTime;

    private AppointmentController controller;
    private DefaultTableModel tableModel;
    private JTable appointmentTable;
    private JLabel statusLabel;
    private JLabel dateDisplayLabel;

    public CalendarUI(AppointmentController controller) {
        this.controller = controller;
        this.activeDate = LocalDate.now();
        this.activeTime = LocalTime.now().withSecond(0).withNano(0);
        controller.setCalendarUI(this);

        setTitle("Calendar Application - Add Calendar Appointment");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        initUI();
        displayCalendar();
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBackground(new Color(240, 242, 248));

        // === HEADER ===
        JPanel headerPanel = new JPanel(new BorderLayout(15, 0));
        headerPanel.setBackground(new Color(55, 65, 120));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("📅 Calendar Appointments");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        userPanel.setOpaque(false);
        JLabel userLabel = new JLabel("👤 " + controller.getCurrentUser().getName());
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        userLabel.setForeground(new Color(200, 210, 255));
        userPanel.add(userLabel);
        headerPanel.add(userPanel, BorderLayout.EAST);

        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // === CENTER: Date selector + Table ===
        JPanel centerPanel = new JPanel(new BorderLayout(0, 10));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Date navigation
        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        datePanel.setOpaque(false);

        JButton prevBtn = new JButton("◀ Hôm trước");
        prevBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        prevBtn.addActionListener(e -> { activeDate = activeDate.minusDays(1); displayCalendar(); });

        dateDisplayLabel = new JLabel();
        dateDisplayLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        dateDisplayLabel.setForeground(new Color(55, 65, 120));

        JButton nextBtn = new JButton("Hôm sau ▶");
        nextBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        nextBtn.addActionListener(e -> { activeDate = activeDate.plusDays(1); displayCalendar(); });

        JButton todayBtn = new JButton("Hôm nay");
        todayBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        todayBtn.setBackground(new Color(70, 130, 200));
        todayBtn.setForeground(Color.WHITE);
        todayBtn.addActionListener(e -> { activeDate = LocalDate.now(); displayCalendar(); });

        datePanel.add(prevBtn);
        datePanel.add(dateDisplayLabel);
        datePanel.add(nextBtn);
        datePanel.add(Box.createHorizontalStrut(20));
        datePanel.add(todayBtn);

        centerPanel.add(datePanel, BorderLayout.NORTH);

        // Appointment Table
        String[] columns = {"ID", "Tên cuộc hẹn", "Địa điểm", "Bắt đầu", "Kết thúc", "Thời lượng (phút)"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        appointmentTable = new JTable(tableModel);
        appointmentTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        appointmentTable.setRowHeight(30);
        appointmentTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        appointmentTable.getTableHeader().setBackground(new Color(70, 80, 140));
        appointmentTable.getTableHeader().setForeground(Color.WHITE);
        appointmentTable.setSelectionBackground(new Color(200, 215, 255));

        // Center align columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < columns.length; i++) {
            appointmentTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        appointmentTable.getColumnModel().getColumn(0).setPreferredWidth(40);
        appointmentTable.getColumnModel().getColumn(1).setPreferredWidth(200);

        JScrollPane scrollPane = new JScrollPane(appointmentTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 220)));
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // === BOTTOM: Buttons + Status ===
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 0));
        bottomPanel.setBackground(new Color(240, 242, 248));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 15, 20));

        statusLabel = new JLabel("Sẵn sàng");
        statusLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        statusLabel.setForeground(new Color(100, 100, 130));
        bottomPanel.add(statusLabel, BorderLayout.WEST);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        btnPanel.setOpaque(false);

        JButton addBtn = new JButton("➕ Add Appointment");
        addBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        addBtn.setBackground(new Color(50, 160, 80));
        addBtn.setForeground(Color.WHITE);
        addBtn.setFocusPainted(false);
        addBtn.setPreferredSize(new Dimension(200, 40));
        // Bước 1: User bấm nút Add Appointment
        addBtn.addActionListener(e -> openAddAppointmentDialog());

        JButton viewRemindersBtn = new JButton("🔔 Xem lời nhắc");
        viewRemindersBtn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        viewRemindersBtn.addActionListener(e -> showReminders());

        btnPanel.add(viewRemindersBtn);
        btnPanel.add(addBtn);
        bottomPanel.add(btnPanel, BorderLayout.EAST);

        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        setContentPane(mainPanel);
    }

    /** Hiển thị lịch và danh sách cuộc hẹn */
    public void displayCalendar() {
        dateDisplayLabel.setText("📆 " + activeDate.format(DateTimeFormatter.ofPattern("EEEE, dd/MM/yyyy")));
        showAppointments(activeDate);
    }

    /** Hiển thị cuộc hẹn theo ngày */
    public void showAppointments(LocalDate date) {
        tableModel.setRowCount(0);
        DateTimeFormatter timeFmt = DateTimeFormatter.ofPattern("HH:mm");

        // Hiển thị tất cả cuộc hẹn (để demo)
        List<Appointment> allApts = controller.getCalendar().getAllAppointments();
        List<Appointment> dateApts = controller.getCalendar().getAppointmentsForDate(date);

        // Hiển thị cuộc hẹn của ngày được chọn
        for (Appointment apt : dateApts) {
            tableModel.addRow(new Object[]{
                    apt.getAppointmentId(),
                    apt.getName(),
                    apt.getLocation(),
                    apt.getStartTime().format(timeFmt),
                    apt.getEndTime().format(timeFmt),
                    apt.getDuration()
            });
        }

        statusLabel.setText("Ngày " + date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                + " - " + dateApts.size() + " cuộc hẹn"
                + " | Tổng: " + allApts.size() + " cuộc hẹn");
    }

    /**
     * Bước 1-2: Mở dialog thêm cuộc hẹn
     * Nếu trùng lịch và user chọn "Giờ khác" → mở lại dialog với dữ liệu cũ
     */
    public void openAddAppointmentDialog() {
        activeTime = LocalTime.now().withSecond(0).withNano(0);

        // Lưu dữ liệu cũ để điền lại nếu cần mở lại dialog
        String prevName = "";
        String prevLocation = "";
        boolean prevReminder = false;
        boolean isRetry = false;

        while (true) {
            // Bước 2: <<create>> AddAppointmentDialog
            AddAppointmentDialog dialog = new AddAppointmentDialog(this, activeDate, activeTime);

            // Nếu đang thử lại → điền sẵn dữ liệu cũ (chỉ cần sửa giờ)
            if (isRetry) {
                dialog.prefill(prevName, prevLocation, prevReminder);
            }

            dialog.setVisible(true); // Modal - đợi đến khi dialog đóng

            // Nếu user bấm Hủy → thoát hoàn toàn
            if (!dialog.isConfirmed()) {
                return;
            }

            // Bước 6: Nhận appointmentData
            Appointment apt = dialog.getAppointmentData();
            boolean reminderSelected = dialog.isReminderSelected();

            // Lưu dữ liệu để nếu cần mở lại, sẽ điền sẵn
            prevName = apt.getName();
            prevLocation = apt.getLocation();
            prevReminder = reminderSelected;

            // Bước 7: Gửi cho Controller xử lý
            boolean success = controller.addAppointment(
                    apt.getName(),
                    apt.getLocation(),
                    apt.getStartTime(),
                    apt.getEndTime(),
                    reminderSelected
            );

            if (success) {
                // Bước 23: success
                JOptionPane.showMessageDialog(this,
                        "Đã thêm cuộc hẹn thành công!",
                        "Thành công", JOptionPane.INFORMATION_MESSAGE);
                return; // Xong, thoát vòng lặp
            }

            // Controller trả false = user chọn "Giờ khác" → mở lại dialog
            isRetry = true;
        }
    }

    /**
     * Bước 10-11: Hiển thị cảnh báo trùng lịch
     * @return REPLACE hoặc FREE_TIME
     */
    public int showConflictWarning(Appointment existing) {
        String message = "⚠ Trùng lịch với cuộc hẹn:\n\n"
                + "   Tên: " + existing.getName() + "\n"
                + "   Thời gian: " + existing.getStartTime().toLocalTime()
                + " - " + existing.getEndTime().toLocalTime() + "\n"
                + "   Địa điểm: " + existing.getLocation() + "\n\n"
                + "Bạn muốn thay thế cuộc hẹn cũ hay chọn giờ khác?";

        Object[] options = {"Thay thế cuộc hẹn cũ", "Chọn giờ khác"};
        int result = JOptionPane.showOptionDialog(this,
                message, "Cảnh báo trùng lịch",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null, options, options[1]);

        return (result == JOptionPane.YES_OPTION) ? REPLACE : FREE_TIME;
    }

    /**
     * Bước 15-16: Hiển thị thông báo cuộc họp nhóm
     * @return true nếu user muốn tham gia
     */
    public boolean showGroupMeetingPrompt(GroupMeeting meeting) {
        String participants = "";
        for (User u : meeting.getParticipants()) {
            participants += "   - " + u.getName() + "\n";
        }
        if (participants.isEmpty()) participants = "   (chưa có ai)\n";

        String message = "🤝 Phát hiện cuộc họp nhóm trùng khớp!\n\n"
                + "   Tên: " + meeting.getName() + "\n"
                + "   Thời gian: " + meeting.getStartTime().toLocalTime()
                + " - " + meeting.getEndTime().toLocalTime() + "\n"
                + "   Người tham gia hiện tại:\n" + participants + "\n"
                + "Bạn có muốn tham gia cuộc họp nhóm này không?";

        int result = JOptionPane.showConfirmDialog(this,
                message, "Tham gia cuộc họp nhóm?",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        return (result == JOptionPane.YES_OPTION);
    }

    /** Bước 24: Cập nhật lại giao diện */
    public void refreshView() {
        showAppointments(activeDate);
    }

    /** Hiển thị danh sách lời nhắc */
    private void showReminders() {
        List<Reminder> reminders = controller.getCurrentUser().getReminders();
        if (reminders.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Không có lời nhắc nào.", "Lời nhắc", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        StringBuilder sb = new StringBuilder("🔔 Danh sách lời nhắc:\n\n");
        for (Reminder r : reminders) {
            sb.append("• ").append(r.getMessage()).append("\n")
              .append("   Nhắc lúc: ").append(r.getReminderTime().toLocalTime()).append("\n\n");
        }
        JOptionPane.showMessageDialog(this, sb.toString(),
                "Lời nhắc (" + reminders.size() + ")", JOptionPane.INFORMATION_MESSAGE);
    }
}
