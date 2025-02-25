package ca.mcgill.ecse428.jerseycabinet;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/US006_log_in_sign_up.feature", glue = "ca.mcgill.ecse428.jerseycabinet")
public class RunCucumberTest {
}