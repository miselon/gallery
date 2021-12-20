package com.miselon.gallerybackend.util;

import org.springframework.stereotype.Service;

import java.util.function.Predicate;

@Service
public class RegistrationTokenValidator implements Predicate<String> {

    @Override
    public boolean test(String token) {
        // Token length
        if(token.length() != 36) return false;

        return true;
    }
}
