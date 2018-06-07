package Component;

import java.util.List;

public abstract class AbstractShape {

    private List<AbstractShape> childs = null;

    private String shapeName;

    public AbstractShape(String shapeName , List<AbstractShape> childs){
        this.shapeName = shapeName;
        this.childs = childs;
    }

    public void printShape(){
        System.out.println("I am " + shapeName + "! ");
        if(this.childs != null){
            System.out.println("Those are my childs:");
            childs.stream().forEach(abstractShape -> abstractShape.printShape());
            System.out.println("Finish " + shapeName + "! ");
        }

    }

    public void addChild(AbstractShape abstractShape) throws Exception{
        if(this.childs != null){
            this.childs.add(abstractShape);
        }
    }

    public void removeChild(AbstractShape abstractShape) throws Exception{
        if(this.childs != null) {
            this.childs.remove(abstractShape);
        }
    }
}
