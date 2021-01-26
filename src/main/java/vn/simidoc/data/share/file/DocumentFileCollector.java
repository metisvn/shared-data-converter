package vn.simidoc.data.share.file;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import vn.simidoc.data.share.constants.FileExtension;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DocumentFileCollector {

    private static final String[] ALLOWED_EXT = new String[]{
            FileExtension.DOC,
            FileExtension.DOCX,
            FileExtension.PDF,
            FileExtension.TXT
    };

    public static List<File> getFiles(File dir) {
        return new ArrayList<>(FileUtils.listFiles(dir, new IOFileFilter() {

            @Override
            public boolean accept(File dir, String name) {
                return true;
            }

            @Override
            public boolean accept(File file) {
                for (String ext : ALLOWED_EXT) {
                    if (file.getName().endsWith(ext)) return true;
                }
                return false;
            }
        }, TrueFileFilter.INSTANCE));
    }
}
