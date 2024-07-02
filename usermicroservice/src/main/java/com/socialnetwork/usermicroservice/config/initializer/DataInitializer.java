package com.socialnetwork.usermicroservice.config.initializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import com.annotation.ExecutionLog;
import com.socialnetwork.usermicroservice.entity.RoleEntity;
import com.socialnetwork.usermicroservice.repository.RoleRepository;

@Configuration
public class DataInitializer {

  @Bean
  @Transactional
  @ExecutionLog
  CommandLineRunner initRoles(RoleRepository roleRepository) {
    return args -> {
        RoleEntity userRole = new RoleEntity();
        userRole.setName("ROLE_USER");

        RoleEntity adminRole = new RoleEntity();
        adminRole.setName("ROLE_ADMIN");

        if (roleRepository.findByName(userRole.getName()) == null) {
            roleRepository.save(userRole);
        }
        if (roleRepository.findByName(adminRole.getName()) == null) {
            roleRepository.save(adminRole);
        }
      };
    }

}
