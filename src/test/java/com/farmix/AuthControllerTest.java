package com.farmix;

import com.farmix.config.JwtProvider;
import com.farmix.entity.USER_ROLE;
import com.farmix.entity.User;
import com.farmix.repository.CartRepository;
import com.farmix.repository.UserRepository;
import com.farmix.service.CustomerUserDetailsService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private JwtProvider jwtProvider;

    @MockBean
    private CustomerUserDetailsService customerUserDetailsService;

    @MockBean
    private CartRepository cartRepository;

    @Test
    public void testUserSignupSuccess() throws Exception {
        // Create a mock user object
        User user = new User();
        user.setEmail("test@example.com");
        user.setUsername("testuser");
        user.setPassword("testpassword");
        user.setRole(USER_ROLE.ROLE_CUSTOMER);
        // Mock userRepository to return null when finding user by email (i.e., user does not exist)
        Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(null);

        // Mock passwordEncoder to return an encoded password
        Mockito.when(passwordEncoder.encode(Mockito.anyString())).thenReturn("encodedPassword");

        // Mock JWT provider
        Mockito.when(jwtProvider.generateToken(Mockito.any(Authentication.class))).thenReturn("mockJwtToken");

        // Create the request body
        String requestBody = """
                {
                "email": "test@example.com",
                "username": "testuser",
                "password": "testpassword",
                "role": "ROLE_CUSTOMER"
                }""";

        // Perform the signup request
        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())  // Expect status to be CREATED (201)
                .andExpect(jsonPath("$.jwt").value("mockJwtToken"))  // Check if JWT is returned
                .andExpect(jsonPath("$.message").value("Successfully created new user"))
                .andExpect(jsonPath("$.role").value("ROLE_CUSTOMER"));

        // Verify the interaction with the repository
        Mockito.verify(userRepository, times(1)).save(Mockito.any(User.class));
    }

    @Test
    public void testUserSignupAlreadyExists() throws Exception {
        // Create a mock user object
        User existingUser = new User();
        existingUser.setEmail("test@example.com");

        // Mock userRepository to return the existing user
        Mockito.when(userRepository.findByEmail(existingUser.getEmail())).thenReturn(existingUser);

        // Create the request body
        String requestBody = """
            {
            "email": "test@example.com",
            "username": "testuser",
            "password": "testpassword",
            "role": "ROLE_CUSTOMER"
            }""";

        // Perform the signup request and expect the exception to be thrown
        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest()) // Expect status to be BAD REQUEST (400)
                .andExpect(content().string("User with this email already exists")); // Verify the error message

        // Verify that userRepository save is not called, as the user already exists
        Mockito.verify(userRepository, times(0)).save(Mockito.any(User.class));
    }

}
