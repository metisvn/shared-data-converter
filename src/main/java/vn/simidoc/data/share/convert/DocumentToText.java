package vn.simidoc.data.share.convert;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.pdf.PDFParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;
import vn.simidoc.data.share.constants.FileExtension;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class DocumentToText {

    public static String parse(byte[] data, String type) throws IOException, TikaException, SAXException {
        String content = "";
        switch (type) {
            case FileExtension.DOC:
                return parseDoc(data);
            case FileExtension.DOCX:
                return parseDocx(data);
            case FileExtension.PDF:
                return parsePdf(data);
            case FileExtension.TXT:
                return new String(data);
            default:
                System.out.println("Invalid file type: " + type);
                break;
        }
        return content;
    }

    private static String parseDoc(byte[] inputFile) throws IOException {

        POIFSFileSystem fs = new POIFSFileSystem(new ByteArrayInputStream(inputFile));
        HWPFDocument doc = new HWPFDocument(fs);
        WordExtractor extractor = new WordExtractor(doc);
        return extractor.getText();
    }

    private static String parseDocx(byte[] inputFile) throws IOException {
        XWPFDocument doc = new XWPFDocument(new ByteArrayInputStream(inputFile));
        XWPFWordExtractor extractor = new XWPFWordExtractor(doc);
        return extractor.getText();
    }

    private static String parsePdf(byte[] inputFile) throws IOException, TikaException, SAXException {
        BodyContentHandler handler = new BodyContentHandler(-1);
        Metadata metadata = new Metadata();
        ParseContext pcontext = new ParseContext();

        //parsing the document using PDF parser
        PDFParser pdfparser = new PDFParser();
        pdfparser.parse(new ByteArrayInputStream(inputFile), handler, metadata, pcontext);

        return handler.toString();
    }

}