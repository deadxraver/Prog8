package gui.frames.commandframes;

import clientlogic.ClientProgram;
import commands.client.ClientCommand;
import datapacks.RequestPackage;
import datapacks.ResponsePackage;
import elements.*;
import elements.Color;
import exceptions.NullFieldException;
import exceptions.NumberOutOfBoundsException;
import gui.GUIMain;
import gui.frames.CollectionFrame;
import gui.frames.MapFrame;
import parsers.IntParser;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

public abstract class MovieFrame extends JFrame {
	protected static ResourceBundle rb = ResourceBundle.getBundle("localization", GUIMain.currentLocale);
	protected JLabel nameLabel = new JLabel(rb.getString("m_name"));
	protected JTextField nameField = new JTextField();
	protected JLabel coordinatesLabel = new JLabel(rb.getString("coordinates"));
	protected JButton coordinatesButton = new JButton(rb.getString("open_map"));
	protected JLabel oscarsCountLabel = new JLabel(rb.getString("oscars_count"));
	protected JTextField oscarsCountField = new JTextField();
	protected JLabel genreLabel = new JLabel(rb.getString("genre"));
	protected JComboBox<MovieGenre> genreBox = new JComboBox<>(MovieGenre.values());
	protected JLabel mpaaLabel = new JLabel(rb.getString("mpaa"));
	protected JComboBox<MpaaRating> mpaaRatingBox = new JComboBox<>(getMpaaRatings());
	protected JLabel operatorLabel = new JLabel(rb.getString("operator"));
	protected JCheckBox operatorCheckBox = new JCheckBox();
	protected JLabel operatorNameLabel = new JLabel(rb.getString("name"));
	protected JTextField operatorNameField = new JTextField();
//	protected JLabel birthdayLabel = new JLabel(rb.getString("birthday"));
	protected JLabel dayLabel = new JLabel(rb.getString("birthday_day"));
	protected JComboBox<Integer> dayBox = new JComboBox<>(getDays());
	protected JLabel monthLabel = new JLabel(rb.getString("birthday_month"));
	protected JComboBox<Integer> monthBox = new JComboBox<>(getMonths());
	protected JLabel yearLabel = new JLabel(rb.getString("birthday_year"));
	protected JComboBox<Integer> yearBox = new JComboBox<>(getYears());
	protected JLabel hairColorLabel = new JLabel(rb.getString("hair_color"));
	protected JComboBox<Color> colorBox = new JComboBox<>(getColors());
	protected JLabel nationalityLabel = new JLabel(rb.getString("nationality"));
	protected JComboBox<Country> countryBox = new JComboBox<>(getCountries());
	protected JButton submitButton = new JButton(rb.getString("submit"));
	protected JLabel errorLabel = new JLabel("");
	protected Movie movie = new Movie(
			0,
			null,
			null,
			null,
			0,
			null,
			null,
			null,
			null
	);
	protected ClientProgram clientProgram;
	protected ClientCommand clientCommand;

	public MovieFrame(ClientProgram clientProgram, ClientCommand clientCommand) {
		super();
		this.clientCommand = clientCommand;
		this.clientProgram = clientProgram;
		placeElements();
	}

