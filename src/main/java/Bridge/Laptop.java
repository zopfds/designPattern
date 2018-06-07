package Bridge;

public class Laptop extends Computer{
    public Laptop(Brand brand) {
        super(brand);
    }

    @Override
    public void printName() {
        System.out.println("I am a " + super.getBrandName() + " laptop!");
    }
}
