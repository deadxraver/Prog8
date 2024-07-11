package gui;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class FilterField {
	public static JTextField createRowFilter(JTable table) {
		RowSorter<? extends TableModel> rs = table.getRowSorter();
		if (rs == null) {
			table.setAutoCreateRowSorter(true);
			rs = table.getRowSorter();
		}

		TableRowSorter<? extends TableModel> rowSorter =
				(rs instanceof TableRowSorter) ? (TableRowSorter<? extends TableModel>) rs : null;


		final JTextField tf = new JTextField(15);
		tf.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				update(e);
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				update(e);
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				update(e);
			}

			private void update(DocumentEvent e) {
				String text = tf.getText();
				if (text.isBlank()) {
					rowSorter.setRowFilter(null);
				} else {
					rowSorter.setRowFilter(RowFilter.regexFilter(text));
				}
			}
		});

		return tf;
	}
}
