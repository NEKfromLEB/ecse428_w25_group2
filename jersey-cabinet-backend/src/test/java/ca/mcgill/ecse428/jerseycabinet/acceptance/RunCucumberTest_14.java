package ca.mcgill.ecse428.jerseycabinet.acceptance;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/US014_view_order_history.feature",
        glue = "ca.mcgill.ecse428.jerseycabinet.acceptance.US014"
)
public class RunCucumberTest_14 {}
