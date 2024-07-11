package gui.frames;

import javax.swing.*;
import java.awt.*;

public class ErrorFrame extends JFrame {
	private static final ErrorFrame ERROR_FRAME = new ErrorFrame();
	private final JLabel errorLabel = new JLabel("error");
	private final JButton button = new JButton("OK");
	private ErrorFrame() {
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 1;
		add(errorLabel, gbc);
		gbc.gridy = 2;
		gbc.gridx = 1;
		add(button, gbc);
		button.addActionListener((l) -> ERROR_FRAME.setVisible(false));
		pack();
	}

	public static void show(String text) {
		ERROR_FRAME.errorLabel.setText(text);
		ERROR_FRAME.setVisible(true);
	}
}
