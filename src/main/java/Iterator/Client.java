package Iterator;

/**
 * ${DESCRIPTION}
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/6/8
 */
public class Client {

    public static void main(String[] args){
        Bookshelf bookshelf = new Bookshelf(3);
        bookshelf.addBook(new Book("生命不能承受之轻","米兰-昆德拉"));
        bookshelf.addBook(new Book("人类群星闪耀时","茨维格"));
        bookshelf.addBook(new Book("java高并发实战","Doug Lea"));

        Iterator iterator = bookshelf.iterator();

        while(iterator.hasNext()){
            Book book = (Book)iterator.next();
            System.out.println("bookName = " + book.getName() + " , author = " + book.getAuthor());
        }
    }
}
