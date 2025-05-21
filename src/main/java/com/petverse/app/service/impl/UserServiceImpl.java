package com.petverse.app.service.impl;

import com.petverse.app.dto.PetRequest;
import com.petverse.app.dto.UpdateProfileRequest;
import com.petverse.app.entity.Pet;
import com.petverse.app.entity.User;
import com.petverse.app.repository.PetRepository;
import com.petverse.app.repository.UserRepository;
import com.petverse.app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PetRepository petRepository;

    @Override
    public void logout(String token) {
        // Placeholder: ใช้ blacklist หรือ token revocation ตามระบบที่ใช้
        System.out.println("Token revoked: " + token);
    }

    @Override
    public User updateProfile(Long userId, UpdateProfileRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setFullName(request.getFullName());
        user.setPhone(request.getPhone());
        user.setAddress(request.getAddress());
        user.setProfilePictureUrl(request.getProfilePictureUrl());

        return userRepository.save(user);
    }

    @Override
    public Pet addPet(Long userId, PetRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Pet pet = new Pet();
        pet.setName(request.getName());
        pet.setSpecies(request.getSpecies());
        pet.setBreed(request.getBreed());
        pet.setAge(request.getAge());
        pet.setProfilePictureUrl(request.getProfilePictureUrl());
        pet.setOwner(user);

        return petRepository.save(pet);
    }

    @Override
    public void deletePet(Long userId, Long petId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found"));

        if (!pet.getOwner().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized");
        }

        petRepository.delete(pet);
    }

    @Override
    public Pet updatePet(Long userId, Long petId, PetRequest request) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found"));

        if (!pet.getOwner().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized");
        }

        pet.setName(request.getName());
        pet.setSpecies(request.getSpecies());
        pet.setBreed(request.getBreed());
        pet.setAge(request.getAge());
        pet.setProfilePictureUrl(request.getProfilePictureUrl());

        return petRepository.save(pet);
    }

    @Override
    public User getCurrentUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

}

