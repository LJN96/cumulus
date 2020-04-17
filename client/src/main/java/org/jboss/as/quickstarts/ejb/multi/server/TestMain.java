package org.jboss.as.quickstarts.ejb.multi.server;

public class TestMain {
    public static void main(String[] args) throws Exception {
        UpdateScout us = new UpdateScout();
        us.retrieveCurrentInstalledVersion();
        System.out.println("version = "+us.installedVersion);
    }
}
