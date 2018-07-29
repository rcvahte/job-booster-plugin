package org.jenkinsci.plugins.jobbooster;

import hudson.maven.MavenBuild;
import hudson.model.AbstractBuild;
import jenkins.model.Jenkins;

import hudson.model.Action;
import hudson.model.AbstractProject;
import hudson.model.Cause;
import hudson.model.Item;
import hudson.model.queue.QueueTaskFuture;

import java.io.IOException;
import java.util.logging.Logger;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

import javax.servlet.ServletException;


public class JobBoosterAction implements Action {
	
	private static final Logger LOG = Logger.getLogger(JobBoosterAction.class.getName());
	private final AbstractProject curProject;
	
	public JobBoosterAction(AbstractProject project) {
		this.curProject = project;
	}
        
	public String getUrlName()
	{
		if (curProject.hasPermission(Item.BUILD))
                {
			return "topofqueue";
		}
		return null;
	}
	
	public String getDisplayName()
	{
		if (curProject.hasPermission(Item.BUILD))
                {
                    if (curProject.isInQueue())
                    {
                        return "Job Boost";
                    }
                    else
                    {
                        return "Boosted Build";
                    }
                }
                return null;
	}	
	
	public String getIconFileName()
	{
		if (curProject.hasPermission(Item.BUILD)) {
			return "/plugin/top-of-queue-plugin/images/speed_up_64px.png";
		}
		return null;
	}        
	
	public void doPrioritize(final StaplerRequest request, final StaplerResponse response) throws ServletException, IOException, InterruptedException, ExecutionException
	{
		if (!curProject.hasPermission(Item.BUILD)) {
			// Jenkins is secured AND the user is not supposed to build this job
			response.sendRedirect(request.getContextPath() + '/' + curProject.getUrl());
		}
                
                if (!curProject.isInQueue())
                {
                    // TODO Fix deprecated
                    // Need to schedule build
                    curProject.doBuild(request, response);
                }

		LOG.info("Boosting project: " + curProject.getName());
		
                JobBoosterSorter qs = (JobBoosterSorter)Jenkins.getInstance().getQueue().getSorter();
		
		// Instruct sorter to put this project to the top
		qs.setTopOfQueueProject(curProject);
                
                // Redirect to job queue overview
                response.sendRedirect(request.getContextPath() + "/job-queue");
	}       
}
