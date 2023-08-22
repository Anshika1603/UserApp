package com.nashtech.repository;

import com.nashtech.model.User;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoDbRepository extends ReactiveMongoRepository<User, Integer> {
}
