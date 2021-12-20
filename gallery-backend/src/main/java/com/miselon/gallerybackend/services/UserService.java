package com.miselon.gallerybackend.services;

import com.miselon.gallerybackend.config.Properties;
import com.miselon.gallerybackend.model.RegistrationToken;
import com.miselon.gallerybackend.model.User;
import com.miselon.gallerybackend.model.requests.RegisterUserRequest;
import com.miselon.gallerybackend.persistance.RegistrationTokenRepository;
import com.miselon.gallerybackend.persistance.UserRepository;
import com.miselon.gallerybackend.util.RegisterUserRequestValidator;
import com.miselon.gallerybackend.util.RegistrationTokenValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final EmailService emailService;
    private final UserRepository userRepository;
    private final RegistrationTokenRepository registrationTokenRepository;
    private final RegisterUserRequestValidator registerUserRequestValidator;
    private final RegistrationTokenValidator registrationTokenValidator;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final Properties properties;

    @Autowired
    public UserService(EmailService emailService, UserRepository userRepository,
                       RegistrationTokenRepository registrationTokenRepository,
                       RegisterUserRequestValidator registerUserRequestValidator,
                       RegistrationTokenValidator registrationTokenValidator,
                       BCryptPasswordEncoder bCryptPasswordEncoder, Properties properties) {
        this.emailService = emailService;
        this.userRepository = userRepository;
        this.registrationTokenRepository = registrationTokenRepository;
        this.registerUserRequestValidator = registerUserRequestValidator;
        this.registrationTokenValidator = registrationTokenValidator;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.properties = properties;
    }

    /**
     * Registers a new user that will require activation.
     * @param registerUserRequest object representing data coming from the client app.
     */
    @Transactional
    public void register(RegisterUserRequest registerUserRequest){
        // Validate request
        if(!this.registerUserRequestValidator.test(registerUserRequest))
            throw new IllegalStateException("User registration request is not valid.");
        // create new user
        User newUser = new User(registerUserRequest);
        // encode the password
        newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
        // create registration confirmation token
        RegistrationToken registrationToken = new RegistrationToken(newUser);
        // send email with token
        this.emailService.sendEmail(registerUserRequest.getEmail(), "Activate your gallery account",
                String.format("Click to activate your gallery account: %s/confirm?token=%s",
                        this.properties.getClientUrl(), registrationToken.getToken()));
        // save both to their respective repositories
        this.userRepository.save(newUser);
        this.registrationTokenRepository.save(registrationToken);
    }

    /**
     * Activates user account
     * @param token received via email
     */
    @Transactional
    public void confirmRegistration(String token){
        // Validate confirmation token
        if(!this.registrationTokenValidator.test(token))
            throw new IllegalStateException("User registration request is not valid.");
        // Retrieve token from repository, if not found exception is thrown
        RegistrationToken registrationToken = this.registrationTokenRepository.findByToken(token).orElseThrow();
        // Enable user assigned to this token
        this.userRepository.enableUserAccount(registrationToken.getUserId());
        // Delete token
        this.registrationTokenRepository.delete(registrationToken);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = this.userRepository.findByUsername(username);
        if(user.isPresent()) return user.get();
        else throw new UsernameNotFoundException("User " + username + " not found.");
    }

    public boolean checkIfUserExists(String username){
        return this.userRepository.findByUsername(username).isPresent();
    }

    public boolean checkIfEmailIsTaken(String email){
        return this.userRepository.findByEmail(email).isPresent();
    }

}
