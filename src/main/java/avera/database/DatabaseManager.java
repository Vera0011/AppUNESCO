package avera.database;
import avera.code.Monument;
import avera.parsers.XMLParser;
//import okhttp3.*;
import org.neo4j.driver.*;
import org.neo4j.driver.Record;
import org.neo4j.driver.exceptions.ClientException;


import java.io.IOException;
import java.util.*;

import static org.neo4j.driver.Values.parameters;

public abstract class DatabaseManager implements AutoCloseable
{
    private static Driver driver;
    private static final String database = "neo4j";
    private static final List<Monument> listadoMonumentos = XMLParser.parseFile();

    /**
     * Open connection to database
     * */
    public static void startConnection(String host, String user, String password)
    {
        driver = GraphDatabase.driver(host, AuthTokens.basic(user, password));
    }

    /**
     * Close connection to database
     * */
    @Override
    public void close() throws RuntimeException
    {
        driver.close();
    }

    /**
     * Create database if not exists
     * */
    public static void createDatabase()
    {
        try (var session = driver.session())
        {
            session.run(new Query("CREATE DATABASE " + database + " IF NOT EXISTS"));
            session.run(new Query("CREATE CONSTRAINT  IF NOT EXISTS FOR (mon:Monument) REQUIRE mon.name IS UNIQUE"));
            //session.run(new Query("CREATE CONSTRAINT IF NOT EXISTS FOR (mon:Monument) REQUIRE EXISTS(mon.name)"));
        }
    }

    /**
     * Insert initial data in database (from file)
     * */
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
    }

    /**
     * Create relations between the data in database (from file)
     * */
    public static void createRelations()
    {
        TreeSet<String> monumentosVisitados = new TreeSet<>();

        try(Session session = driver.session())
        {
            for (int i = 0; i < listadoMonumentos.size(); i++)
            {
                for (int j = i; j < listadoMonumentos.size(); j++)
                {
                    if(!monumentosVisitados.contains(listadoMonumentos.get(i).getName()))
                    {
                        int finalI = i;
                        int finalJ = j;
                        session.executeWriteWithoutResult(tx ->
                        {
                            tx.run("MATCH (n:Monument), (m:Monument) WHERE n.name = $node1 AND m.name = $node2 CREATE (n)-[:DISTANCE {km: $distance, time: $time}]->(m)",
                                    parameters("node1", listadoMonumentos.get(finalI).getName(),
                                            "node2", listadoMonumentos.get(finalJ).getName(), "distance", calculateDistance(listadoMonumentos.get(finalI), listadoMonumentos.get(finalJ))[0],
                                            "miliseconds", calculateDistance(listadoMonumentos.get(finalI), listadoMonumentos.get(finalJ))[1]));
                            System.out.println("Creada la relación entre " + listadoMonumentos.get(finalI).getName() + " y " + listadoMonumentos.get(finalJ).getName());
                        });
                    }
                }

                monumentosVisitados.add(listadoMonumentos.get(i).getName());
            }
        }
    }

    private static double[] calculateDistance(Monument n, Monument m)
    {
        /*OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url("https://maps.googleapis.com/maps/api/distancematrix/json?origins=Washington%2C%20DC&destinations=New%20York%20City%2C%20NY&units=imperial&key=YOUR_API_KEY")
                .method("GET", body)
                .build();
        Response response = client.newCall(request).execute();*/

        return new double[]{0.0};
    }

    /*private static double calculateTime()
    {

    }*/

    /**
     * Clean database
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
     * Make a query
     *
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
        startConnection("bolt://localhost:7687", "neo4j", "12345678");
        createDatabase();
        insertInitialData();
        //createRelations();

        //cleanDatabase();
    }
}
