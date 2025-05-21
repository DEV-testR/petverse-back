package com.petverse.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PetRequest {
    private String name;
    private String species;
    private String breed;
    private Integer age;
    private String profilePictureUrl;
}
