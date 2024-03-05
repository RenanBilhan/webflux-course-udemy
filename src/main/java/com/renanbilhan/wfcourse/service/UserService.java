package com.renanbilhan.wfcourse.service;

import com.mongodb.internal.VisibleForTesting;
import com.renanbilhan.wfcourse.entity.User;
import com.renanbilhan.wfcourse.mapper.UserMapper;
import com.renanbilhan.wfcourse.model.request.UserRequest;
import com.renanbilhan.wfcourse.repository.UserRepository;
import com.renanbilhan.wfcourse.service.exception.ObjectNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper mapper;

    public Mono<User> save(final UserRequest userRequest){
        return userRepository.save(mapper.toEntity(userRequest));
    }

    public Mono<User> findById(final String id) {
        return handleNotFound(userRepository.findById(id), id);

    }
    public Flux<User> findAll(){
        return userRepository.findAll();
    }

    public Mono<User> update(final String id, final UserRequest request){
        return findById(id)
                .map(entity -> mapper.toEntity(request, entity))
                .flatMap(userRepository::save);
    }

    public Mono<User> delete(final String id){
        return handleNotFound(userRepository.findAndRemove(id), id);
    }

    private <T> Mono<T> handleNotFound(Mono<T> mono, String id){
        return mono.switchIfEmpty(
                Mono.error(
                        new ObjectNotFoundException(
                                format("Object not found. Id: %s, Type %s", id, User.class.getSimpleName())
                        )
                )
        );
    }
}
