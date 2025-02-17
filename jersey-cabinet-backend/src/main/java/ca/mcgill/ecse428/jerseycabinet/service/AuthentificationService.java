@Service
public class AuthentificationService {
    @Autowired
    private JerseyRepository jerseyRepo;

    @Transactional
    public Jersey findJerseyById(int id){

    }

    @Transactional
    public Iterable<Jersey> findAllUnlistedJerseys(){
        
    }

    @Transactional
    public void acceptRequestById(int id){

    }

    @Transactional
    public void rejectRequestById(int id){

    }

    
}
