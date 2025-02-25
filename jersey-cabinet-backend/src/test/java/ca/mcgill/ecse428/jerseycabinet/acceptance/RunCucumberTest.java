package ca.mcgill.ecse428.jerseycabinet.acceptance;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/US003_search_listings.feature", glue = "ca.mcgill.ecse428.jerseycabinet.acceptance")
public class RunCucumberTest {
}
