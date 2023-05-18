package avera.database;
import avera.code.Monument;
import avera.parsers.XMLParser;
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
    private static List<Monument> listadoMonumentos;
    private static OkHttpClient client = new OkHttpClient();
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
        listadoMonumentos = XMLParser.parseFile();

        //createDatabase();
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

    /*
     * Initial modifiers of the database
     *
    public static void createDatabase()
    {
        try (var session = driver.session())
        {
            //session.run(new Query("CREATE DATABASE " + database + " IF NOT EXISTS"));
            session.run(new Query("CREATE CONSTRAINT  IF NOT EXISTS FOR (mon:Monument) REQUIRE mon.name IS UNIQUE"));
            //session.run(new Query("CREATE CONSTRAINT IF NOT EXISTS FOR (mon:Monument) REQUIRE EXISTS (mon.name)"));
        }
    }*/

    /*
     * Insert initial data in database (from XML file)
     *
     * @see avera.parsers.XMLParser
     *
    public static void insertInitialData()
    {
        try (Session session = driver.session())
        {
            for (Monument item : listadoMonumentos)
            {
                session.executeWriteWithoutResult(tx ->
                {
                    Result result = tx.run("CREATE (element:Monument) SET element.name = $name, element.description = $description," +
                                    "element.location = $location, element.category = $category, element.httplink = $httplink," +
                                    "element.latitude = $latitude, element.longitude = $longitude, element.region = $region," +
                                    "element.state = $state RETURN element.name",
                            parameters("name", item.getName(), "description", item.getDescription(), "location", item.getLocation(),
                                    "category", item.getCategory(), "httplink", item.getHttpLink(), "latitude", item.getLatitude(),
                                    "longitude", item.getLongitude(), "region", item.getRegionMonument(), "state", item.getStateMonument()));
                    System.out.println("Insertado el valor " + result.list().get(0).get(0));
                });
            }
        }
        catch(ClientException e)
        {
            System.out.println("Error al introducir datos: " + e.getMessage());
        }
    }*/

    /**
     * Create relations between the data in database (from XML file)
     *
     * @see avera.parsers.XMLParser
     * */
    public static void createRelations()
    {
        HashSet<String> monumentosVisitados = new HashSet<>();

        try(Session session = driver.session())
        {
            for (int i = 0; i < listadoMonumentos.size(); i++)
            {
                monumentosVisitados.add(listadoMonumentos.get(i).getName());

                for (int j = 0; j < listadoMonumentos.size(); j++)
                {
                    if(!monumentosVisitados.contains(listadoMonumentos.get(j).getName()))
                    {
                        int finalI = i;
                        int finalJ = j;
                        /*double[] distancesAndTime = calculateDistance(listadoMonumentos.get(finalI), listadoMonumentos.get(finalJ));

                        session.executeWriteWithoutResult(tx ->
                        {
                            tx.run("MATCH (n:Monument), (m:Monument) WHERE n.name = $node1 AND m.name = $node2 CREATE (n)-[:DISTANCE {km: $distance, time: $time}]->(m)",
                                    parameters("node1", listadoMonumentos.get(finalI).getName(),
                                            "node2", listadoMonumentos.get(finalJ).getName(), "distance", (distancesAndTime[0] / 1000),
                                            "time", distancesAndTime[1]));
                            System.out.println("Creada la relaci?n entre " + listadoMonumentos.get(finalI).getName() + " y " + listadoMonumentos.get(finalJ).getName());
                        });*/
                    }
                }
            }
        }
    }

    /**
     * Calculate distance between two monument coordinates (using Google Maps API)
     *
     * @param n Monument 1
     * @param m Monument 2
     * */
    private static double[] calculateDistance(Monument n, Monument m, Monument n2)
    {
        double latitudeN = n.getLatitude();
        double longitudeN = n.getLongitude();

        double latitudeM = m.getLatitude();
        double longitudeM = m.getLongitude();

        double latituden2 = n2.getLatitude();
        double longituden2 = n2.getLongitude();

        String url="https://maps.googleapis.com/maps/api/distancematrix/xml" +
                "?origins=" + latitudeN + "," + longitudeN + "" +
                "&destinations=" + latitudeM + "," + longitudeM + "|" +
                latituden2 + "," + longituden2 + "" +
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

        return new double[]{ 0, 0 };
    }

    /**
     * Clean all nodes and relations from the database
     * */
    public static void cleanDatabase()
    {
        try(Session session = driver.session())
        {
            session.executeWriteWithoutResult(tx ->
            {
                tx.run("MATCH(n) DETACH DELETE n");
                System.out.println("Limpiada la base de datos");
            });
        }
    }

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

    public static void main (String[] args) throws IOException {
        Dotenv dotenv = Dotenv.load();
        startConnection(dotenv.get(""), dotenv.get(""), dotenv.get(""));
        System.out.println(dotenv.get("GOOGLE_API"));
        //createRelations();
        //cleanDatabase();
    }
}
