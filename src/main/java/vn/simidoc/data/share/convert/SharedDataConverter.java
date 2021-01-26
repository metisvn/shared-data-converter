package vn.simidoc.data.share.convert;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import vn.simidoc.data.share.ConvertStatisticLog;
import vn.simidoc.data.share.constants.FileExtension;
import vn.simidoc.data.share.file.DocumentFileCollector;
import vn.simidoc.data.share.util.RandomString;
import vn.simidoc.data.share.util.StringUtils;
import vn.simidoc.data.share.util.UnicodeConverter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SharedDataConverter {

    public static final String STATISTIC_LOG_FILE_PATH = "statistic.log";

    ConvertStatisticLog statisticLog;

    public SharedDataConverter() throws IOException {
        statisticLog = new ConvertStatisticLog(STATISTIC_LOG_FILE_PATH);
    }

    public void close() throws IOException {
        statisticLog.close();
    }


    public void startConvert(String folderPath, String desFolderPath) throws IOException {
        List<File> files = DocumentFileCollector.getFiles(new File(folderPath));
        for (File file : files) {
            convertFile(file, desFolderPath);
        }
        statisticLog.logStatistic();
    }

    static final RandomString randomString = new RandomString(30);

    protected void convertFile(File file, String desFolderPath) throws IOException {
        String filePath = file.getAbsolutePath();
        String extension = FilenameUtils.getExtension(filePath).toLowerCase();
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            byte[] byteData = IOUtils.toByteArray(fileInputStream);
            String id = randomString.nextString();
            String content = DocumentToText.parse(byteData, extension);
            UnicodeConverter converter = new UnicodeConverter(content, null);
            String contentNormalized = converter.transform();

            List<String> sentences = StringUtils.extractSentence(contentNormalized);
            File outputFile = new File(desFolderPath + File.separator + id + "." + FileExtension.TXT);
            if (outputFile.exists()) {
                statisticLog.logFailedDocument(filePath, "EXISTED");
                return;
            }
            if (sentences.isEmpty()) {
                statisticLog.logFailedDocument(filePath, "EMPTY");
            }
            sentences = sentences.stream().map(s -> StringUtils.standardizeText(s)).filter(s -> s != null && !s.isEmpty()).collect(Collectors.toList());
            sentences = shuffleSentence(sentences);
            FileUtils.writeLines(outputFile, sentences);
            statisticLog.logSuccessDocument(filePath, id, String.join("", sentences).length(), outputFile.length());
        } catch (Exception e) {
            e.printStackTrace();
            statisticLog.logFailedDocument(filePath, e.getMessage());
        }
    }

    protected List<String> shuffleSentence(List<String> sentences) {
        List<String> shuffledSentences = new ArrayList<>();
        int[] shuffledIndexes = ArrayShuffleUtil.Shuffle(IntStream.range(0, sentences.size()).toArray());
        for (int i = 0; i < shuffledIndexes.length; i++) {
            shuffledSentences.add(sentences.get(shuffledIndexes[i]));
        }
        return shuffledSentences;
    }
}
