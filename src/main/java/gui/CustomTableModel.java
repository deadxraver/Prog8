package gui;

import elements.Movie;

import javax.swing.table.DefaultTableModel;
import java.util.LinkedList;

public class CustomTableModel extends DefaultTableModel {
	private static final int COLUMN_ID = 0;
	private static final int COLUMN_NAME = 1;
	private static final int COLUMN_OWNER = 2;
	private static final int COLUMN_CREATION_DATE = 3;
	private static final int COLUMN_OSCARS_COUNT = 4;
	private static final int COLUMN_GENRE = 5;
	private static final int COLUMN_MPAA = 6;
	private static final int COLUMN_COORDINATES = 7;
	private static final int COLUMN_OPERATOR = 8;
	private final String[] columnNames = {"id", "name", "owner", "creation_date", "oscars_count", "genre", "mpaa", "coordinates", "operator"};
	private final LinkedList<Movie> movieList = new LinkedList<>();

	public String[] getColumnNames() {
		return columnNames;
	}

	public LinkedList<Movie> getMovieList() {
		return movieList;
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

//	@Override
//	public int getRowCount() {
//		return movieList.size();
//	}

	@Override
	public String getColumnName(int columnIndex) {
		return columnNames[columnIndex];
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (movieList.isEmpty()) {
			return Object.class;
		}
		return getValueAt(0, columnIndex).getClass();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Movie movie = movieList.get(rowIndex);
		return switch (columnIndex) {
			case COLUMN_ID -> movie.getId();
			case COLUMN_NAME -> movie.getName();
			case COLUMN_OWNER -> movie.getOwner();
			case COLUMN_CREATION_DATE -> movie.getCreationDate();
			case COLUMN_OSCARS_COUNT -> movie.getOscarsCount();
			case COLUMN_GENRE -> movie.getGenre();
			case COLUMN_MPAA -> movie.getMpaaRating();
			case COLUMN_COORDINATES -> movie.getCoordinates();
			case COLUMN_OPERATOR -> movie.getOperator();
			default -> throw new IllegalArgumentException("Invalid column index");
		};
	}

}
