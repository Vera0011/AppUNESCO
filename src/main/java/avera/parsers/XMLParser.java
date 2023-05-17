package avera.parsers;

import java.io.File;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import avera.code.Monument;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

/**
 * Parser for converting XML file to Monuments Class
 *
 * @author Vera
 * */

public class XMLParser
{
    private static final String pathToXMLFile = "src/main/resources/dataMonuments.xml";

    /**
     * Parser function
     *
     * @return List A list of the Monuments generated from the XML file
     * */
    public static List<Monument> parseFile()
    {
        File xmlFile = new File(pathToXMLFile);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        List<Monument> listMonuments = new ArrayList<>();

        try
        {
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("row");

            for (int i = 0; i < nodeList.getLength(); i++)
            {
                listMonuments.add(getMonument(nodeList.item(i)));
            }

        }
        catch (ParserConfigurationException | SAXException | IOException e)
        {
            System.out.println("Error cargando fichero XML");
        }


        return listMonuments;
    }

    private static Monument getMonument(Node node)
    {
        Monument newMonument = new Monument();

        if(node.getNodeType() == Node.ELEMENT_NODE)
        {
            Element element = (Element) node;

            try
            {
                if(!element.getElementsByTagName("site").item(0).getTextContent().isBlank())
                {
                    newMonument.setName(element.getElementsByTagName("site").item(0).getTextContent());
                    newMonument.setLocation(element.getElementsByTagName("location").item(0).getTextContent());
                    newMonument.setCategory(element.getElementsByTagName("category").item(0).getTextContent());
                    newMonument.setHttpLink(element.getElementsByTagName("http_url").item(0).getTextContent());
                    newMonument.setLatitude(Double.parseDouble((element.getElementsByTagName("latitude").item(0).getTextContent())));
                    newMonument.setLongitude(Double.parseDouble(element.getElementsByTagName("longitude").item(0).getTextContent()));
                    newMonument.setRegionMonument(element.getElementsByTagName("region").item(0).getTextContent());
                    newMonument.setStateMonument(element.getElementsByTagName("states").item(0).getTextContent());
                    newMonument.setDescription(element.getElementsByTagName("short_description").item(0).getTextContent());
                }
            }
            catch(DOMException | NullPointerException | NumberFormatException e)
            {
                System.out.println("Error importando el elemento: " + e.getMessage());
            }

        }

        return newMonument;
    }
}
