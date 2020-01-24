package com.feedme.exam.queue.write.feed;

import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.Map;
import java.util.TreeMap;


public class FeedmeTypeFactory {
    private static FeedmeTypeFactory ourInstance = new FeedmeTypeFactory();

    public static FeedmeTypeFactory getInstance() {
        return ourInstance;
    }

    private FeedmeTypeFactory() {
    }
    
    public Map<String, FeedmeType> getFeedmeTypes(String uri){
        Map<String, FeedmeType> typeMap = new TreeMap();
        
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(uri);
            doc.getDocumentElement().normalize();

            FeedmeFieldFactory ffFactory = FeedmeFieldFactory.getInstance();

            Element rootElement = doc.getDocumentElement();
            NodeList typeNodes = rootElement.getChildNodes();
            for (int idx = 0; idx < typeNodes.getLength(); idx++) {
                Node node = typeNodes.item(idx);
                if (Node.ELEMENT_NODE == node.getNodeType()) {
                    String typeName = node.getNodeName();
                    FeedmeType type = new FeedmeType(typeName);

                    NodeList subNodes = node.getChildNodes();
                    for (int subIdx = 0; subIdx < subNodes.getLength(); subIdx++) {
                        Node subNode = subNodes.item(subIdx);
                        if (Node.ELEMENT_NODE == subNode.getNodeType()) {
                            String sectionName = subNode.getNodeName();
                            NodeList fieldNodes = subNode.getChildNodes();
                            for (int fIdx = 0; fIdx<fieldNodes.getLength(); fIdx++) {
                                Node fieldNode = fieldNodes.item(fIdx);
                                if (Node.ELEMENT_NODE == fieldNode.getNodeType()) {
                                    NamedNodeMap attribs = fieldNode.getAttributes();

                                    FeedmeField field = ffFactory.getFieldBySpecification(attribs);
                                    type.addFieldToSection(sectionName, field);
                                }
                            }
                        }
                    }
                    typeMap.put(typeName, type);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return typeMap;
    }
    
    public static void main(String[] args) {
        FeedmeTypeFactory factory = FeedmeTypeFactory.getInstance();
        Map<String, FeedmeType> map = factory.getFeedmeTypes("http://localhost:8181/types");
        for (String type : map.keySet()) {
            System.out.println("name="+type);
        }
    }
}

