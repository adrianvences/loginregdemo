package adrian.loginregdemo.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import adrian.loginregdemo.models.User;


public interface UserRepository extends CrudRepository<User,Long> {
  
  Optional<User> findByEmail(String email);
}
