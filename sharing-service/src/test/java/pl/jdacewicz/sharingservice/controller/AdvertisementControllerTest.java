package pl.jdacewicz.sharingservice.controller;

import com.google.gson.Gson;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import pl.jdacewicz.sharingservice.dto.AdvertisementDto;
import pl.jdacewicz.sharingservice.dto.AdvertisementRequest;
import pl.jdacewicz.sharingservice.dto.mapper.AdvertisementMapper;
import pl.jdacewicz.sharingservice.model.Advertisement;
import pl.jdacewicz.sharingservice.service.AdvertisementService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdvertisementController.class)
@EnableMethodSecurity
class AdvertisementControllerTest {

    @MockBean
    private AdvertisementService service;

    @MockBean
    private AdvertisementMapper mapper;

    @Autowired
    private WebApplicationContext context;

    @Value("${spring.application.api-url}")
    private String API_URL;

    private MockMvc mockMvc;

    private static Gson gson;

    @BeforeEach
    void xd() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @BeforeAll
    static void setUp() {
        gson = new Gson();
    }

    @Test
    @WithMockUser(roles = "user")
    @DisplayName("Given id, logged user with role user, x-api-version=1 header " +
            "When getting active advertisement " +
            "Then should return response ok")
    void gettingActiveAdvertisementByIdAndLoggedUserWithRoleUserAndApiVersion1HeaderShouldReturnResponseOk() throws Exception {
        int id = 1;

        this.mockMvc.perform(get(API_URL + "/advertisements/" + id)
                        .header("X-API-VERSION", 1))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "user")
    @DisplayName("Given name, page, sort, directory, logged user with role user, x-api-version=1 header " +
            "When getting advertisements " +
            "Then should return response ok")
    void gettingAdvertisementsByNameAndPageAndSortAndDirectoryAndLoggedUserWithRoleUserAndApiVersion1HeaderShouldReturnResponseOk() throws Exception {
        String name = "";
        int page = 0;
        int size = 1;
        String sort = "sort";
        String directory = "directory";

        Page<Advertisement> ad = new PageImpl<>(List.of());
        when(service.getAdvertisements(name, page, size, sort, directory)).thenReturn(ad);
        AdvertisementDto dto = new AdvertisementDto();
        when(mapper.convertToDto(any(Advertisement.class))).thenReturn(dto);

        this.mockMvc.perform(get(API_URL + "/advertisements")
                    .param("name", name)
                    .param("page", String.valueOf(page))
                    .param("size", String.valueOf(size))
                    .param("sort", sort)
                    .param("directory", directory)
                    .header("X-API-VERSION", 1))
                .andExpect(status().isOk());

    }

