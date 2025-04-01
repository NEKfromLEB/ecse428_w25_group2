package ca.mcgill.ecse428.jerseycabinet.acceptance;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/resources/US012_jersey_order_cancellation.feature",
    glue = "ca.mcgill.ecse428.jerseycabinet.acceptance.US012",
    plugin = {"pretty", "html:target/cucumber-US012-report.html"}
)
public class RunCucumberTest_12 {
    // This class is just a runner - no code needed
}