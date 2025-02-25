package ca.mcgill.ecse428.jerseycabinet.acceptance;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/US009_authentification_form.feature", glue = "ca.mcgill.ecse428.jerseycabinet.acceptance.US009")
public class RunCucumberTest_9 {

}
