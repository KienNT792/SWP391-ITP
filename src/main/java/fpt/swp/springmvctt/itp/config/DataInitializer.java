package fpt.swp.springmvctt.itp.config;

import fpt.swp.springmvctt.itp.entity.Role;
import fpt.swp.springmvctt.itp.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        initializeRoles();
    }

    private void initializeRoles() {
        // Create USER role if not exists
        if (!roleRepository.existsByName("USER")) {
            Role userRole = new Role();
            userRole.setName("USER");
            userRole.setDescription("Default user role");
            userRole.setCreateBy("SYSTEM");
            userRole.setCreateAt(LocalDateTime.now());
            userRole.setUpdateAt(LocalDateTime.now());
            userRole.setIsDeleted(false);
            roleRepository.save(userRole);
        }

        // Create ADMIN role if not exists
        if (!roleRepository.existsByName("ADMIN")) {
            Role adminRole = new Role();
            adminRole.setName("ADMIN");
            adminRole.setDescription("Administrator role");
            adminRole.setCreateBy("SYSTEM");
            adminRole.setCreateAt(LocalDateTime.now());
            adminRole.setUpdateAt(LocalDateTime.now());
            adminRole.setIsDeleted(false);
            roleRepository.save(adminRole);
        }

        // Create SELLER role if not exists
        if (!roleRepository.existsByName("SELLER")) {
            Role sellerRole = new Role();
            sellerRole.setName("SELLER");
            sellerRole.setDescription("Seller role for shop owners");
            sellerRole.setCreateBy("SYSTEM");
            sellerRole.setCreateAt(LocalDateTime.now());
            sellerRole.setUpdateAt(LocalDateTime.now());
            sellerRole.setIsDeleted(false);
            roleRepository.save(sellerRole);
        }
    }
}
