package fpt.swp.springmvctt.itp.service.impl;

import fpt.swp.springmvctt.itp.dto.request.UserCreationRequest;
import fpt.swp.springmvctt.itp.dto.request.UserLoginRequest;
import fpt.swp.springmvctt.itp.entity.Role;
import fpt.swp.springmvctt.itp.entity.User;
import fpt.swp.springmvctt.itp.repository.RoleRepository;
import fpt.swp.springmvctt.itp.repository.UserRepository;
import fpt.swp.springmvctt.itp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired(required = false)
    private PasswordEncoder passwordEncoder;

    @Override
    public User createUser(UserCreationRequest request) {
        // Validate username and email uniqueness
        if (existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        
        if (existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // Basic password check
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            throw new RuntimeException("Password is required");
        }

        // Create new user
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setBalance(BigDecimal.ZERO);
        user.setStatus("ACTIVE");
        
        // Set audit fields
        user.setCreateBy("SYSTEM");
        user.setCreateAt(LocalDateTime.now());
        user.setUpdateAt(LocalDateTime.now());
        user.setIsDeleted(false);

        // Encode password (if encoder is available)
        if (passwordEncoder != null) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        } else {
            // For development only - in production always use encoder
            user.setPassword(request.getPassword());
        }

        // Set default role (USER)
        Optional<Role> userRole = roleRepository.findByName("USER");
        if (userRole.isEmpty()) {
            // Create default USER role if not exists
            Role newRole = new Role();
            newRole.setName("USER");
            newRole.setDescription("Default user role");
            newRole.setCreateBy("SYSTEM");
            newRole.setCreateAt(LocalDateTime.now());
            newRole.setUpdateAt(LocalDateTime.now());
            newRole.setIsDeleted(false);
            roleRepository.save(newRole);
            user.setRole(newRole);
        } else {
            user.setRole(userRole.get());
        }

        return userRepository.save(user);
    }

    @Override
    public User updateUser(Long id, UserCreationRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        // Update fields
        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            if (existsByEmail(request.getEmail())) {
                throw new RuntimeException("Email already exists");
            }
            user.setEmail(request.getEmail());
        }

        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }

        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            if (passwordEncoder != null) {
                user.setPassword(passwordEncoder.encode(request.getPassword()));
            } else {
                user.setPassword(request.getPassword());
            }
        }

        user.setUpdateAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        
        // Soft delete
        user.setIsDeleted(true);
        user.setDeleteBy("SYSTEM");
        user.setUpdateAt(LocalDateTime.now());
        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return userRepository.findById(id)
                .filter(user -> !Boolean.TRUE.equals(user.getIsDeleted()));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username)
                .filter(user -> !Boolean.TRUE.equals(user.getIsDeleted()));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email)
                .filter(user -> !Boolean.TRUE.equals(user.getIsDeleted()));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByUsernameOrEmail(String usernameOrEmail) {
        Optional<User> user = findByUsername(usernameOrEmail);
        if (user.isEmpty()) {
            user = findByEmail(usernameOrEmail);
        }
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll().stream()
                .filter(user -> !Boolean.TRUE.equals(user.getIsDeleted()))
                .toList();
    }

    @Override
    public User authenticate(UserLoginRequest request) {
        Optional<User> userOpt = findByUsernameOrEmail(request.getUsernameOrEmail());
        
        if (userOpt.isEmpty()) {
            throw new RuntimeException("Invalid username or password");
        }

        User user = userOpt.get();
        
        // Check if user is active
        if (!"ACTIVE".equals(user.getStatus())) {
            throw new RuntimeException("Account is not active");
        }

        // Verify password
        boolean passwordMatch;
        if (passwordEncoder != null) {
            passwordMatch = passwordEncoder.matches(request.getPassword(), user.getPassword());
        } else {
            // For development only - in production always use encoder
            passwordMatch = request.getPassword().equals(user.getPassword());
        }

        if (!passwordMatch) {
            throw new RuntimeException("Invalid username or password");
        }

        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(user -> !Boolean.TRUE.equals(user.getIsDeleted()))
                .orElse(false);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(user -> !Boolean.TRUE.equals(user.getIsDeleted()))
                .orElse(false);
    }

    @Override
    public void activateUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        
        user.setStatus("ACTIVE");
        user.setUpdateAt(LocalDateTime.now());
        userRepository.save(user);
    }

    @Override
    public void deactivateUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        
        user.setStatus("INACTIVE");
        user.setUpdateAt(LocalDateTime.now());
        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isUserActive(Long id) {
        return userRepository.findById(id)
                .map(user -> "ACTIVE".equals(user.getStatus()) && !Boolean.TRUE.equals(user.getIsDeleted()))
                .orElse(false);
    }
}
