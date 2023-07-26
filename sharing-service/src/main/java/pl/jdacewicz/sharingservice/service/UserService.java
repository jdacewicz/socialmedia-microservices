package pl.jdacewicz.sharingservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.jdacewicz.sharingservice.exception.RecordNotFoundException;
import pl.jdacewicz.sharingservice.model.User;
import pl.jdacewicz.sharingservice.repository.UserRepository;
import pl.jdacewicz.sharingservice.util.FileUtils;

import java.io.IOException;

@Service
public class UserService {

    @Value("${message.user.not-found}")
    private String notFoundMessage;

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserById(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException(notFoundMessage));
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RecordNotFoundException(notFoundMessage));
    }

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
}
