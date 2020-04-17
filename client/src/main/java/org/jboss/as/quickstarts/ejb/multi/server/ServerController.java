package org.jboss.as.quickstarts.ejb.multi.server;

import org.apache.commons.lang3.StringUtils;

import java.io.IOException;

public class ServerController {
    String status;
    String jbossCLI = "C:\\domain-work\\wildfly-18.0.2.Final\\bin\\jboss-cli.bat";
    String c = "-c";
    String readResources = "/host=master/server-config=app-oneA:read-resource(include-runtime=true)";
    String readStatus = "/host=master/server-config=app-oneA:read-attribute(name=status)";
    String startAppOneServer = "/server-group=quickstart-ejb-multi-appOne-server:";
    int exitCode;

    public String getStatus() {
        callCommander();
        return status;
    }

    private void callCommander() {
        Commander cmdr = new Commander();
        cmdr.foo(jbossCLI, c, readStatus);
        exitCode = cmdr.getExitCode();
        if (exitCode == 0) {
            String cliOut = cmdr.getOutputString();
            System.out.println(cliOut);
            status = StringUtils.substringBetween(cliOut, "\"result\" => \"", "\"");
        }
    }

    public void serverStartStop(SignalEnum se) throws IOException {
        ProcessBuilder pn1 = new ProcessBuilder(jbossCLI,
                c, startAppOneServer + se + "-servers");
        pn1.start();
    }

    public enum SignalEnum {
        start, stop;
    }
}


