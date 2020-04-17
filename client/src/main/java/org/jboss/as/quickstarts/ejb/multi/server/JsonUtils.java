package org.jboss.as.quickstarts.ejb.multi.server;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.json.JSONTokener;

public class JsonUtils {

    public String getUpdateDownloadLink(String updateMetadataURL) throws IOException {
        JSONObject jo = (JSONObject) new JSONTokener(IOUtils.toString(new URL(updateMetadataURL))).nextValue();
        System.out.println(jo.getString("file"));
        return jo.getString("file");
    }

    public ArrayList<String> retrievedSubsequentUpdateNumbers = new ArrayList<>();

    public void getNewUpdates(int currentNumberStart) throws IOException {
        int nextAvailableVersion = 1;
        int currentNumber = currentNumberStart + nextAvailableVersion;
        boolean updateNumberIteratorPresentBool = true;

        while ( updateNumberIteratorPresentBool) {
            for (int i = currentNumber; ; ) {
                try {
                    String updateMetadataRootURL = "http://127.0.0.1:5000/api/update/" + currentNumber;
                    JSONObject newUpdates = (JSONObject) new JSONTokener(IOUtils.toString(new URL(updateMetadataRootURL))).nextValue();
                    retrievedSubsequentUpdateNumbers.add(newUpdates.getString("number"));
                    currentNumber++;
                } catch (Exception updateNotExistException) {
                    updateNumberIteratorPresentBool = false;
                    break;
                }
            }
        }
    }
}
