package com.github.ffcfalcos.logger.view;

import com.github.ffcfalcos.logger.trace.CsvRulesStorageHandler;
import com.github.ffcfalcos.logger.trace.RulesStorageHandler;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

public class ConfigManager {

    private final String filePath = System.getProperty("user.dir") + "/config.xml";

    private File root;
    private RulesStorageHandler passiveRulesStorageHandler;
    private RulesStorageHandler activeRulesStorageHandler;

    public ConfigManager() {
        try {
            if (new File(filePath).createNewFile()) {
                FileWriter fileWriter = new FileWriter(new File(filePath));
                fileWriter.append("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n");
                fileWriter.append("<config>\n");
                fileWriter.append("<root-path>\n");
                fileWriter.append("</root-path>\n");
                fileWriter.append("</config>\n");
                fileWriter.flush();
                fileWriter.close();
            }
        } catch (IOException ignored) { }
        this.root = new File(this.getElement(new File(filePath), "root-path"));
        CsvRulesStorageHandler csvRulesStorageHandler = new CsvRulesStorageHandler();
        csvRulesStorageHandler.setFilePath(System.getProperty("user.dir") + "/rules.csv", false);
        this.passiveRulesStorageHandler = csvRulesStorageHandler;
        initialize();
    }

    /**
     * Return a field from a Xml file identified by his tag name
     *
     * @param file    File
     * @param element String
     * @return String
     */
    public String getElement(File file, String element) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();
            Node node = doc.getElementsByTagName(element).item(0);
            return node.getFirstChild().getNodeValue();
        } catch (Exception e) {
            return null;
        }
    }

    public void setRoot(File root) {
        this.root = root;
        createElement("root-path", root.getAbsolutePath());
    }

    public RulesStorageHandler getPassiveRulesStorageHandler() {
        return passiveRulesStorageHandler;
    }

    public RulesStorageHandler getActiveRulesStorageHandler() {
        return activeRulesStorageHandler;
    }

    public String getRootPath() {
        return getElement(new File(filePath), "root-path");
    }

    public void createElement(String elementName, String elementValue) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(new File(filePath));
            doc.getFirstChild();
            Node rulesPath = doc.getElementsByTagName(elementName).item(0);
            rulesPath.setTextContent(elementValue);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(filePath);
            transformer.transform(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initialize() {
        try {
            File configFile = getLoggerConfigFile();
            String ruleStorageHandlerClassName = getElement(configFile, "storage-handler-class");
            Class<?> ruleStorageHandlerClass = Class.forName(ruleStorageHandlerClassName);
            activeRulesStorageHandler = (RulesStorageHandler) ruleStorageHandlerClass.getConstructor().newInstance();
            activeRulesStorageHandler.setConfig(root, configFile);
        } catch (Exception e) {
            activeRulesStorageHandler = null;
        }
    }

    /**
     * Return the logger config file if existing or null
     *
     * @return File | null
     */
    public File getLoggerConfigFile() {
        return getFileIfExistingInternal(root, "logger-manager.xml");
    }

    /**
     * Look for a file in any project directory
     *
     * @param directory File
     * @param fileName  String
     * @return File | null
     */
    private static File getFileIfExistingInternal(File directory, String fileName) {
        if (directory.isFile() && directory.getName().equals(fileName)) {
            return directory;
        }
        if (directory.isDirectory()) {
            for (File file : Objects.requireNonNull(directory.listFiles())) {
                File fileTest = getFileIfExistingInternal(file, fileName);
                if (fileTest != null) {
                    return file;
                }
            }
        }
        return null;
    }
}