	protected void placeElements() {
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		setLocale(GUIMain.currentLocale);
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		gbc.gridy = 0;
		gbc.gridx = 0;
		add(nameLabel, gbc);
		gbc.gridy = 0;
		gbc.gridx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		add(nameField, gbc);
		gbc.gridy = 1;
		gbc.gridx = 0;
		add(coordinatesLabel, gbc);
		gbc.gridy = 1;
		gbc.gridx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		add(coordinatesButton, gbc);
		gbc.gridy = 2;
		gbc.gridx = 0;
		add(oscarsCountLabel, gbc);
		gbc.gridy = 2;
		gbc.gridx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		add(oscarsCountField, gbc);
		gbc.gridy = 3;
		gbc.gridx = 0;
		add(genreLabel, gbc);
		gbc.gridy = 3;
		gbc.gridx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		add(genreBox, gbc);
		gbc.gridy = 4;
		gbc.gridx = 0;
		add(mpaaLabel, gbc);
		gbc.gridy = 4;
		gbc.gridx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		add(mpaaRatingBox, gbc);
		gbc.gridy = 5;
		gbc.gridx = 0;
		add(operatorLabel, gbc);
		gbc.gridy = 5;
		gbc.gridx = 1;
		add(operatorCheckBox, gbc);


		gbc.gridy = 6;
		gbc.gridx = 0;
		operatorNameLabel.setVisible(false);
		add(operatorNameLabel, gbc);
		gbc.gridy = 6;
		gbc.gridx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		operatorNameField.setVisible(false);
		add(operatorNameField, gbc);
		gbc.gridy = 7;
		gbc.gridx = 0;
//		birthdayLabel.setVisible(false);
//		add(birthdayLabel);
//		gbc.gridy = 8;
//		gbc.gridx = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		dayLabel.setVisible(false);
		add(dayLabel, gbc);
		gbc.gridy = 7;
		gbc.gridx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		dayBox.setVisible(false);
		add(dayBox, gbc);
		gbc.gridy = 8;
		gbc.gridx = 0;
		monthLabel.setVisible(false);
		add(monthLabel, gbc);
		gbc.gridy = 8;
		gbc.gridx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		monthBox.setVisible(false);
		add(monthBox, gbc);
		gbc.gridy = 9;
		gbc.gridx = 0;
		yearLabel.setVisible(false);
		add(yearLabel, gbc);
		gbc.gridy = 9;
		gbc.gridx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		yearBox.setVisible(false);
		add(yearBox, gbc);
		gbc.gridy = 10;
		gbc.gridx = 0;
		hairColorLabel.setVisible(false);
		add(hairColorLabel, gbc);
		gbc.gridy = 10;
		gbc.gridx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		colorBox.setVisible(false);
		add(colorBox, gbc);
		gbc.gridy = 11;
		gbc.gridx = 0;
		nationalityLabel.setVisible(false);
		add(nationalityLabel, gbc);
		gbc.gridy = 11;
		gbc.gridx = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		countryBox.setVisible(false);
		add(countryBox, gbc);

		gbc.gridy = 12;
		gbc.gridx = 0;
		gbc.gridwidth = 3;
		errorLabel.setForeground(java.awt.Color.RED);
		add(errorLabel, gbc);
		gbc.gridy = 12;
		gbc.gridx = 3;
		add(submitButton, gbc);
		addListeners();
		pack();
	}

	protected void addListeners() {
		nameField.addActionListener(l -> {
			String text = nameField.getText().trim();
			if (text.isEmpty()){
				movie.setName(null);
				errorLabel.setText(rb.getString("blank_field"));
			}
			else {
				errorLabel.setText("");
				movie.setName(text);
			}
		});
		oscarsCountField.addActionListener(l -> {
			String text = oscarsCountField.getText().trim();
			int cnt = 0;
			try {
				cnt = Integer.parseInt(text);
				if (cnt < 1) throw new Exception();
				movie.setOscarsCount(cnt);
				errorLabel.setText("");
			} catch (Exception e) {
				errorLabel.setText(rb.getString("only_numbers"));
				if (cnt < 1) errorLabel.setText(rb.getString("positive"));
			}
		});
		coordinatesButton.addActionListener((l) -> {
			MapFrame.setMovie(movie);
			MapFrame.getInstance().setVisible(true);
			errorLabel.setText("");
		});
		operatorCheckBox.addActionListener((l) -> {
			boolean condition = operatorCheckBox.isSelected();
			operatorNameLabel.setVisible(condition);
			operatorNameField.setVisible(condition);
//			birthdayLabel.setVisible(condition);
			dayLabel.setVisible(condition);
			dayBox.setVisible(condition);
			monthLabel.setVisible(condition);
			monthBox.setVisible(condition);
			yearLabel.setVisible(condition);
			yearBox.setVisible(condition);
			hairColorLabel.setVisible(condition);
			colorBox.setVisible(condition);
			nationalityLabel.setVisible(condition);
			countryBox.setVisible(condition);
			errorLabel.setText("");
			pack();
		});
		dayBox.addActionListener(l -> {
			try {
				if (movie.getOperator() == null) {
					movie.setOperator(new Person(null, null, null, null));
				}
				movie.getOperator().setBirthday(dateActionListener());
				errorLabel.setText("");
			} catch (Exception e) {
				errorLabel.setText(rb.getString("date_format"));
			}
		});
		monthBox.addActionListener(l -> {
			try {
				if (movie.getOperator() == null) {
					movie.setOperator(new Person(null, null, null, null));
				}
				movie.getOperator().setBirthday(dateActionListener());
				errorLabel.setText("");
			} catch (Exception e) {
				errorLabel.setText(rb.getString("date_format"));
			}
		});
		yearBox.addActionListener(l -> {
			try {
				if (movie.getOperator() == null) {
					movie.setOperator(new Person(null, null, null, null));
				}
				movie.getOperator().setBirthday(dateActionListener());
				errorLabel.setText("");
			} catch (Exception e) {
				errorLabel.setText(rb.getString("date_format"));
			}
		});
		operatorNameField.addActionListener(l -> {
			String text = operatorNameField.getText().trim();
			if (text.isEmpty()) {
				errorLabel.setText(rb.getString("blank_field"));
				return;
			}
			if (movie.getOperator() == null) movie.setOperator(new Person(text, null, null, null));
			movie.getOperator().setName(text);
		});
		submitButton.addActionListener(l -> {
			String opName = operatorNameField.getText().trim();
			String movieName = nameField.getText().trim();
			try {
				movie.setOscarsCount(IntParser.parse(oscarsCountField.getText()));
			} catch (NumberOutOfBoundsException | NullFieldException e) {
				errorLabel.setText(rb.getString("only_numbers"));
			}
			if (operatorCheckBox.isSelected() && opName.isEmpty() || movie.getOscarsCount() < 1 || movie.getCoordinates() == null) {
				errorLabel.setText(rb.getString("blank_fields_left"));
				return;
			}
			try {
				synchronized (this) {
					clientProgram.send(new RequestPackage(CollectionFrame.username, CollectionFrame.password, clientCommand, movie));
					ResponsePackage responsePackage = clientProgram.receive();
					if (responsePackage.errorsOccurred()) {
						errorLabel.setText((String) responsePackage.response());
						throw new IOException();
					}
					movie = new Movie(
							0,
							null,
							null,
							null,
							0,
							null,
							null,
							null,
							null
					);
				}
				setVisible(false);
			} catch (IOException | ClassNotFoundException e) {
				errorLabel.setText(e.getLocalizedMessage());
			}
		});
	}

