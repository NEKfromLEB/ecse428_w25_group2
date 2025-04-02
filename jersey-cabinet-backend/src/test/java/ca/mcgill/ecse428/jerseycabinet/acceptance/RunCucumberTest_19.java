package ca.mcgill.ecse428.jerseycabinet.acceptance;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/resources/US019_modify_card.feature",
    glue = "ca.mcgill.ecse428.jerseycabinet.acceptance.US019"
)
public class RunCucumberTest_19 {
}  
