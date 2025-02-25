package ca.mcgill.ecse428.jerseycabinet.acceptance;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/resources/US008_publish_auth_listings.feature",
    glue = "ca.mcgill.ecse428.jerseycabinet.acceptance",
    plugin = {"pretty", "html:target/cucumber-reports"},
    monochrome = true
)
public class RunCucumberTest {
}