	protected LocalDateTime dateActionListener() throws Exception {
		Integer day = (Integer) dayBox.getSelectedItem();
		Integer month = (Integer) monthBox.getSelectedItem();
		Integer year = (Integer) yearBox.getSelectedItem();
		if (day == null && month == null && year == null) return null;
		if (day == null || month == null || year == null) throw new Exception();
		else return LocalDateTime.of(year, month, day, 0, 0);
	}

	protected static Integer[] getDays() {
		Integer[] days = new Integer[32];
		for (int i = 0; i < 32; i++) {
			days[i] = i == 0 ? null : i;
		}
		return days;
	}

	protected static Integer[] getMonths() {
		Integer[] months = new Integer[13];
		for (int i = 0; i < 13; i++) {
			months[i] = i == 0 ? null : i;
		}
		return months;
	}

	protected static Integer[] getYears() {
		Integer[] years = new Integer[111];
		for (int i = 0; i < 111; i++) {
			years[i] = i == 0 ? null : i + 1900;
		}
		return years;
	}

	protected static MpaaRating[] getMpaaRatings() {
		MpaaRating[] mpaaRatings = MpaaRating.values();
		MpaaRating[] mpaaRatings1 = new MpaaRating[mpaaRatings.length + 1];
		for (int i = 0; i < mpaaRatings1.length; i++) {
			mpaaRatings1[i] = i == 0 ? null : mpaaRatings[i - 1];
		}
		return mpaaRatings1;
	}

	protected static Color[] getColors() {
		Color[] colors = Color.values();
		Color[] colors1 = new Color[colors.length + 1];
		for (int i = 0; i < colors1.length; i++) {
			colors1[i] = i == 0 ? null : colors[i - 1];
		}
		return colors1;
	}

	protected static Country[] getCountries() {
		Country[] countries = Country.values();
		Country[] countries1 = new Country[countries.length + 1];
		for (int i = 0; i < countries1.length; i++) {
			countries1[i] = i == 0 ? null : countries[i - 1];
		}
		return countries1;
	}

	public void localeUpdate() {
		rb = ResourceBundle.getBundle("localization", GUIMain.currentLocale);
		nameLabel.setText(rb.getString("m_name"));
		coordinatesLabel.setText(rb.getString("coordinates"));
		oscarsCountLabel.setText(rb.getString("oscars_count"));
		genreLabel.setText(rb.getString("genre"));
		mpaaLabel.setText(rb.getString("mpaa"));
		operatorLabel.setText(rb.getString("operator"));
		operatorNameLabel.setText(rb.getString("name"));
		dayLabel.setText(rb.getString("birthday_day"));
		monthLabel.setText(rb.getString("birthday_month"));
		yearLabel.setText(rb.getString("birthday_year"));
		hairColorLabel.setText(rb.getString("hair_color"));
		nationalityLabel.setText(rb.getString("nationality"));
	}

}
