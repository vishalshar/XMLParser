/**
 *
 * @author Vishal Sharma
 *    
 * Organize the extracted code samples as shown below. For example:

 samples/
 |-- ch1
 |   `-- foo.php
 `-- ch2
     `-- qux.php
   
 * 
 */
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.jdom2.Content;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;
import org.xml.sax.SAXException;

public class XMLParser {

    static Boolean debug = false;

    public static void main(String[] args) {

        /**
         * XML file name to Parse
         * 
         * Choose either for parsing
         * */
        
        //String fileName = "book.xml";
        String fileName = "1fadb512.xml";
        
        try {
                /**
                 * check for the book or courselet
                 *
                 * if book 
                 * then 
                 *  get all chapter names search in courselet folder
                 *  and call parseXML
                 *
                 * if not book 
                 *  call parseXML with XML data
                 */
            
                File xmlFile = null;
                if (fileName.equals("book.xml")) {
                    xmlFile = new File("C:\\Users\\Vishal\\Documents\\NetBeansProjects\\GuruLabs\\src\\gurulabs\\GL468\\book.xml");
                    
                    //create folder
                    new File("C:\\Users\\Vishal\\Documents\\NetBeansProjects\\GuruLabs\\samples").mkdir();
                    
                    //Build xml reader
                    SAXBuilder builder = new SAXBuilder();
                    Document document = (Document) builder.build(xmlFile);
                    Element book = document.getRootElement();
                    
                    //get all list of chapter
                    List<Element> list = book.getChildren("chapter");
                    
                    if(debug)
                        System.out.println(list.size());
                    
                    /**
                     * Go through all chapters and get all XML file name
                     * 
                     * Keep track of the Chapter and its count
                     */
                    int i=0;     // count of folder               
                    while(i<list.size())
                    {
                        Element chapter = list.get(i);
                        List<Element> courselet = chapter.getChildren("courselet");
                        for(int j=0;j<courselet.size();j++)
                        {
                            
                            Element course = courselet.get(j);
                            String xmlFileName = course.getAttributeValue("href");
                            
                            if(debug)
                                System.out.println("chapter "+ (i+1) +" "+xmlFileName);
                            
                            //create folder and parse XML
                            new File("C:\\Users\\Vishal\\Documents\\NetBeansProjects\\GuruLabs\\samples\\ch"+(i+1)).mkdir();
                            parseXML(xmlFileName,"C:\\Users\\Vishal\\Documents\\NetBeansProjects\\GuruLabs\\samples\\ch"+(i+1)+"\\");
                        }
                        i++;
                    }
                } 
                
                else {
                    //create folder
                    String loc = "C:\\Users\\Vishal\\Documents\\NetBeansProjects\\GuruLabs\\test\\";
                    new File(loc).mkdir();
                    parseXML(fileName,loc);
                }

        } catch (IOException io) {
            System.out.println(io.getMessage());
        } catch (JDOMException jdomex) {
            System.out.println(jdomex.getMessage());
        }
    }

    public static void parseXML(String xmlFileName, String folder) throws IOException, JDOMException {
        
        File xmlFile = new File("C:\\Users\\Vishal\\Documents\\NetBeansProjects\\GuruLabs\\src\\gurulabs\\GL468\\courselets\\" + xmlFileName);
        
        /**
         * Get the edit node and pass list to generateOutput
         */
        SAXBuilder builder = new SAXBuilder();
        Document document = (Document) builder.build(xmlFile);
        Element rootNode = document.getRootElement();

        Element text = rootNode.getChild("text");
        Element body = text.getChild("body");
        List list = body.getChildren("edit");

        if (debug) {
            System.out.println("list size " + list.size());
        }

        generateOutput(list, folder);
    }

    public static void generateOutput(List list, String folder) throws IOException {
        for (int i = 0; i < list.size(); i++) {

            if (debug) {
                System.out.println("In edit");
            }

            Element node = (Element) list.get(i);

            String file = node.getChildText("eh");
            String data = node.getChildText("body");

            File test = new File(folder + file);
            BufferedWriter output = new BufferedWriter(new FileWriter(test));
            output.write(data.trim());

            output.close();
        }
    }
}
