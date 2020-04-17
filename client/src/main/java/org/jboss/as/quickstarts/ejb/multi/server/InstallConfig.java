package org.jboss.as.quickstarts.ejb.multi.server;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;


@XmlRootElement(name = "install-config")
public class InstallConfig {

    public InstallConfig() throws IOException, SAXException, ParserConfigurationException {
        xmlToJava();
    }

    String username;
    String password;
    String dbinfo;

    public static void setErrorCode(int i) {
    }

    public String getUserName() {
        return username;
    }

    public void setUserName(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDbInfo() {
        return dbinfo;
    }

    public void setDbInfo(String dbinfo) {
        this.dbinfo = dbinfo;
    }

    public void xmlToJava() throws ParserConfigurationException, IOException, SAXException { //TODO atomise this into separate methods
        String jbossHomeEnvironmentVariable; //TODO get from JBOSS_DIST windows env var
        //static String installConfigXML= "C:\\EFACS\\InstallationFiles\\install-config.xml";
        String installConfigXML = "client/install-config.xml";
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        org.w3c.dom.Document document = builder.parse(new File(installConfigXML));
        Element rootElement = document.getDocumentElement();

        setUserName(getString("username", rootElement));
        setPassword(getString("password", rootElement));
        setDbInfo(getString("dbinfo", rootElement));
        //TODO add other setters e.g. last build number
        //TODO consider siutations with different appserver
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
}


