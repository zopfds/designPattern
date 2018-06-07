package Bridge;

public abstract class Computer {

    private Brand brand;

    public String getBrandName(){
        return brand.getBrandName();
    }

    public Computer(Brand brand){
        this.brand = brand;
    }

    public abstract void printName();
}
