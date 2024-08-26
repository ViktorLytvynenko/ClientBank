package org.example.clientbank.security.SysUser.db;

import org.example.clientbank.security.SysUser.SysUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<SysUser, Long> {

    @Query("SELECT u FROM SysUser u WHERE u.userName = :username")
    Optional<SysUser> findByUserName(String username);

    List<SysUser> findAll();

    Optional<SysUser> findById(Long id);
}
