package files;

import gamelogic.Board;
import gamelogic.Square;
import gamelogic.UnitFactory;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import gamelogic.Player;

/**
 * @author Arnaud Bellec
 * Represents a game scenario and all data linked to it like
 * player, board, unit data as well as the success/failure
 * conditions.
 * A Scenario can only created via a corresponding XML file.
 */
public class Scenario {
	protected File file;
	protected Document doc;
	
	/**
	 * Creates a new Scenario object using a XML file.
	 * @param f File object containing the XML file.
	 * @throws IOException An IO Exception thrown by the f argument on reading.
	 */
	public Scenario(File f) throws IOException
	{
		file = f;
		load();
	}
	
	/**
	 * Loads the scenario to the given board.
	 * @param b Board to load the scenario into.
	 */
	public void loadBoard(Board b)
	{
		Vector<Player> pList;
		
		try{
			NodeList players = doc.getElementsByTagName("player");
			pList = new Vector<Player>();
			for(int i=0; i<players.getLength(); i++)
				pList.add(Integer.decode(players.item(i).getAttributes().getNamedItem("id").getTextContent()).intValue(),
						new Player(players.item(i).getAttributes().getNamedItem("name").getTextContent(),
								colorFromText(players.item(i).getAttributes().getNamedItem("color").getTextContent())));
		
			NodeList units = doc.getElementsByTagName("unit");
			for(int i=0; i<units.getLength(); i++)
			{
				Player owner = pList.get(Integer.decode(units.item(i).getAttributes().getNamedItem("owner").getTextContent()));
				Square position = new Square(
						Integer.decode(units.item(i).getChildNodes().item(1).getAttributes().getNamedItem("x").getTextContent()).intValue(), 
						Integer.decode(units.item(i).getChildNodes().item(1).getAttributes().getNamedItem("y").getTextContent()).intValue());
				b.addUnit(UnitFactory.createUnitByName(units.item(i).getAttributes().getNamedItem("type").getTextContent(), owner), position.x, position.y);
			}
		}
		catch(NullPointerException e)
		{
			System.out.println("Malformed scenario file");
			System.out.println(e);
		}
	}
	
	/**
	 * Gets the description that is contained in the XML file.
	 * @return the description, as a String object
	 */
	public String getDescription()
	{
		return doc.getElementsByTagName("description").item(0).getTextContent();
	}
	
	/**
	 * Gets the world file in which the scenario happens.
	 * @return the world file's filename
	 */
	public String getWorld()
	{
		return doc.getElementsByTagName("world").item(0).getTextContent();
	}
	
	/**
	 * Gets the name of the scenario. Each scenario should have
	 * a unique name, so that it can be used to identify them.
	 * @return the name of the scenario, as a String object
	 */
	public String getName()
	{
		return doc.getElementsByTagName("name").item(0).getTextContent();
	}
	
	/**
	 * Loads the XML file into a Document object.
	 */
	protected void load() throws IOException
	{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		
		try 
		{
			DocumentBuilder docBuilder = factory.newDocumentBuilder();
			doc = docBuilder.parse(file);
		} 
		catch (ParserConfigurationException e) 
		{
			e.printStackTrace();
		}
		catch (SAXException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * For testing purposes.
	 * @param args Unused
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException
	{
		File f = new File("/home/kbok/jmax_res/levels/test0.xml");
		Scenario s = new Scenario(f);
		System.out.println(s.getName());
		System.out.println(s.getDescription());
		s.loadBoard(null);
	}
	
	/**
	 * Converts a named-color into a Color object.
	 * Named-colors can be either a full-name standard color such as "blue" or "green"
	 * or a RGB hex color prefixed by a sharp, like #FF6633.
	 * @param color The color as a String.
	 */
	protected Color colorFromText(String color) {
		/* OK, I lied */
		if(color.equals("blue"))    return Color.blue;
		if(color.equals("red"))     return Color.red;
		if(color.equals("green"))   return Color.green;
		if(color.equals("yellow"))  return Color.yellow;
		if(color.equals("magenta")) return Color.magenta;
		if(color.equals("cyan"))    return Color.cyan;
		if(color.equals("black"))   return Color.black;
		if(color.equals("white"))   return Color.white;
		
		return Color.pink;
	}
}
