import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class Parser {

    public Set<Map<String, String>> getXMLData(String xmlInput, String tag) {

        Document doc = null;
        DocumentBuilder documentbuilder;

        InputSource is = new InputSource();
        is.setCharacterStream(new StringReader(xmlInput));
        Set<Map<String, String>> tagSet = new HashSet<Map<String, String>>();
        try {
            documentbuilder = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder();
            doc = documentbuilder.parse(is);

            NodeList list = doc.getElementsByTagName(tag);
            for (int temp = 0; temp < list.getLength(); temp++) {
                Map<String, String> tagMap = new LinkedHashMap<String, String>();
                Node nNode = list.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    NodeList nodeList = eElement.getElementsByTagName("*");
                    for (int i = 0; i < nodeList.getLength(); i++) {
                        tagMap.put(
                                nodeList.item(i).getNodeName(),
                                getTagValues(nodeList.item(i).getNodeName(),
                                        eElement));
                    }
                    tagSet.add(tagMap);
                }
            }
        } catch (SAXException ex) {
            ex.getStackTrace();
        } catch (IOException ex) {
            ex.getStackTrace();
        } catch (ParserConfigurationException ex) {
            ex.getStackTrace();
        }
        return tagSet;
    }

    public String getTagValues(String tagname, Element elt) {
//        if (logger.isDebugEnabled()) {
//            logger.debug("Starting getTagValues method at time:::" + new Date());
//        }
        NodeList nlList =
                elt.getElementsByTagName(tagname);

        if (nlList != null && nlList.getLength() > 0) {
            NodeList subList = nlList.item(0).getChildNodes();
            if (subList != null && subList.getLength() > 0) {
//                logger.info("Tag Value: "+subList.item(0).getNodeValue());
                return subList.item(0).getNodeValue();
            }
        }
        return null;
    }
}
