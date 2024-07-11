package gui.frames.commandframes;

import clientlogic.ClientProgram;
import commands.client.ClientCommand;
import datapacks.RequestPackage;
import elements.Movie;
import gui.frames.CollectionFrame;
import gui.frames.ErrorFrame;

import javax.swing.*;
import java.time.LocalDateTime;

public class UpdateFrame extends MovieFrame{
	private JTable table;
	public UpdateFrame(ClientProgram clientProgram, ClientCommand clientCommand, JTable table) {
		super(clientProgram, clientCommand);
		this.table = table;
	}

	public void process() {
		if (table.getSelectedRowCount() != 1) {
			ErrorFrame.show(rb.getString("select_one"));
			return;
		}
		Long id = (Long) table.getValueAt(table.getSelectedRow(), 0);
		try {
			synchronized (this) {
				clientProgram.send(new RequestPackage(
						CollectionFrame.username,
						CollectionFrame.password,
						clientCommand,
						id.toString()
				));
				movie = clientProgram.receive().movie();
			}
			setComponents();
			setVisible(true);
		} catch (Exception ignored) {
		}
	}

	private void setComponents() {
		nameField.setText(movie.getName());
		oscarsCountField.setText(((Integer)movie.getOscarsCount()).toString());
		genreBox.setSelectedItem(movie.getGenre());
		mpaaRatingBox.setSelectedItem(movie.getMpaaRating());
		if (movie.getOperator() != null) {
			operatorCheckBox.setSelected(true);
			operatorNameField.setText(movie.getOperator().getName());
			LocalDateTime b = movie.getOperator().getBirthday();
			if (b != null) {
				dayBox.setSelectedItem(b.getDayOfMonth());
				monthBox.setSelectedItem(b.getMonthValue());
				yearBox.setSelectedItem(b.getYear());
			}
			colorBox.setSelectedItem(movie.getOperator().getHairColor());
			countryBox.setSelectedItem(movie.getOperator().getNationality());
		}
	}
}
