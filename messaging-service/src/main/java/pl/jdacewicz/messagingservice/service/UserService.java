package pl.jdacewicz.messagingservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.jdacewicz.messagingservice.exception.RecordNotFoundException;
import pl.jdacewicz.messagingservice.model.User;
import pl.jdacewicz.messagingservice.repository.UserRepository;
import pl.jdacewicz.messagingservice.utils.FileUtils;

import java.io.IOException;

@Service
public class UserService {

    @Value("${message.not-found.user}")
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

    public User createUser(String email, MultipartFile profilePicture) throws IOException {
        String newFileName = FileUtils.generateFileName(profilePicture.getOriginalFilename());

        User user = User.builder()
                .email(email)
                .profilePicture(newFileName)
                .build();
        User createdUser = userRepository.save(user);

        FileUtils.saveFile(profilePicture, newFileName, user.getDirectoryPath());
        return createdUser;
    }

    public void updateProfilePicture(String email, MultipartFile profilePicture) throws IOException {
        User user = getUserByEmail(email);

        FileUtils.saveFile(profilePicture, user.getProfilePicture(), user.getDirectoryPath());
    }

    public void addUserToFriends(String email, long id) {
        User loggedUser = getUserByEmail(email);
        User addedUser = getUserById(id);

        loggedUser.getFriends().add(addedUser);
        userRepository.save(loggedUser);
    }

    public void removeUserFromFriends(String email, long id) {
        User loggedUser = getUserByEmail(email);
        User removedUser = getUserById(id);

        loggedUser.getFriends().remove(removedUser);
        userRepository.save(loggedUser);
    }
}
