package org.jboss.as.quickstarts.ejb.multi.server;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class XmlToJava {

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException {
        String jbossHomeEnvironmentVariable;
        //static String installConfigXML= "C:\\EFACS\\InstallationFiles\\install-config.xml";
        String installConfigXML= "client/install-config.xml";
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        org.w3c.dom.Document document = builder.parse(new File(installConfigXML));
        Element rootElement = document.getDocumentElement();
        String un = getString("username", rootElement);
        System.out.println(un);
    }
    protected static String getString(String tagName, Element element) {
        NodeList list = element.getElementsByTagName(tagName);
        if (list != null && list.getLength() > 0) {
            NodeList subList = list.item(0).getChildNodes();

            if (subList != null && subList.getLength() > 0) {
                return subList.item(0).getNodeValue();
            }
        }

        return null;
    }

//    public static void main(String[] args) throws JAXBException {
//        JAXBContext jaxbContext = JAXBContext.newInstance(InstallConfig.class);
//        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
//        InstallConfig ic = (InstallConfig) jaxbUnmarshaller.unmarshal(new File(installConfigXML));
//        System.out.println(ic.getUserName());
//    }
//
//    public static void xmlToObject() throws JAXBException {
//        JAXBContext jaxbContext = JAXBContext.newInstance(InstallConfig.class);
//        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
//        InstallConfig ic = (InstallConfig) jaxbUnmarshaller.unmarshal(new File(installConfigXML));
//        System.out.println(ic.getUserName());
//    }
}