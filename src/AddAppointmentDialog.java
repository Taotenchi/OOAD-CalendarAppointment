import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Boundary: Dialog thêm cuộc hẹn mới
 * Bước 2-6 trong Sequence Diagram
 */
public class AddAppointmentDialog extends JDialog {
    private JTextField nameField;
    private JTextField locationField;
    private JSpinner startHourSpinner, startMinuteSpinner;
    private JSpinner endHourSpinner, endMinuteSpinner;
    private JCheckBox reminderCheckBox;
    private JLabel dateLabel;

    private boolean confirmed = false;
    private LocalDate activeDate;
    private LocalTime activeTime;

    /**
     * Bước 2: <<create>> display(activeDate, activeTime)
     */
    public AddAppointmentDialog(JFrame parent, LocalDate activeDate, LocalTime activeTime) {
        super(parent, "Add Calendar Appointment", true);
        this.activeDate = activeDate;
        this.activeTime = activeTime;

        setSize(500, 420);
        setLocationRelativeTo(parent);
        setResizable(false);
        initUI();
    }

    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(245, 245, 250));

        // === Title ===
        JLabel titleLabel = new JLabel("New Appointment - " +
                activeDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(new Color(50, 50, 120));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // === Form Panel ===
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        // Row 0: Tên cuộc hẹn
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0; gbc.gridwidth = 1;
        formPanel.add(createLabel("Tên cuộc hẹn:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1; gbc.gridwidth = 1;
        nameField = new JTextField(25);
        nameField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        formPanel.add(nameField, gbc);

        // Row 1: Địa điểm
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0; gbc.gridwidth = 1;
        formPanel.add(createLabel("Địa điểm:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1; gbc.gridwidth = 1;
        locationField = new JTextField(25);
        locationField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        formPanel.add(locationField, gbc);

        // Row 2: Giờ bắt đầu (riêng 1 hàng)
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0; gbc.gridwidth = 1;
        formPanel.add(createLabel("Bắt đầu:"), gbc);
        startHourSpinner = new JSpinner(new SpinnerNumberModel(activeTime.getHour(), 0, 23, 1));
        startMinuteSpinner = new JSpinner(new SpinnerNumberModel(activeTime.getMinute(), 0, 59, 5));
        gbc.gridx = 1; gbc.weightx = 1; gbc.gridwidth = 1;
        formPanel.add(createTimePanel(startHourSpinner, startMinuteSpinner), gbc);

        // Row 3: Giờ kết thúc (riêng 1 hàng)
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0; gbc.gridwidth = 1;
        formPanel.add(createLabel("Kết thúc:"), gbc);
        int endHour = Math.min(activeTime.getHour() + 1, 23);
        endHourSpinner = new JSpinner(new SpinnerNumberModel(endHour, 0, 23, 1));
        endMinuteSpinner = new JSpinner(new SpinnerNumberModel(activeTime.getMinute(), 0, 59, 5));
        gbc.gridx = 1; gbc.weightx = 1; gbc.gridwidth = 1;
        formPanel.add(createTimePanel(endHourSpinner, endMinuteSpinner), gbc);

        // Row 4: Reminder checkbox
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        reminderCheckBox = new JCheckBox("Nhắc nhở trước 15 phút");
        reminderCheckBox.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        reminderCheckBox.setOpaque(false);
        formPanel.add(reminderCheckBox, gbc);

        mainPanel.add(formPanel, BorderLayout.CENTER);

        // === Buttons ===
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);

        JButton cancelBtn = new JButton("Hủy");
        cancelBtn.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cancelBtn.addActionListener(e -> {
            confirmed = false;
            dispose();
        });

        JButton saveBtn = new JButton("Lưu cuộc hẹn");
        saveBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        saveBtn.setBackground(new Color(70, 130, 200));
        saveBtn.setForeground(Color.WHITE);
        saveBtn.setFocusPainted(false);
        saveBtn.addActionListener(e -> onSave());

        buttonPanel.add(cancelBtn);
        buttonPanel.add(saveBtn);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }

    /**
     * Bước 4: validateInput()
     * Kiểm tra: tên không trống, thời lượng không âm
     */
    public boolean validateInput() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            // Bước 5: showError - tên trống
            showError("Tên cuộc hẹn không được để trống!");
            return false;
        }

        LocalDateTime start = getStartDateTime();
        LocalDateTime end = getEndDateTime();

        if (!end.isAfter(start)) {
            // Bước 5: showError - thời lượng phủ định
            showError("Thời gian kết thúc phải sau thời gian bắt đầu!");
            return false;
        }

        return true;
    }

    /** Bước 5: Hiển thị lỗi */
    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Lỗi nhập liệu",
                JOptionPane.ERROR_MESSAGE);
    }

    /** Bước 6: Trả về dữ liệu cuộc hẹn */
    public Appointment getAppointmentData() {
        return new Appointment(
                nameField.getText().trim(),
                locationField.getText().trim(),
                getStartDateTime(),
                getEndDateTime()
        );
    }

    /** Xử lý khi bấm Lưu */
    private void onSave() {
        // Bước 4: validateInput
        if (validateInput()) {
            // Bước 6: valid → confirmed
            confirmed = true;
            dispose();
        }
        // Nếu invalid → showError đã hiển thị, dialog vẫn mở
    }

    /** Điền sẵn dữ liệu cũ khi mở lại dialog (sau khi chọn "Giờ khác") */
    public void prefill(String name, String location, boolean reminder) {
        nameField.setText(name);
        locationField.setText(location);
        reminderCheckBox.setSelected(reminder);
    }

    public boolean isConfirmed() { return confirmed; }
    public boolean isReminderSelected() { return reminderCheckBox.isSelected(); }

    public LocalDateTime getStartDateTime() {
        int h = (int) startHourSpinner.getValue();
        int m = (int) startMinuteSpinner.getValue();
        return LocalDateTime.of(activeDate, LocalTime.of(h, m));
    }

    public LocalDateTime getEndDateTime() {
        int h = (int) endHourSpinner.getValue();
        int m = (int) endMinuteSpinner.getValue();
        return LocalDateTime.of(activeDate, LocalTime.of(h, m));
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return label;
    }

    private JPanel createTimePanel(JSpinner hourSpinner, JSpinner minuteSpinner) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 2, 0));
        panel.setOpaque(false);
        hourSpinner.setPreferredSize(new Dimension(55, 28));
        minuteSpinner.setPreferredSize(new Dimension(55, 28));
        panel.add(hourSpinner);
        panel.add(new JLabel("h"));
        panel.add(minuteSpinner);
        panel.add(new JLabel("m"));
        return panel;
    }
}
