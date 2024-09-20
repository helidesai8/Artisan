package com.example.Artisan.config;

import com.example.Artisan.services.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ApplicationConfigTest {

    @InjectMocks
    private ApplicationConfig applicationConfig;

    @Mock
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Mock
    private AuthenticationConfiguration authConfig;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void modelMapperReturnsModelMapperInstance() {
        ModelMapper modelMapper = applicationConfig.modelMapper();

        assertNotNull(modelMapper);
        assertInstanceOf(ModelMapper.class, modelMapper);
    }

    @Test
    public void passwordEncoderReturnsBCryptPasswordEncoder() {
        PasswordEncoder passwordEncoder = applicationConfig.passwordEncoder();

        assertNotNull(passwordEncoder);
        assert (passwordEncoder instanceof BCryptPasswordEncoder);
    }

    @Test
    public void authenticationManagerReturnsAuthenticationManager() throws Exception {
        when(authConfig.getAuthenticationManager()).thenReturn(mock(AuthenticationManager.class));
        var returnedAuthManager = applicationConfig.authenticationManager(authConfig);

        assertNotNull(returnedAuthManager);
    }

    @Test
    public void daoAuthenticationProviderReturnsDaoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = applicationConfig.daoAuthenticationProvider();

        assertNotNull(daoAuthenticationProvider);
    }
}