package pl.jdacewicz.postservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import pl.jdacewicz.postservice.model.Advertisement;
import pl.jdacewicz.postservice.repository.AdvertisementRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AdvertisementServiceTest {

    @InjectMocks
    AdvertisementService advertisementService;

    @Mock
    AdvertisementRepository advertisementRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Given null name " +
            "When getting advertisements " +
            "Then should return all advertisements")
    void gettingAdvertisementsByNullNameShouldReturnAllAdvertisements() {
        String name = null;

        List<Advertisement> list = List.of(new Advertisement());
        Page<Advertisement> page = new PageImpl<>(list);
        when(advertisementRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<Advertisement> returnedPage = advertisementService.getAdvertisements(name, 1 , 1,
                "id", "asc");
        assertFalse(returnedPage.isEmpty());
    }

    @Test
    @DisplayName("Given empty name " +
            "When getting advertisements " +
            "Then should return all advertisements")
    void gettingAdvertisementsByEmptyNameShouldReturnAllAdvertisements() {
        String name = "";

        List<Advertisement> list = List.of(new Advertisement());
        Page<Advertisement> page = new PageImpl<>(list);
        when(advertisementRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<Advertisement> returnedPage = advertisementService.getAdvertisements(name, 1 , 1,
                "id", "asc");
        assertFalse(returnedPage.isEmpty());
    }

    @Test
    @DisplayName("Given not null and not empty name " +
            "When getting advertisements " +
            "Then should return advertisements with name")
    void gettingAdvertisementsByNotNullAndNotEmptyNameShouldReturnAllAdvertisementsWithName() {
        String name = "test";

        Page<Advertisement> page = Page.empty();
        when(advertisementRepository.findAllByName(any(String.class), any(Pageable.class)))
                .thenReturn(page);

        Page<Advertisement> returnedPage = advertisementService.getAdvertisements(name, 1 , 1,
                "id", "asc");
        assertTrue(returnedPage.isEmpty());
    }
}