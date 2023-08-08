package pl.jdacewicz.sharingservice.controller;

import com.google.gson.Gson;
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
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
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
    AdvertisementService service;

    @MockBean
    AdvertisementMapper mapper;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Value("${spring.application.api-url}")
    private String API_URL;

    @BeforeEach
    void xd() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
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
    @WithMockUser(roles = "test")
    @DisplayName("Given id, logged user hasn't got role user, x-api-version=1 header " +
            "When getting active advertisement " +
            "Then should return response unauthorized")
    void gettingActiveAdvertisementByIdAndLoggedUserWithoutRoleUserAndApiVersion1HeaderShouldReturnResponseForbidden() throws Exception {
        int id = 1;

        this.mockMvc.perform(get(API_URL + "/advertisements/" + id)
                        .header("X-API-VERSION", 1))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    @DisplayName("Given id, user is not logged, x-api-version=1 header " +
            "When getting active advertisement " +
            "Then should return response unauthenticated")
    void gettingActiveAdvertisementByIdAndNotLoggedUserAndApiVersion1HeaderShouldReturnResponseUnauthorized() throws Exception {
        int id = 1;

        this.mockMvc.perform(get(API_URL + "/advertisements/" + id)
                        .header("X-API-VERSION", 1))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "user")
    @DisplayName("Given name, page, sort, directory, logged user with role user, x-api-version=1 header " +
            "When getting advertisements " +
            "Then should return response bad request")
    void x() throws Exception {
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
    @WithMockUser(roles = "test")
    @DisplayName("Given name, invalid page, sort, directory, logged user without role user, x-api-version=1 header " +
            "When getting advertisements " +
            "Then should return response bad request")
    void x2() throws Exception {
        String name = "";
        int page = 0;
        int size = 1;
        String sort = "sort";
        String directory = "directory";

        this.mockMvc.perform(get(API_URL + "/advertisements")
                        .param("name", name)
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .param("sort", sort)
                        .param("directory", directory)
                        .header("X-API-VERSION", 1))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    @DisplayName("Given name, invalid page, sort, directory, not logged user, x-api-version=1 header " +
            "When getting advertisements " +
            "Then should return response bad request")
    void x3() throws Exception {
        String name = "";
        int page = 0;
        int size = 1;
        String sort = "sort";
        String directory = "directory";

        this.mockMvc.perform(get(API_URL + "/advertisements")
                        .param("name", name)
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .param("sort", sort)
                        .param("directory", directory)
                        .header("X-API-VERSION", 1))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "admin")
    @DisplayName("Given image, null email, valid name, valid content " +
            "When creating advertisement " +
            "Then should return response bad request")
    void creatingAdvertisementByImageAndNullEmailAndValidNameAndValidContentShouldReturnResponseBadRequest() throws Exception {
        AdvertisementRequest request = new AdvertisementRequest(null, "name", "content");
        MockMultipartFile image = new MockMultipartFile("image", "filename.png", "image/png", "xdd".getBytes());

        Gson gson = new Gson();
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
    @DisplayName("Given image, empty email, valid name, valid content " +
            "When creating advertisement " +
            "Then should return response bad request")
    void creatingAdvertisementByImageAndEmptyEmailAndValidNameAndValidContentShouldReturnResponseBadRequest() throws Exception {
        AdvertisementRequest request = new AdvertisementRequest("", "name", "content");
        MockMultipartFile image = new MockMultipartFile("image", "filename.png", "image/png", "xdd".getBytes());

        Gson gson = new Gson();
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
    @DisplayName("Given image, valid email, null name, valid content " +
            "When creating advertisement " +
            "Then should return response bad request")
    void creatingAdvertisementByImageAndValidEmailAndNullNameAndValidContentShouldReturnResponseBadRequest() throws Exception {
        AdvertisementRequest request = new AdvertisementRequest("test@test.com", null, "content");
        MockMultipartFile image = new MockMultipartFile("image", "filename.png", "image/png", "xdd".getBytes());

        Gson gson = new Gson();
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
    @DisplayName("Given image, valid email, empty name, valid content " +
            "When creating advertisement " +
            "Then should return response bad request")
    void creatingAdvertisementByImageAndValidEmailAndEmptyNameAndValidContentShouldReturnResponseBadRequest() throws Exception {
        AdvertisementRequest request = new AdvertisementRequest("test@test.com", "", "content");
        MockMultipartFile image = new MockMultipartFile("image", "filename.png", "image/png", "xdd".getBytes());

        Gson gson = new Gson();
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
    @DisplayName("Given image, valid email, valid name, empty content " +
            "When creating advertisement " +
            "Then should return response bad request")
    void creatingAdvertisementByImageAndValidEmailAndValidNameAndEmptyContentShouldReturnResponseBadRequest() throws Exception {
        AdvertisementRequest request = new AdvertisementRequest("test@test.com", "name", "");
        MockMultipartFile image = new MockMultipartFile("image", "filename.png", "image/png", "xdd".getBytes());

        Gson gson = new Gson();
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
    @DisplayName("Given image, valid email, valid name, null content " +
            "When creating advertisement " +
            "Then should return response bad request")
    void creatingAdvertisementByImageAndValidEmailAndValidNameAndNullContentShouldReturnResponseBadRequest() throws Exception {
        AdvertisementRequest request = new AdvertisementRequest("test@test.com", "name", null);
        MockMultipartFile image = new MockMultipartFile("image", "filename.png", "image/png", "xdd".getBytes());

        Gson gson = new Gson();
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
    @DisplayName("Given image, not proper email, valid name, valid content " +
            "When creating advertisement " +
            "Then should return response bad request")
    void creatingAdvertisementByImageAndNotProperEmailAndValidNameAndValidContentShouldReturnResponseBadRequest() throws Exception {
        AdvertisementRequest request = new AdvertisementRequest("test", "name", "content");
        MockMultipartFile image = new MockMultipartFile("image", "filename.png", "image/png", "xdd".getBytes());

        Gson gson = new Gson();
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
    @DisplayName("Given image, valid email, 3 characters length name, valid content " +
            "When creating advertisement " +
            "Then should return response bad request")
    void creatingAdvertisementByImageAndValidEmailAndTooShortNameAndValidContentShouldReturnResponseBadRequest() throws Exception {
        AdvertisementRequest request = new AdvertisementRequest("test", "na", "content");
        MockMultipartFile image = new MockMultipartFile("image", "filename.png", "image/png", "xdd".getBytes());

        Gson gson = new Gson();
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
    @DisplayName("Given image, valid email, valid name, valid content " +
            "When creating advertisement " +
            "Then should return response created")
    void creatingAdvertisementByImageAndValidEmailAndValidNameAndValidContentShouldReturnResponseCreated() throws Exception {
        AdvertisementRequest request = new AdvertisementRequest("test@test.com", "name", "content");
        MockMultipartFile image = new MockMultipartFile("image", "filename.png", "image/png", "xdd".getBytes());

        Gson gson = new Gson();
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
    @DisplayName("Given id, logged user with role admin, x-api-version=1 header " +
            "When deleting advertisement " +
            "Then should return response ok")
    void deletingAdvertisementByIdAndLoggedUserWithAdminRoleAndApiVersion1HeaderShouldReturnResponseOk() throws Exception {
        int id = 1;

        this.mockMvc.perform(delete(API_URL + "/advertisements/" + id)
                        .header("X-API-VERSION", 1))
                .andExpect(status().isOk());
    }
}