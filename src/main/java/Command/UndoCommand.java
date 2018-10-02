package Command;

/**
 * ${DESCRIPTION}
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/6/19
 */
public class UndoCommand implements Command{
    private Document document;

    public UndoCommand(Document document) {
        this.document = document;
    }

    @Override
    public void execute() {
        document.undo();
    }
}
