package ca.mcgill.ecse428.jerseycabinet.acceptance;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/US017_modifying_employee.feature", glue = "ca.mcgill.ecse428.jerseycabinet.acceptance.US017")
public class RunCucumberTest_17 {

}
