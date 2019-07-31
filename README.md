# TIBCOBw6Validation: bw6-validation-maven-plugin
bw6-validation-maven-plugin is a maven plugin for performing validation of TIBCO BW 6.x version projects during build process.
It makes use of bwdesign utility for the execution part. It first creates a workspace and imports the application modules in the workspace using 
bwdesign's system:import. Finally it calls system:validate to perform validation of the workspace and report any validation errors.

Pre-requisites:
- JAVA_HOME should be set and available in the path. This can be confirmed by running java -version from command prompt.
- Maven should be installed on the machine. M2_HOME should be set. The maven executable should be available in the path. This can be confirmed by running
mvn -version from command prompt.
- Tibco Active Matrix 6.x version should be installed on the machine. BW_HOME should be set and BW_HOME\bin should be available in the path. This can be confirmed
by executing bwdesign edition command from command prompt.

Goals:

The plugin provides goal - "ProjectValidate" for validation of TIBCO projects.
The goal execution will first create a workspace in the target directory and imports the application modules in the workspace. It will then run the validation
on the workspace and will fail the build only in case there is any validation error.

Usage:

To use the plugin, you need to copy below to your pom.xml under build -> plugins.

			<plugin>
              <groupId>com.tibco.bw6.plugins</groupId>
              <artifactId>bw6-validation-maven-plugin</artifactId>
			  <version>1.0.0</version>
			  <executions>
				<execution>
					<goals>
						<goal>ProjectValidate</goal>
					</goals>
				</execution>
              </executions>
            </plugin>
			
To skip execution, use below configuration:

			<plugin>
              <groupId>com.tibco.bw6.plugins</groupId>
              <artifactId>bw6-validation-maven-plugin</artifactId>
			  <version>1.0.0</version>
			  <configuration>
				<skip>true</skip>
              </configuration>
            </plugin>
			
For verbose logging, use below configuration:

			<plugin>
              <groupId>com.tibco.bw6.plugins</groupId>
              <artifactId>bw6-validation-maven-plugin</artifactId>
			  <version>1.0.0</version>
			  <configuration>
				<verbose>true</verbose>
              </configuration>
			  <executions>
				<execution>
					<goals>
						<goal>ProjectValidate</goal>
					</goals>
				</execution>
              </executions>
            </plugin>


