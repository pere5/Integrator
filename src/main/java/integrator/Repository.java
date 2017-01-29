package integrator;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by Per Eriksson on 2017-01-29.
 */
public interface Repository extends MongoRepository<GoogleProductCategory, String> {

    public GoogleProductCategory findByFirstName(String firstName);
    public List<GoogleProductCategory> findByLastName(String lastName);

}
