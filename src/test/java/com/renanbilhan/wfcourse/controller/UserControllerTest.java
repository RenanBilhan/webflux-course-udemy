package com.renanbilhan.wfcourse.controller;

import com.mongodb.reactivestreams.client.MongoClient;
import com.renanbilhan.wfcourse.entity.User;
import com.renanbilhan.wfcourse.mapper.UserMapper;
import com.renanbilhan.wfcourse.model.request.UserRequest;
import com.renanbilhan.wfcourse.model.response.UserResponse;
import com.renanbilhan.wfcourse.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.AutoConfigureWebClient;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;



import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;
import static reactor.core.publisher.Mono.just;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureWebTestClient
class UserControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private UserService userService;

    @MockBean
    private UserMapper userMapper;

    @MockBean
    private MongoClient mongoClient;

    private UserRequest request;

    private UserRequest badRequestTrim;

    private UserRequest badRequestSize;

    private UserResponse userResponse;

    @BeforeEach
    public void init(){
        request = new UserRequest("user1", "user1@mail.com", "123");
        badRequestTrim = new UserRequest(" user1", "user1@mail.com", "123");
        badRequestSize = new UserRequest("us", "user1@mail.com", "123");
        userResponse = new UserResponse("123", "user1", "user1@mail.com", "123");
    }


    @Test
    @DisplayName("Test endpoint save with success.")
    void testSaveWithSuccess() {

        when(userService.save(any(UserRequest.class))).thenReturn(just(User.builder().build()));

        webTestClient.post().uri("/users")
                .contentType(APPLICATION_JSON)
                .body(fromValue(request))
                .exchange()
                .expectStatus().isCreated();

        verify(userService, times(1)).save(any(UserRequest.class));

    }

    @Test
    @DisplayName("Test endpoint save with bad request.")
    void testSaveWithBadRequest() {

        webTestClient.post().uri("/users")
                .contentType(APPLICATION_JSON)
                .body(fromValue(badRequestTrim))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody()
                .jsonPath("$.path").isEqualTo("/users")
                .jsonPath("$.status").isEqualTo(BAD_REQUEST.value())
                .jsonPath("$.error").isEqualTo("Validation error")
                .jsonPath("$.message").isEqualTo("Error on validation attributes")
                .jsonPath("$.errors[0].fieldName").isEqualTo("name");

    }

    @Test
    @DisplayName("Test find by id endpoint with success")
    void testFindByIdWithSuccess() {
        when(userService.findById(anyString())).thenReturn(just(User.builder().build()));
        when(userMapper.toResponse(any(User.class))).thenReturn(userResponse);

        webTestClient.get().uri("/users/" + userResponse.id())
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(userResponse.id())
                .jsonPath("$.name").isEqualTo("user1")
                .jsonPath("$.email").isEqualTo("user1@mail.com")
                .jsonPath("$.password").isEqualTo("123");

        verify(userService).findById(anyString());
        verify(userMapper).toResponse(any(User.class));

    }

    @Test
    @DisplayName("Test find all endpoint with success")
    void testFindAllWithSuccess() {
        when(userService.findAll()).thenReturn(Flux.just(User.builder().build()));
        when(userMapper.toResponse(any(User.class))).thenReturn(userResponse);

        webTestClient.get().uri("/users")
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.[0].id").isEqualTo(userResponse.id())
                .jsonPath("$.[0].name").isEqualTo("user1")
                .jsonPath("$.[0].email").isEqualTo("user1@mail.com")
                .jsonPath("$.[0].password").isEqualTo("123");

        verify(userService).findAll();
        verify(userMapper).toResponse(any(User.class));

    }

    @Test
    @DisplayName("Test update endpoint with success")
    void testUpdateWithSuccess() {
        when(userService.update(anyString(), any(UserRequest.class)))
                .thenReturn(just(User.builder().build()));
        when(userMapper.toResponse(any(User.class))).thenReturn(userResponse);

        webTestClient.patch().uri("/users/" + userResponse.id())
                .contentType(APPLICATION_JSON)
                .body(fromValue(request))
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo(userResponse.id())
                .jsonPath("$.name").isEqualTo("user1")
                .jsonPath("$.email").isEqualTo("user1@mail.com")
                .jsonPath("$.password").isEqualTo("123");

        verify(userService).update(anyString(), any(UserRequest.class));
        verify(userMapper).toResponse(any(User.class));
    }

    @Test
    @DisplayName("Test delete endpoint with success")
    void testDeleteWithSuccess() {
        when(userService.delete(anyString())).thenReturn(just(User.builder().build()));

        webTestClient.delete().uri("/users/" + userResponse.id())
                .exchange()
                .expectStatus().isOk();

        verify(userService).delete(anyString());
    }
}