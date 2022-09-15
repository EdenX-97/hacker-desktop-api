package com.eden.hackerdesktopapi.repository;

import com.eden.hackerdesktopapi.model.User;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;


/**
 * Repository for users
 *
 * @author Mo Xu
 * @date 2022/09/01
 */
@EnableScan
public interface UserRepository extends CrudRepository<User, String> {
    User findUserByEmail(String email);

    Boolean existsByEmail(String email);
}
