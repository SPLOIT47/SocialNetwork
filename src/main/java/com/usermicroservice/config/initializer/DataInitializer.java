package com.usermicroservice.config.initializer;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import com.usermicroservice.entity.RoleEntity;
import com.usermicroservice.repository.RoleRepository;

@Configuration
public class DataInitializer {
  
  @Bean
  @Transactional
  CommandLineRunner initRoles(RoleRepository roleRepository) {
    return args -> {
        System.out.println("Initializing roles...");

        RoleEntity userRole = new RoleEntity();
        userRole.setName("USER");

        RoleEntity adminRole = new RoleEntity();
        adminRole.setName("ADMIN");

        if (roleRepository.findByName(userRole.getName()) == null) {
            System.out.println("Saving role: USER");
            roleRepository.save(userRole);
            System.out.println("Saved role: USER");
        }
        if (roleRepository.findByName(adminRole.getName()) == null) {
            System.out.println("Saving role: ADMIN");
            roleRepository.save(adminRole);
            System.out.println("Saved role: ADMIN");
        }
      };
    }

}
