package com.craftware.gmailfilter;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class Dom4JParser {

    private File file;

    public Dom4JParser(File file) {
        this.file = file;
    }



    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }



    public void generateModifiedDocument3(String filenameDestination) {
        try {
            SAXReader reader = new SAXReader();
            Document document = reader.read(file);

            List<Node> extractBaseNodes = extractBaseNodes(document);



            List<Node> nodes = document.selectNodes("//entry");

            System.out.printf("document %s%n", document.asXML());



            Element newRoot = DocumentHelper.createElement("feed");
            extractBaseNodes.forEach(baseNode -> {
                newRoot.add((Node) baseNode.clone());
            });




            for (Node node : nodes) {
                System.out.printf("node : %s%n", node.asXML());


                Element element = (Element) node;
                Iterator<Element> iterator = element.elementIterator("property");
                System.out.println("##############");
                while (iterator.hasNext()) {
                    System.out.println("##############");
                    Element labelElement = (Element) iterator.next();
                    System.out.printf("next -> :%s%n", labelElement.asXML());
                    //labelElement.setText(labelElement.getText() + " updated");
                    System.out.println("Attribute value: " + labelElement.attributeValue("name"));

                    if ("label".equals(labelElement.attributeValue("name"))) {
                        String labelValue = labelElement.attributeValue("value");
                        List<String> allLevelsSplit = new ArrayList<>(Arrays.asList(labelValue.split("/")));
                        int originalSize = allLevelsSplit.size();
                        List<String> labelsToCreate = new ArrayList<>();

                        for (int i = 0; i < originalSize; i++) {
                            //remove last Element
                            String lastRemoved = allLevelsSplit.remove(allLevelsSplit.size() - 1);
                            System.out.printf("allLevelsSplit after: %s - %d%n",allLevelsSplit,allLevelsSplit.size());
                            String toAdd = String.join("/", allLevelsSplit);
                            if(allLevelsSplit.size() == 0){
                                //toAdd = lastRemoved;
                                //System.out.printf("adding and breaking %s%n",toAdd);
//                                labelsToCreate.add(toAdd);
                                break;
                            }
                            System.out.printf("adding %s%n",toAdd);
                            labelsToCreate.add(toAdd);
                        }

                        labelsToCreate.forEach(level -> {
                            labelElement.addAttribute("value", level);
                            newRoot.add((Node) node.clone());
                        });
                    }
                }


            }

//            XMLWriter writer = new XMLWriter(new FileWriter(new File("src/test/resources/example_dom4j_updated2.xml")));
//            writer.write(document);
//            writer.close();
//
            XMLWriter writer2 = new XMLWriter(new FileWriter(new File(filenameDestination)),OutputFormat.createPrettyPrint());
            writer2.write(DocumentHelper.createDocument(newRoot));
            writer2.close();

        } catch (DocumentException e) {
            e.printStackTrace();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private List<Node> extractBaseNodes(Document document) {
        List<Node> returnList = new ArrayList<>();

        Node titleNode = document.selectSingleNode("/feed/title");
        System.out.printf("found title:%s %n", titleNode.getStringValue());

        Node idNode = document.selectSingleNode("/feed/id");
        System.out.printf("found idNode:%s %n", idNode.getStringValue());

        Node updatedNode = document.selectSingleNode("/feed/updated");
        System.out.printf("found updatedNode:%s %n", updatedNode.getStringValue());

        Node authorNode = document.selectSingleNode("/feed/author");
        System.out.printf("found authorNode:%s %n", authorNode.getStringValue());

        returnList.add(titleNode);
        returnList.add(idNode);
        returnList.add(updatedNode);
        returnList.add(authorNode);

        return returnList;
    }
}
