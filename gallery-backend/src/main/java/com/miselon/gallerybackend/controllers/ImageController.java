package com.miselon.gallerybackend.controllers;

import com.miselon.gallerybackend.exceptions.ThumbnailCreationException;
import com.miselon.gallerybackend.model.Image;
import com.miselon.gallerybackend.services.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping(path = "/api/files")
public class ImageController {

    private final ImageService imageService;

    @Autowired
    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    /**
     * Secured endpoint for uploading images, available only for authenticated users.
     * @param file - uploaded file
     * @param user - authenticated user's information
     */
    @PostMapping(path = "/upload")
    public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file, Principal user){
        this.imageService.saveImage(user.getName(), file);
        return ResponseEntity.ok().build();
    }

    /**
     * Secured endpoint for deleting images, available only for authenticated users.
     * @param id - name of the file to be removed
     * @param user - authenticated user's information
     */
    @DeleteMapping(path = "/delete")
    public ResponseEntity<?> delete(@RequestParam("id") String id, Principal user){
        this.imageService.deleteImage(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/{location}/{filename}")
    public ResponseEntity<Resource> download(@PathVariable("location") String location,
                                             @PathVariable("filename") String id,
                                             @RequestParam(value = "thumb", required = false) boolean thumb){

        Image image = this.imageService.findById(id, thumb);
        Resource file = new ByteArrayResource(image.getBytes());
        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_TYPE, "image/jpeg")
                .body(file);
    }

    @GetMapping(path = "/list")
    public ResponseEntity<List<String>> listFilesInLocation(@RequestParam("location") String location){
        return ResponseEntity.ok(this.imageService.listInLocation(location));
    }

    @ExceptionHandler({ThumbnailCreationException.class})
    public ResponseEntity<String> handleThumbnailException(){
        return ResponseEntity.internalServerError().build();
    }

    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<String> handleEntityNotFoundException(){
        return ResponseEntity.notFound().build();
    }

}
