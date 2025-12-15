package com.hatterscraft.hatters_backend;

import com.hatterscraft.hatters_backend.controller.UserController;
import com.hatterscraft.hatters_backend.model.User;
import com.hatterscraft.hatters_backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserController controller;

    private MockMvc mockMvc;

    private User user;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        user = new User();
        user.setId(1L);
        user.setMinecraftNickname("TestNick");
    }

    @Test
    void getUser_shouldReturnUser() throws Exception {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void getUser_shouldReturnNotFound() throws Exception {
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/user/2"))
                .andExpect(status().isNotFound());
    }
}
