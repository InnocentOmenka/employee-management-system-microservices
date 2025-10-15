package com.innocodes.auth_service.config;

import com.innocodes.auth_service.entity.User;
import com.innocodes.auth_service.enums.Role;
import com.innocodes.auth_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserSeeder implements CommandLineRunner {

        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;

        @Override
        public void run(String... args) {
            seedAdmin();
        }

    private void seedAdmin() {
        String adminEmail = "admin@company.com";
        if (userRepository.existsByEmail(adminEmail)) {
            log.info("✅ Admin user already exists. Skipping seeding.");
            return;
        }

        User admin = User.builder()
                .email(adminEmail)
                .password(passwordEncoder.encode("Admin@123"))
                .role(Role.ADMIN)
                .build();

        userRepository.save(admin);
        log.info("🌱 Admin user seeded successfully with email: {}", adminEmail);
    }
    }
