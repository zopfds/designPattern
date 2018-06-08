package Iterator;

import java.util.Arrays;

/**
 * ${DESCRIPTION}
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/6/8
 */
public class Bookshelf implements Aggregate{

    private Book[] books;

    private int last;

    public Bookshelf(int booksSize) {
        this.books = new Book[booksSize];
    }

    public Book getBookAt(int index){
        return books[index];
    }

    public void addBook(Book book){
       books[last++] = book;
    }

    public void removeBook(Book book){
        Arrays.stream(books).anyMatch(book1 -> {
            return book1.getName().equals(book.getName());
        });
    }

    public int getLength(){
        return books.length;
    }

    @Override
    public Iterator iterator() {
        return new BookIterator();
    }

    private class BookIterator implements Iterator{

        private int index;

        @Override
        public boolean hasNext() {
            return index < books.length && index > -1;
        }

        @Override
        public Object next() {
            return getBookAt(index++);
        }
    }
}
