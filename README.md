# Ebase testrunner

This project provides a wrapper around Selenium webdriver. With the form navigator it is possible to navigate through forms or webapps created with the EbaseXi platform. 

# Setup

## Prerequisites 

- Mozilla geckodriver binary: https://github.com/mozilla/geckodriver/releases
- Apache Maven
- Java 8
- EbaseXi: free community version availabe at http://www.ebasetech.com 


## Build

- Clone or download the repository 
- Create /src/main/resources/application.properties (or copy from sample.application.properties)
- Set correct correct path to the geckodriver binary in the properties file
- Open the projectfolder as Workspace in ebase Designer and make sure the local testserver is running
- Go to the Navigator folder in a terminal
- mvn install

If all is setup correctly, the tests will pass and the EbaseTestRunner will be installed in the local Maven repository. It can now be refferenced in new Maven projects where the unit tests will be written.

If the tests don't pass, check if the demo form is running at the addres that is provided in the init() method in /src/test/java/EbaseUnitTestExampleTest.java



