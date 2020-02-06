package fr.fpage.timemanagementapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@SpringBootApplication
public class Application {

    private static Database db = new Database("jdbc:mysql://localhost:3306/auth?useUnicode=true" +
            "&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&" +
            "serverTimezone=UTC", System.getenv("mysqlUser"), System.getenv("mysqlPassword"));

    private static String APITOKEN = "be9a1da9-dc91-4f54-b507-7fa6194f12f9";

    public static void main(String[] args)
    {
        SpringApplication.run(Application.class, args);
        initDb();
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
            PreparedStatement preparedStatement = connection.prepareStatement("CREATE DATABASE IF NOT EXISTS `timeManagement`");
            preparedStatement.execute();
            preparedStatement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `timeManagement`.`salles` ( `uuid` VARCHAR(40) NOT NULL , `name` VARCHAR(40) NULL , `size` INTEGER NULL , UNIQUE (`uuid`))");
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
