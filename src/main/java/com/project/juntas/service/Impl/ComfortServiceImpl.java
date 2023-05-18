package com.project.juntas.service.Impl;

import com.project.juntas.dto.comfort.ComfortRequestDto;
import com.project.juntas.dto.comfort.ComfortResponseDto;
import com.project.juntas.exception.ResourceNotFoundException;
import com.project.juntas.mapper.GenericMapper;
import com.project.juntas.model.Comfort;
import com.project.juntas.model.User;
import com.project.juntas.repository.ComfortRepository;
import com.project.juntas.service.ComfortService;

import lombok.RequiredArgsConstructor;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

import static com.project.juntas.model.enums.MessageCode.RESOURCE_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ComfortServiceImpl implements ComfortService {

    private final GenericMapper mapper;
    private final ComfortRepository repository;
    private final MessageSource messenger;

    @Override
    public ComfortResponseDto create(ComfortRequestDto request) {
        Comfort comfort = mapper.map(request, Comfort.class);

        return mapper.map(repository.save(comfort), ComfortResponseDto.class);
    }


    @Override
    public ComfortResponseDto update(Long id, ComfortRequestDto request) {
        Comfort comfort = repository.findById(id).orElseThrow(()->new ResourceNotFoundException(messenger.getMessage(RESOURCE_NOT_FOUND.name(),
                new Object[]{Comfort.class.getSimpleName()}, Locale.getDefault())));

        comfort.setAcceptChild(request.getAcceptChild());
        //comfort.setLuggage(request.getLuggage());
        comfort.setAcceptSmokers(request.getAcceptSmokers());
        comfort.setPetFriendly(request.getPetFriendly());

        return mapper.map(repository.save(comfort), ComfortResponseDto.class);
    }

    @Override
    public ComfortResponseDto getById(Long id){
        Comfort comfort = repository.findById(id).orElseThrow(()->new ResourceNotFoundException(messenger.getMessage(RESOURCE_NOT_FOUND.name(),
                new Object[]{Comfort.class.getSimpleName(), id}, Locale.getDefault())));
        return mapper.map(comfort,ComfortResponseDto.class);
    }

    @Override
    public void delete(Long id){
        Comfort comfort = repository.findById(id).orElseThrow(()->new ResourceNotFoundException(messenger.getMessage(RESOURCE_NOT_FOUND.name(),
                new Object[]{Comfort.class.getSimpleName(), id}, Locale.getDefault())));
        repository.delete(comfort);
    }

}
