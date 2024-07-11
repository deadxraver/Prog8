package gui.frames;

import clientlogic.ClientProgram;
import commands.client.*;
import datapacks.RequestPackage;
import elements.*;
import gui.ColorGenerator;
import gui.CustomTableModel;
import gui.FilterField;
import gui.GUIMain;
import gui.frames.commandframes.*;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.Color;
import java.io.IOException;
import java.net.ConnectException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.Vector;


public class CollectionFrame extends JFrame {
	public static String username;
	public static String password;
	protected ClientProgram clientProgram;
	protected JTable table;
	protected ResourceBundle rb = ResourceBundle.getBundle("localization", GUIMain.currentLocale);
	protected DefaultTableModel tm;
	protected Vector<Vector<Object>> data = new Vector<>();
	protected JComboBox<String> languageChoose = new JComboBox<>();
	protected final JButton addButton = new JButton(rb.getString("add"));
	protected final JButton addIfMaxButton = new JButton(rb.getString("add_if_max"));
	protected final JButton updateButton = new JButton(rb.getString("update"));
	protected final JButton removeLowerButton = new JButton(rb.getString("remove_lower"));
	protected final JButton clearButton = new JButton(rb.getString("clear"));
	protected final JButton removeAllByOscarsCountButton = new JButton(rb.getString("remove_all_by_oscars_count"));
	protected final JButton removeHeadButton = new JButton(rb.getString("remove_head"));
	protected final JLabel usernameLabel = new JLabel(rb.getString("username") + ": ");
	protected final JLabel superuserLabel = new JLabel(rb.getString("superuser") + ": ");
	protected JTextField regexFilter;
	protected TableRowSorter<TableModel> sorter;
	protected User user = null;

	protected AddFrame addFrame;
	protected AddIfMaxFrame addIfMaxFrame;
	protected RemoveLowerFrame removeLowerFrame;
	protected UpdateFrame updateFrame;
	protected RemoveByOscarsCountFrame rbocf;

	public CollectionFrame(LogRegFrame lrf, ClientCommand clientCommand, String username, String password) throws ConnectException {
		this.password = password;
		this.username = username;
		clientProgram = new ClientProgram(lrf, clientCommand, username, password);

		if (!clientProgram.login()) {
			lrf.setVisible(true);
			throw new ConnectException();
		}
		setDefaultCloseOperation(EXIT_ON_CLOSE);
//		setSize(800, 600);
		setLayout(new BorderLayout());
		Vector<String> headers = new Vector<>(Arrays.asList(
				rb.getString("id"),
				rb.getString("name"),
				rb.getString("owner"),
				rb.getString("creation_date"),
				rb.getString("oscars_count"),
				rb.getString("genre"),
				rb.getString("mpaa"),
				rb.getString("coordinates"),
				rb.getString("operator")
		));
		tm = new DefaultTableModel(
				data,
				headers
		) {
			private static final int COLUMN_ID = 0;
			private static final int COLUMN_NAME = 1;
			private static final int COLUMN_OWNER = 2;
			private static final int COLUMN_CREATION_DATE = 3;
			private static final int COLUMN_OSCARS_COUNT = 4;
			private static final int COLUMN_GENRE = 5;
			private static final int COLUMN_MPAA = 6;
			private static final int COLUMN_COORDINATES = 7;
			private static final int COLUMN_OPERATOR = 8;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}

			@Override
			public Object getValueAt(int row, int index) {
				Object o = data.get(row).get(index);
				return switch (index) {
					case COLUMN_ID -> (Long) o;
					case COLUMN_NAME, COLUMN_OPERATOR, COLUMN_OWNER -> (String) o;
					case COLUMN_CREATION_DATE -> (LocalDate) o;
					case COLUMN_OSCARS_COUNT -> (Integer) o;
					case COLUMN_GENRE -> (MovieGenre) o;
					case COLUMN_MPAA -> (MpaaRating) o;
					case COLUMN_COORDINATES -> (Coordinates) o;
					default -> null;
				};
			}

			@Override
			public Class<?> getColumnClass(int index) {
				return switch (index) {
					case COLUMN_ID -> Long.class;
					case COLUMN_NAME, COLUMN_OPERATOR, COLUMN_OWNER -> String.class;
					case COLUMN_CREATION_DATE -> LocalDate.class;
					case COLUMN_OSCARS_COUNT -> Integer.class;
					case COLUMN_GENRE -> MovieGenre.class;
					case COLUMN_MPAA -> MpaaRating.class;
					case COLUMN_COORDINATES -> Coordinates.class;
					default -> null;
				};
			}
		};

