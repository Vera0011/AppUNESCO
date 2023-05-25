package avera.database;

import org.neo4j.driver.*;
import org.neo4j.driver.Record;
import org.neo4j.driver.exceptions.ClientException;

import java.util.*;


/**
 * Class charged of database management
 *
 * @author Vera
 * */
public final class DatabaseManager implements AutoCloseable
{
    private static Driver driver;

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
    public void close()
    {
        driver.close();
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
    public static Object[] query(String query, String type) throws ClientException
    {
        Session session = driver.session();

        try
        {
            if(type.equalsIgnoreCase("SELECT"))
            {
                return new Object[]{ session.run(query), session };
            }
            else
            {
                session.executeWriteWithoutResult(tx ->
                {
                    tx.run(query);
                });

                session.close();
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

        return new Object[]{ null, session };
    }
}
