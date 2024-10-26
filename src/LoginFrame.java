import java.awt.*;
import javax.swing.*;

public class LoginFrame extends JFrame {
    private final Color PRIMARY_COLOR = new Color(123, 31, 162);
    private final Color BUTTON_COLOR = new Color(255, 87, 34);
    
    private JTextField tfUsername;
    private JPasswordField pfPassword;
    
    public LoginFrame() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        initialize();
    }
    
    private void initialize() {
        setTitle("internXconnect - Login");
        setSize(450, 550);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Main Panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        
        // Logo and Title
        // Logo and Title
        try {
            ImageIcon gifIcon = new ImageIcon(getClass().getResource("/icon1.gif"));
            // Reduced size to 60x60 pixels (you can adjust these numbers)
            Image scaledImage = gifIcon.getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT);
            ImageIcon scaledGifIcon = new ImageIcon(scaledImage);
            
            // Use the scaled version instead of original
            JLabel logoLabel = new JLabel(scaledGifIcon);  // Using scaled version here
            
            logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            mainPanel.add(logoLabel);
        } catch (Exception e) {
            System.out.println("GIF not found");
            // Fallback to text or emoji if GIF is not found
            JLabel fallbackLabel = new JLabel("👥");
            fallbackLabel.setFont(new Font("Segoe UI", Font.PLAIN, 48));
            fallbackLabel.setForeground(PRIMARY_COLOR);
            fallbackLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            mainPanel.add(fallbackLabel);
        }
        JLabel titleLabel = new JLabel("internXconnect");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel subtitleLabel = new JLabel("Welcome back!");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subtitleLabel.setForeground(Color.GRAY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(5));
        mainPanel.add(subtitleLabel);
        mainPanel.add(Box.createVerticalStrut(30));
        
        // Login Form
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 0, 5, 0);
        
        // Username field
        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(userLabel, gbc);
        
        tfUsername = createTextField();
        gbc.gridy = 1;
        formPanel.add(tfUsername, gbc);
        
        // Password field
        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        gbc.gridy = 2;
        gbc.insets = new Insets(15, 0, 5, 0);
        formPanel.add(passLabel, gbc);
        
        pfPassword = createPasswordField();
        gbc.gridy = 3;
        gbc.insets = new Insets(5, 0, 5, 0);
        formPanel.add(pfPassword, gbc);
        
        formPanel.setMaximumSize(new Dimension(300, 200));
        mainPanel.add(formPanel);
        mainPanel.add(Box.createVerticalStrut(20));
        
        // Login button
        JButton loginButton = createLoginButton("Login");
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.addActionListener(e -> handleLogin());
        mainPanel.add(loginButton);
        
        mainPanel.add(Box.createVerticalStrut(20));
        
        // Register link
        JLabel registerLabel = new JLabel("Don't have an account? Sign up");
        registerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        registerLabel.setForeground(BUTTON_COLOR);
        registerLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(registerLabel);
        
        add(mainPanel);
        setVisible(true);
    }
    
    private JTextField createTextField() {
        JTextField field = new JTextField(20);
        field.setPreferredSize(new Dimension(300, 35));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return field;
    }
    
    private JPasswordField createPasswordField() {
        JPasswordField field = new JPasswordField(20);
        field.setPreferredSize(new Dimension(300, 35));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return field;
    }
    
    private JButton createLoginButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(300, 40));
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(BUTTON_COLOR);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
    
    private void handleLogin() {
        String username = tfUsername.getText();
        String password = new String(pfPassword.getPassword());
        
        if (username.equals("admin") && password.equals("admin")) {
            dispose();
            SwingUtilities.invokeLater(() -> {
                MainFrame mainFrame = new MainFrame();
                mainFrame.initialize();
            });
        } else {
            JOptionPane.showMessageDialog(this,
                "Invalid credentials.\nUse: username = admin, password = admin",
                "Login Failed",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}