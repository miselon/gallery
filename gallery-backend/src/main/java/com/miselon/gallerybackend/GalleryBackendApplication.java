package com.miselon.gallerybackend;

import com.miselon.gallerybackend.model.User;
import com.miselon.gallerybackend.model.UserRole;
import com.miselon.gallerybackend.persistance.UserRepository;
import com.miselon.gallerybackend.services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@EnableAsync
public class GalleryBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(GalleryBackendApplication.class, args);
	}

}
