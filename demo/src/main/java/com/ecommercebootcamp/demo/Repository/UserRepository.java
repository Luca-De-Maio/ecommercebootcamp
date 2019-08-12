package com.ecommercebootcamp.demo.Repository;

import com.ecommercebootcamp.demo.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}