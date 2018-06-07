package Bridge;

public class PCcomputer extends Computer{


    public PCcomputer(Brand brand) {
        super(brand);
    }

    @Override
    public void printName() {
        System.out.println("I am a " + super.getBrandName() + " PC!");
    }
}
