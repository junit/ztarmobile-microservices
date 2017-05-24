package com.ztarmobile.account.repository;

import com.ztarmobile.account.model.UserAccount;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RepositoryRestResource(collectionResourceRel = "users")
public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {

    List<UserAccount> findByFirstNameContaining(@Param("word") String word);

    List<UserAccount> findByLastNameContaining(@Param("word") String word);

    List<UserAccount> findByEmailContaining(@Param("word") String word);
}
