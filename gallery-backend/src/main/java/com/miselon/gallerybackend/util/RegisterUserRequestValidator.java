package com.miselon.gallerybackend.util;

import com.miselon.gallerybackend.model.requests.RegisterUserRequest;
import org.springframework.stereotype.Service;

import java.util.function.Predicate;

@Service
public class RegisterUserRequestValidator implements Predicate<RegisterUserRequest> {

    @Override
    //TODO
    public boolean test(RegisterUserRequest registerUserRequest) {
        return true;
    }

}
