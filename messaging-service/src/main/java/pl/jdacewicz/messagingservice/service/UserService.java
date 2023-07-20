package pl.jdacewicz.messagingservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.jdacewicz.messagingservice.exception.RecordNotFoundException;
import pl.jdacewicz.messagingservice.model.User;
import pl.jdacewicz.messagingservice.repository.UserRepository;

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
}
