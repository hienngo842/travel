package com.fake.travel.config;

import com.fake.travel.entity.Role;
import com.fake.travel.entity.UserEntity;
import com.fake.travel.repository.RoleRepository;
import com.fake.travel.repository.UserRepository;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;


import java.util.HashSet;

@Component
@Configuration
public class DataSeedingListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    private void addRoleIfMissing(String name, String description){
        if (roleRepository.findByName(name) == null) {
            roleRepository.save(new Role(name, description));
        }
    }

    private void addUserIfMissing(String email, String password, String... roles){
        if (userRepository.findByEmail(email) == null) {
            UserEntity user = new UserEntity(email, "Full Name", new BCryptPasswordEncoder().encode(password));
            user.setRoles(new HashSet<>());

            for (String role: roles) {
                user.getRoles().add(roleRepository.findByName(role).get());
            }
            userRepository.save(user);
        }
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        addRoleIfMissing("ROLE_ADMIN", "Administrators");
        addRoleIfMissing("ROLE_MEMBER", "Members");

        addUserIfMissing("user", "456", "ROLE_MEMBER");
        addUserIfMissing("admin", "1234", "ROLE_MEMBER", "ROLE_ADMIN");

        if(signingKey == null || signingKey.length() ==0){
            String jws = Jwts.builder()
                    .setSubject("Travel")
                    .signWith(SignatureAlgorithm.HS256, "TravelAPI").compact();

            System.out.println("Use this jwt key:");
            System.out.println("jwt-key=" + jws);
        }
    }

    @Value("${jwt-key}")
    private String signingKey;
}