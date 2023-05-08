package org.gm2.pdv.service;

import org.gm2.pdv.dto.UserDTO;
import org.gm2.pdv.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private UserRepository userRepository;

    public List<UserDTO> findAll() {
        return userRepository.findAll().stream().map(user -> {
            UserDTO userDTO = new UserDTO(user.getId(),user.getName(), user.isEnabled());
            return userDTO;
        }).collect(Collectors.toList());
    }
}
