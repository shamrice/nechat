package io.github.shamrice.nechat.webservice.testDB;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;

/**
 * Created by Erik on 10/21/2017.
 */
public interface UserRepository extends CrudRepository<User, Long>{

}
