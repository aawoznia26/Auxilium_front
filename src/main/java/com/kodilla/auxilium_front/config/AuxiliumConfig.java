package com.kodilla.auxilium_front.config;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class AuxiliumConfig {

    @Value("${auxilium.endpoint}")
    private String auxiliumEndpoint;

}
