package com.kaptue.dev.maintenance.security;

import com.kaptue.dev.maintenance.entity.User; // Changement de package
import com.kaptue.dev.maintenance.repository.UserRepository;
import com.kaptue.dev.maintenance.service.PermissionMappingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PermissionMappingService permissionMappingService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé avec l'email: " + email));

        Set<String> permissionStrings = permissionMappingService.getPermissionsForUser(user);

        // Inclure le ROLE_USER ou ROLE_ADMIN comme une autorité en plus des permissions spécifiques
        // Cela peut simplifier certaines règles globales si besoin, mais les permissions par poste restent prioritaires.
        Set<GrantedAuthority> authorities = permissionStrings.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet()); // Utiliser un Set pour éviter les doublons

        // Ajout du rôle Spring Security préfixé
        if (user.getRole() != null) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
        }


        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                authorities // Utiliser les permissions granulaires comme autorités
        );
    }
}