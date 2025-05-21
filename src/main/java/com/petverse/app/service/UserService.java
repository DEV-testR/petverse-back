package com.petverse.app.service;

import com.petverse.app.dto.PetRequest;
import com.petverse.app.dto.UpdateProfileRequest;
import com.petverse.app.entity.Pet;
import com.petverse.app.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    void logout(String token);

    User updateProfile(Long userId, UpdateProfileRequest request);

    Pet addPet(Long userId, PetRequest request);

    void deletePet(Long userId, Long petId);

    Pet updatePet(Long userId, Long petId, PetRequest request);

    User getCurrentUser(Long id);
}


