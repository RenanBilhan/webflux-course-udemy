package com.renanbilhan.wfcourse.service;

import com.renanbilhan.wfcourse.entity.User;
import com.renanbilhan.wfcourse.mapper.UserMapper;
import com.renanbilhan.wfcourse.model.request.UserRequest;
import com.renanbilhan.wfcourse.repository.UserRepository;
import com.renanbilhan.wfcourse.service.exception.ObjectNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Objects;

import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @Test
    void testSave() {
        UserRequest request = new UserRequest("user1", "user1@mail.com", "123");
        User entity = User.builder().build();

        when(userMapper.toEntity(any(UserRequest.class))).thenReturn(entity);
        when(userRepository.save(any(User.class))).thenReturn(Mono.just(User.builder().build()));

        Mono<User> result = userService.save(request);

        StepVerifier.create(result)
                .expectNextMatches(Objects::nonNull)
                .expectComplete()
                .verify();

        Mockito.verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testFindById(){
        when(userRepository.findById(anyString())).thenReturn(Mono.just(User.builder()
                .id("123")
                .build()));

        Mono<User> result = userService.findById("123");

        StepVerifier.create(result)
                .expectNextMatches(user -> user.getId().equals("123"))
                .expectComplete()
                .verify();

        Mockito.verify(userRepository, times(1)).findById(anyString());
    }

    @Test
    void testFindAll(){
        when(userRepository.findAll()).thenReturn(Flux.just(User.builder()
                .id("123")
                .build()));

        Flux<User> result = userService.findAll();

        StepVerifier.create(result)
                .expectNextMatches(user -> user.getId().equals("123"))
                .expectComplete()
                .verify();

        Mockito.verify(userRepository, times(1)).findAll();
    }

    @Test
    void testUpdate(){
        UserRequest request = new UserRequest("user1", "user1@mail.com", "123");
        User entity = User.builder().build();

        when(userMapper.toEntity(any(UserRequest.class), any(User.class))).thenReturn(entity);
        when(userRepository.findById(anyString())).thenReturn(Mono.just(entity));
        when(userRepository.save(any(User.class))).thenReturn(Mono.just(entity));


        Mono<User> result = userService.update("123", request);

        StepVerifier.create(result)
                .expectNextMatches(Objects::nonNull)
                .expectComplete()
                .verify();

        Mockito.verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testdelete(){
        User entity = User.builder().build();
        when(userRepository.findAndRemove(anyString())).thenReturn(Mono.just(entity));

        Mono<User> result = userService.delete("123");

        StepVerifier.create(result)
                .expectNextMatches(Objects::nonNull)
                .expectComplete()
                .verify();

        Mockito.verify(userRepository, times(1)).findAndRemove(anyString());

    }

    @Test
    void testHandleNotFound(){
        when(userRepository.findById(anyString())).thenReturn(Mono.empty());

        try{
            userService.findById("123").block();
        }catch (Exception ex){
            Assertions.assertEquals(ex.getClass(), ObjectNotFoundException.class);
            Assertions.assertEquals(ex.getMessage(), format("Object not found. Id: %s, Type %s", "123", User.class.getSimpleName()));
        }
    }
}