package com.gateway.quinbook.repository;

import com.gateway.quinbook.entity.Sessions;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface SessionRepository extends CrudRepository<Sessions,String> {

    @Query(value = "select user_name from sessions where sessionid = ?1",nativeQuery = true )
    String findbysessionid(String sessionid);

    @Modifying
    @Query(value = "delete from sessions where sessionid = ?1",nativeQuery = true)
    void deleteSession(String uname);
}








