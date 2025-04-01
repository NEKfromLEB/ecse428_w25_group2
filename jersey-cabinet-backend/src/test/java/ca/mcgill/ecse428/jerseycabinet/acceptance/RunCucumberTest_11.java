package ca.mcgill.ecse428.jerseycabinet.acceptance;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/US011_adding_jerseys_to_wishlist.feature", glue = "ca.mcgill.ecse428.jerseycabinet.acceptance.US011")
public class RunCucumberTest_11 {
}