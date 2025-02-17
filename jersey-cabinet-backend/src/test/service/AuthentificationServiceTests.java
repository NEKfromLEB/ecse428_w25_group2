package ca.mcgill.ecse428.jerseycabinet.service;

import org.hibernate.annotations.TimeZoneStorage;

import ca.mcgill.ecse428.jerseycabinet.dao.JerseyRepository;
import ca.mcgill.ecse428.jerseycabinet.model.Jersey;
import ca.mcgill.ecse428.jerseycabinet.model.Jersey.RequestState;

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
        //Action
        try{
            service.findJerseyById(id);
        }catch (ServiceException e){
            error = e.getMessage();
        }
        //Assertions
        assertEquals("There is no jersey with ID" + id,error);
    }

    @Test 
    public void testFindAllUnreviewedJerseys(){
        //setup
        ArrayList<Jersey> listJerseys = new ArrayList<Jersey>();
        //create  new object
        Employee employee = new Employee(1,"a@gmail.com","abc");
        Customer customer = new Customer(3,"b@gmail.com","hi");
        Jersey jersey1 = new Jersey(null, "This is a jersey",employee,customer);
        Jersey jersey2 = new Jersey(RequestState.Rejected, "This is a jersey",employee,customer);
        Jersey jersey3 = new Jersey(RequestState.Unlisted, "This is a jersey",employee,customer);
        Jersey jersey4 = new Jersey(RequestState.Bought, "This is a jersey",employee,customer);
        Jersey jersey5 = new Jersey(RequestState.Listed,"This is a jersey",employee,customer);
        Jersey jersey6 = new Jersey(null,"This is a jersey", employee,customer);

        listJerseys.add(jersey1); listJerseys.add(jersey2); listJerseys.add(jersey3); listJerseys.add(jersey4);
        listJerseys.add(jersey5); listJerseys.add(jersey6);

        when(repo.findAll()).thenReturn(listJerseys);

        //act
        ArrayList<Jersey> foundUnreviewedJerseys = (ArrayList<Jersey>) service.findAllUnreviewedJerseys();

        //assertions
        assertNotNull(foundUnreviewedJerseys);
        assertEquals(foundUnreviewedJersey.size(),2);

    }

    @Test 
    public void testAcceptJersey(){
         //Set up
         int id = 1;
         Employee employee = new Employee(1,"a@gmail.com","abc");
         Customer customer = new Customer(3,"b@gmail.com","hi");
         Jersey jersey_test = new Jersey(null, "This is a jersey",employee,customer);
         Jersey expected_jersey = new Jersey(RequestState.Unlisted,"This is a jersey",employee,customer);
         when(repo.findJerseyById(id)).thenReturn(jersey_test);
         when(repo.save(any(Jersey.class))).thenReturn(jersey_test); 
 
         //Act 
         Jersey jersey = service.acceptRequestById(id);

 
         //Assert
         assertNotNull(jersey);
         assertEquals(expected_jersey.getRequestState(),jersey.getRequestState());
         assertEquals(expected_jersey.getDescription(),jersey.getDescription());
         assertEquals(expected_jersey.getEmployee(),jersey.getEmployee());
         assertEquals(expected_jersey.getSeller(),jersey.getSeller());

    }

    @Test 
    public void testRejectJersey(){
        //Set up
        int id = 1;
        Employee employee = new Employee(1,"a@gmail.com","abc");
        Customer customer = new Customer(3,"b@gmail.com","hi");
        Jersey jersey_test = new Jersey(null, "This is a jersey",employee,customer);
        Jersey expected_jersey = new Jersey(RequestState.Rejected,"This is a jersey",employee,customer);
        when(repo.findJerseyById(id)).thenReturn(jersey_test);
        when(repo.save(any(Jersey.class))).thenReturn(jersey_test); 

        //Act 
        Jersey jersey = service.rejectRequestById(id);


        //Assert
        assertNotNull(jersey);
        assertEquals(expected_jersey.getRequestState(),jersey.getRequestState());
        assertEquals(expected_jersey.getDescription(),jersey.getDescription());
        assertEquals(expected_jersey.getEmployee(),jersey.getEmployee());
        assertEquals(expected_jersey.getSeller(),jersey.getSeller());


    }
}
