package org.jenkinsci.plugins.jobbooster;

import hudson.model.AbstractBuild;
import hudson.model.BuildBadgeAction;
import org.kohsuke.stapler.export.Exported;

public class JobBoosterBoostedBadgeAction implements BuildBadgeAction
{
  private final static String ICON_PATH =  "/plugin/top-of-queue-plugin/images/speed_up_16px.png";
  private final AbstractBuild boostedBuild;

  public JobBoosterBoostedBadgeAction(AbstractBuild boostedBuild) {
    this.boostedBuild =  boostedBuild;
  }

  @Exported
  public String getIconPath()
  {
      return ICON_PATH;
  }

  @Exported
  public String getBoostedBuildUrl()
  {
      return boostedBuild.getProject().getUrl();
  }

  @Exported
  public String getText()
  {
      return "This build was boosted";
  }

  public String getDisplayName()
  {
    return "";
  }

  public String getIconFileName()
  {
    return "";
  }

  public String getUrlName()
  {
    return "";
  }

}
