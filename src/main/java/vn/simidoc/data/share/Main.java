package vn.simidoc.data.share;

import vn.simidoc.data.share.convert.SharedDataConverter;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {

        if (args.length < 2){
            System.out.println("COMMAND: java -jar convert.jar {folder_path} {destination_path}");
        }
        String folderPath = args[0];
        String desPath = args[1];
        try {
            SharedDataConverter sharedDataConverter = new SharedDataConverter();
            sharedDataConverter.startConvert(folderPath, desPath);
            sharedDataConverter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
