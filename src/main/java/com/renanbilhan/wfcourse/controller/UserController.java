package com.renanbilhan.wfcourse.controller;

import com.renanbilhan.wfcourse.documentation.UserDoc;
import com.renanbilhan.wfcourse.mapper.UserMapper;
import com.renanbilhan.wfcourse.model.request.UserRequest;
import com.renanbilhan.wfcourse.model.response.UserResponse;
import com.renanbilhan.wfcourse.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Validated
public class UserController implements UserDoc {

    private final UserService userService;
    private final UserMapper mapper;

    @Override
    public ResponseEntity<Mono<Void>> save(@Valid UserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.save(request).then());
    }

    @Override
    public ResponseEntity<Mono<UserResponse>> findById(String id) {
        return ResponseEntity.ok().body(
                userService.findById(id)
                        .map(mapper::toResponse)
        );
    }

    @Override
    public ResponseEntity<Flux<UserResponse>> findAll() {
        return ResponseEntity.ok()
                .body(userService.findAll()
                        .map(mapper::toResponse));
    }

    @Override
    public ResponseEntity<Mono<UserResponse>> update(String id, UserRequest request) {
        return ResponseEntity.ok().body(
                userService.update(id, request).map(mapper::toResponse)
        );
    }

    @Override
    public ResponseEntity<Mono<Void>> delete(String id) {
        return ResponseEntity.ok().body(
                userService.delete(id).then()
        );
    }
}
