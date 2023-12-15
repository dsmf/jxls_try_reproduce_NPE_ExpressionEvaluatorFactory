package de.foo.writer;

import de.foo.writer.api.Person;
import org.jxls.area.Area;
import org.jxls.builder.AreaBuilder;
import org.jxls.builder.xls.XlsCommentAreaBuilder;
import org.jxls.common.CellRef;
import org.jxls.common.Context;
import org.jxls.transform.Transformer;
import org.jxls.util.TransformerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Paths;
import java.util.List;


public class MyWriter {

    private static final Logger LOG = LoggerFactory.getLogger(MyWriter.class);

    private static final String EXCEL_FILE_ENDING = "xlsx";

    private static final String TEMPLATE_FILENAME = "template";
    private static final String OUTPUT_FILENAME = "test";

    private static final String SHEET_NAME = "Data";


    public String process(List<Person> persons) {

        final String tmpdir = System.getProperty("java.io.tmpdir");

        final String template = TEMPLATE_FILENAME + "." + EXCEL_FILE_ENDING;
        final String targetFilePath = Paths.get(tmpdir, OUTPUT_FILENAME + "." + EXCEL_FILE_ENDING).toAbsolutePath().toString();

        LOG.info("Creating {} using " + TEMPLATE_FILENAME + " {}", targetFilePath, template);

        try (final InputStream is = MyWriter.class.getResourceAsStream(template)) {

            try (final OutputStream os = new FileOutputStream(targetFilePath)) {


                // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                // In our real project the error occurs in the following line
                // hen creating the transformer.
                // However, in this demo I cannot reproduce this behaviour yet.
                // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                final Transformer transformer = TransformerFactory.createTransformer(is, os);




                final Area xlsArea = buildArea(transformer);

                final Context context = new Context();
                context.putVar("persons", persons);

                xlsArea.applyAt(new CellRef(SHEET_NAME + "!A1"), context);

                transformer.write();

                return targetFilePath;

            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Area buildArea(final Transformer transformer) {
        final AreaBuilder areaBuilder = new XlsCommentAreaBuilder(transformer);
        final List<Area> xlsAreaList = areaBuilder.build();
        return xlsAreaList.get(0);
    }


}
