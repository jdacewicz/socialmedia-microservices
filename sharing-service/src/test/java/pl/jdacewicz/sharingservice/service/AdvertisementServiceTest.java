package pl.jdacewicz.sharingservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import pl.jdacewicz.sharingservice.model.Advertisement;
import pl.jdacewicz.sharingservice.repository.AdvertisementRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
            "Then should return Page of all advertisements")
    void x() {
        String name = null;

        Page<Advertisement> page = new PageImpl<>(List.of());
        when(advertisementRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<Advertisement> result = advertisementService.getAdvertisements(name, 1,
                1, "sort", "directory");
        assertEquals(page, result);
    }

    @Test
    @DisplayName("Given empty name " +
            "When getting advertisements " +
            "Then should return Page of all advertisements")
    void x32() {
        String name = "";

        Page<Advertisement> page = new PageImpl<>(List.of());
        when(advertisementRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<Advertisement> result = advertisementService.getAdvertisements(name, 1,
                1, "sort", "directory");
        assertEquals(page, result);
    }

    @Test
    @DisplayName("Given name " +
            "When getting advertisements " +
            "Then should return Page of all advertisements by name")
    void x2() {
        String name = "test";

        Page<Advertisement> page = new PageImpl<>(List.of());
        when(advertisementRepository.findAllByName(any(String.class), any(Pageable.class))).thenReturn(page);

        Page<Advertisement> result = advertisementService.getAdvertisements(name, 1,
                1, "sort", "directory");
        assertEquals(page, result);
    }
}