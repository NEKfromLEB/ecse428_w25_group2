package ca.mcgill.ecse428.jerseycabinet.service;

import org.hibernate.annotations.TimeZoneStorage;

import ca.mcgill.ecse428.jerseycabinet.dao.JerseyRepository;
import ca.mcgill.ecse428.jerseycabinet.model.Jersey;

@SpringBootTest
public class AuthentificationServiceTests {
    @Mock
    private JerseyRepository repo;
    @InjectMocks
    private AuthentificationService service;

    @SuppressWarnings("null")
    @Test 
    public void testReadJerseyByValidId(){
        //Set up
        int id = 1;
        Employee employee = new Employee(1,"a@gmail.com","abc");
        Customer customer = new Customer(3,"b@gmail.com","hi");
        Jersey jersey_test = new Jersey(null, "This is a jersey",employee,customer);
        when(repo.findJerseyById(id)).thenReturn(jersey_test);

        //Act 
        Jersey jersey = service.findJerseyById(id);

        //Assert
        assertNotNull(jersey);
        assertEquals(jerse_test.getRequestState(),jersey.getRequestState());
        assertEquals(jersey_test.getDescription(),jersey.getDescription());
        assertEquals(jersey_test.getEmployee(),jersey.getEmployee());
        assertEquals(jersey_test.getSeller(),jersey.getSeller());
    }

    @Test 
    public void testReadJerseyByInvalidId(){
        //Set Up
        int id = 42;
        when(repo.findJerseyById(id)).thenReturn(null);
        try{
            service.findJerseyById(id);
        }catch (ServiceException e){
            error = e.getMessage();
        }

        //assertions
        assertEquals("There is no jersey with ID" + id,error);
    }

    @Test 
    public void testFindAllUnreviewedJerseys(){
        //setup
        ArrayList<Jersey> listJerseys = new ArrayList<Jersey>();
        //create  new object
        //setid
        //add to list

        //act
        ArrayList<Jersey> foundUnreviewedJerseys = (ArrayList<Jersey>) service.findAllUnlistedJerseys();

        //assertions
        assertNotNull(foundUnreviewedJerseys);
        assertEquals(foundUnreviewedJersey.size(),2);

    }

    @Test 
    public void testAcceptJersey(){

    }

    @Test 
    public void testRejectJersey(){

    }
}
