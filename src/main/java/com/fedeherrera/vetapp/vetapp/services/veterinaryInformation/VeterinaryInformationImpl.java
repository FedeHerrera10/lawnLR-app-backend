package com.fedeherrera.vetapp.vetapp.services.veterinaryInformation;

import com.fedeherrera.vetapp.vetapp.dtos.request.DTOVeterinaryInformation;
import com.fedeherrera.vetapp.vetapp.dtos.response.DTOVeterinaryInfo;
import com.fedeherrera.vetapp.vetapp.entities.Specialization;
import com.fedeherrera.vetapp.vetapp.entities.User;
import com.fedeherrera.vetapp.vetapp.entities.VeterinaryInformation;
import com.fedeherrera.vetapp.vetapp.repositories.SpecializationRepository;
import com.fedeherrera.vetapp.vetapp.repositories.UserRepository;
import com.fedeherrera.vetapp.vetapp.repositories.VeterinaryInformationRepository;
import com.fedeherrera.vetapp.vetapp.utils.MapperUtil;

import jakarta.persistence.EntityNotFoundException;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VeterinaryInformationImpl implements VeterinaryInformationService {

    @Autowired
    private VeterinaryInformationRepository veterinaryInformationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SpecializationRepository specializationRepository;

    @Autowired
    private MapperUtil mapper;


    @Transactional
    public DTOVeterinaryInfo saveVeterinaryInformation(Long id, DTOVeterinaryInformation dto) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
        
        VeterinaryInformation info = veterinaryInformationRepository.findByUser(user)
            .orElseGet(() -> {
                VeterinaryInformation newInfo = new VeterinaryInformation();
                newInfo.setUser(user);
                return newInfo;
            });
        
        // Actualizar datos básicos
        info.setLicenseNumber(dto.getLicenseNumber());
        info.setProfessionalTitle(dto.getProfessionalTitle());
        
        // Manejar especializaciones
        info.clearSpecializations(); // Limpiar especializaciones existentes
        if (dto.getSpecializations() != null && !dto.getSpecializations().isEmpty()) {
            Iterable<Specialization> specializationIterable = specializationRepository.findAllById(dto.getSpecializations());
            Set<Specialization> specializations = new HashSet<>();
            specializationIterable.forEach(specializations::add);
            specializations.forEach(info::addSpecialization);
        }
        
        VeterinaryInformation saved = veterinaryInformationRepository.save(info);
        return mapper.mapEntityToDto(saved, DTOVeterinaryInfo.class);
    }


    @Override
    public DTOVeterinaryInfo getVeterinaryInformation(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado"));
        VeterinaryInformation info = veterinaryInformationRepository.findByUser(user).orElse(null);
        return mapper.mapEntityToDto(info, DTOVeterinaryInfo.class);
    }

    


}
