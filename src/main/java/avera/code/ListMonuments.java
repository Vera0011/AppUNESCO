package avera.code;

import javafx.scene.control.Alert;
import avera.interfaces.scenes.MessageDisplayer;

import java.util.HashSet;
import java.util.LinkedHashSet;

public class ListMonuments
{
    private static HashSet<String> actualRoute;

    public static void addMonument(String monumentName)
    {
        if(actualRoute == null)
        {
            actualRoute = new HashSet<>();
        }

        if(actualRoute.size() > 15)
        {
            MessageDisplayer.displayMessage("Error añadiendo un monumento", null, Alert.AlertType.ERROR, "La máxima longotud permitida es de 15 monumentos");
        }
        else actualRoute.add(monumentName);
    }

    public static HashSet<String> getRoute ()
    {
        return actualRoute;
    }

    public static LinkedHashSet<String> getCalculation()
    {
        LinkedHashSet<String> result = new LinkedHashSet<>();

        return result;
    }

    public static void removeRoute (String entryName)
    {
        actualRoute.remove(entryName);
    }
}
