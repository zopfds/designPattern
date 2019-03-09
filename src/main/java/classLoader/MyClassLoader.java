package classLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MyClassLoader extends ClassLoader{

    private final static Path DEFAULT_CALSS_DIR = Paths.get("D:" , "classloader1");

    private final Path classDir;

    public MyClassLoader() {
        super();
        this.classDir = DEFAULT_CALSS_DIR;
    }

    public MyClassLoader(String classDir) {
        super();
        this.classDir = Paths.get(classDir);
    }

    public MyClassLoader(String classDir , ClassLoader classLoader) {
        super(classLoader);
        this.classDir = Paths.get(classDir);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {
//            if(!Paths.get(name).toFile().exists()){
//                throw new ClassNotFoundException("the class" + name + " not found!");
//            }
            byte[] classBytes = Files.readAllBytes(Paths.get(name + ".class"));
            if(classBytes == null || classBytes.length == 0){
                throw new ClassNotFoundException("Can not load the class" + name);
            }
            return this.defineClass("classLoader." + name.substring(name.lastIndexOf("\\") + 1) , classBytes , 0 , classBytes.length);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return super.findClass(name);
    }

    @Override
    public String toString() {
        return "MyClassLoader{" +
                "classDir=" + classDir +
                '}';
    }
}
