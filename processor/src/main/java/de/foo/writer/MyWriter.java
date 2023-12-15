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


    public String process(List<Person> persons) {

        String tmpdir = System.getProperty("java.io.tmpdir");
        final String template = "template.xlsm";
        final String targetFilePath = Paths.get(tmpdir, "test.xlsm").toAbsolutePath().toString();

        LOG.info("Creating {} using template {}", targetFilePath, template);

        try (final InputStream is = MyWriter.class.getResourceAsStream(template)) {

            try (final OutputStream os = new FileOutputStream(targetFilePath)) {


                // FIXME here crashes with NPE
                final Transformer transformer = TransformerFactory.createTransformer(is, os);

                final Area xlsArea = buildArea(transformer);

                final Context context = new Context();
                context.putVar("persons", persons);

                xlsArea.applyAt(new CellRef("Data!A1"), context);

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
