package com.example.Artisan.services;

import com.example.Artisan.entities.Artist;
import com.example.Artisan.entities.Role;
import com.example.Artisan.entities.User;
import com.example.Artisan.repositories.ArtistRepository;
import com.example.Artisan.repositories.UserRepository;
import com.example.Artisan.services.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ArtistRepository artistRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void loadUserByUsername_UserExists() {
        String username = "user@user.com";
        User user = new User();
        user.setEmail(username);
        user.setPassword("password");
        user.setRole(Role.valueOf("USER"));
        when(userRepository.findByEmail(username)).thenReturn(user);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        assertEquals(user.getUsername(), userDetails.getUsername());
        assertEquals(user.getPassword(), userDetails.getPassword());
    }

    @Test
    void loadUserByUsername_ArtistExists() {
        String username = "artist@artist.com";
        Artist artist = new Artist();
        artist.setEmail(username);
        artist.setPassword("password");
        artist.setRole(Role.valueOf("ARTIST"));

        when(artistRepository.findByEmail(username)).thenReturn(artist);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        assertEquals(artist.getUsername(), userDetails.getUsername());
        assertEquals(artist.getPassword(), userDetails.getPassword());
    }

    @Test
    void loadUserByUsername_UserAndArtistDoNotExist() {
        String username = "nonexistent@dndjd.com";

        when(userRepository.findByEmail(username)).thenReturn(null);
        when(artistRepository.findByEmail(username)).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> userDetailsService.loadUserByUsername(username));
    }
}
