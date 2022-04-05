package com.somefood.jwt.config.auth;

import com.somefood.jwt.model.User;
import com.somefood.jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// http://localhost:8080/login
@Slf4j
@RequiredArgsConstructor
@Service
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("PrincipalDetailsService loadUserByUsername = {}", username);
        User userEntity = userRepository.findByUsername(username);
        log.info("userEntity = {}", userEntity);
        return new PrincipalDetails(userEntity);
    }
}
