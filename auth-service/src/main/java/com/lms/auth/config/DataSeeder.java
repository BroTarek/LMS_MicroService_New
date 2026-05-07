package com.lms.auth.config;

import com.lms.auth.entity.Credential;
import com.lms.auth.repository.CredentialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final CredentialRepository credentialRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (credentialRepository.findByUsername("admin").isEmpty()) {
            Credential admin = new Credential();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("secret123"));
            admin.setRole("ADMIN");
            credentialRepository.save(admin);
            System.out.println("✅ Seeded Admin User: admin/secret123");
        }
    }
}
