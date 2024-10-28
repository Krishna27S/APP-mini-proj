import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

public class MainFrame extends JFrame {
    final private Font mainFont = new Font("Segoe UI", Font.BOLD, 18);
    final private Font titleFont = new Font("Segoe UI", Font.BOLD, 24);
    final private Font subtitleFont = new Font("Segoe UI", Font.PLAIN, 16);
    
    // Components for template management
    private JTable templateTable;
    private DefaultTableModel tableModel;
    private JTextField tfTemplateName, tfTemplateDescription;
    private JTextArea taTemplateContent;
    
    // Database components
    private JobDAO jobDAO;
    private List<Job> currentJobs;
    
    // Custom colors
    private final Color PRIMARY_COLOR = new Color(123, 31, 162); // Deep Purple
    private final Color BUTTON_COLOR = new Color(255, 87, 34);   // Deep Orange
    private final Color TEXT_COLOR = new Color(33, 33, 33);      // Dark Grey
    private final Color BACKGROUND_COLOR = new Color(248, 245, 250); // Light Purple-ish
    private final Color ACCENT_COLOR = new Color(171, 71, 188); // Lighter Purple

    public void initialize() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Initialize database
        jobDAO = new JobDAO();
        jobDAO.createTable();
        currentJobs = new ArrayList<>();

        // Create split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setBorder(null);
        splitPane.setDividerSize(1);
        
        // Create panels
        JPanel leftPanel = createLeftPanel();
        JPanel rightPanel = createTemplatePanel();
        
        // Add panels to split pane
        splitPane.setLeftComponent(leftPanel);
        splitPane.setRightComponent(rightPanel);
        splitPane.setDividerLocation(300);
        
        // Add split pane to frame
        add(splitPane);
        
        setTitle("internXconnect - Job Manager");
        setSize(1200, 700);
        setMinimumSize(new Dimension(1000, 600));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Load initial data
        refreshJobList();
        
