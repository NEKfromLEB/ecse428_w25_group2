package ca.mcgill.ecse428.jerseycabinet.acceptance;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
    features = "src/test/resources/US020_Wishlist_Keywords.feature",
    glue = "ca.mcgill.ecse428.jerseycabinet.acceptance.US020",
    plugin = {"pretty", "html:target/cucumber-US020-report.html"}
)
public class RunCucumberTest_20 {
} 