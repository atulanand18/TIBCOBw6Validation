package com.tibco.bw6.maven.plugin.validation;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

import org.apache.maven.plugin.logging.Log;

public class ValidationHelper 
{
	private final Logger logDebug;

    private final Logger logInfo;

    private final Logger logVerbose;

    private final Logger logWarn;

    ValidationHelper(final Log log,boolean verbose)
    {
        logDebug = ( log == null || !log.isDebugEnabled() ) ? null : new Logger()
        {
            public void log( CharSequence message )
            {
                log.debug( message );
            }
        };

        logInfo = ( log == null || !log.isInfoEnabled() ) ? null : new Logger()
        {
            public void log( CharSequence message )
            {
                log.info( message );
            }
        };

        logWarn = ( log == null || !log.isWarnEnabled() ) ? null : new Logger()
        {
            public void log( CharSequence message )
            {
                log.warn( message );
            }
        };
        
        logVerbose = verbose ? logInfo : logDebug;
    }
    
    public boolean deleteDirectory(File path)
    {
    	if(path.exists())
    	{
    		File[] files=path.listFiles();
    		for(int i=0; i<files.length; i++)
    		{
    			if(files[i].isDirectory())
    			{
    				deleteDirectory(files[i]);
    			}
    			else
    			{
    				files[i].delete();
    			}
    		}
    	}
    	return(path.delete());
    }
    
    public void callCreateWorkspaceCommand(String createWorkspaceCommand) throws Exception
    {
    	try
    	{
    		Process p = Runtime.getRuntime().exec(createWorkspaceCommand);
    		p.waitFor();
    		InputStream in = p.getInputStream();
    		ByteArrayOutputStream baos = new ByteArrayOutputStream();
    		int c=-1;
    		while((c=in.read())!= -1)
    		{
    			baos.write(c);
    		}
    		
    		String response = new String(baos.toByteArray());
    		
    		if(logVerbose != null)
    		{
    			logVerbose.log(response);
    		}
    		if(response.contains("ERROR"))
    		{
    			if(logVerbose == null)
        		{
    				if(logInfo != null)
    	    		{
    					logInfo.log("Error creating workspace or error importing modules into workspace. For details, re-run the maven by setting the verbose configuration as true for the plugin or re-run maven using the -X switch.");
    	    		}
        		}
    			throw new Exception("Error creating workspace or error importing modules into workspace");
    		}
    		
    		if(!(response.contains("ERROR")))
    		{
    			if(logVerbose == null)
        		{
    				if(logInfo != null)
    	    		{
    					logInfo.log("Workspace created successfully and modules imported to the workspace");
    	    		}
        		}
    		}
    	}
    	catch(Exception e)
    	{
    		throw new Exception(e.getLocalizedMessage(),e);
    	}
    }
    
    public void callValidateWorkspaceCommand(String validateWorkspaceCommand) throws Exception
    {
    	try
    	{
    		Process p = Runtime.getRuntime().exec(validateWorkspaceCommand);
    		p.waitFor();
    		InputStream in = p.getInputStream();
    		ByteArrayOutputStream baos = new ByteArrayOutputStream();
    		int c=-1;
    		while((c=in.read())!= -1)
    		{
    			baos.write(c);
    		}
    		
    		String response = new String(baos.toByteArray());
    		
    		if(logVerbose != null)
    		{
    			logVerbose.log(response);
    		}
    		if(response.contains("ERROR"))
    		{
    			if(logVerbose == null)
        		{
    				if(logInfo != null)
    	    		{
    					logInfo.log("Validation errors found. For details, re-run the maven by setting the verbose configuration as true for the plugin or re-run maven using the -X switch.");
    	    		}
        		}
    			throw new Exception("Validation failed.");
    		}
    		
    		if(!(response.contains("ERROR")))
    		{
    			if(logVerbose == null)
        		{
    				if(logInfo != null)
    	    		{
    					logInfo.log("No validation errors found.");
    	    		}
        		}
    		}
    	}
    	catch(Exception e)
    	{
    		throw new Exception(e.getLocalizedMessage(),e);
    	}
    }
    
    private interface Logger
    {
        void log( CharSequence message );
    }
}
