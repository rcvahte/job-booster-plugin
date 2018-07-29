package org.jenkinsci.plugins.jobbooster;

import hudson.Plugin;
import hudson.model.queue.QueueSorter;
import java.util.logging.Logger;
import jenkins.model.Jenkins;

public class JobBoosterPlugin extends Plugin {
    private final static Logger LOG = Logger.getLogger(JobBoosterPlugin.class.getName());

    public void start() throws Exception {
        LOG.info("Starting Job Booster Plugin");
        
        // Override existing Jenkins sorter
        LOG.info("Overriding Jenkins queue sorter");
        QueueSorter originalQueueSorter = Jenkins.getInstance().getQueue().getSorter();
        JobBoosterSorter topOfQueueSorter = new JobBoosterSorter(originalQueueSorter);        
        Jenkins.getInstance().getQueue().setSorter(topOfQueueSorter);
    }
}
