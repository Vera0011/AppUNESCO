package avera.database;
import io.github.cdimascio.dotenv.Dotenv;
import okhttp3.*;
import org.neo4j.driver.*;
import org.neo4j.driver.Record;
import org.neo4j.driver.exceptions.ClientException;

import java.io.IOException;
import java.util.*;

import static org.neo4j.driver.Values.parameters;


/**
 * Class charged of database management
 *
 * @author Vera
 * */
public abstract class DatabaseManager implements AutoCloseable
{
    private static Driver driver;
    private static Dotenv dotenv = Dotenv.load();

    /**
     * Open connection to database
     *
     * @param host URL of the host that contains the database
     * @param user User credential
     * @param password Password credential
     * */
    public static void startConnection(String host, String user, String password)
    {
        driver = GraphDatabase.driver(host, AuthTokens.basic(user, password));

        System.out.println("Conexión a la base de datos exitosa");
    }

    /**
     * Close connection to database
     * */
    @Override
    public void close() throws RuntimeException
    {
        driver.close();
    }

        /*String url="https://maps.googleapis.com/maps/api/distancematrix/xml" +
                "?origins=" + latitudeN + "," + longitudeN + "" +
                "&destinations=" + latitudeM + "," + longitudeM +
                "&units=imperial&language=es&key="+ dotenv.get("GOOGLE_API");
        Request request = new Request.Builder().url(url).build();

        try(Response response = client.newCall(request).execute())
        {
            System.out.println(response.body().string());
            //return XMLParser.parsePetition(response.body());
        }
        catch (IOException | NullPointerException e)
        {
            System.out.println("Error al obtener datos: " + e.getMessage());
        }

        return new double[]{ 0, 0 };*/

    /**
     * Make a query in the database
     *
     * @param query Query in String
     * @param type Type of the query
     *
     * @throws ClientException If the query is invalid
     * @return ArrayList of the query
     * */
    public static Map<String, List<String>> query(String query, String type) throws ClientException
    {
        Map<String, List<String>> list = new HashMap<>();

        try(Session session = driver.session())
        {
            if(type.equalsIgnoreCase("SELECT"))
            {
                Result result = session.run(query);

                while (result.hasNext())
                {
                    Record entry = result.next();
                    List<String> listAttributes = new ArrayList<>();

                    for (int i = 1; i < entry.size(); i++)
                    {
                        listAttributes.add(entry.get(i).asString());
                    }

                    list.put(entry.get(0).asString(), listAttributes);
                }

                return list;
            }
            else
            {
                session.executeWriteWithoutResult(tx ->
                {
                    tx.run(query);
                });
            }
        }

        return list;
    }
}
