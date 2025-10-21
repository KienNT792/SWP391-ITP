package fpt.swp.springmvctt.itp.service;

import fpt.swp.springmvctt.itp.dto.request.UserCreationRequest;
import fpt.swp.springmvctt.itp.dto.request.UserLoginRequest;
import fpt.swp.springmvctt.itp.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    
    // User Management
    User createUser(UserCreationRequest request);
    User updateUser(Long id, UserCreationRequest request);
    void deleteUser(Long id);
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByUsernameOrEmail(String usernameOrEmail);
    List<User> findAll();
    
    // Authentication
    User authenticate(UserLoginRequest request);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    
    // User Status
    void activateUser(Long id);
    void deactivateUser(Long id);
    boolean isUserActive(Long id);
}
