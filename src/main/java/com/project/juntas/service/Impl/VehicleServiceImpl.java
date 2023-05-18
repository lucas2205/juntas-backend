package com.project.juntas.service.Impl;

import com.project.juntas.dto.vehicle.VehicleRequestDto;
import com.project.juntas.dto.vehicle.VehicleResponseDto;
import com.project.juntas.exception.ResourceNotFoundException;
import com.project.juntas.mapper.GenericMapper;
import com.project.juntas.model.User;
import com.project.juntas.model.Vehicle;
import com.project.juntas.repository.UserRepository;
import com.project.juntas.repository.VehicleRepository;
import com.project.juntas.service.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

import static com.project.juntas.model.enums.MessageCode.RESOURCE_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    private final GenericMapper mapper;
    private final VehicleRepository repository;
    private final MessageSource messenger;



    @Override
    public VehicleResponseDto create(VehicleRequestDto request) {
        Vehicle vehicle = mapper.map(request, Vehicle.class);
        return mapper.map(repository.save(vehicle), VehicleResponseDto.class);
    }

    @Override
    public VehicleResponseDto update(Long id,VehicleRequestDto request) {
        Vehicle vehicle = repository.findById(id).orElseThrow(()->new ResourceNotFoundException(messenger.getMessage(RESOURCE_NOT_FOUND.name(),
                new Object[]{Vehicle.class.getSimpleName(), request.getPatentNumber()}, Locale.getDefault())));

        vehicle.setVehicleColor(request.getVehicleColor());
        vehicle.setEmptySeats(request.getEmptySeats());
        vehicle.setPrimaryBrand(request.getPrimaryBrand());
        vehicle.setModelName(request.getModelName());
        vehicle.setPatentNumber(request.getPatentNumber());


        return mapper.map(repository.save(vehicle), VehicleResponseDto.class);
    }

    @Override
    public VehicleResponseDto getById(Long id){
        Vehicle vehicle = repository.findById(id).orElseThrow(()->new ResourceNotFoundException(messenger.getMessage(RESOURCE_NOT_FOUND.name(),
                new Object[]{Vehicle.class.getSimpleName(), id}, Locale.getDefault())));
        return mapper.map(vehicle,VehicleResponseDto.class);
    }

    @Override
    public void delete(Long id){
        Vehicle vehicle = repository.findById(id).orElseThrow(()->new ResourceNotFoundException(messenger.getMessage(RESOURCE_NOT_FOUND.name(),
                new Object[]{Vehicle.class.getSimpleName(), id}, Locale.getDefault())));
        repository.delete(vehicle);
    }

}
