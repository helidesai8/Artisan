package com.example.Artisan.services;

import com.example.Artisan.entities.Artist;
import com.example.Artisan.entities.User;
import com.example.Artisan.repositories.ArtistRepository;
import com.example.Artisan.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static com.example.Artisan.entities.Role.ARTIST;

/**
 * This class implements the UserDetailsService interface to provide user details for authentication.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ArtistRepository artistRepo;

    /**
     * Loads the user details by username.
     *
     * @param username the username of the user
     * @return the UserDetails object containing the user details
     * @throws UsernameNotFoundException if no user is found with the given username
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepo.findByEmail(username);
        Artist artist = artistRepo.findByEmail(username);

        if(user == null && artist == null){
            throw new UsernameNotFoundException("No user found");
        }

        else{
            String userName = user != null ? user.getUsername() : artist.getUsername();
            String password = user != null ? user.getPassword() : artist.getPassword();
            var authorities = user != null ? user.getAuthorities() : artist.getAuthorities();
            var userBuilder = org.springframework.security.core.userdetails.User.builder();
            var userDetails = userBuilder.username(userName)
                    .password(password )
                    .authorities(authorities);
            return userDetails.build();
        }
    }

}