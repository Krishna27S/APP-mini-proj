import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

public class LoginFrame extends JFrame {
    private final Color PRIMARY_COLOR = new Color(123, 31, 162);
    private final Color BUTTON_COLOR = new Color(255, 87, 34);
    private final Color BACKGROUND_COLOR = new Color(248, 245, 250);
    
    private JTextField tfEmail;
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
        setSize(400, 600);
        setMinimumSize(new Dimension(350, 550));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Main Panel with gradient background
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gradient = new GradientPaint(
                    0, 0, PRIMARY_COLOR,
                    0, getHeight(), PRIMARY_COLOR.darker()
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BorderLayout());
        
        // Login Card
        JPanel loginCard = createLoginCard();
        mainPanel.add(loginCard, BorderLayout.CENTER);
        
        add(mainPanel);
        setVisible(true);
    }
    
    private JPanel createLoginCard() {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setOpaque(false);
        card.setBorder(BorderFactory.createEmptyBorder(50, 40, 50, 40));
        
        // Logo and Title
        try {
            ImageIcon originalIcon = new ImageIcon(getClass().getResource("/icon.png"));
            Image image = originalIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
            JLabel logoLabel = new JLabel(new ImageIcon(image));
            logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            card.add(logoLabel);
        } catch (Exception e) {
            // If icon not found, use text
            JLabel logoText = new JLabel("👤");
            logoText.setFont(new Font("Segoe UI", Font.PLAIN, 48));
            logoText.setForeground(Color.WHITE);
            logoText.setAlignmentX(Component.CENTER_ALIGNMENT);
            card.add(logoText);
        }
        
        card.add(Box.createVerticalStrut(20));
        
        JLabel titleLabel = new JLabel("internXconnect");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(titleLabel);
        
        JLabel subtitleLabel = new JLabel("Login to continue");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(255, 255, 255, 200));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(subtitleLabel);
        
        card.add(Box.createVerticalStrut(40));
        
        // Login Form
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setOpaque(false);
        formPanel.setMaximumSize(new Dimension(300, 400));
        
        // Email field
        JLabel emailLabel = new JLabel("Username");
        emailLabel.setForeground(Color.WHITE);
        formPanel.add(emailLabel);
        
        tfEmail = new JTextField();
        tfEmail.setMaximumSize(new Dimension(300, 35));
        tfEmail.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tfEmail.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(10),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        formPanel.add(tfEmail);
        formPanel.add(Box.createVerticalStrut(20));
        
        // Password field
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setForeground(Color.WHITE);
        formPanel.add(passwordLabel);
        
        pfPassword = new JPasswordField();
        pfPassword.setMaximumSize(new Dimension(300, 35));
        pfPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        pfPassword.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(10),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        formPanel.add(pfPassword);
        formPanel.add(Box.createVerticalStrut(30));
        
        // Login button
        JButton loginButton = new JButton("Login") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(getModel().isPressed() ? BUTTON_COLOR.darker() : BUTTON_COLOR);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g);
            }
        };
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginButton.setMaximumSize(new Dimension(300, 40));
        loginButton.setBorderPainted(false);
        loginButton.setContentAreaFilled(false);
        loginButton.setFocusPainted(false);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.addActionListener(e -> handleLogin());
        
        formPanel.add(loginButton);

        // Register link
        JButton registerLink = new JButton("Don't have an account? Register here");
        registerLink.setForeground(new Color(255, 255, 255, 200));
        registerLink.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        registerLink.setBorderPainted(false);
        registerLink.setContentAreaFilled(false);
        registerLink.setFocusPainted(false);
        registerLink.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(registerLink);
        
        card.add(formPanel);
        
        return card;
    }
    
    private void handleLogin() {
        String username = tfEmail.getText();
        String password = new String(pfPassword.getPassword());
        
        // Simple validation (you should implement proper authentication)
        if (username.equals("admin") && password.equals("admin")) {
            dispose(); // Close login window
            SwingUtilities.invokeLater(() -> {
                MainFrame mainFrame = new MainFrame();
                mainFrame.initialize();
            });
        } else {
            JOptionPane.showMessageDialog(this,
                "Invalid credentials. Please try again.\nHint: use 'admin' for both username and password",
                "Login Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Custom rounded border for text fields
    private class RoundedBorder extends AbstractBorder {
        private final int radius;
        
        RoundedBorder(int radius) {
            this.radius = radius;
        }
        
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(Color.WHITE);
            g2d.drawRoundRect(x, y, width-1, height-1, radius, radius);
            g2d.dispose();
        }
        
        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(radius/2, radius/2, radius/2, radius/2);
        }
    }
}