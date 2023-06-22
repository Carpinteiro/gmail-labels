package com.craftware.gmailfilter;

import org.dom4j.Element;
import org.dom4j.Node;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class Dom4JParserUnitTest {


    final String fileNameGmail = "src/test/resources/mailfilters_promotions.xml";

    Dom4JParser parser;



    @Test
    public void changeLabelAttribute() {
        parser = new Dom4JParser(new File(fileNameGmail));
        parser.generateModifiedDocument3("src/test/resources/mailfilterschanged.xml");

        File generatedFile = new File("src/test/resources/mailfilterschanged.xml");
        assertTrue(generatedFile.exists());


    }

}
