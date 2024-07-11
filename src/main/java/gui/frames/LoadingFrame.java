package gui.frames;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class LoadingFrame extends JFrame {
	private static final LoadingFrame loadingFrame = new LoadingFrame();

	private LoadingFrame() {
		setLocation(450, 200);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 3;
		gbc.gridy = 3;
		setSize(300, 300);
		URL url = Thread.currentThread().getContextClassLoader().getResource("loading.gif");
		Icon icon = new ImageIcon(url);
		JLabel loading = new JLabel(icon);
		loading.setSize(50, 50);
		add(loading, gbc);
	}

	public static LoadingFrame getInstance() {
		return loadingFrame;
	}
}
