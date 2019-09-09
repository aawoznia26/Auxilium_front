package com.kodilla.auxilium_front.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.kodilla.auxilium_front.domain.ServicesTransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServicesDto {

    private Long id;

    private String name;

    private String description;

    private int points;

    private String city;

    private ServicesTransactionStatus servicesTransactionStatus;

    public ServicesDto(String name, String description, String city) {
        this.name = name;
        this.description = description;
        this.city = city;
    }

    public ServicesDto(String name, String description, String city, ServicesTransactionStatus servicesTransactionStatus) {
        this.name = name;
        this.description = description;
        this.city = city;
        this.servicesTransactionStatus = servicesTransactionStatus;
    }
}
