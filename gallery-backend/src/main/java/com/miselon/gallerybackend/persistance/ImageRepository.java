package com.miselon.gallerybackend.persistance;

import com.miselon.gallerybackend.model.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, String> {

    @Query("SELECT i.id FROM Image i WHERE i.location = :location")
    List<String> listInLocation(@Param("location") String location);

}
