
package ca.mcgill.ecse428.jerseycabinet.acceptance;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/US_21_apply_discount_to_category.feature", glue = "ca.mcgill.ecse428.jerseycabinet.acceptance.US021")
public class RunCucumberTest_21 {
}