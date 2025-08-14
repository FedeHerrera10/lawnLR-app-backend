package com.fedeherrera.vetapp.vetapp.services.veterinaryInformation;

import com.fedeherrera.vetapp.vetapp.dtos.request.DTOVeterinaryInformation;
import com.fedeherrera.vetapp.vetapp.dtos.response.DTOVeterinaryInfo;

public interface VeterinaryInformationService {
    DTOVeterinaryInfo saveVeterinaryInformation(Long id, DTOVeterinaryInformation veterinaryInformation);
    DTOVeterinaryInfo getVeterinaryInformation(Long id);
}
