package server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;


/**
 * @author ty
 */
@EnableEurekaClient
@EnableConfigServer
@SpringBootApplication
public class CsApplication {
    public static void main(String[] args) {

        SpringApplication.run(CsApplication.class, args);

    }
}


