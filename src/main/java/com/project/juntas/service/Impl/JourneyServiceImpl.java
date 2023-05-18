package com.project.juntas.service.Impl;

import com.project.juntas.dto.journey.JourneyRequestDto;
import com.project.juntas.dto.journey.JourneyResponseDto;
import com.project.juntas.dto.place.PlaceApi;
import com.project.juntas.exception.ResourceNotFoundException;
import com.project.juntas.mapper.GenericMapper;
import com.project.juntas.model.*;
import com.project.juntas.repository.*;
import com.project.juntas.service.JourneyService;

import com.project.juntas.service.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestOperations;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Locale;

import static com.project.juntas.model.enums.MessageCode.RESOURCE_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class JourneyServiceImpl implements JourneyService {

    private final GenericMapper mapper;
    private final JourneyRepository repository;
    private final ComfortRepository comfortRepository;
    private final VehicleRepository vehicleRepository;
    private final PlaceRepository placeRepository;
    private final UserRepository userRepository;
    private final MessageSource messenger;



    @Override
    public JourneyResponseDto create(JourneyRequestDto request) {

        Comfort comfort = new Comfort();
        comfort.setAcceptChild(request.isAcceptChild());
        comfort.setAcceptSmokers(request.isAcceptSmokers());
        comfort.setPetFriendly(request.isPetFriendly());
        comfort.setLuggageBig(request.isLuggageBig());
        comfort.setLuggageMid(request.isLuggageMid());
        comfort.setLuggageSma(request.isLuggageSma());
        comfortRepository.save(comfort);

        Place departure = new Place();
        departure.setProvince(request.getProvinceDep());
        departure.setCity(request.getCityDep());
        placeRepository.save(departure);
        Place arrival = new Place();
        arrival.setProvince(request.getProvinceArr());
        arrival.setCity(request.getCityArr());
        placeRepository.save(arrival);

        Vehicle vehicle = vehicleRepository.findById(request.getVehicleId()).orElseThrow(() -> new ResourceNotFoundException(messenger.getMessage(RESOURCE_NOT_FOUND.name(),
                new Object[]{Vehicle.class.getName(), request.getVehicleId()}, Locale.getDefault())));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User driver = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new ResourceNotFoundException(messenger.getMessage(RESOURCE_NOT_FOUND.name(),
                        new Object[]{User.class.getName()}, Locale.getDefault())));

        Journey journey = new Journey();
        journey.setArrivalDate(request.getArrivalDate());
        journey.setDepartureDate(request.getDepartureDate());
        journey.setDriver(driver);
        journey.setComfort(comfort);
        journey.setVehicle(vehicle);
        journey.setDeparture(departure);
        journey.setArrival(arrival);

        return mapper.map(repository.save(journey), JourneyResponseDto.class);
    }

    @Override
    public JourneyResponseDto update(JourneyRequestDto request) {
/*      Journey journey = repository.findById(request.getId()).orElseThrow(()->new ResourceNotFoundException(""));
        Comfort comfort = comfortRepository.findById(request.getComfortId()).orElseThrow(()->new ResourceNotFoundException(""));
        Vehicle vehicle = vehicleRepository.findById(request.getVehicleId()).orElseThrow(()->new ResourceNotFoundException(""));
        Place arrival = placeRepository.findById(request.getArrivalId()).orElseThrow(()->new ResourceNotFoundException(""));
        Place departure = placeRepository.findById(request.getDepartureId()).orElseThrow(()->new ResourceNotFoundException(""));

        journey.setArrivalDate(request.getArrivalDate());
        journey.setDepartureDate(request.getDepartureDate());
        journey.setComfort(comfort);
        journey.setVehicle(vehicle);
        journey.setArrival(arrival);
        journey.setDeparture(departure);

        return mapper.map(repository.save(journey), JourneyResponseDto.class);*/
        return null;
    }

    @Override
    public JourneyResponseDto getById(Long id){
        Journey journey = repository.findById(id).orElseThrow(()->new ResourceNotFoundException(messenger.getMessage(RESOURCE_NOT_FOUND.name(),
                new Object[]{Journey.class.getSimpleName(), id}, Locale.getDefault())));
        return mapper.map(journey,JourneyResponseDto.class);
    }

    @Override
    public List<JourneyResponseDto> getAll(){
        return mapper.mapAll(repository.findAll(),JourneyResponseDto.class);
    }

    @Override
    public void delete(Long id){
        Journey journey = repository.findById(id).orElseThrow(()->new ResourceNotFoundException(messenger.getMessage(RESOURCE_NOT_FOUND.name(),
                new Object[]{Journey.class.getSimpleName(), id}, Locale.getDefault())));
        repository.delete(journey);
    }

}
