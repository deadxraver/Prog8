package gui.frames.commandframes;

import clientlogic.ClientProgram;
import commands.client.RemoveAllByOscarsCount;
import datapacks.RequestPackage;
import gui.GUIMain;
import gui.frames.CollectionFrame;

import javax.swing.*;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.ResourceBundle;

public class RemoveByOscarsCountFrame extends JFrame {
	ResourceBundle rb = ResourceBundle.getBundle("localization", GUIMain.currentLocale);
	protected JButton submitButton = new JButton(rb.getString("submit"));
	protected JTextField numberField = new JTextField();
	protected ClientProgram clientProgram;
	public RemoveByOscarsCountFrame(ClientProgram clientProgram) {
		this.clientProgram = clientProgram;
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		add(numberField, gbc);
		gbc.gridy = 3;
		add(submitButton, gbc);
		pack();
		submitButton.addActionListener(l -> {
			try {
				synchronized (this) {
					clientProgram.send(new RequestPackage(
							CollectionFrame.username,
							CollectionFrame.password,
							new RemoveAllByOscarsCount(null),
							numberField.getText()
					));
					clientProgram.receive();
				}
				setVisible(false);
			} catch (Exception ignored) {}
		});
	}
}
