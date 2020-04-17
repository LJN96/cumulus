import org.apache.commons.lang3.StringUtils;

public class ReturnServerStatus {
    String status;
    String jbossCLI = "C:\\domain-work\\wildfly-18.0.2.Final\\bin\\jboss-cli.bat";
    String c = "-c";
    String readResources = "/host=master/server-config=app-oneA:read-resource(include-runtime=true)";
    String readStatus = "/host=master/server-config=app-oneA:read-attribute(name=status)";
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
}

