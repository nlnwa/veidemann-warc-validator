package no.nb.warcvalidator.validator;

import edu.harvard.hul.ois.jhove.App;
import edu.harvard.hul.ois.jhove.JhoveBase;
import edu.harvard.hul.ois.jhove.Module;
import edu.harvard.hul.ois.jhove.OutputHandler;
import edu.harvard.hul.ois.jhove.handler.XmlHandler;
import edu.harvard.hul.ois.jhove.module.WarcModule;
import no.nb.warcvalidator.config.AppConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;


public class JhoveWarcFileValidator {

    private static final Logger logger = LoggerFactory.getLogger(JhoveWarcFileValidator.class);
    private File xmlOutFile;
    private File warcFilename;
    private AppConfig appConfig;

    public JhoveWarcFileValidator(String filename, String outFile, AppConfig config) {

        this.appConfig = config;

        this.warcFilename = new File(filename);

        if (!warcFilename.exists()) {
            throw new RuntimeException("Kan ikke finne warc-filen: " + warcFilename.getAbsolutePath());
        }


        this.xmlOutFile = new File(outFile);
    }

    public void validateFile() throws Exception {


        int time[]= {2018,20,02};

        App app = new App ("Veidemann WARC validator", "1.0", time,"usage","rights");


        JhoveBase je = new JhoveBase();
        OutputHandler handler = new XmlHandler();
        Module module = new WarcModule();
        module.setDefaultParams(new ArrayList<>());

        File conf = new File(appConfig.getJhoveConfigFilePath());

        if (!conf.exists()) {
            throw new RuntimeException("Kan ikke finne filen: " + conf.getAbsolutePath());
        } else {
            System.out.println("Bruker nå fil: " + appConfig.getJhoveConfigFilePath());
        }
        je.setLogLevel ("info");
        je.init (conf.getAbsolutePath(), null);

        je.setEncoding ("utf-8");
        je.setTempDirectory ("src/test/resources/");
        je.setBufferSize (4096);
        je.setChecksumFlag (false);
        je.setShowRawFlag (false);
        je.setSignatureFlag (false);

        je.dispatch (app, module, null, handler, xmlOutFile.getAbsolutePath(), new String[] {warcFilename.getAbsolutePath()});
    }
}

