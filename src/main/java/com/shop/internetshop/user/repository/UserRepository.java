package com.shop.internetshop.user.repository;

import com.shop.internetshop.user.model.enums.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.shop.internetshop.user.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    Optional<User> findByEmail(String email);
    Optional<User> findByVerificationCode(String verificationCode);


    Optional<User> findByUsername(String username);


    List<User> findByRole(Role role);
    List<User> findByRoleAndEnabledTrue(Role role);
    long countByRole(Role role);


    List<User> findByBanned(boolean banned);
    List<User> findByEnabled(boolean enabled);

}
