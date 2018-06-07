package Bridge;

public class Client {
    public static void main(String[] args){
        Brand lenovo = new LenovoBrand();
        Brand fangzheng = new FangZhengBrand();

        Computer lenovoLaptop = new Laptop(lenovo);
        Computer fangzhengLaptop = new Laptop(fangzheng);

        Computer lenovoPc = new PCcomputer(lenovo);
        Computer fangzhengPc = new PCcomputer(fangzheng);

        lenovoLaptop.printName();
        fangzhengLaptop.printName();
        lenovoPc.printName();
        fangzhengPc.printName();
    }
}
