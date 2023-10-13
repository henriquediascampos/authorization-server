package com.br.authorizationserver.service.userEntity;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.br.authorizationserver.entity.userEntity.UserEntity;


@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity, UUID> {

    public Optional<UserEntity> findByUsername(final String userName);

    public Optional<UserEntity> findByEmail(final String email);
}
