package ca.mcgill.ecse428.jerseycabinet.acceptance;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/resources/US020_update_wishlist_and_notifications.feature",
    glue = "ca.mcgill.ecse428.jerseycabinet.acceptance.US020"
)
public class RunCucumberTest_20 {
}
