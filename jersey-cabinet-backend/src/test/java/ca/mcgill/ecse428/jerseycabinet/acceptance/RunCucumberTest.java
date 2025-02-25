package ca.mcgill.ecse428.jerseycabinet.acceptance;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/resources/US008_publish_auth_listings.feature",
    glue = {"ca.mcgill.ecse428.jerseycabinet.acceptance", "ca.mcgill.ecse428.jerseycabinet.config"},
    plugin = {"pretty", "html:target/cucumber-reports"}
)
public class RunCucumberTest {
}
