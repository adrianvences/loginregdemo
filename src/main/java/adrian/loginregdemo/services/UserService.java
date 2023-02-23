package adrian.loginregdemo.services;

import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import adrian.loginregdemo.models.LoginUser;
import adrian.loginregdemo.models.User;
import adrian.loginregdemo.repositories.UserRepository;
    
@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepo;
    
    // TO-DO: Write register and login methods!
    public User register(User newUser, BindingResult result) {
        Optional<User> user = userRepo.findByEmail(newUser.getEmail());

        // checks to see if user is in db
        if(user.isPresent()){
          result.rejectValue("email", "Email", "Email already resgistered");
        }

        // checks to see if passwords match
        if (!newUser.getPassword().equals(newUser.getConfirm())){
          result.rejectValue("confirm", "Confirm","passwords must match");
        }

        if(result.hasErrors()){
          return null;
        }

        // at this point everything is okay
        // TO-DO: Additional validations!

        //hash the password
        String hashed = BCrypt.hashpw(newUser.getPassword(), BCrypt.gensalt());
        newUser.setPassword(hashed);
        return userRepo.save(newUser);
    }

    public User login(LoginUser newLoginObject, BindingResult result) {
        // TO-DO: Additional validations!
        // what does it mean to log a user in
        // compare what they are giving us to the db
        // check to see if user has registered
        if(!this.checkEmail(newLoginObject.getEmail())) {
          result.rejectValue("email", "noEmail", "Invalid Credentials");
        } // if there is no email we negate it 

        if (result.hasErrors()){
          return null;
        }

        // at this point we found an email
        User user = userRepo.findByEmail(newLoginObject.getEmail()).orElse(null);
        if(!BCrypt.checkpw(newLoginObject.getPassword(), user.getPassword())){
          result.rejectValue("password", "Password","Invalid Credentials");
        }
        
        if(result.hasErrors()){
          return null;
        }

        return user;

    }

    // helper function to see if account exist 
    public Boolean checkEmail(String email){
      Optional<User> user = userRepo.findByEmail(email);
      if(user.isPresent()){
        return true;
      } else {
        return false;
      }
    }
}