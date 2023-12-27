package org.omarkincal.chatapp.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;


    public void saveUser(User user) {
        user.setStatus(Status.ONLINE);
        repository.save(user);
    }

    public User getUserById(String id) {
        return repository.findById(id).get();
    }

    public void disconnect(User user) {
        var connectedUser = repository.findById(user.getNickName()).orElse(null);
        if (connectedUser != null) {
            connectedUser.setStatus(Status.OFFLINE);
            repository.save(connectedUser);
        }
    }

    public List<User> findConnectedUsers() {
        return repository.findAllByStatus(Status.ONLINE);
    }
}
