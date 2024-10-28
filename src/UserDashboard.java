import javax.swing.*;//Imports all classes in the Swing library for building graphical user interfaces in Java.
import javax.swing.border.*;// Imports classes for creating and managing borders around Swing components.


import javax.swing.table.*;//Imports classes for creating and managing tables in Swing.
import java.awt.*;//Imports all classes in the Abstract Window Toolkit (AWT) for building user interfaces and graphical components.
import java.awt.event.*;//Imports classes for handling various types of events in AWT and Swing components.
import java.text.SimpleDateFormat;//Imports the SimpleDateFormat class for formatting and parsing dates in a locale-sensitive manner.
import java.util.List;//Imports the List interface for defining a collection of ordered elements that can contain duplicates.

public class UserDashboard extends JFrame {
    private final Font mainFont = new Font("Segoe UI", Font.PLAIN, 14);
    private final Color PRIMARY_COLOR = new Color(123, 31, 162);
    private final Color BACKGROUND_COLOR = new Color(248, 245, 250);
    private final Color APPLY_BUTTON_COLOR = new Color(46, 125, 50);  // Green color for apply button

    private JTable jobTable;
    private DefaultTableModel tableModel;
    private JobDAO jobDAO;
    private String loggedInUsername;
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
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(BACKGROUND_COLOR);

        // Add components
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createMainPanel(), BorderLayout.CENTER);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout(15, 0));
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Welcome message
        JLabel welcomeLabel = new JLabel("Welcome, " + loggedInUsername);
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        welcomeLabel.setForeground(PRIMARY_COLOR);

        // Logout button
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setFocusPainted(false);
        logoutBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutBtn.addActionListener(e -> handleLogout());

        header.add(welcomeLabel, BorderLayout.WEST);
        header.add(logoutBtn, BorderLayout.EAST);

        return header;
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setOpaque(false);
        mainPanel.setBorder(new EmptyBorder(0, 20, 20, 20));

        // Create and setup table
        String[] columns = {"Job Title", "Company", "Location", "Status", "Posted Date", "Actions"};
        tableModel = new DefaultTableModel(columns, 0);
        jobTable = new JTable(tableModel);

        // Style table
        jobTable.setFont(mainFont);
        jobTable.setRowHeight(40);
        jobTable.setShowGrid(false);
        jobTable.setSelectionBackground(new Color(245, 245, 250));

        // Style header
        JTableHeader header = jobTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(Color.WHITE);
        header.setForeground(PRIMARY_COLOR);

        // Add table to scroll pane
        JScrollPane scrollPane = new JScrollPane(jobTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);

        // Add apply button to last column
        TableColumn actionColumn = jobTable.getColumnModel().getColumn(5);
        actionColumn.setCellRenderer(new ButtonRenderer());
        actionColumn.setCellEditor(new ButtonEditor(new JCheckBox()));

        mainPanel.add(scrollPane, BorderLayout.CENTER);
        return mainPanel;
    }

    private void loadJobs() {
        currentJobs = jobDAO.getAllJobs();
        tableModel.setRowCount(0);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");

        for (Job job : currentJobs) {
            tableModel.addRow(new Object[]{
                    job.getTitle(),
                    "Company Name", // Add company field to Job class
                    "Location",     // Add location field to Job class
                    job.getStatus(),
                    dateFormat.format(job.getCreatedDate()),
                    "Apply"
            });
        }
    }

    private void handleLogout() {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to logout?",
                "Confirm Logout",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            new LoginFrame();
        }
    }

    // Custom button renderer for the Actions column
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setBackground(APPLY_BUTTON_COLOR);  // Using green color
            setForeground(Color.WHITE);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            setText("Apply");
            return this;
        }
    }

    // Custom button editor for the Actions column
    class ButtonEditor extends DefaultCellEditor {
        private JButton button;

        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton();
            button.setOpaque(true);
            button.setBackground(APPLY_BUTTON_COLOR);  // Using green color
            button.setForeground(Color.WHITE);

            button.addActionListener(e -> {
                fireEditingStopped();
                int row = jobTable.getSelectedRow();
                String jobTitle = (String) jobTable.getValueAt(row, 0);
                JOptionPane.showMessageDialog(UserDashboard.this,
                        "Applied for: " + jobTitle,
                        "Application Submitted",
                        JOptionPane.INFORMATION_MESSAGE);
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            button.setText("Apply");
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return "Apply";
        }
    }
}