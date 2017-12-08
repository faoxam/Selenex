# Selenex
Selenium WebDriver. Scenario and data driven using Excel files 

# Preparing Excel Scenario
 - Supported action
 - Annotation
 - Input


#Build project
Execute below command at folder containing pom.xml
mvn clean install

#How to run
Execute below command at folder containing pom.xml
java -Dlog4jonfiguration=file:configuration/log4j.properties -jar target/SelenEx-Maven-beta-1.0-snapshot.jar configuration/selenex.properties 


#Input
 Default to text
 
#Validate text/REGEX
 Default to text
  
#Select (Single/Multiple)
 support annotations
 @value
 @text
 @index
 Default to text
 
#Validation of Selected (Single/Multiple)
 Default to text
 
#Radio
 Default to text
 support annotations
 @text
 @id
 @xpath
 @name