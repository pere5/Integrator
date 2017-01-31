package integrator;

import integrator.model.Category;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by Per Eriksson on 2017-01-29.
 */
public interface Repository extends MongoRepository<Category, String> {

    public Category findByFirstName(String firstName);
    public List<Category> findByLastName(String lastName);

}
