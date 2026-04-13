import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Image;
import java.net.URI;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class AboutBox extends JDialog {

    private static final long serialVersionUID = 1L;
    private static final String DONATION_URL = "https://www.paypal.com/donate/?hosted_button_id=XS9MXN5AE5P3S";

    public AboutBox(Frame owner) {
        super(owner, "About Domenico", true);

        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setSize(430, 430);
        setLocationRelativeTo(owner);
        setResizable(false);
        getContentPane().setLayout(new BorderLayout());

        JPanel contentPanel = new JPanel();
        contentPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        getContentPane().add(contentPanel, BorderLayout.CENTER);

        JLabel iconLabel = new JLabel();
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        URL iconUrl = getClass().getResource("/Domenico.png");
        if (iconUrl != null) {
            ImageIcon originalIcon = new ImageIcon(iconUrl);
            Image scaledImage = originalIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            iconLabel.setIcon(new ImageIcon(scaledImage));
        }

        contentPanel.add(iconLabel);
        contentPanel.add(Box.createVerticalStrut(12));

        JLabel titleLabel = new JLabel("Domenico");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 28));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(titleLabel);

        JLabel versionLabel = new JLabel("Version 1.0.1");
        versionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        contentPanel.add(versionLabel);

        contentPanel.add(Box.createVerticalStrut(16));

        JPanel infoPanel = new JPanel(new GridLayout(0, 1, 0, 8));

        JLabel copyrightLabel = new JLabel("© 2026 Ross Contino", SwingConstants.CENTER);
        JLabel descriptionLabel = new JLabel("Uses FFmpeg to create small animated GIFs", SwingConstants.CENTER);
        JLabel licenseLabel = new JLabel("MIT License", SwingConstants.CENTER);
        JLabel githubLabel = new JLabel("GitHub: github.com/RossContino1/Domenico", SwingConstants.CENTER);
        JLabel websiteLabel = new JLabel("Website: bytesbreadbbq.com/domenico", SwingConstants.CENTER);

        infoPanel.add(copyrightLabel);
        infoPanel.add(descriptionLabel);
        infoPanel.add(licenseLabel);
        infoPanel.add(githubLabel);
        infoPanel.add(websiteLabel);

        contentPanel.add(infoPanel);
        contentPanel.add(Box.createVerticalGlue());

        JPanel buttonPanel = new JPanel();

        JButton donateButton = new JButton("Donate");
        donateButton.addActionListener(e -> openDonationLink());

        JButton okButton = new JButton("OK");
        okButton.addActionListener(e -> dispose());

        buttonPanel.add(donateButton);
        buttonPanel.add(okButton);
        getRootPane().setDefaultButton(okButton);

        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    }

    private void openDonationLink() {
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(new URI(DONATION_URL));
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Desktop browsing is not supported on this system.",
                        "Open Link Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                    this,
                    "Could not open donation link.",
                    "Open Link Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}