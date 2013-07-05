package digester.sample;

import java.util.Vector;

public class Catalog {
	private Vector books;
	private Vector magazines;
	private String library;

	public String getLibrary() {
		return library;
	}

	public void setLibrary(String library) {
		this.library = library;
	}

	public Catalog() {
		books = new Vector();
		magazines = new Vector();
	}

	public void addBook(Book rhs) {
		books.addElement(rhs);
	}

	public void addMagazine(Magazine rhs) {
		magazines.addElement(rhs);
	}

	public String toString() {
		String newline = System.getProperty("line.separator");
		StringBuffer buf = new StringBuffer();

		buf.append("--- Books ---").append(newline);
		for (int i = 0; i < books.size(); i++) {
			buf.append(books.elementAt(i)).append(newline);
		}

		buf.append("--- Magazines ---").append(newline);
		for (int i = 0; i < magazines.size(); i++) {
			buf.append(magazines.elementAt(i)).append(newline);
		}

		return buf.toString();
	}
}
