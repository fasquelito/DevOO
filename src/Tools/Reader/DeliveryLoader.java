
package Tools.Reader;

import Controller.SaxHandler;
import Model.Area;
import Model.DeliveryPoint;
import Model.Itinary;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
/**
 *
 * @author Aleks
 */
public class DeliveryLoader {
    
    private final DeliveryHandler mDeliveryHandler;
    private final File mFile;
    
    public DeliveryLoader(String filePath, Area area) throws Exception {
        try{
                mFile = new File(filePath);
                mDeliveryHandler = new DeliveryHandler(area);
        }
        catch(Exception pce){
                        System.out.println("Le fichier est un string null");
                        throw new Exception("Le fichier est un string null");
			
        }
    }
    
    public void process() throws ParserConfigurationException, SAXException, IOException {
        try{
            SAXParserFactory fabrique = SAXParserFactory.newInstance();
            SAXParser parseur = fabrique.newSAXParser();
            SaxHandler gestionnaire = new SaxHandler();
            parseur.parse(mFile, mDeliveryHandler);  
        }
        catch(SAXException se){
			System.out.println("Erreur de parsing");
			System.out.println("une balise manque");
                        throw new SAXException("Erreur de parssage une balise est manquante");
        }catch(IOException ioe){
			System.out.println("Le fichier n'est pas trouvable");
                        throw new SAXException("Le fichier n'est pas trouvable");
	}
                
                
        //Pas erreur si  mauvais nom fichier
    }
    
    
    public static class DeliveryHandler extends DefaultHandler{
        
        private Area mArea;
        
        private Itinary mItinary;
        
        public DeliveryHandler(Area area){
            super();
            mArea = area;
        }
   
        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            
            if (qName.equals("Entrepot")) {
                mArea.setWareHouse(attributes.getValue("adresse"));
                
            } else if(qName.equals("Plage")) {
                Date start;
                Date end;

                
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                    start = sdf.parse(attributes.getValue("heureDebut"));
                    end = sdf.parse(attributes.getValue("heureFin"));
                    mItinary = mArea.addItinary(start, end);
                    
                } catch(ParseException e) {
                      System.out.println("Erreur de parssage sur l'heure de début ou de fin");
                }
                
            } else if(qName.equals("Livraison")) {
                    String deliveryId = attributes.getValue("id");
                    String cliendId = attributes.getValue("client");
                    String deliveryAdress = attributes.getValue("adresse");
             
                    mArea.addDelivery(mItinary, cliendId, deliveryAdress);
            }
            else{
		//erreur, on peut lever une exception
		//System.out.println("Balise "+qName+" inconnue.");
	}
        }
    }
    
    /*private static class Delivery {
        private final String mAdress;
        private final String mIdClient;

        public Delivery(String adress, String idClient) {
            mAdress = adress;
            mIdClient = idClient;
        }
        
        public String getAdress() {
            return mAdress;
        }

        public String getIdClient() {
            return mIdClient;
        }
        
        
    }*/
}
