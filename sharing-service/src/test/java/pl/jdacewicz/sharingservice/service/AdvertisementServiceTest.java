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
import pl.jdacewicz.sharingservice.exception.RecordNotFoundException;
import pl.jdacewicz.sharingservice.model.Advertisement;
import pl.jdacewicz.sharingservice.repository.AdvertisementRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    void gettingAdvertisementsByNullNameShouldReturnProperPage() {
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
    void gettingAdvertisementsByEmptyNameShouldReturnProperPage() {
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
    void gettingAdvertisementsByNameShouldReturnProperPage() {
        String name = "test";

        Page<Advertisement> page = new PageImpl<>(List.of());
        when(advertisementRepository.findAllByName(any(String.class), any(Pageable.class))).thenReturn(page);

        Page<Advertisement> result = advertisementService.getAdvertisements(name, 1,
                1, "sort", "directory");
        assertEquals(page, result);
    }

    @Test
    @DisplayName("Given existing id " +
            "When getting advertisement " +
            "Then should return advertisement")
    void gettingAdvertisementByExistingIdShouldReturnAdvertisement() {
        int id = 1;

        Advertisement ad = new Advertisement();
        when(advertisementRepository.findById(id)).thenReturn(Optional.of(ad));

        Advertisement result = advertisementService.getAdvertisementById(id);

        assertEquals(ad, result);
    }

    @Test
    @DisplayName("Given not existing id " +
            "When getting advertisement " +
            "Then should throw RecordNotFoundException")
    void gettingAdvertisementByNotExistingIdShouldThrowException() {
        int id = 1;

        when(advertisementRepository.findById(id)).thenThrow(RecordNotFoundException.class);

        assertThrows(RecordNotFoundException.class,
                () -> advertisementService.getAdvertisementById(id));
    }

    @Test
    @DisplayName("Given existing id " +
            "When getting active advertisement " +
            "Then should throw RecordNotFoundException")
    void gettingActiveAdvertisementByExistingIdShouldReturnAdvertisement() {
        int id = 1;

        Advertisement ad = new Advertisement();
        when(advertisementRepository.findByIdAndActive(id, true)).thenReturn(Optional.of(ad));

        Advertisement result = advertisementService.getActiveAdvertisementById(id);

        assertEquals(ad, result);
    }

    @Test
    @DisplayName("Given not existing id " +
            "When getting active advertisement " +
            "Then should throw RecordNotFoundException")
    void gettingActiveAdvertisementByNotExistingIdShouldThrowException() {
        int id = 1;

        when(advertisementRepository.findByIdAndActive(id, true)).thenThrow(RecordNotFoundException.class);

        assertThrows(RecordNotFoundException.class,
                () -> advertisementService.getActiveAdvertisementById(id));
    }
}