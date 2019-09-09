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
public class TransactionDto {

    private Long id;

    private UserDto ownerDto;

    private UserDto serviceProviderDto;

    private ServicesDto servicesDto;

    private ServicesTransactionStatus servicesTransactionStatus;

    public TransactionDto(UserDto ownerDto, ServicesDto servicesDto) {
        this.ownerDto = ownerDto;
        this.servicesDto = servicesDto;
    }
}
