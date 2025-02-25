package ca.mcgill.ecse428.jerseycabinet.acceptance;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/US002_review_auth_requests.feature", glue = "ca.mcgill.ecse428.jerseycabinet.acceptance.US002")
public class RunCucumberTest_2 {
}


