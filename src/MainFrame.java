import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class MainFrame extends JFrame {
    final private Font mainFont = new Font("Segoe print", Font.BOLD, 18);
    JTextField tfFirstName, tfLastName;
    JLabel lbWelcome;
    
    // Components for template management
    private JTable templateTable;
    private DefaultTableModel tableModel;
    private JTextField tfTemplateName, tfTemplateDescription;
    private JTextArea taTemplateContent;

    @SuppressWarnings("Convert2Lambda")
    public void initialize() {
        // Create split pane for main content and template panel
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        
        /* LEFT SIDE - EXISTING FORM PANEL */
        JPanel leftPanel = createLeftPanel();
        
        /* RIGHT SIDE - TEMPLATE MANAGEMENT */
        JPanel rightPanel = createTemplatePanel();
        
        // Add panels to split pane
        splitPane.setLeftComponent(leftPanel);
        splitPane.setRightComponent(rightPanel);
        splitPane.setDividerLocation(250);
        
        // Add split pane to frame
        add(splitPane);
        
        setTitle("Job Template Manager");
        setSize(900, 600);
        setMinimumSize(new Dimension(800, 400));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }
    
    private JPanel createLeftPanel() {
        /* FORM PANEL */
        JLabel lbFirstName = new JLabel("First name");
        lbFirstName.setFont(mainFont);
        tfFirstName = new JTextField();
        tfFirstName.setFont(mainFont);
        
        JLabel lbLastName = new JLabel("Last Name");
        lbLastName.setFont(mainFont);
        tfLastName = new JTextField();
        tfLastName.setFont(mainFont);
        
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(4, 1, 5, 5));
        formPanel.setOpaque(false);
        formPanel.add(lbFirstName);
        formPanel.add(tfFirstName);
        formPanel.add(lbLastName);
        formPanel.add(tfLastName);
        
        /* WELCOME LABEL */
        lbWelcome = new JLabel();
        lbWelcome.setFont(mainFont);
        
        /* BUTTON PANEL */
        JPanel buttonsPanel = createButtonPanel();
        
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());
        leftPanel.setBackground(new Color(128, 128, 255));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        leftPanel.add(formPanel, BorderLayout.NORTH);
        leftPanel.add(lbWelcome, BorderLayout.CENTER);
        leftPanel.add(buttonsPanel, BorderLayout.SOUTH);
        
        return leftPanel;
    }
    
    private JPanel createButtonPanel() {
        JButton btnOK = new JButton("OK");
        btnOK.setFont(mainFont);
        btnOK.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String firstName = tfFirstName.getText();
                String lastName = tfLastName.getText();
                lbWelcome.setText("Hello " + firstName + " " + lastName);
            }
        });
        
        JButton btnClear = new JButton("Clear");
        btnClear.setFont(mainFont);
        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tfFirstName.setText("");
                tfLastName.setText("");
                lbWelcome.setText("");
            }
        });
        
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(1, 2, 5, 5));
        buttonsPanel.setOpaque(false);
        buttonsPanel.add(btnOK);
        buttonsPanel.add(btnClear);
        
        return buttonsPanel;
    }
    
    private JPanel createTemplatePanel() {
        JPanel templatePanel = new JPanel(new BorderLayout());
        templatePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create table
        String[] columnNames = {"Template Name", "Description"};
        tableModel = new DefaultTableModel(columnNames, 0);
        templateTable = new JTable(tableModel);
        templateTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        // Create template form
        JPanel templateForm = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Template name field
        gbc.gridx = 0; gbc.gridy = 0;
        templateForm.add(new JLabel("Template Name:"), gbc);
        
        tfTemplateName = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 0;
        templateForm.add(tfTemplateName, gbc);
        
        // Template description field
        gbc.gridx = 0; gbc.gridy = 1;
        templateForm.add(new JLabel("Description:"), gbc);
        
        tfTemplateDescription = new JTextField(20);
        gbc.gridx = 1; gbc.gridy = 1;
        templateForm.add(tfTemplateDescription, gbc);
        
        // Template content area
        gbc.gridx = 0; gbc.gridy = 2;
        templateForm.add(new JLabel("Content:"), gbc);
        
        taTemplateContent = new JTextArea(5, 20);
        taTemplateContent.setLineWrap(true);
        JScrollPane scrollPane = new JScrollPane(taTemplateContent);
        gbc.gridx = 1; gbc.gridy = 2;
        templateForm.add(scrollPane, gbc);
        
        // Add template button
        JButton btnAddTemplate = new JButton("Add Template");
        btnAddTemplate.addActionListener(e -> addTemplate());
        gbc.gridx = 1; gbc.gridy = 3;
        templateForm.add(btnAddTemplate, gbc);
        
        // Add components to template panel
        templatePanel.add(new JScrollPane(templateTable), BorderLayout.CENTER);
        templatePanel.add(templateForm, BorderLayout.SOUTH);
        
        return templatePanel;
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
        } else {
            JOptionPane.showMessageDialog(this, 
                "Please fill in both template name and description",
                "Input Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainFrame myFrame = new MainFrame();
            myFrame.initialize();
        });
    }
    
}
