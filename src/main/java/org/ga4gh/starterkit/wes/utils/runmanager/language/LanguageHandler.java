package org.ga4gh.starterkit.wes.utils.runmanager.language;

import org.ga4gh.starterkit.wes.config.language.LanguageConfig;
import org.ga4gh.starterkit.wes.model.RunLog;
import org.ga4gh.starterkit.wes.model.RunStatus;
import org.ga4gh.starterkit.wes.model.WesRun;
import org.ga4gh.starterkit.wes.utils.runmanager.engine.EngineHandler;

/**
 * Generic interface for classes facilitating data access under a particular
 * workflow language.
 */
public interface LanguageHandler {

    // common operations
    public void setup();
    public void setWesRun(WesRun wesRun);
    public WesRun getWesRun();
    public void setLanguageConfig(LanguageConfig languageConfig);
    public LanguageConfig getLanguageConfig();
    public void setEngineHandler(EngineHandler engineHandler);
    public EngineHandler getEngineHandler();

    /**
     * Request the contents of a file that should be in the working directory
     * @param filename name/path of the file to be loaded
     * @return contents of requested file
     * @throws Exception a server-side error occurred
     */
    public String requestFileContentsFromEngine(String filename) throws Exception;

    /**
     * Request the stdout output from a command executed in the working directory
     * @param command the CLI command
     * @return stdout output of the command
     * @throws Exception a server-side error occurred
     */
    public String requestCommandStdoutFromEngine(String[] command) throws Exception;

    // for launching workflow runs

    /**
     * Create the CLI command to launch the workflow run for the specific workflow language
     * @return CLI command to launch workflow run 
     * @throws Exception a server-side error occurred
     */
    public String[] constructWorkflowRunCommand() throws Exception;

    // for reading workflow run status

    /**
     * Determine the status of a workflow run
     * @return workflow run status
     * @throws Exception a server-side error occurred
     */
    public RunStatus determineRunStatus() throws Exception;

    // for reading workflow run info

    /**
     * Populate/complete the RunLog with information stored in workflow language-specific
     * locations (files or CLI command output) 
     * @param runLog incomplete run log filled with generic WesRun information
     * @throws Exception a server-side error occurred
     */
    public void completeRunLog(RunLog runLog) throws Exception;
}