package com.tibco.bw6.maven.plugin.validation;

import java.io.File;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

@Mojo(name="ProjectValidate", defaultPhase=LifecyclePhase.VALIDATE)

public class ValidateProjectMojo extends AbstractMojo
{
	@Parameter(property="skip",defaultValue="false")
	private boolean skip;
	
	@Parameter(defaultValue="${project.build.directory}",readonly=true,required=true)
	private File targetDirectory;
	
	@Parameter(defaultValue="${project.basedir}",readonly=true,required=true)
	private File baseDirectory;
	
	@Parameter(property="verbose")
	private Boolean verbose;
	
	String operatingSystem=System.getProperty("os.name");
	String createWorkspaceCommand;
	String validateWorkspaceCommand;
	String workspaceDir;
		
	public void execute() throws MojoExecutionException, MojoFailureException 
	{
		if(this.skip)
		{
			getLog().info("TIBCO Project validation is skipped");
		}
		else
		{
			ValidationHelper validate = new ValidationHelper(getLog(), isVerbose());
			
			workspaceDir = targetDirectory + "/Workspace";
			
			validate.deleteDirectory(new File(workspaceDir));
			
			if(this.operatingSystem.contains("Windows"))
			{
				createWorkspaceCommand ="bwdesign -data " + workspaceDir + " system:import -f " + baseDirectory;
				validateWorkspaceCommand ="bwdesign -data " + workspaceDir + " system:validate";
			}
			
			if(this.operatingSystem.contains("Linux"))
			{
				createWorkspaceCommand ="bwdesign.sh -data " + workspaceDir + " system:import -f " + baseDirectory;
				validateWorkspaceCommand ="bwdesign.sh -data " + workspaceDir + " system:validate";
			}
			
			try 
			{
				validate.callCreateWorkspaceCommand(createWorkspaceCommand);
				validate.callValidateWorkspaceCommand(validateWorkspaceCommand);
			} 
			catch (Exception e) 
			{
				throw new MojoFailureException(e.getLocalizedMessage(),e);
			}	
		}
	}
	
	private boolean isVerbose()
	{
		return(verbose != null ? verbose : getLog().isDebugEnabled());
	}
}
