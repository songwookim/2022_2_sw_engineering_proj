package com.example.finalprj.web.config;


import com.example.finalprj.db.domain.Authority;
import com.example.finalprj.db.domain.User;
import com.example.finalprj.db.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
@RequiredArgsConstructor
public class DBInit implements CommandLineRunner {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (!userService.findByEmail("admin@test.com").isPresent()) {
            User admin = User.builder()
                    .name("admin")
                    .email("admin@test.com")
                    .password(passwordEncoder.encode("admin"))
                    .enabled(true)
                    .playground(null)
                    .build();
            admin = userService.save(admin);

            Authority auth = new Authority(admin.getId(), Authority.ROLE_ADMIN);
            admin.setAuthorities(new HashSet<Authority>() {{
                add(auth);
            }});
            userService.save(admin);
        }
    }
}
