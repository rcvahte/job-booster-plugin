package org.jenkinsci.plugins.jobbooster;

import hudson.Extension;
import hudson.model.AbstractProject;
import hudson.model.Action;
import hudson.model.TransientProjectActionFactory;

import java.util.ArrayList;
import java.util.Collection;

@Extension
public class JobBoosterActionFactory extends TransientProjectActionFactory
{
	@Override
	public Collection<? extends Action> createFor(AbstractProject target)
	{
		ArrayList<Action> actions = new ArrayList<Action>();
		
		actions.add(new JobBoosterAction(target));
		
		return actions;
	}
}
