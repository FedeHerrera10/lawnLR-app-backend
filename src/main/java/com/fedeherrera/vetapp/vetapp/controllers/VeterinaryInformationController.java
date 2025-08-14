package com.fedeherrera.vetapp.vetapp.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fedeherrera.vetapp.vetapp.dtos.request.DTOVeterinaryInformation;
import com.fedeherrera.vetapp.vetapp.dtos.response.DTOSpecialization;
import com.fedeherrera.vetapp.vetapp.dtos.response.DTOVeterinaryInfo;
import com.fedeherrera.vetapp.vetapp.repositories.SpecializationRepository;
import com.fedeherrera.vetapp.vetapp.services.veterinaryInformation.VeterinaryInformationService;
import com.fedeherrera.vetapp.vetapp.utils.MapperUtil;


@RestController
@RequestMapping("/api/veterinary-information")
public class VeterinaryInformationController {
    
    @Autowired
    private SpecializationRepository specializationRepository;

    @Autowired
    private VeterinaryInformationService service;

    @Autowired
    private MapperUtil mapper;

    @GetMapping
    public List<DTOSpecialization> getAllSpecializations() {
        return mapper.mapList(specializationRepository.findAll(), DTOSpecialization.class);
    }

    @PostMapping("/add/{id}")
    public DTOVeterinaryInfo createSpecialization(@PathVariable Long id, @RequestBody DTOVeterinaryInformation dtoVeterinaryInformation) {
        return service.saveVeterinaryInformation(id, dtoVeterinaryInformation);
    }
    
    @GetMapping("/{id}")
    public DTOVeterinaryInfo getVeterinaryInformation(@PathVariable Long id) {
        return service.getVeterinaryInformation(id);
    }
}
