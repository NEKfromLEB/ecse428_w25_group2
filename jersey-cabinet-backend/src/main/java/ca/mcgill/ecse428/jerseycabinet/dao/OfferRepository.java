package ca.mcgill.ecse428.jerseycabinet.dao;

import ca.mcgill.ecse428.jerseycabinet.model.Offer;
import org.springframework.data.repository.CrudRepository;

public interface OfferRepository extends CrudRepository<Offer, Offer.Key> {
    public Offer findOfferByKey(Offer.Key key);
}
