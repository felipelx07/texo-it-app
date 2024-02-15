package it.texo.app;


import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class Run {

    public static void main(String... args) {
        System.out.println("Running TexoIT App");
        Quarkus.run(args);
    }
}
