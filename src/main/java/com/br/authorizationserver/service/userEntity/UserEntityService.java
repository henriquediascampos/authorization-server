package com.br.authorizationserver.service.userEntity;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.br.authorizationserver.entity.userEntity.UserEntity;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserEntityService implements UserDetailsService {

    private UserEntityRepository repository;
    private PasswordEncoder encoder;

    public List<UserEntity> findAll() {
        return repository.findAll();
    }

    public UserEntity save(final UserEntity user) {
        user.setPassword(encoder.encode(user.getPassword()));
        user.setCreatedAt(OffsetDateTime.now());
        return repository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException {
        final Optional<UserEntity> user = repository.findByEmail(email);
        if (user.isEmpty()) {
            new UsernameNotFoundException("User not found");
        }
        return user.get();
    }

}
