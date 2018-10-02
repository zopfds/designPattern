package Assert;

public class AssertTest {

    public static void main(String[] args){

        boolean isUseAssert = false;


        assert isUseAssert: isUseAssert = true;

        if(!isUseAssert){
            throw new RuntimeException("assert not enable!");
        }

        System.out.println(isUseAssert);
    }
}
