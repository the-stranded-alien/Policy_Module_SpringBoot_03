package SpringBoot.Policy_Module_Pro.services;

import SpringBoot.Policy_Module_Pro.dto.UserRegistrationDto;
import SpringBoot.Policy_Module_Pro.models.User;

import java.util.List;

public interface UserService {
    List<User> findAll();
    User findByUsername(String username);
    User save(UserRegistrationDto registration);
}
