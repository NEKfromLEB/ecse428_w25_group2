package ca.mcgill.ecse428.jerseycabinet.acceptance;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    RunCucumberSteps_7.class,
    RunCucumberTest_2.class,
    RunCucumberTest_3.class,
    RunCucumberTest_4.class,
    RunCucumberTest_5.class,
    RunCucumberTest_9.class,
    RunCucumberTest_10.class,
    RunCucumberTest_8.class
})
public class AllCucumberTests {

}
