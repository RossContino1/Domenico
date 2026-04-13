import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.FileDialog;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.prefs.Preferences;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;

public class MainWindow {

    private static final Preferences prefs = Preferences.userNodeForPackage(MainWindow.class);
    private static final String PREF_DONATION_SHOWN = "donationPromptShown";
    private static final String DONATION_URL = "https://www.paypal.com/donate/?hosted_button_id=XS9MXN5AE5P3S";

    private JFrame frmDomenico;
    private JTextField textField;
    private JTextField textField_1;
    private JProgressBar progressBar;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                setSystemTheme();
                MainWindow window = new MainWindow();
                window.frmDomenico.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public MainWindow() {
        initialize();
    }

    private void initialize() {
        frmDomenico = new JFrame();
        frmDomenico.setTitle("Domenico 1.0.0");
        frmDomenico.setBounds(100, 100, 520, 340);
        frmDomenico.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frmDomenico.setLocationRelativeTo(null);
        frmDomenico.getContentPane().setLayout(null);

        JMenuBar menuBar = new JMenuBar();
        frmDomenico.setJMenuBar(menuBar);

        JMenu mnFile = new JMenu("File");
        menuBar.add(mnFile);

        JMenuItem mntmExit = new JMenuItem("Exit");
        mntmExit.addActionListener(e -> System.exit(0));
        mnFile.add(mntmExit);

        JMenu mnView = new JMenu("View");
        menuBar.add(mnView);

        JMenuItem mntmSystemTheme = new JMenuItem("System Theme");
        mntmSystemTheme.addActionListener(e -> {
            setSystemTheme();
            refreshUI();
        });
        mnView.add(mntmSystemTheme);

        JMenuItem mntmLightTheme = new JMenuItem("Light Theme");
        mntmLightTheme.addActionListener(e -> {
            setLightTheme();
            refreshUI();
        });
        mnView.add(mntmLightTheme);

        JMenuItem mntmDarkTheme = new JMenuItem("Dark Theme");
        mntmDarkTheme.addActionListener(e -> {
            setDarkTheme();
            refreshUI();
        });
        mnView.add(mntmDarkTheme);

        JMenu mnHelp = new JMenu("Help");
        menuBar.add(mnHelp);

        JMenuItem mntmHelp = new JMenuItem("Help");
        mntmHelp.addActionListener(e -> {
            HelpBox helpDialog = new HelpBox(frmDomenico);
            helpDialog.setVisible(true);
        });
        mnHelp.add(mntmHelp);

        JMenuItem mntmAbout = new JMenuItem("About");
        mntmAbout.addActionListener(e -> {
            AboutBox aboutDialog = new AboutBox(frmDomenico);
            aboutDialog.setVisible(true);
        });
        mnHelp.add(mntmAbout);

        JLabel lblFileToBeConverted = new JLabel("File To Be Converted:");
        lblFileToBeConverted.setBounds(25, 25, 180, 20);
        frmDomenico.getContentPane().add(lblFileToBeConverted);

        textField = new JTextField();
        textField.setBounds(25, 50, 340, 28);
        frmDomenico.getContentPane().add(textField);

        JButton btnBrowse = new JButton("Browse");
        btnBrowse.setBounds(375, 50, 100, 28);
        btnBrowse.addActionListener(e -> chooseFile());
        frmDomenico.getContentPane().add(btnBrowse);

        JLabel lblOutputGif = new JLabel("Output GIF:");
        lblOutputGif.setBounds(25, 95, 180, 20);
        frmDomenico.getContentPane().add(lblOutputGif);

        textField_1 = new JTextField();
        textField_1.setBounds(25, 120, 340, 28);
        frmDomenico.getContentPane().add(textField_1);

        JButton btnClear = new JButton("Clear");
        btnClear.setBounds(155, 190, 90, 30);
        btnClear.addActionListener(e -> {
            textField.setText("");
            textField_1.setText("");
            resetProgressBar();
        });
        frmDomenico.getContentPane().add(btnClear);

        JButton btnConvert = new JButton("Convert");
        btnConvert.setBounds(255, 190, 90, 30);
        btnConvert.addActionListener(e -> {
            btnConvert.setEnabled(false);

            Thread worker = new Thread(() -> {
                try {
                    convertToGif();
                } finally {
                    SwingUtilities.invokeLater(() -> btnConvert.setEnabled(true));
                }
            });

            worker.start();
        });
        frmDomenico.getContentPane().add(btnConvert);

        progressBar = new JProgressBar();
        progressBar.setBounds(25, 255, 450, 24);
        progressBar.setStringPainted(true);
        progressBar.setString("Ready");
        progressBar.setValue(0);
        frmDomenico.getContentPane().add(progressBar);
    }

    private void chooseFile() {
        FileDialog fd = new FileDialog(frmDomenico, "Select File", FileDialog.LOAD);
        fd.setDirectory(System.getProperty("user.home"));
        fd.setVisible(true);

        if (fd.getFile() != null) {
            File selectedFile = new File(fd.getDirectory(), fd.getFile());
            textField.setText(selectedFile.getAbsolutePath());
            textField_1.setText(buildGifOutputPath(selectedFile));
            resetProgressBar();
        }
    }

    private String buildGifOutputPath(File inputFile) {
        String fileName = inputFile.getName();
        int dotIndex = fileName.lastIndexOf('.');

        String baseName = (dotIndex > 0) ? fileName.substring(0, dotIndex) : fileName;
        File parent = inputFile.getParentFile();

        if (parent != null) {
            return new File(parent, baseName + ".gif").getAbsolutePath();
        }
        return baseName + ".gif";
    }

