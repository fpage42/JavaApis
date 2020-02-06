package fr.fpage.timemanagementapi.managers;

import fr.fpage.timemanagementapi.Application;
import fr.fpage.timemanagementapi.exceptions.BddLoadException;
import fr.fpage.timemanagementapi.models.Salle;
import fr.fpage.timemanagementapi.requests.CreateSalleRequest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class SallesManager {
    private static SallesManager ourInstance = new SallesManager();

    public static SallesManager getInstance() {
        return ourInstance;
    }

    private HashMap<UUID, Salle> salles = new HashMap<>();

    private SallesManager() {
    }

    public static Salle[] getAllSalles() {
        List<Salle> salles = new ArrayList<>();
        try
        {
            Connection connection = Application.getDb().getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT uuid FROM `timeManagement`.`salles`");
            ResultSet res = preparedStatement.executeQuery();
            while (res.next())
            {
                try {
                    salles.add(new Salle(UUID.fromString(res.getString("uuid"))));
                } catch (BddLoadException ignored) {
                }
            }
            connection.close();
        }
        catch (SQLException e)
        {
            System.out.println(e.getSQLState());
        }
        return salles.toArray(new Salle[0]);
    }

    public Salle createSalle(CreateSalleRequest createSalleRequest)
    {
        return new Salle(createSalleRequest);
    }

    public Salle getSalle(UUID uuid) throws BddLoadException {
        if (!salles.containsKey(uuid))
            salles.put(uuid, new Salle(uuid));
        return salles.get(uuid);
    }
}
