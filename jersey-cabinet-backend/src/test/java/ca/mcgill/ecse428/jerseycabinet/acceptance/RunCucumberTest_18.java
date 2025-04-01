package ca.mcgill.ecse428.jerseycabinet.acceptance;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/US018_counter_offer.feature", glue = "ca.mcgill.ecse428.jerseycabinet.acceptance.US018")
public class RunCucumberTest_18 {

}