    private void convertToGif() {
        String inputPath = textField.getText().trim();
        String outputPath = textField_1.getText().trim();

        if (inputPath.isEmpty()) {
            showError("Please select an input MP4 or MOV file.", "No Input File");
            return;
        }

        if (outputPath.isEmpty()) {
            showError("Please enter an output GIF filename.", "No Output File");
            return;
        }

        File inputFile = new File(inputPath);
        if (!inputFile.exists()) {
            showError("The selected input file does not exist.", "Input File Error");
            return;
        }

        File outputFile = new File(outputPath);
        File outputDir = outputFile.getParentFile();
        if (outputDir != null && !outputDir.exists()) {
            showError("The output folder does not exist.", "Output Folder Error");
            return;
        }

        File paletteFile = (outputDir != null)
                ? new File(outputDir, "palette.png")
                : new File("palette.png");

        try {
            setProgressState(10, false, "Checking duration...");
            double duration = getVideoDuration(inputPath);

            if (duration > 10.0) {
                setProgressState(0, false, "Video too long");
                showError(
                        String.format("Video is %.2f seconds long. Domenico only converts files 10 seconds or less.", duration),
                        "Video Too Long");
                return;
            }

            setProgressState(25, true, "Generating palette...");
            boolean paletteCreated = runCommand(new String[] {
                    "ffmpeg",
                    "-y",
                    "-i", inputPath,
                    "-vf", "fps=10,scale=800:-1:flags=lanczos,palettegen",
                    paletteFile.getAbsolutePath()
            });

            if (!paletteCreated) {
                setProgressState(0, false, "Palette generation failed");
                showError("Failed to generate palette.png", "FFmpeg Error");
                return;
            }

            setProgressState(65, true, "Creating GIF...");
            boolean gifCreated = runCommand(new String[] {
                    "ffmpeg",
                    "-y",
                    "-i", inputPath,
                    "-i", paletteFile.getAbsolutePath(),
                    "-lavfi", "fps=10,scale=800:-1:flags=lanczos,paletteuse",
                    outputPath
            });

            if (!gifCreated) {
                setProgressState(0, false, "GIF creation failed");
                showError("Failed to create GIF.", "FFmpeg Error");
                return;
            }

            setProgressState(100, false, "Done");
            showInfo("GIF created successfully.", "Success");

            if (!prefs.getBoolean(PREF_DONATION_SHOWN, false)) {
                prefs.putBoolean(PREF_DONATION_SHOWN, true);
                showDonationDialog();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            setProgressState(0, false, "Error");
            showError("Error during conversion:\n" + ex.getMessage(), "Conversion Error");
        } finally {
            if (paletteFile.exists() && !paletteFile.delete()) {
                System.out.println("Could not delete temporary palette file: " + paletteFile.getAbsolutePath());
            }
        }
    }

    private void showDonationDialog() {
        SwingUtilities.invokeLater(() -> {
            int result = JOptionPane.showConfirmDialog(
                    frmDomenico,
                    "Domenico successfully created your GIF.\n\n" +
                    "If this saved you time, consider supporting the project.",
                    "Support Domenico",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.INFORMATION_MESSAGE
            );

            if (result == JOptionPane.YES_OPTION) {
                openDonationLink();
            }
        });
    }

    private void openDonationLink() {
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(new URI(DONATION_URL));
            } else {
                showError("Desktop browsing is not supported on this system.", "Open Link Error");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            showError("Could not open donation link.", "Open Link Error");
        }
    }

    private double getVideoDuration(String inputPath) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(
                "ffprobe",
                "-v", "error",
                "-show_entries", "format=duration",
                "-of", "default=noprint_wrappers=1:nokey=1",
                inputPath
        );

        pb.redirectErrorStream(true);
        Process process = pb.start();

        String line;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            line = reader.readLine();
        }

        int exitCode = process.waitFor();

        if (exitCode != 0 || line == null || line.trim().isEmpty()) {
            throw new IOException("Could not determine video duration with ffprobe.");
        }

        return Double.parseDouble(line.trim());
    }

    private boolean runCommand(String[] command) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectErrorStream(true);

        Process process = pb.start();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            while (reader.readLine() != null) {
                // consume ffmpeg output
            }
        }

        int exitCode = process.waitFor();
        return exitCode == 0;
    }

    private void resetProgressBar() {
        SwingUtilities.invokeLater(() -> {
            progressBar.setIndeterminate(false);
            progressBar.setValue(0);
            progressBar.setString("Ready");
        });
    }

    private void setProgressState(int value, boolean indeterminate, String text) {
        SwingUtilities.invokeLater(() -> {
            progressBar.setIndeterminate(indeterminate);
            progressBar.setValue(value);
            progressBar.setString(text);
        });
    }

    private void showError(String message, String title) {
        SwingUtilities.invokeLater(() ->
                JOptionPane.showMessageDialog(frmDomenico, message, title, JOptionPane.ERROR_MESSAGE));
    }

    private void showInfo(String message, String title) {
        SwingUtilities.invokeLater(() ->
                JOptionPane.showMessageDialog(frmDomenico, message, title, JOptionPane.INFORMATION_MESSAGE));
    }

    private static void setSystemTheme() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }

    private static void setLightTheme() {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }

    private static void setDarkTheme() {
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }

    private void refreshUI() {
        SwingUtilities.updateComponentTreeUI(frmDomenico);
        frmDomenico.setSize(520, 340);
        frmDomenico.setLocationRelativeTo(null);
    }
}