    @Test
    @WithMockUser(roles = "admin")
    @DisplayName("Given image, null email, valid name, valid content, " +
            "logged user with role admin, x-api-version=1 header " +
            "When creating advertisement " +
            "Then should return response bad request")
    void creatingAdvertisementByImageAndNullEmailAndValidNameAndValidContentAndLoggedUserWithRoleAdminAndApiVersion1HeaderShouldReturnResponseBadRequest() throws Exception {
        AdvertisementRequest request = new AdvertisementRequest(null, "name", "content");
        MockMultipartFile image = new MockMultipartFile("image", "filename.png", "image/png", "xdd".getBytes());

        String json = gson.toJson(request);
        MockMultipartFile jsonFile = new MockMultipartFile("request", "", "application/json", json.getBytes());

        this.mockMvc.perform(multipart(API_URL + "/advertisements")
                        .file(image)
                        .file(jsonFile)
                        .header("X-API-VERSION", 1)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "admin")
    @DisplayName("Given image, empty email, valid name, valid content, " +
            "logged user with role admin, x-api-version=1 header " +
            "When creating advertisement " +
            "Then should return response bad request")
    void creatingAdvertisementByImageAndEmptyEmailAndValidNameAndValidContentAndLoggedUserWithRoleAdminAndApiVersion1HeaderShouldReturnResponseBadRequest() throws Exception {
        AdvertisementRequest request = new AdvertisementRequest("", "name", "content");
        MockMultipartFile image = new MockMultipartFile("image", "filename.png", "image/png", "xdd".getBytes());

        String json = gson.toJson(request);
        MockMultipartFile jsonFile = new MockMultipartFile("request", "", "application/json", json.getBytes());

        this.mockMvc.perform(multipart(API_URL + "/advertisements")
                        .file(image)
                        .file(jsonFile)
                        .header("X-API-VERSION", 1)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "admin")
    @DisplayName("Given image, valid email, null name, valid content, " +
            "logged user with role admin, x-api-version=1 header " +
            "When creating advertisement " +
            "Then should return response bad request")
    void creatingAdvertisementByImageAndValidEmailAndNullNameAndValidContentAndLoggedUserWithRoleAdminAndApiVersion1HeaderShouldReturnResponseBadRequest() throws Exception {
        AdvertisementRequest request = new AdvertisementRequest("test@test.com", null, "content");
        MockMultipartFile image = new MockMultipartFile("image", "filename.png", "image/png", "xdd".getBytes());

        String json = gson.toJson(request);
        MockMultipartFile jsonFile = new MockMultipartFile("request", "", "application/json", json.getBytes());

        this.mockMvc.perform(multipart(API_URL + "/advertisements")
                        .file(image)
                        .file(jsonFile)
                        .header("X-API-VERSION", 1)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "admin")
    @DisplayName("Given image, valid email, empty name, valid content, " +
            "logged user with role admin, x-api-version=1 header " +
            "When creating advertisement " +
            "Then should return response bad request")
    void creatingAdvertisementByImageAndValidEmailAndEmptyNameAndValidContentLoggedUserWithRoleAdminAndApiVersion1HeaderShouldReturnResponseBadRequest() throws Exception {
        AdvertisementRequest request = new AdvertisementRequest("test@test.com", "", "content");
        MockMultipartFile image = new MockMultipartFile("image", "filename.png", "image/png", "xdd".getBytes());

        String json = gson.toJson(request);
        MockMultipartFile jsonFile = new MockMultipartFile("request", "", "application/json", json.getBytes());

        this.mockMvc.perform(multipart(API_URL + "/advertisements")
                        .file(image)
                        .file(jsonFile)
                        .header("X-API-VERSION", 1)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "admin")
    @DisplayName("Given image, valid email, valid name, empty content, " +
            "logged user with role admin, x-api-version=1 header " +
            "When creating advertisement " +
            "Then should return response bad request")
    void creatingAdvertisementByImageAndValidEmailAndValidNameAndEmptyContentAndLoggedUserWithRoleAdminAndApiVersion1HeaderShouldReturnResponseBadRequest() throws Exception {
        AdvertisementRequest request = new AdvertisementRequest("test@test.com", "name", "");
        MockMultipartFile image = new MockMultipartFile("image", "filename.png", "image/png", "xdd".getBytes());

        String json = gson.toJson(request);
        MockMultipartFile jsonFile = new MockMultipartFile("request", "", "application/json", json.getBytes());

        this.mockMvc.perform(multipart(API_URL + "/advertisements")
                        .file(image)
                        .file(jsonFile)
                        .header("X-API-VERSION", 1)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "admin")
    @DisplayName("Given image, valid email, valid name, null content, " +
            "logged user with role admin, x-api-version=1 header " +
            "When creating advertisement " +
            "Then should return response bad request")
    void creatingAdvertisementByImageAndValidEmailAndValidNameAndNullContentAndLoggedUserWithRoleAdminAndApiVersion1HeaderShouldReturnResponseBadRequest() throws Exception {
        AdvertisementRequest request = new AdvertisementRequest("test@test.com", "name", null);
        MockMultipartFile image = new MockMultipartFile("image", "filename.png", "image/png", "xdd".getBytes());

        String json = gson.toJson(request);
        MockMultipartFile jsonFile = new MockMultipartFile("request", "", "application/json", json.getBytes());

        this.mockMvc.perform(multipart(API_URL + "/advertisements")
                        .file(image)
                        .file(jsonFile)
                        .header("X-API-VERSION", 1)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "admin")
    @DisplayName("Given image, not proper email, valid name, valid content, " +
            "logged user with role admin, x-api-version=1 header " +
            "When creating advertisement " +
            "Then should return response bad request")
    void creatingAdvertisementByImageAndNotProperEmailAndValidNameAndValidContentAndLoggedUserWithRoleAdminAndApiVersion1HeaderShouldReturnResponseBadRequest() throws Exception {
        AdvertisementRequest request = new AdvertisementRequest("test", "name", "content");
        MockMultipartFile image = new MockMultipartFile("image", "filename.png", "image/png", "xdd".getBytes());

        String json = gson.toJson(request);
        MockMultipartFile jsonFile = new MockMultipartFile("request", "", "application/json", json.getBytes());

        this.mockMvc.perform(multipart(API_URL + "/advertisements")
                        .file(image)
                        .file(jsonFile)
                        .header("X-API-VERSION", 1)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "admin")
    @DisplayName("Given image, valid email, too short name, valid content, " +
            "logged user with role admin, x-api-version=1 header " +
            "When creating advertisement " +
            "Then should return response bad request")
    void creatingAdvertisementByImageAndValidEmailAndTooShortNameAndValidContentAndLoggedUserWithRoleAdminAndApiVersion1HeaderShouldReturnResponseBadRequest() throws Exception {
        AdvertisementRequest request = new AdvertisementRequest("test@test.com", "na", "content");
        MockMultipartFile image = new MockMultipartFile("image", "filename.png", "image/png", "xdd".getBytes());

        String json = gson.toJson(request);
        MockMultipartFile jsonFile = new MockMultipartFile("request", "", "application/json", json.getBytes());

        this.mockMvc.perform(multipart(API_URL + "/advertisements")
                        .file(image)
                        .file(jsonFile)
                        .header("X-API-VERSION", 1)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isBadRequest());
    }


    @Test
    @WithMockUser(roles = "admin")
    @DisplayName("Given image, valid email, too long name, valid content, " +
            "logged user with role admin, x-api-version=1 header " +
            "When creating advertisement " +
            "Then should return response bad request")
    void creatingAdvertisementByImageAndValidEmailAndTooLongNameAndValidContentAndLoggedUserWithRoleAdminAndApiVersion1HeaderShouldReturnResponseBadRequest() throws Exception {
        String name = RandomStringUtils.randomAlphanumeric(33);
        AdvertisementRequest request = new AdvertisementRequest("test@test.com", name, "content");
        MockMultipartFile image = new MockMultipartFile("image", "filename.png", "image/png", "xdd".getBytes());

        String json = gson.toJson(request);
        MockMultipartFile jsonFile = new MockMultipartFile("request", "", "application/json", json.getBytes());

        this.mockMvc.perform(multipart(API_URL + "/advertisements")
                        .file(image)
                        .file(jsonFile)
                        .header("X-API-VERSION", 1)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "admin")
    @DisplayName("Given image, valid email, valid name, too long content, " +
            "logged user with role admin, x-api-version=1 header " +
            "When creating advertisement " +
            "Then should return response bad request")
    void creatingAdvertisementByImageAndValidEmailAndValidNameAndTooLongContentAndLoggedUserWithRoleAdminAndApiVersion1HeaderShouldReturnResponseBadRequest() throws Exception {
        String content = RandomStringUtils.randomAlphanumeric(256);
        AdvertisementRequest request = new AdvertisementRequest("test@test.com", "name", content);
        MockMultipartFile image = new MockMultipartFile("image", "filename.png", "image/png", "xdd".getBytes());

        String json = gson.toJson(request);
        MockMultipartFile jsonFile = new MockMultipartFile("request", "", "application/json", json.getBytes());

        this.mockMvc.perform(multipart(API_URL + "/advertisements")
                        .file(image)
                        .file(jsonFile)
                        .header("X-API-VERSION", 1)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "admin")
    @DisplayName("Given image, valid email, valid name, valid content, " +
            "logged user with role admin, x-api-version=1 header " +
            "When creating advertisement " +
            "Then should return response created")
    void creatingAdvertisementByImageAndValidEmailAndValidNameAndValidContentAndLoggedUserWithRoleAdminAndApiVersion1HeaderShouldReturnResponseCreated() throws Exception {
        AdvertisementRequest request = new AdvertisementRequest("test@test.com", "name", "content");
        MockMultipartFile image = new MockMultipartFile("image", "filename.png", "image/png", "xdd".getBytes());

        String json = gson.toJson(request);
        MockMultipartFile jsonFile = new MockMultipartFile("request", "", "application/json", json.getBytes());

        this.mockMvc.perform(multipart(API_URL + "/advertisements")
                        .file(image)
                        .file(jsonFile)
                        .header("X-API-VERSION", 1)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "admin")
    @DisplayName("Given id, image, null email, valid name, valid content, " +
            "logged user with role admin, x-api-version=1 header " +
            "When updating advertisement " +
            "Then should return response bad request")
    void updatingAdvertisementByIdAndNullEmailAndValidNameAndValidContentAndLoggedUserWithRoleAdminAndApiVersion1HeaderShouldReturnResponseBadRequest() throws Exception {
        int id = 1;
        AdvertisementRequest request = new AdvertisementRequest(null, "name", "content");
        MockMultipartFile image = new MockMultipartFile("image", "filename.png", "image/png", "xdd".getBytes());

        String json = gson.toJson(request);
        MockMultipartFile jsonFile = new MockMultipartFile("request", "", "application/json", json.getBytes());

        MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart(API_URL + "/advertisements/" + id);
        builder.with(builtRequest -> {
            builtRequest.setMethod("PUT");
            return builtRequest;
        });
        this.mockMvc.perform(builder
                        .file(image)
                        .file(jsonFile)
                        .header("X-API-VERSION", 1)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "admin")
    @DisplayName("Given id, image, empty email, valid name, valid content, " +
            "logged user with role admin, x-api-version=1 header " +
            "When updating advertisement " +
            "Then should return response bad request")
    void updatingAdvertisementByIdAndEmptyEmailAndValidNameAndValidContentAndLoggedUserWithRoleAdminAndApiVersion1HeaderShouldReturnResponseBadRequest() throws Exception {
        int id = 1;
        AdvertisementRequest request = new AdvertisementRequest("", "name", "content");
        MockMultipartFile image = new MockMultipartFile("image", "filename.png", "image/png", "xdd".getBytes());

        String json = gson.toJson(request);
        MockMultipartFile jsonFile = new MockMultipartFile("request", "", "application/json", json.getBytes());

        MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart(API_URL + "/advertisements/" + id);
        builder.with(builtRequest -> {
            builtRequest.setMethod("PUT");
            return builtRequest;
        });
        this.mockMvc.perform(builder
                        .file(image)
                        .file(jsonFile)
                        .header("X-API-VERSION", 1)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "admin")
    @DisplayName("Given id, image, not proper email, valid name, valid content, " +
            "logged user with role admin, x-api-version=1 header " +
            "When updating advertisement " +
            "Then should return response bad request")
    void updatingAdvertisementByIdAndNotProperEmailAndValidNameAndValidContentAndLoggedUserWithRoleAdminAndApiVersion1HeaderShouldReturnResponseBadRequest() throws Exception {
        int id = 1;
        AdvertisementRequest request = new AdvertisementRequest("test", "name", "content");
        MockMultipartFile image = new MockMultipartFile("image", "filename.png", "image/png", "xdd".getBytes());

        String json = gson.toJson(request);
        MockMultipartFile jsonFile = new MockMultipartFile("request", "", "application/json", json.getBytes());

        MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart(API_URL + "/advertisements/" + id);
        builder.with(builtRequest -> {
            builtRequest.setMethod("PUT");
            return builtRequest;
        });
        this.mockMvc.perform(builder
                        .file(image)
                        .file(jsonFile)
                        .header("X-API-VERSION", 1)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "admin")
    @DisplayName("Given id, image, valid email, empty name, valid content, " +
            "logged user with role admin, x-api-version=1 header " +
            "When updating advertisement " +
            "Then should return response bad request")
    void updatingAdvertisementByIdAndNotValidEmailAndEmptyNameAndValidContentAndLoggedUserWithRoleAdminAndApiVersion1HeaderShouldReturnResponseBadRequest() throws Exception {
        int id = 1;
        AdvertisementRequest request = new AdvertisementRequest("test@test.com", "", "content");
        MockMultipartFile image = new MockMultipartFile("image", "filename.png", "image/png", "xdd".getBytes());

        String json = gson.toJson(request);
        MockMultipartFile jsonFile = new MockMultipartFile("request", "", "application/json", json.getBytes());

        MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart(API_URL + "/advertisements/" + id);
        builder.with(builtRequest -> {
            builtRequest.setMethod("PUT");
            return builtRequest;
        });
        this.mockMvc.perform(builder
                        .file(image)
                        .file(jsonFile)
                        .header("X-API-VERSION", 1)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "admin")
    @DisplayName("Given id, image, valid email, null name, valid content, " +
            "logged user with role admin, x-api-version=1 header " +
            "When updating advertisement " +
            "Then should return response bad request")
    void updatingAdvertisementByIdAndNotValidEmailAndNullNameAndValidContentAndLoggedUserWithRoleAdminAndApiVersion1HeaderShouldReturnResponseBadRequest() throws Exception {
        int id = 1;
        AdvertisementRequest request = new AdvertisementRequest("test@test.com", null, "content");
        MockMultipartFile image = new MockMultipartFile("image", "filename.png", "image/png", "xdd".getBytes());

        String json = gson.toJson(request);
        MockMultipartFile jsonFile = new MockMultipartFile("request", "", "application/json", json.getBytes());

        MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart(API_URL + "/advertisements/" + id);
        builder.with(builtRequest -> {
            builtRequest.setMethod("PUT");
            return builtRequest;
        });
        this.mockMvc.perform(builder
                        .file(image)
                        .file(jsonFile)
                        .header("X-API-VERSION", 1)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "admin")
    @DisplayName("Given id, image, valid email, too short name, valid content, " +
            "logged user with role admin, x-api-version=1 header " +
            "When updating advertisement " +
            "Then should return response bad request")
    void updatingAdvertisementByIdAndNotValidEmailAndTooShortNameAndValidContentAndLoggedUserWithRoleAdminAndApiVersion1HeaderShouldReturnResponseBadRequest() throws Exception {
        int id = 1;
        AdvertisementRequest request = new AdvertisementRequest("test@test.com", "x", "content");
        MockMultipartFile image = new MockMultipartFile("image", "filename.png", "image/png", "xdd".getBytes());

        String json = gson.toJson(request);
        MockMultipartFile jsonFile = new MockMultipartFile("request", "", "application/json", json.getBytes());

        MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart(API_URL + "/advertisements/" + id);
        builder.with(builtRequest -> {
            builtRequest.setMethod("PUT");
            return builtRequest;
        });
        this.mockMvc.perform(builder
                        .file(image)
                        .file(jsonFile)
                        .header("X-API-VERSION", 1)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "admin")
    @DisplayName("Given id, image, valid email, too long name, valid content, " +
            "logged user with role admin, x-api-version=1 header " +
            "When updating advertisement " +
            "Then should return response bad request")
    void updatingAdvertisementByIdAndNotValidEmailAndTooLongNameAndValidContentAndLoggedUserWithRoleAdminAndApiVersion1HeaderShouldReturnResponseBadRequest() throws Exception {
        int id = 1;
        String name = RandomStringUtils.randomAlphanumeric(33);
        AdvertisementRequest request = new AdvertisementRequest("test@test.com", name, "content");
        MockMultipartFile image = new MockMultipartFile("image", "filename.png", "image/png", "xdd".getBytes());

        String json = gson.toJson(request);
        MockMultipartFile jsonFile = new MockMultipartFile("request", "", "application/json", json.getBytes());

        MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart(API_URL + "/advertisements/" + id);
        builder.with(builtRequest -> {
            builtRequest.setMethod("PUT");
            return builtRequest;
        });
        this.mockMvc.perform(builder
                        .file(image)
                        .file(jsonFile)
                        .header("X-API-VERSION", 1)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "admin")
    @DisplayName("Given id, image, valid email, valid name, null content, " +
            "logged user with role admin, x-api-version=1 header " +
            "When updating advertisement " +
            "Then should return response bad request")
    void updatingAdvertisementByIdAndNotValidEmailAndValidNameAndNullContentAndLoggedUserWithRoleAdminAndApiVersion1HeaderShouldReturnResponseBadRequest() throws Exception {
        int id = 1;
        AdvertisementRequest request = new AdvertisementRequest("test@test.com", "name", null);
        MockMultipartFile image = new MockMultipartFile("image", "filename.png", "image/png", "xdd".getBytes());

        String json = gson.toJson(request);
        MockMultipartFile jsonFile = new MockMultipartFile("request", "", "application/json", json.getBytes());

        MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart(API_URL + "/advertisements/" + id);
        builder.with(builtRequest -> {
            builtRequest.setMethod("PUT");
            return builtRequest;
        });
        this.mockMvc.perform(builder
                        .file(image)
                        .file(jsonFile)
                        .header("X-API-VERSION", 1)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "admin")
    @DisplayName("Given id, image, valid email, valid name, empty content, " +
            "logged user with role admin, x-api-version=1 header " +
            "When updating advertisement " +
            "Then should return response bad request")
    void updatingAdvertisementByIdAndNotValidEmailAndValidNameAndEmptyContentAndLoggedUserWithRoleAdminAndApiVersion1HeaderShouldReturnResponseBadRequest() throws Exception {
        int id = 1;
        AdvertisementRequest request = new AdvertisementRequest("test@test.com", "name", "");
        MockMultipartFile image = new MockMultipartFile("image", "filename.png", "image/png", "xdd".getBytes());

        String json = gson.toJson(request);
        MockMultipartFile jsonFile = new MockMultipartFile("request", "", "application/json", json.getBytes());

        MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart(API_URL + "/advertisements/" + id);
        builder.with(builtRequest -> {
            builtRequest.setMethod("PUT");
            return builtRequest;
        });
        this.mockMvc.perform(builder
                        .file(image)
                        .file(jsonFile)
                        .header("X-API-VERSION", 1)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "admin")
    @DisplayName("Given id, image, valid email, valid name, too long content, " +
            "logged user with role admin, x-api-version=1 header " +
            "When updating advertisement " +
            "Then should return response bad request")
    void updatingAdvertisementByIdAndNotValidEmailAndValidNameAndTooLongContentAndLoggedUserWithRoleAdminAndApiVersion1HeaderShouldReturnResponseBadRequest() throws Exception {
        int id = 1;
        String content = RandomStringUtils.randomAlphanumeric(256);
        AdvertisementRequest request = new AdvertisementRequest("test@test.com", "name", content);
        MockMultipartFile image = new MockMultipartFile("image", "filename.png", "image/png", "xdd".getBytes());

        String json = gson.toJson(request);
        MockMultipartFile jsonFile = new MockMultipartFile("request", "", "application/json", json.getBytes());

        MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart(API_URL + "/advertisements/" + id);
        builder.with(builtRequest -> {
            builtRequest.setMethod("PUT");
            return builtRequest;
        });
        this.mockMvc.perform(builder
                        .file(image)
                        .file(jsonFile)
                        .header("X-API-VERSION", 1)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = "admin")
    @DisplayName("Given id, image, valid email, valid name, valid content, " +
            "logged user with role admin, x-api-version=1 header " +
            "When updating advertisement " +
            "Then should return response ok")
    void updatingAdvertisementByIdAndNotValidEmailAndValidNameAndValidContentAndLoggedUserWithRoleAdminAndApiVersion1HeaderShouldReturnResponseOk() throws Exception {
        int id = 1;
        AdvertisementRequest request = new AdvertisementRequest("test@test.com", "name", "content");
        MockMultipartFile image = new MockMultipartFile("image", "filename.png", "image/png", "xdd".getBytes());

        String json = gson.toJson(request);
        MockMultipartFile jsonFile = new MockMultipartFile("request", "", "application/json", json.getBytes());

        MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart(API_URL + "/advertisements/" + id);
        builder.with(builtRequest -> {
            builtRequest.setMethod("PUT");
            return builtRequest;
        });
        this.mockMvc.perform(builder
                        .file(image)
                        .file(jsonFile)
                        .header("X-API-VERSION", 1)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "admin")
    @DisplayName("Given id, logged user with role admin, " +
            "logged user with role admin, x-api-version=1 header " +
            "When deleting advertisement " +
            "Then should return response ok")
    void deletingAdvertisementByIdAndLoggedUserWithAdminRoleAndLoggedUserWithRoleAdminAndApiVersion1HeaderShouldReturnResponseOk() throws Exception {
        int id = 1;

        this.mockMvc.perform(delete(API_URL + "/advertisements/" + id)
                        .header("X-API-VERSION", 1))
                .andExpect(status().isOk());
    }
}