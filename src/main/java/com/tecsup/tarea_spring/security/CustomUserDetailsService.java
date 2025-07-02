package com.tecsup.tarea_spring.security;

import com.tecsup.tarea_spring.modelo.User;
import com.tecsup.tarea_spring.modelo.Role; // <--- Importa la NUEVA entidad Role
import com.tecsup.tarea_spring.repositorio.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Set; // Asegúrate de que sea java.util.Set
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con nombre de usuario: " + username));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                mapRolesToAuthorities(user.getRoles()) // Ahora getRoles() devuelve Set<modelo.Role>
        );
    }

    // *** CAMBIO CRÍTICO AQUÍ: Recibe Set<modelo.Role> y usa getName() ***
    private Collection<GrantedAuthority> mapRolesToAuthorities(Set<com.tecsup.tarea_spring.modelo.Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName())) // Usa role.getName() de la entidad Role
                .collect(Collectors.toList());
    }
}