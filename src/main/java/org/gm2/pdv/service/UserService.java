package org.gm2.pdv.service;

import org.gm2.pdv.dto.UserDTO;
import org.gm2.pdv.entity.User;
import org.gm2.pdv.exceptions.NoItemException;
import org.gm2.pdv.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<UserDTO> findAll() {
        return userRepository.findAll().stream().map(user ->
                new UserDTO(user.getId(), user.getName(), user.isEnabled()))
                .collect(Collectors.toList());
    }

    public UserDTO save(User user){
        userRepository.save(user);

        /* Apenas simplificando meu código
        UserDTO newUserDTO = new UserDTO();

        newUserDTO.setId(user.getId());
        newUserDTO.setName(user.getName());
        newUserDTO.setEnabled(user.isEnabled());
         */

        // Retorando um DTO em vez do User inteiro
        return new UserDTO(user.getId(), user.getName(), user.isEnabled());
    }

    // Método que retorna um UserDTO pelo ID
    // Aqui abaixo eu passo um tipo de dados, do tipo inteiro, explico mais abaixo...
    public UserDTO findById(long id){
        // Como eu to acessando o repositorio por um número inteiro qualquer, eu tenho que usar
        // um .get depois
        Optional<User> optional = userRepository.findById(id);

        if (!optional.isPresent()){
            throw new NoItemException("Usuário não encontrado!");
        }

        // Só pra diminuir o código e não ter que escrever no return: optional.get().getID()
        // Estou usando o get aqui agora:
        User user = optional.get();

        // Sem o get eu poderia retornar assim:
        // return new UserDTO(user.get().getID(), user.get().getName(), user.get().isEnabled());

        // Retornei o UserDTO via ID
        return new UserDTO(user.getId(), user.getName(), user.isEnabled());
    }

    // PUT (update) do controller
    public UserDTO update(User user) {
        Optional<User> userToEdit = userRepository.findById(user.getId());

        if (!userToEdit.isPresent()){
            throw new NoItemException("Usuário não encontrado!");
        }

        // Faz o UPDATE
        userRepository.save(user);

        // Retorna o UserDTO
        return new UserDTO(user.getId(), user.getName(), user.isEnabled());
    }

    public void deleteById(long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (!userOptional.isPresent()){
            throw new NoItemException("Não foi possivel localizar usuário!");
        }

        userRepository.deleteById(id);
    }
}