        setVisible(true);
    }

    // Your existing createLeftPanel() method stays the same
    private JPanel createLeftPanel() {
        JPanel leftPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                
                // Create a gradient from PRIMARY_COLOR to ACCENT_COLOR
                GradientPaint gradient = new GradientPaint(
                    0, 0, PRIMARY_COLOR,
                    0, getHeight(), ACCENT_COLOR
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        
        leftPanel.setLayout(new BorderLayout(0, 20));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        
        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setOpaque(false);
        
        // Icon Panel with glowing effect
        JPanel iconPanel = new JPanel();
        iconPanel.setLayout(new BorderLayout());
        iconPanel.setOpaque(false);
        iconPanel.setBorder(BorderFactory.createCompoundBorder(
            new EmptyBorder(10, 10, 10, 10),
            new CompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 255, 255, 50), 2),
                new EmptyBorder(10, 10, 10, 10)
            )
        ));

        // Load and resize icon
        try {
            ImageIcon gifIcon = new ImageIcon(getClass().getResource("/icon1.gif"));
            // Increased size to be more visible - adjust these numbers as needed
            Image scaledImage = gifIcon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
            ImageIcon scaledGifIcon = new ImageIcon(scaledImage);
            
            JLabel iconLabel = new JLabel(scaledGifIcon);
            iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            // Add padding around the icon
            iconLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            iconPanel.add(iconLabel, BorderLayout.CENTER);
        } catch (Exception e) {
            System.out.println("Error loading GIF: " + e.getMessage());
            JLabel fallbackLabel = new JLabel("👥");
            fallbackLabel.setFont(new Font("Segoe UI", Font.PLAIN, 48));
            fallbackLabel.setForeground(Color.WHITE);
            fallbackLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            iconPanel.add(fallbackLabel, BorderLayout.CENTER);
        }
        
        // Title and Subtitle with shadow effect
        JLabel titleLabel = new JLabel("internXconnect");
        titleLabel.setFont(titleFont);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Job Manager");
        subtitleLabel.setFont(subtitleFont);
        subtitleLabel.setForeground(new Color(255, 255, 255, 220));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Add components to header
        headerPanel.add(iconPanel);
        headerPanel.add(Box.createVerticalStrut(15));
        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(5));
        headerPanel.add(subtitleLabel);
        
        // Features Panel with hover effect
        JPanel featuresPanel = new JPanel();
        featuresPanel.setLayout(new BoxLayout(featuresPanel, BoxLayout.Y_AXIS));
        featuresPanel.setOpaque(false);
        featuresPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        
        String[] features = {
            "🌟 Create Job Posts",
            "📊 Track Applications",
            "✨ Easy Management",
            "🎯 Quick Updates"
        };
        
        for (String feature : features) {
            JLabel featureLabel = new JLabel(feature);
            featureLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            featureLabel.setForeground(Color.WHITE);
            featureLabel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(8, 0, 8, 0),
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(255, 255, 255, 30))
            ));
            featuresPanel.add(featureLabel);
            featuresPanel.add(Box.createVerticalStrut(5));
        }
        
        // Add components to left panel
        leftPanel.add(headerPanel, BorderLayout.NORTH);
        leftPanel.add(featuresPanel, BorderLayout.CENTER);
        
        return leftPanel;
    }

    private JPanel createTemplatePanel() {
        JPanel templatePanel = new JPanel(new BorderLayout(10, 10));
        templatePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        templatePanel.setBackground(BACKGROUND_COLOR);
        
        // Header Panel with Stats
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        
        JLabel headerLabel = new JLabel("Job Listings");
        headerLabel.setFont(titleFont);
        headerLabel.setForeground(PRIMARY_COLOR);
        
        // Stats Panel
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        statsPanel.setOpaque(false);
        addCountLabel(statsPanel, "Total Jobs: ", currentJobs.size());
        
        headerPanel.add(headerLabel, BorderLayout.NORTH);
        headerPanel.add(statsPanel, BorderLayout.CENTER);
        headerPanel.add(new JSeparator(), BorderLayout.SOUTH);

        // Enhanced Table
        String[] columnNames = {"ID", "Job Title", "Description", "Status", "Posted Date"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        templateTable = new JTable(tableModel) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component comp = super.prepareRenderer(renderer, row, column);
                if (!isCellSelected(row, column)) {
                    comp.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 249, 250));
                    
                    // Color code the status column
                    if (column == 3) {
                        String status = (String) getValueAt(row, column);
                        comp.setForeground(getStatusColor(status));
                    } else {
                        comp.setForeground(TEXT_COLOR);
                    }
                }
                return comp;
            }
        };

        // Table Styling
        templateTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        templateTable.setRowHeight(40);
        templateTable.setShowGrid(false);
        templateTable.setShowHorizontalLines(true);
        templateTable.setGridColor(new Color(230, 230, 230));
        templateTable.setSelectionBackground(new Color(232, 240, 254));
        
        // Hide ID column
        templateTable.getColumnModel().getColumn(0).setMinWidth(0);
        templateTable.getColumnModel().getColumn(0).setMaxWidth(0);
        templateTable.getColumnModel().getColumn(0).setWidth(0);

        // Set other column widths
        templateTable.getColumnModel().getColumn(1).setPreferredWidth(200); // Title
        templateTable.getColumnModel().getColumn(2).setPreferredWidth(300); // Description
        templateTable.getColumnModel().getColumn(3).setPreferredWidth(100); // Status
        templateTable.getColumnModel().getColumn(4).setPreferredWidth(150); // Date

        // Add right-click menu
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem deleteItem = new JMenuItem("Delete");
        JMenu statusMenu = new JMenu("Change Status");
        
        String[] statuses = {"Active", "Pending", "Closed"};
        for (String status : statuses) {
            JMenuItem statusItem = new JMenuItem(status);
            statusItem.addActionListener(e -> updateJobStatus(status));
            statusMenu.add(statusItem);
        }
        
        deleteItem.addActionListener(e -> deleteSelectedJob());
        popupMenu.add(statusMenu);
        popupMenu.add(deleteItem);
        
        templateTable.setComponentPopupMenu(popupMenu);

        // Scroll Pane
        JScrollPane scrollPane = new JScrollPane(templateTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(230, 230, 230)),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Form fields
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(createStyledLabel("Job Title:"), gbc);
        
        tfTemplateName = createStyledTextField();
        gbc.gridx = 1; gbc.gridy = 0;
        formPanel.add(tfTemplateName, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(createStyledLabel("Description:"), gbc);
        
        tfTemplateDescription = createStyledTextField();
        gbc.gridx = 1; gbc.gridy = 1;
        formPanel.add(tfTemplateDescription, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(createStyledLabel("Details:"), gbc);
        
        taTemplateContent = createStyledTextArea();
        JScrollPane contentScroll = new JScrollPane(taTemplateContent);
        contentScroll.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        gbc.gridx = 1; gbc.gridy = 2;
        formPanel.add(contentScroll, gbc);
        
        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setOpaque(false);
        
        JButton refreshButton = createStyledButton("Refresh");
        refreshButton.addActionListener(e -> refreshJobList());
        
        JButton addButton = createStyledButton("Add Job");
        addButton.addActionListener(e -> addTemplate());
        
        buttonPanel.add(refreshButton);
        buttonPanel.add(addButton);
        
        gbc.gridx = 1; gbc.gridy = 3;
        formPanel.add(buttonPanel, gbc);
        
        // Add all components
        templatePanel.add(headerPanel, BorderLayout.NORTH);
        templatePanel.add(scrollPane, BorderLayout.CENTER);
        templatePanel.add(formPanel, BorderLayout.SOUTH);
        
        return templatePanel;
    }

    // Continue with Part 2...

    // Helper methods for UI components
    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(PRIMARY_COLOR);
        return label;
    }
    
    private JTextField createStyledTextField() {
        JTextField field = new JTextField(20);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 7, 5, 7)
        ));
        return field;
    }
    
    private JTextArea createStyledTextArea() {
        JTextArea area = new JTextArea(4, 20);
        area.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBorder(BorderFactory.createEmptyBorder(5, 7, 5, 7));
        return area;
    }
    
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) {
                    g2d.setColor(BUTTON_COLOR.darker());
                } else if (getModel().isRollover()) {
                    g2d.setColor(BUTTON_COLOR.brighter());
                } else {
                    g2d.setColor(BUTTON_COLOR);
                }
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g);
            }
        };
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    // Database operation methods
    private void addTemplate() {
        String title = tfTemplateName.getText();
        String description = tfTemplateDescription.getText();
        String details = taTemplateContent.getText();
        
        if (!title.isEmpty() && !description.isEmpty()) {
            if (jobDAO.addJob(title, description, details)) {
                refreshJobList();
                
                // Clear form
                tfTemplateName.setText("");
                tfTemplateDescription.setText("");
                taTemplateContent.setText("");
                
                JOptionPane.showMessageDialog(this,
                    "Job added successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this,
                "Please fill in both job title and description",
                "Input Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void refreshJobList() {
        currentJobs = jobDAO.getAllJobs();
        tableModel.setRowCount(0);
        
        for (Job job : currentJobs) {
            tableModel.addRow(new Object[]{
                job.getId(),
                job.getTitle(),
                job.getDescription(),
                job.getStatus(),
                new SimpleDateFormat("MMM dd, yyyy").format(job.getCreatedDate())
            });
        }
    }
    
    private void updateJobStatus(String newStatus) {
        int selectedRow = templateTable.getSelectedRow();
        if (selectedRow >= 0) {
            int jobId = (Integer) tableModel.getValueAt(selectedRow, 0);
            if (jobDAO.updateStatus(jobId, newStatus)) {
                refreshJobList();
            }
        } else {
            JOptionPane.showMessageDialog(this,
                "Please select a job to update",
                "Selection Required",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void deleteSelectedJob() {
        int selectedRow = templateTable.getSelectedRow();
        if (selectedRow >= 0) {
            int jobId = (Integer) tableModel.getValueAt(selectedRow, 0);
            
            int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete this job?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
                
            if (confirm == JOptionPane.YES_OPTION) {
                if (jobDAO.deleteJob(jobId)) {
                    refreshJobList();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this,
                "Please select a job to delete",
                "Selection Required",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void addCountLabel(JPanel panel, String text, int count) {
        JLabel label = new JLabel(text + count);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(TEXT_COLOR);
        panel.add(label);
    }
    
    private Color getStatusColor(String status) {
        if (status == null) return TEXT_COLOR;
        switch (status.toLowerCase()) {
            case "active": return new Color(46, 125, 50);  // Green
            case "pending": return new Color(245, 124, 0); // Orange
            case "closed": return new Color(211, 47, 47);  // Red
            default: return TEXT_COLOR;
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginFrame(); // Start with login screen
        });
    }

    public JobDAO getJobDAO() {
        return jobDAO;
    }
}