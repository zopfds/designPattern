package Command;

/**
 * ${DESCRIPTION}
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/6/19
 */
public class Client {

    public static void main(String[] args){
        Document document = new Document();
        DocumentInvoker documentInvoker = new DocumentInvoker(new DisplayCommand(document) , new UndoCommand(document));

        documentInvoker.display();
        documentInvoker.undo();
    }
}
