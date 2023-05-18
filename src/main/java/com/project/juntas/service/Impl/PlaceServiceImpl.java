package com.project.juntas.service.Impl;

import com.project.juntas.dto.place.PlaceApi;
import com.project.juntas.dto.place.PlaceRequestDto;
import com.project.juntas.dto.place.PlaceResponseDto;
import com.project.juntas.exception.ResourceNotFoundException;
import com.project.juntas.mapper.GenericMapper;
import com.project.juntas.model.Comfort;
import com.project.juntas.model.Place;
import com.project.juntas.repository.PlaceRepository;
import com.project.juntas.service.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Locale;

import static com.project.juntas.model.enums.MessageCode.RESOURCE_NOT_FOUND;


@Service
@RequiredArgsConstructor
public class PlaceServiceImpl implements PlaceService {

    private final PlaceRepository repository;
    private final GenericMapper mapper;
    private final MessageSource messenger;
    private final RestTemplate restTemplate;


    @Override
    public PlaceResponseDto createPlace(PlaceRequestDto requestDto) {

        Place newPlace = mapper.map(requestDto, Place.class);
        return mapper.map(repository.save(newPlace), PlaceResponseDto.class);
    }
    @Override
    public PlaceResponseDto updatePlace(Long id, PlaceRequestDto toUpdate) {
        Place updatedPlace = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(messenger.getMessage(RESOURCE_NOT_FOUND.name(),
                new Object[]{Place.class.getSimpleName(), id}, Locale.getDefault())));

        return mapper.map(repository.save(updatedPlace), PlaceResponseDto.class);
    }

    @Override
    public PlaceResponseDto findById(Long id) {
        Place place = repository
                .findById(id)
                .orElseThrow( () -> new ResourceNotFoundException(messenger.getMessage(RESOURCE_NOT_FOUND.name(),
                        new Object[]{Place.class.getSimpleName(), id}, Locale.getDefault())));
        return mapper.map(place, PlaceResponseDto.class);
    }

    @Override
    public void deletePlace(Long id) {
        Place place = repository
                .findById(id)
                .orElseThrow( () -> new ResourceNotFoundException(messenger.getMessage(RESOURCE_NOT_FOUND.name(),
                        new Object[]{Place.class.getSimpleName(), id}, Locale.getDefault())));
        repository.delete(place);
    }

}
