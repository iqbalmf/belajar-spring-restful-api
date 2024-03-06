package net.iqbalfauzan.belajarspringbootrestfulapi.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.iqbalfauzan.belajarspringbootrestfulapi.entity.User;
import net.iqbalfauzan.belajarspringbootrestfulapi.model.ContactResponse;
import net.iqbalfauzan.belajarspringbootrestfulapi.model.CreateContactRequest;
import net.iqbalfauzan.belajarspringbootrestfulapi.model.WebResponse;
import net.iqbalfauzan.belajarspringbootrestfulapi.repository.ContactRepository;
import net.iqbalfauzan.belajarspringbootrestfulapi.repository.UserRepository;
import net.iqbalfauzan.belajarspringbootrestfulapi.security.BCrypt;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ContactControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup(){
        contactRepository.deleteAll();
        userRepository.deleteAll();

        User user = new User();
        user.setUsername("test");
        user.setPassword(BCrypt.hashpw("rahasia", BCrypt.gensalt()));
        user.setName("TESTING");
        user.setToken("testtoken");
        user.setTokenExpiredAt(System.currentTimeMillis() + 10000000000L);
        userRepository.save(user);
    }

    @Test
    @DisplayName("Create Contact request but return bad request")
    void testCreateContactBadRequest() throws Exception{
        CreateContactRequest request = new CreateContactRequest();
        request.setFirstName("");
        request.setEmail("salahformatemail");

        mockMvc.perform(
                post("/api/contacts")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("x-api-header", "testtoken")
        ).andExpectAll(
                status().isBadRequest()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getErrors());
        });
    }

    @Test
    @DisplayName("Create Contact request but return unauthorized")
    void testCreateContactUnauthorized() throws Exception{
        CreateContactRequest request = new CreateContactRequest();
        request.setFirstName("Iqbal");
        request.setLastName("Fauzan");
        request.setEmail("test@test.com");
        request.setPhone("08782313213");

        mockMvc.perform(
                post("/api/contacts")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getErrors());
        });
    }

    @Test
    @DisplayName("Create Contact request return Success")
    void testCreateContactSuccess() throws Exception{
        CreateContactRequest request = new CreateContactRequest();
        request.setFirstName("Iqbal");
        request.setLastName("Fauzan");
        request.setEmail("test@test.com");
        request.setPhone("08782313213");

        mockMvc.perform(
                post("/api/contacts")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("x-api-header", "testtoken")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<ContactResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getErrors());
            assertEquals("Iqbal", response.getData().getFirstName());
            assertEquals("Fauzan", response.getData().getLastName());
            assertEquals("test@test.com", response.getData().getEmail());
            assertEquals("08782313213", response.getData().getPhone());
            assertTrue(contactRepository.existsById(response.getData().getId()));
        });
    }


    @Test
    @DisplayName("Get contact not found")
    void testGetContactNotFound() throws Exception{

        mockMvc.perform(
                get("/api/contacts/23123123123")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("x-api-header", "testtoken")
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getErrors());
        });
    }

    @Test
    @DisplayName("Get contact return unauthorized")
    void testGetContactUnauthorized() throws Exception{
        mockMvc.perform(
                get("/api/contacts/1")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getErrors());
        });
    }
}