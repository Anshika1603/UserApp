package com.nashtech.service;
import com.azure.spring.data.cosmos.exception.CosmosAccessException;
import com.mongodb.MongoException;
import com.nashtech.repository.MongoDbRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.nashtech.model.User;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Slf4j
@Service
public class UserService {


    private MongoDbRepository mongoDbRepository;

    @Autowired
    public UserService(MongoDbRepository mongoDbRepository) {
        this.mongoDbRepository = mongoDbRepository;
    }
    public Flux<User> getUser(){
        Flux<User> users= mongoDbRepository.findAll();
        return users
                .doOnComplete( () ->
                       log.info("Successfully fetched")
                )
                .switchIfEmpty(Flux.error(new MongoException("Error while executing")))
                .onErrorResume(CosmosAccessException.class, error -> {
                    log.error("Error while retrieving data: {}",
                            error.getMessage());
                    return Flux.error(
                            new CosmosAccessException("Failed to retrieve data",
                                    error)
                    );
                });
    }

    public Mono<Void> postUser(User user){
        return mongoDbRepository.save(user).then();
    }
}
