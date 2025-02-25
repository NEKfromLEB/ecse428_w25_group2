package ca.mcgill.ecse428.jerseycabinet.config;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@CucumberContextConfiguration
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.MOCK,
    classes = ca.mcgill.ecse428.jerseycabinet.JerseycabinetApplication.class
)
@TestPropertySource(locations = "classpath:application-test.properties")
public class CucumberSpringConfiguration {
    // Configuration class for Cucumber tests
} 