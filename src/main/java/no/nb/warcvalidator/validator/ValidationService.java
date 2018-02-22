package no.nb.warcvalidator.validator;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.*;
import java.util.ArrayList;

public class ValidationService {

    public ValidationService() {
    }

    public boolean warcMovedToValid(String directory, String warc) {
        boolean check = new File(directory, warc).exists();
        return check;
    }

    public boolean warcStatusIsValidAndWellFormed(File xmlReportFile)
            throws ParserConfigurationException, IOException, SAXException, XPathExpressionException {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document xmlDocument = documentBuilder.parse(xmlReportFile);

        XPath xpath = XPathFactory.newInstance().newXPath();
        String expression = "/jhove/repInfo/status";
        Node widgetNode = (Node) xpath.evaluate(expression, xmlDocument, XPathConstants.NODE);

        if (widgetNode != null) {
            String validWarc = "Well-Formed and valid";
            if (widgetNode.getTextContent().equalsIgnoreCase(validWarc)) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public void copyWarcToValidWarcsFolder(File source, File destination) throws IOException {
        InputStream is = null;
        OutputStream os = null;

        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(destination);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }

        } finally {
            is.close();
            os.close();
        }
    }

    public ArrayList<File> findAllWarcs(File[] FileDirectory) {


        ArrayList<File> warcFiles = new ArrayList<>();
        for (File file : FileDirectory) {
            if (warcIsReady(file)) {
                warcFiles.add(file);
            }
        }
        return warcFiles;
    }

    public boolean warcIsReady(File warc) {
        final String WARC_COMPRESSED_AND_OPEN = ".open.warc.gz";
        final String WARC_OPEN = ".open.warc";
        final String IS_WARC = ".warc";
        final String IS_COMPRESSED_WARC = ".warc.gz";

        boolean isOpen = warc.getName().endsWith(WARC_COMPRESSED_AND_OPEN) || warc.getName().endsWith(WARC_OPEN);
        boolean isReady = warc.getName().endsWith(IS_WARC) || warc.getName().endsWith(IS_COMPRESSED_WARC);

        if (isReady && !isOpen) {
            return true;
        }
        return false;
    }

    public ArrayList<File> findAllReports(File[] FileDirectory) {
        ArrayList<File> reportFiles = new ArrayList<>();
        for (File file : FileDirectory) {
            if (file.getName().endsWith(".warc.gz.xml") || file.getName().endsWith(".warc.xml")) {
                reportFiles.add(file);
            }
        }
        return reportFiles;
    }

    public File reportForWarcExist(ArrayList<File> warcReports, String warcFileName) {
        String warcValidationReportName = warcFileName + ".xml";
        for (File report : warcReports) {
            if (warcValidationReportName.equalsIgnoreCase(report.getName())) {
                return report;
            }
        }
        return null;
    }

    public void validateWarc(String warcFilePath, String reportName) {
        JhoveWarcFileValidator validator = new JhoveWarcFileValidator(warcFilePath, reportName);
        try {
            validator.validateFile();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
