package net.engineeringdigest.journalApp.service;

import lombok.extern.slf4j.Slf4j;
import net.engineeringdigest.journalApp.entity.User;
import net.engineeringdigest.journalApp.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public static final PasswordEncoder passwordEncode = new BCryptPasswordEncoder();

//    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public boolean saveNewUser(User user){
        try{
            user.setPassword(passwordEncode.encode(user.getPassword()));
            user.setRoles(Collections.singletonList("USER"));
            userRepository.save(user);
            return true;
        }catch (Exception e){
            log.info("Error occured for {} : ",user.getUsername(),e);
            return false;
        }
    }
    public void saveNewAdmin(User user){
        user.setPassword(passwordEncode.encode(user.getPassword()));
        user.setRoles(Arrays.asList("USER","ADMIN"));
        userRepository.save(user);
    }

    // when deleting a journal and saving it to db again ,  then this function will be useful
    // as we dont want to encode the password again.
    // hence this fuction will only save the user without encoding the password
    public void saveUser(User user){
        userRepository.save(user);
    }

    public List<User> getAll(){
        return userRepository.findAll();
    }

    public Optional<User> findById(ObjectId id){
        return userRepository.findById(id);
    }

    public void deleteById(ObjectId id){
        userRepository.deleteById(id);
    }

    public User findByUsername(String username){
        return userRepository.findByUsername(username);
    }
}
