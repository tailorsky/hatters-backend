package com.hatterscraft.hatters_backend.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.hatterscraft.hatters_backend.model.User;
import com.hatterscraft.hatters_backend.repository.UserRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner init(UserRepository userRepository) {
        return args -> {
            if(userRepository.count() == 0) {
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

                User admin = new User();
                admin.setUsername("admin");
                admin.setEmail("admin@hatterscraft.net");
                admin.setPassword(encoder.encode("admin123"));
                admin.setName("Admin");
                admin.setBalance(1000);

                userRepository.save(admin);

                System.out.println("Admin user created: username=admin, password=admin123");
            }
        };
    }
}