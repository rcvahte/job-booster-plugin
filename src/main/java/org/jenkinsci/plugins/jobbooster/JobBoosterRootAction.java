package org.jenkinsci.plugins.jobbooster;

import hudson.Extension;
import hudson.model.AbstractProject;
import hudson.model.Queue;
import hudson.model.RootAction;
import hudson.model.queue.QueueSorter;
import java.util.ArrayList;
import java.util.List;
import jenkins.model.Jenkins;

@Extension
public class JobBoosterRootAction implements RootAction {
    
    @Override
    public String getIconFileName() {
        return "/plugin/top-of-queue-plugin/images/speed_up_64px.png";
    }

    @Override
    public String getDisplayName() {
        return "Job Booster";
    }

    @Override
    public String getUrlName() {
        return "job-queue";
    }
    
    public String getNumberOfJobsInQueue()
    {
        Queue queue = Jenkins.getInstance().getQueue();
        
        return Integer.toString(queue.getItems().length);
    }
    
    public String getNumberOfPrioritizedJobs()
    {
        int count = 0;

        try
        {
            JobBoosterSorter tqs = (JobBoosterSorter)Jenkins.getInstance().getQueue().getSorter();
            List<AbstractProject> items = tqs.getTopOfQueueProjects();        
            count = items.size();
        }
        catch (Exception e)
        {
            // Ignore exception -- not yet initialized
        }        
        
        return Integer.toString(count);
    }
    
    public List<String> getJobs()
    {
        List<String> list = new ArrayList<String>();
                
        // Get buildable items
        List<Queue.BuildableItem> items = Jenkins.getInstance().getQueue().getBuildableItems();
        
        // Get boosted projects
        JobBoosterSorter tqs = (JobBoosterSorter)Jenkins.getInstance().getQueue().getSorter();
        List<AbstractProject> boostedProjects = tqs.getTopOfQueueProjects();
        
        for (Queue.BuildableItem item : items)
        {
            boolean skip = false;
            for (AbstractProject project : boostedProjects)
            {
                if (project.getFullDisplayName().equals(item.task.getDisplayName()))
                {
                    // Do not display items which are boosted
                    skip = true;
                    break;
                }
            }
            if (!skip)
            {
                list.add(item.task.getDisplayName());
            }
        }
        
        return list;
    }
    
    public List<String> getPrioJobs()
    {
        List<String> list = new ArrayList<String>();
        
        // Get prio jobs
        JobBoosterSorter tqs = (JobBoosterSorter)Jenkins.getInstance().getQueue().getSorter();
        List<AbstractProject> items = tqs.getTopOfQueueProjects();
        
        for (AbstractProject item : items)
        {
            list.add(item.getFullDisplayName());
        }
        
        return list;
    }    
}
