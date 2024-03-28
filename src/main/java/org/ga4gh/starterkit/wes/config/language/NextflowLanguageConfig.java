package org.ga4gh.starterkit.wes.config.language;

import java.util.ArrayList;

import org.ga4gh.starterkit.wes.model.WorkflowEngine;
import org.ga4gh.starterkit.wes.model.WorkflowType;

public class NextflowLanguageConfig extends AbstractLanguageConfig {

    public NextflowLanguageConfig() {
        setEnabled(true);
        setVersions(new ArrayList<>() {{
            add("23.10.0");
        }});
        setEngine(WorkflowEngine.NATIVE);
    }

    @Override
    public WorkflowType getType() {
        return WorkflowType.NFL;
    }
}
