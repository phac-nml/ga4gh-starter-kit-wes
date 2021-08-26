package org.ga4gh.starterkit.wes.utils.requesthandler;

import org.ga4gh.starterkit.common.exception.BadRequestException;
import org.ga4gh.starterkit.common.exception.ResourceNotFoundException;
import org.ga4gh.starterkit.common.requesthandler.RequestHandler;
import org.ga4gh.starterkit.wes.model.RunLog;
import org.ga4gh.starterkit.wes.model.State;
import org.ga4gh.starterkit.wes.model.WesRun;
import org.ga4gh.starterkit.wes.utils.hibernate.WesHibernateUtil;
import org.ga4gh.starterkit.wes.utils.runmanager.RunManager;
import org.ga4gh.starterkit.wes.utils.runmanager.RunManagerFactory;
import org.ga4gh.starterkit.wes.utils.runmanager.detailshandler.type.RunTypeDetailsHandler;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Request handling logic for getting full log information (according to the WES spec)
 * from a requested workflow run
 * 
 * @see org.ga4gh.starterkit.wes.controller.Runs#getRunLog getRunLog
 */
public class GetRunLogRequestHandler implements RequestHandler<RunLog> {

    @Autowired
    private RunManagerFactory runManagerFactory;

    @Autowired
    private WesHibernateUtil hibernateUtil;

    private String runId;

    /**
     * Instantiates a new GetRunLogRequestHandler
     */
    public GetRunLogRequestHandler() {

    }

    /**
     * Prepares the request handler with input params from the controller function
     * @param runId run identifier
     * @return the prepared request handler
     */
    public GetRunLogRequestHandler prepare(String runId) {
        this.runId = runId;
        return this;
    }

    /**
     * Obtains full log information for the requested workflow run
     */
    public RunLog handleRequest() {
        // load the persisten WesRun by its id to obtain workflow language,
        // engine associated with the run
        RunLog runLog = new RunLog();
        runLog.setRunId(runId);
        runLog.setState(State.UNKNOWN);
        WesRun wesRun = hibernateUtil.readEntityObject(WesRun.class, runId, true);
        if (wesRun == null) {
            throw new ResourceNotFoundException("No WES Run by the id: " + runId);
        }
        runLog.setRequest(wesRun.toWesRequest());

        // allow the low-level RunManager to perform language/engine-dependent
        // methods to obtain run status
        try {
            RunManager runManager = runManagerFactory.createRunManager(wesRun);
            RunTypeDetailsHandler runTypeDetailsHandler = runManager.getRunTypeDetailsHandler();
            runLog.setState(runTypeDetailsHandler.determineRunStatus().getState());
            runTypeDetailsHandler.completeRunLog(runLog);
        } catch (Exception ex) {
            throw new BadRequestException("Could not load WES run log");
        }
        return runLog;
    }
}