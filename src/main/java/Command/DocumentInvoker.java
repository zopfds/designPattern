package Command;

/**
 * ${DESCRIPTION}
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/6/19
 */
public class DocumentInvoker {

    private DisplayCommand displayCommand;

    private UndoCommand undoCommand;

    public DocumentInvoker(DisplayCommand displayCommand, UndoCommand undoCommand) {
        this.displayCommand = displayCommand;
        this.undoCommand = undoCommand;
    }

    public void display(){
        displayCommand.execute();
    }

    public void undo(){
        undoCommand.execute();
    }
}
