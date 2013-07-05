package digester.sample;

public class Book {
	private String author;
	private String title;

	public Book() {
		super();
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String toString() {
		return "Book: Author='" + author + "' Title='" + title + "'";
	}
}
