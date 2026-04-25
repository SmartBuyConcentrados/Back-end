package com.smartbuyconcentrados.demo.service;

import com.smartbuyconcentrados.demo.model.User;
import com.smartbuyconcentrados.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + email));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getSenha())
                .roles(user.getRole())
                .build();
    }

    public void cadastrar(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("E-mail já cadastrado.");
        }
        user.setSenha(passwordEncoder.encode(user.getSenha()));
        user.setRole("USER");
        userRepository.save(user);
    }

    public List<User> listarTodos() {
        return userRepository.findAll();
    }
}