package dev.xred.castremote;

import org.apache.juli.logging.Log;
import org.slf4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import su.litvak.chromecast.api.v2.ChromeCasts;

import java.io.IOException;

@SpringBootApplication
public class App {


    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

}
