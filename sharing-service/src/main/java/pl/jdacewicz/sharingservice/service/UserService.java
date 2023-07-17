package pl.jdacewicz.sharingservice.service;

import jakarta.persistence.Transient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.jdacewicz.sharingservice.exception.RecordNotFoundException;
import pl.jdacewicz.sharingservice.model.User;
import pl.jdacewicz.sharingservice.repository.UserRepository;
import pl.jdacewicz.sharingservice.util.FileUtils;

import java.io.IOException;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserById(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Could not find user with id: " + id));
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RecordNotFoundException("Could not find user with email: " + email));
    }

    @Transient
    public User createUser(String userEmail, MultipartFile profilePicture) throws IOException {
        String newFileName = FileUtils.generateFileName(profilePicture.getOriginalFilename());

        User user = User.builder()
                .email(userEmail)
                .profilePicture(newFileName)
                .build();
        User createdUser = userRepository.save(user);

        FileUtils.saveFile(profilePicture, newFileName, createdUser.getDirectoryPath());
        return createdUser;
    }

    public void updateProfilePicture(String userEmail, MultipartFile profilePicture) throws IOException {
        User user = getUserByEmail(userEmail);

        FileUtils.saveFile(profilePicture, user.getProfilePicture(), user.getDirectoryPath());
    }

    public void deleteUserById(long id) {
        userRepository.deleteById(id);
    }
}
