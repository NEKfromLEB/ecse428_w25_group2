package ca.mcgill.ecse428.jerseycabinet.config;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import ca.mcgill.ecse428.jerseycabinet.dao.JerseyRepository;

@CucumberContextConfiguration
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.MOCK,
    classes = CucumberSpringConfiguration.class
)
@ComponentScan(basePackages = "ca.mcgill.ecse428.jerseycabinet")
public class CucumberSpringConfiguration {
    
    @MockBean
    private JerseyRepository jerseyRepository;
}