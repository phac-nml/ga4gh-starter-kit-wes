package org.ga4gh.starterkit.wes.utils.runmanager.detailshandler.engine;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.FileUtils;

public class NativeEngineDetailsHandler extends AbstractRunEngineDetailsHandler {

    private static final Path wesRunDir = Paths.get("wes_runs");

    private Path jobDirectory;

    public NativeEngineDetailsHandler() {
        
    }

    // common operations

    public List<String> provideDirectoryContents(String directory) throws Exception {
        setJobDirectory();
        File dir = Paths.get(jobDirectory.toString(), directory).toFile();
        return Arrays.asList(dir.list());
    }

    // for launching workflow runs

    public void stageWorkingArea() throws Exception {
        setJobDirectory();
        Path paramsFile = Paths.get(jobDirectory.toString(), "params.json");
        Files.createDirectories(jobDirectory);
        FileUtils.writeStringToFile(paramsFile.toFile(), getWesRun().getWorkflowParams(), "utf-8");
    }

    public void launchWorkflowRunCommand(String[] workflowRunCommand) throws Exception {
        new ProcessBuilder()
            .command(workflowRunCommand)
            .directory(jobDirectory.toFile())
            .start();
    }

    // for reading workflow run status

    public Map<String, String> getFileContentsToDetermineRunStatus(Map<String, String> requestedFileMap) throws Exception {
        Map<String, String> requestedFileContents = new HashMap<>();

        for (String key : requestedFileMap.keySet()) {
            String content = new String ( Files.readAllBytes( Paths.get(jobDirectory.toString(), requestedFileMap.get(key))));
            requestedFileContents.put(key, content);
        }
        
        return requestedFileContents;
    }

    /* Private convenience methods */

    private void setJobDirectory() throws Exception {
        String id = getWesRun().getId();
        Path path = Paths.get(
            wesRunDir.toString(),
            id.substring(0, 2),
            id.substring(2, 4),
            id.substring(4, 6),
            id
        );
        jobDirectory = path;
    }
}