		table = new JTable(tm);
		table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
				final Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
				Color color = ColorGenerator.generateColor((String) table.getValueAt(row, 2));
				c.setBackground(color);
				c.setForeground(ColorGenerator.getContrastColor(color));
				return c;
			}
		});
		sorter = new TableRowSorter<>(table.getModel());
		table.setRowSorter(sorter);
		add(new JScrollPane(table), BorderLayout.CENTER);
		addFrame = new AddFrame(clientProgram, new Add(null));
		addIfMaxFrame = new AddIfMaxFrame(clientProgram, new AddIfMax(null));
		removeLowerFrame = new RemoveLowerFrame(clientProgram, new RemoveLower(null));
		updateFrame = new UpdateFrame(clientProgram, new Update(null), table);
		rbocf = new RemoveByOscarsCountFrame(clientProgram);
		processElements();
		prepareUserSpace();

		pack();
		Thread t = new Thread(this::receiveCollection);
		t.start();
	}

	private void receiveCollection() {
		while (true) {
			try {
				synchronized (this) {
					clientProgram.send(new RequestPackage(
							username,
							password,
							new Show(null),
							null
					));
					MovieCollection movieCollection = (MovieCollection) clientProgram.receive().response();
					processTable(movieCollection);
				}
				synchronized (this) {
					clientProgram.send(new RequestPackage(
							username,
							password,
							new Info(),
							null
					));
					user = (User) clientProgram.receive().response();
					setUserInfo();
				}
				Thread.sleep(5000);
			} catch (IOException | ClassNotFoundException | InterruptedException ignored) {
			}
		}
	}

	private void processTable(MovieCollection movieCollection) {
		data.clear();
		for (Object o : movieCollection.getCollection()) {
			Movie movie = (Movie) o;
			data.add(new Vector<>(Arrays.asList(
					movie.getId(),
					movie.getName(),
					movie.getOwner().getUsername(),
					movie.getCreationDate(),
					movie.getOscarsCount(),
					movie.getGenre(),
					movie.getMpaaRating(),
					movie.getCoordinates(),
					movie.getOperator() == null ? null : movie.getOperator().getName()
			)));
		}

		tm.fireTableDataChanged();
	}

	private void processElements() {
		processButtons();
		regexFilter = FilterField.createRowFilter(table);
		languageChoose.addItem("RU");
		languageChoose.addItem("ES_PR");
		languageChoose.addItem("IT");
		languageChoose.addItem("CS");
		languageChoose.setToolTipText("language");
		languageChoose.setSelectedItem(GUIMain.currentLocale.toLanguageTag());
		add(languageChoose, BorderLayout.NORTH);
		add(regexFilter, BorderLayout.NORTH);
	}

	private void setUserInfo() {
		Color color = ColorGenerator.generateColor(user.getUsername());
		usernameLabel.setForeground(color);
		usernameLabel.setOpaque(true);
		usernameLabel.setBackground(ColorGenerator.getContrastColor(color));
		usernameLabel.setText(rb.getString("username") + ": " + user.getUsername());
		superuserLabel.setText(rb.getString("superuser") + ": " + rb.getString(((Boolean) user.isSuperuser()).toString()));
	}

	private void prepareUserSpace() {
		Panel panel = new Panel(new GridLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		panel.add(usernameLabel, gbc);
		panel.add(superuserLabel, gbc);
		add(panel, BorderLayout.SOUTH);
	}

	private void processButtons() {
		GridBagConstraints gbc = new GridBagConstraints();
		Panel panel = new Panel(new GridBagLayout());
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		panel.add(addButton, gbc);
		addButton.addActionListener(l -> addFrame.setVisible(true));
		gbc.gridy = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		panel.add(updateButton, gbc);
		updateButton.addActionListener(l -> updateFrame.process());
		gbc.gridy = 2;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		panel.add(addIfMaxButton, gbc);
		addIfMaxButton.addActionListener(l -> addIfMaxFrame.setVisible(true));
		add(panel, BorderLayout.WEST);

		Panel panel1 = new Panel(new GridBagLayout());
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		panel1.add(clearButton, gbc);
		clearButton.addActionListener(l -> {
			try {
				synchronized (this) {
					clientProgram.send(new RequestPackage(username, password, new Clear(null), "-m"));
					clientProgram.receive();
				}
			} catch (Exception ignored){}
		});
		gbc.gridy = 1;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		panel1.add(removeAllByOscarsCountButton, gbc);
		removeAllByOscarsCountButton.addActionListener(l -> rbocf.setVisible(true));
		gbc.gridy = 2;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		panel1.add(removeHeadButton, gbc);
		removeHeadButton.addActionListener(l -> {
			try {
				synchronized (this) {
					clientProgram.send(new RequestPackage(username, password, new RemoveHead(null), "-m"));
					clientProgram.receive();
				}
			} catch (IOException | ClassNotFoundException ignored) {
			}
		});
		gbc.gridy = 3;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		panel1.add(removeLowerButton, gbc);
		removeLowerButton.addActionListener(l -> removeLowerFrame.setVisible(true));
		add(panel1, BorderLayout.EAST);
	}

}
