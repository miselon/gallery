package com.miselon.gallerybackend.model;

import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.io.IOException;
import java.util.UUID;

@Entity(name = "Image")
@Table( name="image_table",
        indexes = @Index(columnList = "location"))
public class Image {

    @Id
    private String id;
    private String location;
    private String extension;
    @Lob
    private byte[] bytes;

    public Image(MultipartFile multipartFile, String location){
        // Auto generated ID
        this.id = UUID.randomUUID().toString();
        this.location = location;
        // Extract file extension from the file name
        this.extension = multipartFile.getOriginalFilename()
                .substring(multipartFile.getOriginalFilename().lastIndexOf('.')+1);
        // Save file content
        try { this.bytes = multipartFile.getBytes(); }
        catch (IOException e) { e.printStackTrace(); }
    }

    public Image() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public String getFilename(){
//        return new StringBuffer()
//                .append(this.id)
//                .append('.')
//                .append(this.extension)
//                .toString();
        return String.format("%s.%s", this.id, this.extension);
    }
}
