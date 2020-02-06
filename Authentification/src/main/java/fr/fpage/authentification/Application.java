package fr.fpage.authentification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    private static Database db = new Database("jdbc:mysql://localhost:3306/auth?useUnicode=true" +
            "&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&" +
            "serverTimezone=UTC", System.getenv("mysqlUser"), System.getenv("mysqlPassword"));

    public static void main(String[] args)
    {
        SpringApplication.run(Application.class, args);
    }

    public static Database getDb() {
        return db;
    }

}
