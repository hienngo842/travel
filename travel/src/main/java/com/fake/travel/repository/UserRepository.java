package com.fake.travel.repository;

import com.fake.travel.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity,Long> {
    Optional<UserEntity>  findByEmail(String email);

    UserEntity findOneById(Long aLong);
}
