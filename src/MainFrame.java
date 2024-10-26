import java.awt.*;
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
    
    // Custom colors - New Vibrant Color Scheme
    // Using a rich purple gradient
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
        setVisible(true);
    }
    
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
        ImageIcon originalIcon = new ImageIcon(getClass().getResource("/icon1.gif"));
        Image image = originalIcon.getImage();
        Image resizedImage = image.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
        ImageIcon icon = new ImageIcon(resizedImage);
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        iconPanel.add(iconLabel, BorderLayout.CENTER);
        
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
        
        // Header
        JLabel headerLabel = new JLabel("Job Listings");
        headerLabel.setFont(titleFont);
        headerLabel.setForeground(PRIMARY_COLOR);
        
        // Table with alternating colors
        String[] columnNames = {"Job Title", "Description"};
        tableModel = new DefaultTableModel(columnNames, 0);
        templateTable = new JTable(tableModel) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
                Component comp = super.prepareRenderer(renderer, row, col);
                if (!isCellSelected(row, col)) {
                    comp.setBackground(row % 2 == 0 ? Color.WHITE : new Color(243, 237, 247));
                }
                return comp;
            }
        };
        
        templateTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        templateTable.setRowHeight(35);
        templateTable.setShowGrid(false);
        templateTable.setIntercellSpacing(new Dimension(0, 5));
        
        JScrollPane scrollPane = new JScrollPane(templateTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));
        
        // Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230)),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Form fields with styled borders
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
        
        // Styled button
        JButton addButton = createStyledButton("Add Job");
        addButton.addActionListener(e -> addTemplate());
        gbc.gridx = 1; gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.LINE_END;
        formPanel.add(addButton, gbc);
        
        // Add components to template panel
        templatePanel.add(headerLabel, BorderLayout.NORTH);
        templatePanel.add(scrollPane, BorderLayout.CENTER);
        templatePanel.add(formPanel, BorderLayout.SOUTH);
        
        return templatePanel;
    }
    
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
    
    private void addTemplate() {
        String name = tfTemplateName.getText();
        String description = tfTemplateDescription.getText();
        
        if (!name.isEmpty() && !description.isEmpty()) {
            tableModel.addRow(new Object[]{name, description});
            
            // Clear form
            tfTemplateName.setText("");
            tfTemplateDescription.setText("");
            taTemplateContent.setText("");
            
            JOptionPane.showMessageDialog(this,
                "Job added successfully!",
                "Success",
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                "Please fill in both job title and description",
                "Input Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginFrame(); // This will show the login screen first
        });
    }
}