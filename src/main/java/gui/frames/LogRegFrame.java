package gui.frames;

import commands.client.logreg.Login;
import gui.GUIMain;

import javax.swing.*;
import java.awt.*;
import java.net.ConnectException;
import java.util.Locale;
import java.util.ResourceBundle;

public class LogRegFrame extends JFrame {
	protected ResourceBundle localization = ResourceBundle.getBundle("localization", GUIMain.currentLocale);
	protected JLabel usernameLabel = new JLabel(localization.getString("username"));
	protected JTextField loginField = new JTextField(15);
	protected JLabel passwordLabel = new JLabel(localization.getString("password"));
	protected JPasswordField passwordField = new JPasswordField(15);
	protected JButton loginButton = new JButton(localization.getString("log_in"));
	protected JButton registerButton = new JButton(localization.getString("sign_up"));
	protected JComboBox<String> languageChoose = new JComboBox<>();
	protected Label errorLabel = new Label("");

	public LogRegFrame() {
		setLayout(new GridBagLayout());
		setLocation(450, 200);
		GridBagConstraints gbc = new GridBagConstraints();
		setLocale(GUIMain.currentLocale);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
//		setSize(400, 300);
		setTitle(localization.getString("welcome"));
		languageChoose.addItem("RU");
		languageChoose.addItem("ES_PR");
		languageChoose.addItem("IT");
		languageChoose.addItem("CS");
		languageChoose.setToolTipText("language");
		addEventListeners();

		gbc.gridx = 0;
		gbc.gridy = 0;

		add(usernameLabel, gbc);

		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		add(loginField, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		add(passwordLabel, gbc);

		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		add(passwordField, gbc);

		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		add(loginButton, gbc);

		gbc.gridx = 1;
		gbc.gridy = 3;
		add(registerButton, gbc);

//		gbc.insets = new Insets(1, 0, 0, 1);
		add(languageChoose);

		errorLabel.setForeground(Color.RED);
		gbc.anchor = GridBagConstraints.SOUTH;
		gbc.gridy = 5;
		gbc.gridx = 0;
		gbc.gridwidth = 2;
		add(errorLabel, gbc);
		pack();
	}

	private void addEventListeners() {
		languageChoose.addActionListener((actionEvent) -> {
			String lang = (String) ((JComboBox<?>) actionEvent.getSource()).getSelectedItem();
			assert lang != null;
			GUIMain.currentLocale = new Locale(lang);
			localization = ResourceBundle.getBundle("localization", GUIMain.currentLocale);
			usernameLabel.setText(localization.getString("username"));
			passwordLabel.setText(localization.getString("password"));
			loginButton.setText(localization.getString("log_in"));
			registerButton.setText(localization.getString("sign_up"));
			setTitle(localization.getString("welcome"));
		});
		setLocale(GUIMain.currentLocale);
		loginButton.addActionListener((actionEvent) -> {
			String username = loginField.getText();
			String password = passwordField.getPassword() == null ? null : new String(passwordField.getPassword());
			if (
					username != null && !username.isBlank()
							&& password != null && !password.isBlank()
			) {
				setVisible(false);
				LoadingFrame.getInstance().setVisible(true);
				this.setVisible(false);
				try {
					CollectionFrame collectionFrame = new CollectionFrame(this, new Login(), username, password);
					collectionFrame.setVisible(true);
					LoadingFrame.getInstance().setVisible(false);
				} catch (ConnectException ignored) {}
			}
			else if (username == null || username.isBlank()) {
				errorLabel.setText(localization.getString("blank_username_error"));
			} else {
				errorLabel.setText(localization.getString("blank_password_error"));
			}
		});
	}

}
