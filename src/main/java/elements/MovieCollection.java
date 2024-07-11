package elements;

import exceptions.AccessException;
import exceptions.EmptyCollectionException;
import exceptions.NoSuchMovieException;
import serverlogic.DBManipulation;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArrayList;

public class MovieCollection implements Serializable {
	public MovieCollection(Movie... movies) {
		collection = new CopyOnWriteArrayList<>();
		collection.addAll(Arrays.asList(movies));
	}

	private final CopyOnWriteArrayList<Movie> collection;

	public Movie getElement(long id) throws NoSuchMovieException {
		for (Movie movie : collection) {
			if (movie.getId() == id) return movie;
		}
		throw new NoSuchMovieException();
	}

	public void replace(long id, Movie newMovie, User user) {
		if (DBManipulation.replaceMovie(id, newMovie, user)){
			for (Movie movie : collection) {
				if (movie.getId() == id) {
					collection.set(collection.indexOf(movie), newMovie);
					return;
				}
			}
		}
	}

	public Movie getElement(long id, User user) throws NoSuchMovieException, AccessException {
		for (Movie movie : collection) {
			if (movie.getId() == id) {
				if (!movie.belongsTo(user) && !user.isSuperuser()) throw new AccessException();
				return movie;
			}
		}
		throw new NoSuchMovieException();
	}

	public int getNumberOfMovies() {
		return collection.size();
	}

	public int getNumberOfMovies(User user) {
		if (user == null) return collection.size();
		int n = 0;
		for (Movie movie : collection) {
			if (movie.belongsTo(user)) {
				n++;
			}
		}
		return n;
	}

	public String getCollectionType() {
		return Movie.class.getSimpleName();
	}

	public void addMovie(User user, Movie movie) throws SQLException {
		if (DBManipulation.addMovie(user, movie)) collection.add(movie);
		else throw new SQLException();
	}

	public void addMovie(Movie movie) {
		collection.add(movie);
	}

	public void removeMovie(long id, User user) throws NoSuchMovieException, AccessException {
		for (Movie movie : collection) {
			if (movie.getId() == id) {
				if (!movie.belongsTo(user) && !user.isSuperuser()) throw new AccessException();
				else {
					collection.remove(movie);
					DBManipulation.deleteMovie(user, movie);
				}
				return;
			}
		}
		throw new NoSuchMovieException();
	}

	public void removeMovie(Movie movie, User user) throws AccessException {
		if (movie.belongsTo(user)) {
			collection.remove(movie);
		}
		else throw new AccessException();
	}

	public void clear() throws SQLException {
		if (DBManipulation.truncate()) collection.clear();
		else throw new SQLException();
	}

	public void clear(User user) throws SQLException {
		if (user == null) {
			clear();
		} else {
			for (Movie movie : collection) {
				if (movie.belongsTo(user)) {
					collection.remove(movie);
					DBManipulation.deleteMovie(user, movie);
				}
			}
		}
	}

	public Movie getMax(User user) throws EmptyCollectionException {
		long id = 0;
		Movie maxMovie = null;
		for (Movie movie : collection) {
			if (movie.getId() > id && movie.belongsTo(user)) {
				maxMovie = movie;
				id = movie.getId();
			}
		}
		if (maxMovie == null) throw new EmptyCollectionException();
		return maxMovie;
	}

	public Movie getMax() throws EmptyCollectionException {
		return getMax(null);
	}

	public boolean removeLower(Movie movie, User user) {
		boolean foundLower = false;
		for (Movie movie1 : collection) {
			if (movie.getCoordinates().getLength() >
					movie1.getCoordinates().getLength() && movie1.belongsTo(user)) {
				collection.remove(movie1);
				if (!DBManipulation.deleteMovie(user, movie1)) return false;
				foundLower = true;
			}
		}
		return foundLower;
	}

	public boolean removeByOscar(int number, User user) {
		boolean found = false;
		for (Movie movie : collection) {
			if (movie.getOscarsCount() == number && movie.belongsTo(user)) {
				try {
					this.removeMovie(movie, user);
				} catch (AccessException ignored) {
				}
				found = true;
			}
		}
		return found;
	}

	public Movie getMpaaMax() throws EmptyCollectionException {
		for (Movie movie : collection) {
			if (movie.getMpaaRating() == MpaaRating.NC_17) {
				return movie;
			}
		}
		for (Movie movie : collection) {
			if (movie.getMpaaRating() == MpaaRating.PG) {
				return movie;
			}
		}
		for (Movie movie : collection) {
			if (movie.getMpaaRating() == MpaaRating.R) {
				return movie;
			}
		}
		throw new EmptyCollectionException();
	}

	public Object[] getOperatorList() {
		ArrayList<Person> list = new ArrayList<>();
		for (Movie movie : collection) {
			if (movie.getOperator() != null) list.add(movie.getOperator());
		}
		return list.toArray();
	}

	public Object[] getCollection() {
		return collection.toArray();
	}

	@Override
	public String toString() {
		StringBuilder string = new StringBuilder();
		for (Movie movie : collection) {
			string.append(movie).append("\n");
		}
		return string.toString();
	}
}
