package org.jenkinsci.plugins.jobbooster;

import hudson.maven.MavenBuild;
import hudson.model.AbstractBuild;
import hudson.model.Queue;
import hudson.model.AbstractProject;
import java.util.ArrayList;

import java.util.List;
import java.util.Comparator;
import java.util.logging.Logger;

public class JobBoosterComparator implements Comparator<Queue.BuildableItem>
{	
	private final List<AbstractProject> topOfQueueProjects;
        
        private static final Logger LOG = Logger.getLogger(JobBoosterAction.class.getName());

	public JobBoosterComparator()
	{
            this.topOfQueueProjects = new ArrayList<AbstractProject>();
	}

	public void addTopOfQueueProject(AbstractProject project)
	{
            // Check for duplicates
            if (!this.topOfQueueProjects.contains(project))
            {
                this.topOfQueueProjects.add(project);
            }
	}
	
        public void cleanTopOfQueueProjects(List<Queue.BuildableItem> buildables)
        {
            for (AbstractProject topQueueProject : topOfQueueProjects)
            {
                Boolean match = false;
                for (Queue.BuildableItem item : buildables)
                {
                    if (topQueueProject.getFullDisplayName().equals(item.task.getFullDisplayName()))
                    {
                        match = true;
                        break;
                    }
                }
                // Not found in buildable queue -> job was started -> remove from boosted projects list
                if (!match)
                {
                    // Add badge that it was boosted
                    AbstractBuild projectBuild = getLastBuild(topQueueProject);
                    projectBuild.getActions().add(new JobBoosterBoostedBadgeAction(projectBuild));                    
                    
                    topOfQueueProjects.remove(topQueueProject);
                    
                    LOG.info("Removing from topQueue project: " + topQueueProject.getFullDisplayName());
                }
            }
        }
	
	public int compare(Queue.BuildableItem item1, Queue.BuildableItem item2)
	{
		AbstractProject<?, ?> project1 = (AbstractProject<?, ?>) item1.task;
		AbstractProject<?, ?> project2 = (AbstractProject<?, ?>) item2.task;
		
		if (this.topOfQueueProjects.contains(project1) && this.topOfQueueProjects.contains(project2))
		{
			// Both are top of queue, give them priority according to order in top queue
			if (this.topOfQueueProjects.indexOf(project1) < this.topOfQueueProjects.indexOf(project2))
                        {
                            // First item is in queue before second item
                            return -1;
                        }
                        else
                        {
                            return 1;
                        }
		}
		else if (this.topOfQueueProjects.contains(project1))
		{
			return -1;
		}
		else if (this.topOfQueueProjects.contains(project2))
		{
			return 1;
		}
		else
		{
			// None of them is in priority, give them equal priority
			return 0;
		}
	}	

    List<AbstractProject> getTopOfQueueProjects() {
        return topOfQueueProjects;
    }
    
    boolean isProjectBoosted()
    {
        // TODO
        return false;
    }

    private AbstractBuild getLastBuild(AbstractProject projectConsidered)
    {
        AbstractBuild lastBuild = projectConsidered.getLastBuild();
        // be careful that lastBuild can be a maven module building !
        if (lastBuild instanceof MavenBuild) {
          lastBuild = ((MavenBuild) lastBuild).getParentBuild();
        }
        return lastBuild;
    }
}