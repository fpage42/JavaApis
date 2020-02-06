package fr.fpage.conservatoireuserapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@SpringBootApplication
public class Application {

    private static Database db = new Database("jdbc:mysql://localhost:3306/?useUnicode=true" +
            "&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&" +
            "serverTimezone=UTC", System.getenv("mysqlUser"), System.getenv("mysqlPassword"));

    private static String APITOKEN = "be9a1da9-dc91-4f54-b507-7fa6194f12f9";

    public static void main(String[] args)
    {
        System.out.println("Test de dl git");
        initDb();
        SpringApplication.run(Application.class, args);
    }

    public static Database getDb() {
        return db;
    }

    public static String getAPITOKEN() {
        return APITOKEN;
    }

    private static void initDb()
    {
        try {
            Connection connection = Application.getDb().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("CREATE DATABASE IF NOT EXISTS `conservatoireUsers`");
            preparedStatement.execute();
            preparedStatement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `conservatoireUsers`.`users` ( `uuid` VARCHAR(40) NOT NULL , `firstName` VARCHAR(40) NULL , `lastName` VARCHAR(40) NULL , UNIQUE (`uuid`))");
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
