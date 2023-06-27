package pl.jdacewicz.postservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.jdacewicz.postservice.dto.AdvertisementDto;
import pl.jdacewicz.postservice.dto.AdvertisementRequest;
import pl.jdacewicz.postservice.dto.mapper.AdvertisementMapper;
import pl.jdacewicz.postservice.model.Advertisement;
import pl.jdacewicz.postservice.service.AdvertisementService;

@RestController
@RequestMapping("/api/advertisements")
public class AdvertisementController {

    private final AdvertisementService advertisementService;
    private final AdvertisementMapper advertisementMapper;

    @Autowired
    public AdvertisementController(AdvertisementService advertisementService, AdvertisementMapper advertisementMapper) {
        this.advertisementService = advertisementService;
        this.advertisementMapper = advertisementMapper;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AdvertisementDto getAdvertisementById(@PathVariable int id) {
        Advertisement advertisement = advertisementService.getAdvertisementById(id);
        return advertisementMapper.convertToDto(advertisement);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AdvertisementDto createAdvertisement(@RequestBody AdvertisementRequest advertisementRequest) {
        Advertisement advertisement = advertisementMapper.convertFromRequest(advertisementRequest);
        Advertisement createdAdvertisement = advertisementService.createAdvertisement(advertisement);
        return advertisementMapper.convertToDto(createdAdvertisement);
    }
}
