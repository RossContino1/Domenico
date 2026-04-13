import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class HelpBox extends JDialog {

    private static final long serialVersionUID = 1L;

    public HelpBox(Frame owner) {
        super(owner, "Domenico Help", true);

        setSize(500, 450);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout());

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font("SansSerif", Font.PLAIN, 14));

        textArea.setText(
                "DOMENICO HELP\n\n" +
                "What this app does:\n" +
                "Domenico converts short MP4 or MOV video clips into animated GIFs.\n\n" +

                "IMPORTANT:\n" +
                "- Only videos 10 seconds or less are allowed.\n" +
                "- Longer videos will be rejected.\n\n" +

                "How to use:\n" +
                "1. Click 'Browse' and select a video file.\n" +
                "2. The output GIF path will be filled automatically.\n" +
                "3. Click 'Convert'.\n\n" +

                "What happens during conversion:\n" +
                "- A palette is generated for better GIF quality.\n" +
                "- The GIF is created using that palette.\n" +
                "- Temporary files are automatically deleted.\n\n" +

                "Requirements:\n" +
                "- FFmpeg must be installed.\n" +
                "- FFprobe must be installed.\n\n" +

                "Tips:\n" +
                "- Short clips (3–6 seconds) give the best results.\n" +
                "- Lower FPS or scale can reduce file size.\n\n" +

                "Website:\n" +
                "bytesbreadbbq.com/domenico\n"
        );

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(480, 350));
        add(scrollPane, BorderLayout.CENTER);

        JButton btnClose = new JButton("Close");
        btnClose.addActionListener(e -> dispose());
        add(btnClose, BorderLayout.SOUTH);
    }
}