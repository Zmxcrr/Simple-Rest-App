package controllerTests;

import Zmxcrr.Main;
import Zmxcrr.dto.CatDto;
import Zmxcrr.dto.UserDto;
import Zmxcrr.enums.CatColor;
import Zmxcrr.repositories.CatRepository;
import Zmxcrr.repositories.OwnerRepository;
import Zmxcrr.security.SecurityConfig;
import Zmxcrr.security.UserDetailsImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = {Main.class, SecurityConfig.class})
@TestPropertySource(locations = "classpath:application-integrationtest.properties")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CatControllerTests {
    private final int INITIAL_SIZE = 5;
    public static ObjectMapper mapper;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private CatRepository catRepository;
    @Autowired
    private OwnerRepository ownerRepository;
    private MockMvc mvc;
    private UserDto currentUser;

    @BeforeAll
    public static void init() {
        mapper = new JsonMapper();

        mapper
                .registerModule(new ParameterNamesModule())
                .registerModule(new Jdk8Module())
                .registerModule(new JavaTimeModule());
    }

    @BeforeEach
    public void setup(@Autowired Flyway flyway) {
        flyway.clean();
        flyway.migrate();

        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

        currentUser = ((UserDetailsImpl) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal())
                .getUser();
    }

    @Test
    @WithUserDetails(value = "Mishka", userDetailsServiceBeanName = "userDetailsService")
    public void getCats_whenOwner_shouldOnlySeeOwnedCats() throws Exception {
        int expectedSize = 1;

        mvc.perform(get("/cats")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(expectedSize))
                );
    }

    @Test
    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "userDetailsService")
    public void getCats_whenAdmin_shouldGetAllCats() throws Exception {
        int expectedSize = INITIAL_SIZE;

        mvc.perform(get("/cats")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(expectedSize))
                );
    }

    @Test
    @WithUserDetails(value = "Mishka", userDetailsServiceBeanName = "userDetailsService")
    public void getById_whenNotExists_thenStatus400() throws Exception {
        long id = 10;
        mvc.perform(get("/cats/%s".formatted(id))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "userDetailsService")
    public void getById_whenExistsAndAdmin_thenStatus200() throws Exception {
        System.out.println(currentUser.getRole().name());
        long id = 3;
        mvc.perform(get("/cats/%s".formatted(id))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Simba"));
    }

    @Test
    @WithUserDetails(value = "Mishka", userDetailsServiceBeanName = "userDetailsService")
    public void save_whenOwnerAndSavesHisCat_thenStatus200() throws Exception {
        long currentOwnerId = currentUser.getOwner();

        CatDto dto = new CatDto();
        dto.setName("Testy");
        dto.setBreed("Fluffy");
        dto.setColor(CatColor.SEMICOLOR);
        dto.setBirthdate(LocalDate.now());
        dto.setOwner(currentOwnerId);
        String body = mapper.writeValueAsString(dto);

        mvc.perform(post("/cats")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        int actualSize = catRepository.findAll().size();

        assertEquals(INITIAL_SIZE + 1, actualSize);
    }

    @Test
    @WithUserDetails(value = "Mishka", userDetailsServiceBeanName = "userDetailsService")
    public void save_whenNotOwner_thenStatus400() throws Exception {
        long ownerId = currentUser.getOwner() + 1;
        assertTrue(ownerRepository.findById(ownerId).isPresent());

        CatDto dto = new CatDto();
        dto.setName("Testy");
        dto.setBreed("Fluffy");
        dto.setColor(CatColor.SEMICOLOR);
        dto.setBirthdate(LocalDate.now());
        dto.setOwner(ownerId);
        String body = mapper.writeValueAsString(dto);

        mvc.perform(post("/cats")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        int actualSize = catRepository.findAll().size();

        assertEquals(INITIAL_SIZE, actualSize);
    }

    @Test
    @WithUserDetails(value = "Mishka", userDetailsServiceBeanName = "userDetailsService")
    public void delete_whenExistsAndOwned_thenStatus200() throws Exception {
        long id = 1;

        var initialOptional = catRepository.findById(id);
        assertTrue(initialOptional.isPresent());

        mvc.perform(delete("/cats/%s".formatted(id))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        int actualSize = catRepository.findAll().size();
        var postOptional = catRepository.findById(id);

        assertEquals(INITIAL_SIZE - 1, actualSize);
        assertTrue(postOptional.isEmpty());
    }

    @Test
    @WithUserDetails(value = "admin", userDetailsServiceBeanName = "userDetailsService")
    public void delete_whenExistsAndAdmin_thenStatus200() throws Exception {
        long id = 5;

        var initialOptional = catRepository.findById(id);
        assertTrue(initialOptional.isPresent());

        mvc.perform(delete("/cats/%s".formatted(id))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        int actualSize = catRepository.findAll().size();
        var postOptional = catRepository.findById(id);

        assertEquals(INITIAL_SIZE - 1, actualSize);
        assertTrue(postOptional.isEmpty());
    }

    @Test
    @WithUserDetails(value = "Mishka", userDetailsServiceBeanName = "userDetailsService")
    public void delete_whenNotOwned_thenStatus400() throws Exception {
        long id = 5;

        mvc.perform(delete("/cats/%s".formatted(id))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
