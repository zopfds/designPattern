package Command;

/**
 * ${DESCRIPTION}
 *
 * @author jianbo.pan@mljr.com
 * @version ${VERSION}
 * @create 2018/6/19
 */
public class DisplayCommand implements Command{
    private Document document;

    public DisplayCommand(Document document) {
        this.document = document;
    }

    @Override
    public void execute() {
        document.display();
    }
}
