package vn.simidoc.data.share;

import org.apache.commons.io.output.FileWriterWithEncoding;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ConvertStatisticLog implements Closeable {

    public AtomicInteger totalDocument = new AtomicInteger(0);
    public AtomicInteger totalErrorDocument = new AtomicInteger(0);
    public AtomicLong totalSize = new AtomicLong(0);
    public AtomicInteger totalCharacter = new AtomicInteger(0);

    FileWriterWithEncoding writer;

    public List<String> errorFileUrls = Collections.synchronizedList(new ArrayList<>());

    public Date startDate = new Date();

    String pattern = "HH:mm:ss Z EEE dd/MM/YY";
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
    DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getInstance(Locale.US);

    public ConvertStatisticLog(String logFile) throws IOException {
        DecimalFormatSymbols symbols = decimalFormat.getDecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        decimalFormat.setDecimalFormatSymbols(symbols);
        writer = new FileWriterWithEncoding(new File(logFile), "UTF-8", true);
        write("------STARTING-------");
    }

    public void logSuccessDocument(String link, String id, int characterCount, long size) throws IOException {
        write("SUCCESS: " + id + " => " + link + " => " + decimalFormat.format(characterCount) + " characters => " + decimalFormat.format(size) + " bytes");
        totalDocument.incrementAndGet();
        totalCharacter.getAndAdd(characterCount);
        totalSize.getAndAdd(size);

    }

    public void logFailedDocument(String link, String reason) throws IOException {
        write("FAILED: " + link + " => " + reason);
        errorFileUrls.add(link);
        totalDocument.incrementAndGet();
        totalErrorDocument.incrementAndGet();
    }

    public void logStatistic() throws IOException {
        int totalSuccess = totalDocument.get() - totalErrorDocument.get();
        write("--------------------");
        write("<<<< FINISHED >>>>");
        if (errorFileUrls.isEmpty()){
            write("------------------");
            write("All DOCUMENT IS SUCCESS");
        } else {
            write("------------------");
            write("FAILED DOCUMENTS: (TOTAL " + decimalFormat.format(errorFileUrls.size()) + " documents)");
            for (int i = 0;i<errorFileUrls.size();i++){
                write((i+1) + " => " + errorFileUrls.get(i));
            }
        }

        write("--------------------");
        write("START TIME: " + simpleDateFormat.format(startDate));
        write("END TIME: " + simpleDateFormat.format(new Date()));
        write("TOTAL: " + decimalFormat.format(totalDocument.get()) + " documents");
        write("ERROR: " + decimalFormat.format(totalErrorDocument.get()) + " documents");
        if (totalSuccess > 0) {
            write("TOTAL SIZE: " + decimalFormat.format(totalSize.get()) + " bytes => AVERAGE: " + decimalFormat.format(totalSize.get()/totalSuccess) + " bytes per document");
            write("TOTAL CHARACTER: " + decimalFormat.format(totalCharacter.get()) + " characters => AVERAGE: " + decimalFormat.format(totalCharacter.get()/totalSuccess) + " characters per document");
        }
        write("--------------------");

    }


    public void write(String text) throws IOException {
        System.out.println(text);
        writer.write(text + "\n");
    }


    @Override
    public void close() throws IOException {
        if (writer != null) {
            writer.close();
        }
    }
}
