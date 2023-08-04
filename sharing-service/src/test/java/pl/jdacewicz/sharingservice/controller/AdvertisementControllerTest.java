package pl.jdacewicz.sharingservice.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import pl.jdacewicz.sharingservice.dto.AdvertisementDto;
import pl.jdacewicz.sharingservice.dto.mapper.AdvertisementMapper;
import pl.jdacewicz.sharingservice.model.Advertisement;
import pl.jdacewicz.sharingservice.service.AdvertisementService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdvertisementController.class)
@EnableMethodSecurity
class AdvertisementControllerTest {

    @MockBean
    AdvertisementService service;

    @MockBean
    AdvertisementMapper mapper;

    @Autowired
    private MockMvc mockMvc;

    @Value("${spring.application.api-url}")
    private String API_URL;

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

}