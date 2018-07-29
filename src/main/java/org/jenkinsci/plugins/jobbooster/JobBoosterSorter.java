package org.jenkinsci.plugins.jobbooster;

import hudson.model.Queue;
import hudson.model.queue.QueueSorter;
import hudson.model.AbstractProject;

import java.util.logging.Logger;
import java.util.Collections;
import java.util.List;

public class JobBoosterSorter extends QueueSorter {

	private final QueueSorter originalQueueSorter;
	private final JobBoosterComparator comparator;
	
	private static final Logger LOG = Logger.getLogger(JobBoosterAction.class.getName());
	
	public JobBoosterSorter(QueueSorter originalQueueSorter)
	{
		this.originalQueueSorter =  originalQueueSorter;
		comparator = new JobBoosterComparator();
	}

	@Override
	public void sortBuildableItems(List<Queue.BuildableItem> buildables)
	{
		// Remove any jobs that we have in topOfQueue, which are no longer 
                comparator.cleanTopOfQueueProjects(buildables);
		
		// Apply the original sorter
		if(this.originalQueueSorter != null) {
			this.originalQueueSorter.sortBuildableItems(buildables);
		}
		
		// Use custom sorter to raise priority of selected top queue project(s)
		Collections.sort(buildables, comparator);
	}

	public void setTopOfQueueProject(AbstractProject project)
	{
		comparator.addTopOfQueueProject(project);
		
		LOG.info("Called setTopOfQueueProject for " + project.getName());
	}
        
        public List<AbstractProject> getTopOfQueueProjects()
        {
            return comparator.getTopOfQueueProjects();
        }
	
}
