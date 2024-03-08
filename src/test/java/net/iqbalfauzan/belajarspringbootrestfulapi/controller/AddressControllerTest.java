package net.iqbalfauzan.belajarspringbootrestfulapi.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.iqbalfauzan.belajarspringbootrestfulapi.entity.Address;
import net.iqbalfauzan.belajarspringbootrestfulapi.entity.Contact;
import net.iqbalfauzan.belajarspringbootrestfulapi.entity.User;
import net.iqbalfauzan.belajarspringbootrestfulapi.model.request.CreateAddressRequest;
import net.iqbalfauzan.belajarspringbootrestfulapi.model.request.UpdateAddressRequest;
import net.iqbalfauzan.belajarspringbootrestfulapi.model.response.AddressResponse;
import net.iqbalfauzan.belajarspringbootrestfulapi.model.response.WebResponse;
import net.iqbalfauzan.belajarspringbootrestfulapi.repository.AddressRepository;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by IqbalMF on 2024.
 * Package net.iqbalfauzan.belajarspringbootrestfulapi.controller
 */
@SpringBootTest
@AutoConfigureMockMvc
public class AddressControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private AddressRepository addressRepository;

    @BeforeEach
    void setUp() {
        addressRepository.deleteAll();
        contactRepository.deleteAll();
        userRepository.deleteAll();

        User user = new User();
        user.setUsername("test");
        user.setPassword(BCrypt.hashpw("rahasia", BCrypt.gensalt()));
        user.setName("TESTING");
        user.setToken("testtoken");
        user.setTokenExpiredAt(System.currentTimeMillis() + 10000000000L);
        userRepository.save(user);

        Contact contact = new Contact();
        contact.setId("test");
        contact.setUser(user);
        contact.setFirstname("Iqbal");
        contact.setLastname("Fauzan");
        contact.setEmail("test@test.com");
        contact.setPhone("123123132");
        contactRepository.save(contact);
    }

    @Test
    @DisplayName("Create Address return bad request")
    void testCreateAddressBadRequest() throws Exception{
        CreateAddressRequest request = new CreateAddressRequest();
        request.setCountry("");

        mockMvc.perform(
                post("/api/contacts/test/addresses")
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
    @DisplayName("Create Address return Unauthorized")
    void testCreateAddressUnauthorized() throws Exception{
        CreateAddressRequest request = new CreateAddressRequest();

        request.setStreet("test jalan");
        request.setCity("test city");
        request.setProvince("test province");
        request.setCountry("test country");
        request.setPostalCode("123456");

        mockMvc.perform(
                post("/api/contacts/test/addresses")
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
    @DisplayName("Create Address return Success")
    void testCreateAddressSuccess() throws Exception{
        CreateAddressRequest request = new CreateAddressRequest();

        request.setStreet("test jalan");
        request.setCity("test city");
        request.setProvince("test province");
        request.setCountry("test country");
        request.setPostalCode("123456");

        mockMvc.perform(
                post("/api/contacts/test/addresses")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("x-api-header", "testtoken")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<AddressResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getErrors());
            assertEquals(request.getStreet(), response.getData().getStreet());
            assertEquals(request.getCity(), response.getData().getCity());
            assertEquals(request.getProvince(), response.getData().getProvince());
            assertEquals(request.getCountry(), response.getData().getCountry());
            assertEquals(request.getPostalCode(), response.getData().getPostalCode());

            assertTrue(addressRepository.existsById(response.getData().getId()));
        });
    }


    @Test
    @DisplayName("Get Address return not found")
    void testGetAddressNotFound() throws Exception{

        mockMvc.perform(
                get("/api/contacts/test/addresses/testaddress")
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
    @DisplayName("Get Address return Unauthorized")
    void testGetAddressUnauthorized() throws Exception{

        mockMvc.perform(
                get("/api/contacts/test/addresses/testaddress")
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

    @Test
    @DisplayName("Get Address return Success")
    void testGetAddressSuccess() throws Exception{
        Contact contact = contactRepository.findById("test").orElseThrow();
        Address address = new Address();
        address.setId("test");
        address.setStreet("test");
        address.setCity("test");
        address.setProvince("test");
        address.setCountry("test");
        address.setPostalCode("test");
        address.setContact(contact);
        addressRepository.save(address);
        mockMvc.perform(
                get("/api/contacts/test/addresses/test")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("x-api-header", "testtoken")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<AddressResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getErrors());
            assertEquals(address.getId(), response.getData().getId());
            assertEquals(address.getStreet(), response.getData().getStreet());
            assertEquals(address.getCity(), response.getData().getCity());
            assertEquals(address.getProvince(), response.getData().getProvince());
            assertEquals(address.getCountry(), response.getData().getCountry());
            assertEquals(address.getPostalCode(), response.getData().getPostalCode());
        });
    }


    @Test
    @DisplayName("Update Address return bad request")
    void testUpdateAddressBadRequest() throws Exception{
        UpdateAddressRequest request = new UpdateAddressRequest();
        request.setCountry("");

        mockMvc.perform(
                put("/api/contacts/test/addresses/test")
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
    @DisplayName("Update Address return Unauthorized")
    void testUpdateAddressUnauthorized() throws Exception{
        UpdateAddressRequest request = new UpdateAddressRequest();
        request.setStreet("street");
        request.setCity("city");
        request.setProvince("province");
        request.setCountry("country");
        request.setPostalCode("123321");

        mockMvc.perform(
                put("/api/contacts/test/addresses/test")
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
    @DisplayName("Update Address return Success")
    void testUpdateAddressSuccess() throws Exception{
        Contact contact = contactRepository.findById("test").orElseThrow();
        Address address = new Address();
        address.setId("test");
        address.setStreet("test");
        address.setCity("test");
        address.setProvince("test");
        address.setCountry("test");
        address.setPostalCode("test");
        address.setContact(contact);
        addressRepository.save(address);

        UpdateAddressRequest request = new UpdateAddressRequest();
        request.setStreet("street");
        request.setCity("city");
        request.setProvince("province");
        request.setCountry("country");
        request.setPostalCode("123321");

        mockMvc.perform(
                put("/api/contacts/test/addresses/test")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header("x-api-header", "testtoken")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<AddressResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getErrors());
            assertNotNull(response.getData().getId());
            assertEquals(request.getStreet(), response.getData().getStreet());
            assertEquals(request.getCity(), response.getData().getCity());
            assertEquals(request.getProvince(), response.getData().getProvince());
            assertEquals(request.getCountry(), response.getData().getCountry());
            assertEquals(request.getPostalCode(), response.getData().getPostalCode());
        });
    }


    @Test
    @DisplayName("Delete Address not found")
    void testDeleteContactNotFound() throws Exception{

        mockMvc.perform(
                delete("/api/contacts/test/addresses/test")
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
    @DisplayName("Delete Address return Unauthorized")
    void testDeleteContactUnauthorized() throws Exception{
        Contact contact = contactRepository.findById("test").orElseThrow();
        Address address = new Address();
        address.setId("test");
        address.setStreet("test");
        address.setCity("test");
        address.setProvince("test");
        address.setCountry("test");
        address.setPostalCode("test");
        address.setContact(contact);
        addressRepository.save(address);

        mockMvc.perform(
                delete("/api/contacts/test/addresses/test")
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

    @Test
    @DisplayName("Delete Address return Success")
    void testDeleteContactSuccess() throws Exception{
        Contact contact = contactRepository.findById("test").orElseThrow();
        Address address = new Address();
        address.setId("test");
        address.setStreet("test");
        address.setCity("test");
        address.setProvince("test");
        address.setCountry("test");
        address.setPostalCode("test");
        address.setContact(contact);
        addressRepository.save(address);

        mockMvc.perform(
                delete("/api/contacts/test/addresses/test")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("x-api-header", "testtoken")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getErrors());
            assertEquals("OK",  response.getData());
        });
    }

    @Test
    @DisplayName("Get List Address return Not Found")
    void testGetListAddressesNotFound() throws Exception{

        mockMvc.perform(
                get("/api/contacts/salah/addresses")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("x-api-header", "testtoken")
        ).andExpectAll(
                status().isNotFound()
        ).andDo(result -> {
            WebResponse<AddressResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getErrors());
        });
    }


    @Test
    @DisplayName("Get List Address return Unauthorized")
    void testGetListAddressesUnauthorized() throws Exception{
        Contact contact = contactRepository.findById("test").orElseThrow();

        for (int i = 0; i < 5; i++) {
            Address address = new Address();
            address.setId("test-"+i);
            address.setStreet("test");
            address.setCity("test");
            address.setProvince("test");
            address.setCountry("test");
            address.setPostalCode("test");
            address.setContact(contact);
            addressRepository.save(address);
        }

        mockMvc.perform(
                get("/api/contacts/test/addresses")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpectAll(
                status().isUnauthorized()
        ).andDo(result -> {
            WebResponse<List<AddressResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNotNull(response.getErrors());
        });
    }


    @Test
    @DisplayName("Get List Address return Success")
    void testGetListAddressesSuccess() throws Exception{
        Contact contact = contactRepository.findById("test").orElseThrow();

        for (int i = 0; i < 5; i++) {
            Address address = new Address();
            address.setId("test-"+i);
            address.setStreet("test");
            address.setCity("test");
            address.setProvince("test");
            address.setCountry("test");
            address.setPostalCode("test");
            address.setContact(contact);
            addressRepository.save(address);
        }

        mockMvc.perform(
                get("/api/contacts/test/addresses")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("x-api-header", "testtoken")
        ).andExpectAll(
                status().isOk()
        ).andDo(result -> {
            WebResponse<List<AddressResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
            });
            assertNull(response.getErrors());
            assertEquals(5, response.getData().size());
        });
    }
}
