package com.kodilla.auxilium_front.clients;

import com.google.gson.Gson;
import com.kodilla.auxilium_front.dto.ServicesDto;
import com.kodilla.auxilium_front.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class AuciliumClient {

    @Autowired
    private RestTemplate restTemplate;

    public Stream<String> getCities(){
        ServicesDto[] boardsResponse = restTemplate.getForObject("http://localhost:8080/v1/services", ServicesDto[].class);
        Stream stream = Stream.of(boardsResponse).map(s -> s.getCity()).distinct();
        return stream;
    }

    public List<ServicesDto> getAllServices(){
        ServicesDto[] boardsResponse = restTemplate.getForObject("http://localhost:8080/v1/services", ServicesDto[].class);
        List stream = Stream.of(boardsResponse).collect(Collectors.toList());
        return stream;
    }

    public List<ServicesDto> getServicesFilteredByCity(String city){
        ServicesDto[] boardsResponse = restTemplate.getForObject("http://localhost:8080/v1/services/city/" + city, ServicesDto[].class);
        List stream = Stream.of(boardsResponse).collect(Collectors.toList());
        return stream;
    }

    public List<ServicesDto> getServicesFilteredByType(String type){
        ServicesDto[] boardsResponse = restTemplate.getForObject("http://localhost:8080/v1/services/type/" + type, ServicesDto[].class);
        List stream = Stream.of(boardsResponse).collect(Collectors.toList());
        return stream;
    }

    public List<ServicesDto> getServicesFilteredByCityAndType(String city, String type){
        ServicesDto[] boardsResponse = restTemplate.getForObject("http://localhost:8080/v1/services/filter/" + city + "/" + type, ServicesDto[].class);
        List stream = Stream.of(boardsResponse).collect(Collectors.toList());
        return stream;
    }

    public List<ServicesDto> addService(){
        ServicesDto[] boardsResponse = restTemplate.getForObject("http://localhost:8080/v1/services", ServicesDto[].class);
        List stream = Stream.of(boardsResponse).collect(Collectors.toList());
        return stream;
    }

    public boolean login(String password, String email){
        return restTemplate.getForObject("http://localhost:8080/v1/user/login?password=" + password + "&email=" + email, Boolean.class);
    }

    public UserDto getUserByLoginData(String password, String email){
        return restTemplate.getForObject("http://localhost:8080/v1/user?password=" + password + "&email=" + email, UserDto.class);
    }

    public Long getUserAvailablePoints(String uuid){
        return restTemplate.getForObject("http://localhost:8080/v1/points/" + uuid, Long.class);
    }

    public String createUser(){
        UserDto userDto = new UserDto("Ania", 83729200, "andndkdik@janh.pl", "Jdshfbfeh763$22");
        Gson gson = new Gson();
        String jsonContent = gson.toJson(userDto);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<String>(jsonContent, headers);
        return restTemplate.postForObject("http://localhost:8080/v1/user", request,  String.class);
    }

}
