package com.nashtech.controller;

import com.nashtech.model.User;
import com.nashtech.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequestMapping("/v1/data")
@RestController
public class UserController {
    private static final Logger LOGGER = LoggerFactory.getLogger(User.class);

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Void> send(@RequestBody User user) {
        return userService.postUser(user)
                .doOnSuccess(success -> LOGGER.info("Received '{}'", user))
                .then();
    }

    @CrossOrigin(
            origins = "http://localhost:63342",
            methods = {RequestMethod.GET},
            allowCredentials = "true"
    )
    @GetMapping(path = "/user/records", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<Object>> consumer() {
        return Flux.create(sink -> {
            // Define a custom event source that emits data indefinitely
            while (!sink.isCancelled()) {
                Flux<User> userFlux = userService.getUser(); // Fetch users from the database

                // Subscribe to the userFlux to receive user objects
                userFlux.subscribe(
                        user -> {
                            ServerSentEvent<Object> event = ServerSentEvent.builder()
                                    .data(user)
                                    .event("user")
                                    .build();
                            sink.next(event); // Emit the event to the client
                        },
                        error -> sink.error(error), // Handle errors
                        () -> sink.complete() // Complete the Flux when the connection is closed
                );
            }
        });

//        return Flux.create(sink -> {
//                while(!sink.isCancelled()){
//                userService.getUser().subscribe(
//                user -> sink.next(ServerSentEvent.builder().data(user).event("user").build()),
//                error -> sink.error(error),
//                () -> sink.complete()
//        );
//                }});
    }
}
