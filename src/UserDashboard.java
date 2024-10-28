import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.table.*;
import java.text.SimpleDateFormat;
import javax.swing.border.*;
import java.util.List;

public class UserDashboard extends JFrame {
    private final Font mainFont = new Font("Segoe UI", Font.PLAIN, 14);
    private final Font titleFont = new Font("Segoe UI", Font.BOLD, 24);
    private final Color PRIMARY_COLOR = new Color(123, 31, 162);
    private final Color BACKGROUND_COLOR = new Color(248, 245, 250);
    private final Color ACCENT_COLOR = new Color(171, 71, 188);

    private JTable jobTable;
    private DefaultTableModel tableModel;
    private JobDAO jobDAO;
    private String loggedInUsername;
    private JTextField searchField;
    private List<Job> currentJobs;

    public UserDashboard(String username) {
        this.loggedInUsername = username;
        this.jobDAO = new JobDAO();
        initializeUI();
        loadJobs();
    }

    private void initializeUI() {
        setTitle("User Dashboard - " + loggedInUsername);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1000, 600));
        setLayout(new BorderLayout());
        getContentPane().setBackground(BACKGROUND_COLOR);

        // Add components
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createMainPanel(), BorderLayout.CENTER);

        // Center the window
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Welcome message
        JLabel welcomeLabel = new JLabel("Welcome, " + loggedInUsername);
        welcomeLabel.setFont(titleFont);
        welcomeLabel.setForeground(Color.WHITE);

        // Create search and filter panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setOpaque(false);

        searchField = new JTextField(20);
        searchField.setPreferredSize(new Dimension(200, 30));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        // Add search functionality
        searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void changedUpdate(javax.swing.event.DocumentEvent e) { searchJobs(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { searchJobs(); }
            public void insertUpdate(javax.swing.event.DocumentEvent e) { searchJobs(); }
        });

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBackground(new Color(255, 87, 34));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFocusPainted(false);
        logoutButton.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.addActionListener(e -> logout());

        searchPanel.add(searchField);
        searchPanel.add(Box.createHorizontalStrut(20));
        searchPanel.add(logoutButton);

        headerPanel.add(welcomeLabel, BorderLayout.WEST);
        headerPanel.add(searchPanel, BorderLayout.EAST);

        return headerPanel;
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(BACKGROUND_COLOR);

        // Create the table
        String[] columns = {"Job Title", "Description", "Status", "Posted Date", "Actions"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; // Only allow editing of the Actions column
            }
        };

        jobTable = new JTable(tableModel) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component comp = super.prepareRenderer(renderer, row, column);
                if (!isCellSelected(row, column)) {
                    comp.setBackground(row % 2 == 0 ? Color.WHITE : new Color(250, 250, 252));
                    if (column == 2) { // Status column
                        String status = (String) getValueAt(row, column);
                        comp.setForeground(getStatusColor(status));
                    } else {
                        comp.setForeground(Color.BLACK);
                    }
                }
                return comp;
            }
        };

        // Table styling
        jobTable.setFont(mainFont);
        jobTable.setRowHeight(40);
        jobTable.setShowGrid(false);
        jobTable.setShowHorizontalLines(true);
        jobTable.setGridColor(new Color(230, 230, 230));
        jobTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        jobTable.getTableHeader().setBackground(new Color(250, 250, 252));
        jobTable.getTableHeader().setForeground(PRIMARY_COLOR);

        // Add button renderer/editor to the Actions column
        jobTable.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer());
        jobTable.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor(new JCheckBox()));

        // Set column widths
        jobTable.getColumnModel().getColumn(0).setPreferredWidth(200); // Title
        jobTable.getColumnModel().getColumn(1).setPreferredWidth(300); // Description
        jobTable.getColumnModel().getColumn(2).setPreferredWidth(100); // Status
        jobTable.getColumnModel().getColumn(3).setPreferredWidth(150); // Date
        jobTable.getColumnModel().getColumn(4).setPreferredWidth(100); // Actions

        JScrollPane scrollPane = new JScrollPane(jobTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        return mainPanel;
    }

    private void loadJobs() {
        currentJobs = jobDAO.getAllJobs();
        updateTableWithJobs(currentJobs);
    }

    private void updateTableWithJobs(List<Job> jobs) {
        tableModel.setRowCount(0);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");

        for (Job job : jobs) {
            tableModel.addRow(new Object[]{
                    job.getTitle(),
                    job.getDescription(),
                    job.getStatus(),
                    dateFormat.format(job.getCreatedDate()),
                    "Apply"
            });
        }
    }

    private void searchJobs() {
        String searchText = searchField.getText().toLowerCase();
        List<Job> filteredJobs = currentJobs.stream()
                .filter(job ->
                        job.getTitle().toLowerCase().contains(searchText) ||
                                job.getDescription().toLowerCase().contains(searchText))
                .collect(java.util.stream.Collectors.toList());
        updateTableWithJobs(filteredJobs);
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to logout?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            this.dispose();
            new LoginFrame();
        }
    }

    private Color getStatusColor(String status) {
        if (status == null) return Color.BLACK;
        switch (status.toLowerCase()) {
            case "active": return new Color(46, 125, 50);  // Green
            case "pending": return new Color(245, 124, 0); // Orange
            case "closed": return new Color(211, 47, 47);  // Red
            default: return Color.BLACK;
        }
    }

    // Custom button renderer
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setBackground(new Color(63, 81, 181));
            setForeground(Color.WHITE);
            setBorder(new EmptyBorder(5, 10, 5, 10));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value != null ? value.toString() : "");
            return this;
        }
    }

    // Custom button editor
    class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private String label;
        private boolean isPushed;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.setBackground(new Color(63, 81, 181));
            button.setForeground(Color.WHITE);
            button.setBorder(new EmptyBorder(5, 10, 5, 10));

            button.addActionListener(e -> fireEditingStopped());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            isPushed = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                // Handle job application
                int row = jobTable.getSelectedRow();
                String jobTitle = (String) jobTable.getValueAt(row, 0);
                JOptionPane.showMessageDialog(
                        UserDashboard.this,
                        "Application submitted for: " + jobTitle,
                        "Application Submitted",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
            isPushed = false;
            return label;
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }
    }
}