package observer;

import java.util.ArrayList;
import java.util.List;

public class ObserverTest {

    public final List<Integer> problems = new ArrayList<>();

    public void addObserver(Integer i){
        synchronized (problems){
            problems.add(i);
            if(i.equals(23)){
                removeObserver(i);
            }
        }
    }

    public void removeObserver(Integer i){
        synchronized (problems){
            problems.remove(i);
        }
    }

    public static void main(String[] args){

        ObserverTest currentProblem = new ObserverTest();

        for(int i = 0 ; i < 100 ; i++){
            currentProblem.addObserver(i);
        }
    }
}
