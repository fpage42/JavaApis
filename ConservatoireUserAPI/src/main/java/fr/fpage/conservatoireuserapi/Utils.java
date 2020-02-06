package fr.fpage.conservatoireuserapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.fpage.conservatoireuserapi.authentification.AuthUser;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Utils {

    public static <T> T jsonApiCallObject(String apiUrl, String token, String method, Class<T> object)
    {
        T ret = null;
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Authorization", token);
            con.setRequestMethod(method);
            con.setDoOutput(true);
            con.connect();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            con.disconnect();
            ret = new ObjectMapper().readValue(content.toString(), object);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }
}
