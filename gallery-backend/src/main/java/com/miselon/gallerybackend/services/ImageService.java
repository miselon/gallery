package com.miselon.gallerybackend.services;

import com.miselon.gallerybackend.model.Image;
import com.miselon.gallerybackend.persistance.ImageRepository;
import com.miselon.gallerybackend.util.ImageValidator;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ImageService {

    private final int THUMBNAIL_SIZE = 300;

    private final ImageRepository imageRepository;
    private final ImageValidator imageValidator;

    @Autowired
    public ImageService(ImageRepository imageRepository, ImageValidator imageValidator) {
        this.imageRepository = imageRepository;
        this.imageValidator = imageValidator;
    }

    public void saveImage(String username, MultipartFile file){
        // Validate multipart file
        if(!this.imageValidator.test(file))
            throw new IllegalStateException("Image failed validation.");
        // Convert multipart file to image object
        Image image = new Image(file, username);
        // Create a thumbnail
        Image thumbnail = null;
        try {
            thumbnail = getThumbnailFromImage(image);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to generate thumbnail: " + e.getMessage());
        }
        // Save both images to the image repo
        this.imageRepository.save(thumbnail);
        this.imageRepository.save(image);
    }

    public void deleteImage(String id){
        this.imageRepository.deleteById(UUID.fromString(id).toString());
    }

    public List<String> listInLocation(String location){
        return this.imageRepository.listInLocation(location).stream()
                .filter(i -> !i.endsWith("_thumb"))
                .collect(Collectors.toList());
    }

    public Image findById(String id, boolean thumb){
        Optional<Image> opt = this.imageRepository.findById(id + (thumb ? "_thumb" : ""));
        return opt.orElseThrow(EntityNotFoundException::new);
    }

    /**
     * Creates a thumbnail of a given image
     * @param image to make a thumbnail out of
     * @return generated thumbnail
     */
    private Image getThumbnailFromImage(Image image) throws IOException {
        // Prepare Image object
        Image thumbnail = new Image();
        thumbnail.setId(image.getId() + "_thumb");
        thumbnail.setExtension("jpg");
        thumbnail.setLocation(image.getLocation());
        // Convert image to thumbnail
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Thumbnails.of(new ByteArrayInputStream(image.getBytes()))
                .crop(Positions.CENTER)
                .size(THUMBNAIL_SIZE, THUMBNAIL_SIZE)
                .outputFormat("JPG")
                .toOutputStream(outputStream);
        // Save thumbnail bytes to image object
        thumbnail.setBytes(outputStream.toByteArray());
        return thumbnail;
    }
}
