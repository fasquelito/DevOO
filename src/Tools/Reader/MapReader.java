/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Tools.Reader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.*;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author Aleks
 */


public class MapReader extends DefaultHandler{
    
    private PlanHandler mPlanHandler = new PlanHandler();
    private File mFile;
    
    public MapReader(String filePath) throws Exception {
     try{
        mFile = new File(filePath);
    }catch(Exception pce){
                        System.out.println("Le fichier est un string null");
                        throw new Exception("Le fichier est un string null");
			
        }
    }
    
    public void process() throws ParserConfigurationException, SAXException, IOException {
       try{
           SAXParserFactory fabrique = SAXParserFactory.newInstance();
       
        SAXParser parseur = fabrique.newSAXParser();
        parseur.parse(mFile, mPlanHandler);
       } catch(SAXException se){
			System.out.println("Erreur de parsing");
			System.out.println("une balise manque");
                        throw new SAXException("Erreur de parssage une balise est manquante");
        }catch(IOException ioe){
			System.out.println("Le fichier n'est pas trouvable");
                        throw new SAXException("Le fichier n'est pas trouvable");
	}
        
    }
    
    public List<Node> getNodes() {
        return mPlanHandler.mNodes;
    }

    public List<Edge> getEdges() {
        return mPlanHandler.mEdges;
    }
    
     public static class PlanHandler extends DefaultHandler{

        private List<Node> mNodes = new ArrayList<Node>();
        private List<Edge> mEdges = new ArrayList<Edge>();
        private Node mNode;


        public PlanHandler() {
            super();
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
           if (qName.equals("Noeud")) {
               int id = Integer.parseInt(attributes.getValue("id"));
               int x = Integer.parseInt(attributes.getValue("x"));
               int y = Integer.parseInt(attributes.getValue("y"));
               
               mNode = new Node(id, x, y); 
               mNodes.add(id, mNode);

           } else if(qName.equals("TronconSortant")) {

                    try{
                            String name = attributes.getValue("nomRue");
                            double speed = Float.parseFloat(attributes.getValue("vitesse").replace(",", "."));
                            double lenght = Float.parseFloat(attributes.getValue("longueur").replace(",", "."));
                            int destination = Integer.parseInt(attributes.getValue("destination"));
                            
                            Edge edge = new Edge(mNode.getId(), destination, name, speed, lenght);
                            mEdges.add(edge);
                            
                    }catch(Exception e){
                            //erreur, le contenu de id n'est pas un entier
                            throw new SAXException(e);
                    }
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if (qName.equals("Noeud")) {
               mNode = null;  
            }
        }
    }
    
    public static class Node {
            private final int mId, mX, mY;

            public Node(int id, int x, int y) {
                mId = id;
                mX = x;
                mY = y;
            }

            public int getId() {
                return mId;
            }

            public int getX() {
                return mX;
            }

            public int getY() {
                return mY;
            }

        }
        public static class Edge {
            private final int mNodeIdStart, mNodeIdFinish;
            private final double mSpeed, mLength;
            private final String mName;

            public Edge(int nodeIdStart, int nodeIdFinish, String name, double speed, double lenght) {
                mNodeIdStart = nodeIdStart;
                mNodeIdFinish = nodeIdFinish;
                mName = name;
                mSpeed = speed;
                mLength = lenght;
            }

            public String getName() {
                return mName;
            }

            public double getweight() {
                return mLength/mSpeed;
            }
            
            public int getNodeIdL() {
                return mNodeIdStart;
            }

            public int getNodeIdR() {
                return mNodeIdFinish;
            }        
        }
}