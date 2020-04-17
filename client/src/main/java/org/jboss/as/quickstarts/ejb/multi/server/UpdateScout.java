package org.jboss.as.quickstarts.ejb.multi.server;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class UpdateScout {

    int installedVersion;
    ArrayList<String> retrievedUpdateList;
    String[] recommendedUpdates;
    private String selectedVersion;

    JsonUtils ju = new JsonUtils();
    ServerController sc = new ServerController();

    public void sequence() throws Exception {
        retrieveCurrentInstalledVersion();
        downloadUpdateList();
        install(profferUpdate());
    }

    String defaultdb = "efacsmaster";

    public void retrieveCurrentInstalledVersion() throws Exception {
        InstallConfig ic = new InstallConfig();
        String url = "jdbc:jtds:sqlserver://" + ic.getDbInfo() + "/"
                + defaultdb + ";SelectMethod=Cursor;PrepareSQL=0";
        ResultSet rs = getData(query, url, ic.getUserName(), ic.getPassword());
        String id = null;

        while(rs.next()) {
            id = rs.getString("id");
        }

        if(id == null){
            throw new Exception("Unable to determine version");
        } else {
            installedVersion = Integer.valueOf(id);
        }
    }
    private String query2 = "select * from [efacsmaster].[dbo].[softwareupdates]";

    private String query =   "SELECT TOP (1) [id] " +
            ",[description]" +
            ",[processed]" +
            ",[buildnumber]" +
            ",[createddate]" +
            "FROM [efacsmaster].[dbo].[softwareupdates]" +
            "WHERE processed = 1" +
            "ORDER BY id desc";

    public ResultSet getData(String query, String url, String username, String password) {
        Connection connection = null;
        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            connection = DriverManager.getConnection(url, username, password);
            result = connection.createStatement().executeQuery(query);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return result;
    }

    private ResultSet result;

    private void downloadUpdateList() throws IOException {
        retrievedUpdateList = accessRestApi(); 
    }

    private ArrayList<String> accessRestApi() throws IOException {
        ju.getNewUpdates(installedVersion);
        return ju.retrievedSubsequentUpdateNumbers;
    }

    private void rulesForUpdateAcceptance() {
    }

    public String profferUpdate(){
        System.out.println("The following updates are available: " + Arrays.toString(retrievedUpdateList.toArray()));
        System.out.println("Enter update number to install: ");
        Scanner scn = new Scanner(System.in);
        selectedVersion = scn.nextLine();

        return selectedVersion;
    }

    public int install(String version) throws IOException {
        takeDownEfacs();
        downloadUpdate();
        actuateInstall(new File("downloaded-updates/update.jar"), new File("install_location/update.jar"));
        startEfacs();

        int exitCode = 0;
        return exitCode;
    }

    private void actuateInstall(File in, File out) throws IOException {
            FileChannel inChannel = new
                    FileInputStream(in).getChannel();
            FileChannel outChannel = new
                    FileOutputStream(out).getChannel();
            try {
                inChannel.transferTo(0, inChannel.size(),
                        outChannel);
            }
            catch (IOException e) {
                throw e;
            }
            finally {
                if (inChannel != null) inChannel.close();
                if (outChannel != null) outChannel.close();
            }
    }

    private void startEfacs() throws IOException {
        sc.serverStartStop(ServerController.SignalEnum.start);
    }

    private void downloadUpdate() throws IOException {
        String url = "http://127.0.0.1:5000/api/update/"+selectedVersion;
        String downloadLink = ju.getUpdateDownloadLink(url);
        URL website = new URL(downloadLink);
        File update = new File("downloaded-updates/update.jar");
        FileUtils.copyURLToFile(website, update);
    }

    private void takeDownEfacs() throws IOException {
        sc.serverStartStop(ServerController.SignalEnum.stop);
    }
}