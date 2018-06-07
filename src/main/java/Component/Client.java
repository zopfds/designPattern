package Component;

import java.util.ArrayList;
import java.util.List;

public class Client {

    public static void main(String[] args){
        List<AbstractShape> list = new ArrayList<>();
        list.add(new Line());
        list.add(new Line());
        list.add(new Line());
        AbstractShape triangle = new Triangle(list);
        List<AbstractShape> list1 = new ArrayList<>();
        list1.add(new Line());
        list1.add(new Line());
        list1.add(new Line());
        list1.add(new Line());
        AbstractShape quadrangle = new Quadrangle(list1);

        List<AbstractShape> list2 = new ArrayList<>();
        list2.add(triangle);
        list2.add(quadrangle);
        list2.add(new Line());


        AbstractShape multipartShape = new MulitipartShapre("多边形集合",list2);
        multipartShape.printShape();
    }
}
