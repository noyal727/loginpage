package com.gateway.quinbook.repository;

import com.gateway.quinbook.entity.Login;
import org.springframework.data.repository.CrudRepository;

public interface LoginRepository extends CrudRepository<Login,String> {
}
