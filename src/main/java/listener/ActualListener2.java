package listener;

import java.io.*;

public class ActualListener2 implements Listener{

    @Override
    public void listenAndDoSth(Event e){

        FileWriter fw = null;

        try {
            fw = new FileWriter("D://test.txt" , true);

            fw.append(e.getWorld());

            fw.flush();

        }catch(IOException ie) {
            System.out.println(ie.toString());
        }finally {
            if(fw != null){
                try {
                    fw.close();
                } catch (IOException e1) {
                    System.out.println(e1.toString());
                }
            }
        }
    }
}
