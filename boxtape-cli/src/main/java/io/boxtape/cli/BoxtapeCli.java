package io.boxtape.cli;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"io.boxtape.cli", "io.boxtape.core"})
public class BoxtapeCli {
    public static void main(String[] args) {
        SpringApplication.run(BoxtapeCli.class,args);
    }
}
