package com.miselon.gallerybackend.util;

import com.miselon.gallerybackend.model.requests.RegisterUserRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.function.Predicate;

@Service
public class ImageValidator implements Predicate<MultipartFile> {

    @Override
    // TODO
    public boolean test(MultipartFile multipartFile) {
        return true;
    }

}
