package com.kodilla.auxilium_front.clients;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kodilla.auxilium_front.config.AuxiliumConfig;
import com.kodilla.auxilium_front.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class AuxiliumClient {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AuxiliumConfig auxiliumConfig;

    private HttpHeaders headers = new HttpHeaders();


    public Stream<String> getCities(){
        ServicesDto[] boardsResponse = restTemplate.getForObject(auxiliumConfig.getAuxiliumEndpoint() + "/services", ServicesDto[].class);
        return Stream.of(boardsResponse).map(s -> s.getCity()).distinct();
    }

    public List<ServicesDto> getAllServices(){
        ServicesDto[] boardsResponse = restTemplate.getForObject(auxiliumConfig.getAuxiliumEndpoint() + "/services", ServicesDto[].class);
        return Stream.of(boardsResponse).collect(Collectors.toList());
    }

    public List<ServicesDto> getAllServicesOwnedByUser(String uuid){
        TransactionDto[] boardsResponse = restTemplate.getForObject(auxiliumConfig.getAuxiliumEndpoint() + "/transaction/owned/" + uuid
                , TransactionDto[].class);
        return Stream.of(boardsResponse).map(t -> t.getServicesDto()).collect(Collectors.toList());
    }

    public List<ServicesDto> getAllServicesAssignedToUser(String uuid){
        TransactionDto[] boardsResponse = restTemplate.getForObject(auxiliumConfig.getAuxiliumEndpoint() + "/transaction/serviced/" + uuid
                , TransactionDto[].class);
        return Stream.of(boardsResponse).map(t -> t.getServicesDto()).collect(Collectors.toList());
    }

    public List<ServicesDto> getServicesFilteredByCity(String city){
        ServicesDto[] boardsResponse = restTemplate.getForObject(auxiliumConfig.getAuxiliumEndpoint() + "/services/city/" + city, ServicesDto[].class);
        return Stream.of(boardsResponse).collect(Collectors.toList());
    }

    public List<ServicesDto> getServicesFilteredByType(String type){
        ServicesDto[] boardsResponse = restTemplate.getForObject(auxiliumConfig.getAuxiliumEndpoint() + "/services/type/" + type, ServicesDto[].class);
        return Stream.of(boardsResponse).collect(Collectors.toList());
    }

    public List<ServicesDto> getServicesFilteredByCityAndType(String city, String type){
        ServicesDto[] boardsResponse = restTemplate.getForObject(auxiliumConfig.getAuxiliumEndpoint() + "/services/filter/" + city + "/" + type, ServicesDto[].class);
        return Stream.of(boardsResponse).collect(Collectors.toList());
    }

    public Boolean login(String password, String email){

        HttpHeaders headers = new HttpHeaders();
        headers.set("password", password);
        headers.set("email", email);
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

        URI url = UriComponentsBuilder.fromHttpUrl(auxiliumConfig.getAuxiliumEndpoint() + "/user/login").build().encode().toUri();

        ResponseEntity<Boolean> respEntity = restTemplate.exchange(url, HttpMethod.GET, entity,  Boolean.class);
        return respEntity.getBody();
    }

    public UserDto getUserByLoginData(String password, String email){

        HttpHeaders headers = new HttpHeaders();
        headers.set("password", password);
        headers.set("email", email);
        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

        URI url = UriComponentsBuilder.fromHttpUrl(auxiliumConfig.getAuxiliumEndpoint() + "/user").build().encode().toUri();

        ResponseEntity<UserDto> respEntity = restTemplate.exchange(url, HttpMethod.GET, entity,  UserDto.class);
        return Optional.ofNullable(respEntity.getBody()).orElse(new UserDto());
    }

    public UserDto getUserByUUID(String uuid){
        return restTemplate.getForObject(auxiliumConfig.getAuxiliumEndpoint() + "/user/" + uuid, UserDto.class);
    }

    public void changeUserData(UserDto userDto){
        Gson gson = new Gson();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String jsonContent = gson.toJson(userDto);
        HttpEntity<String> request = new HttpEntity<String>(jsonContent, headers);
        restTemplate.put(auxiliumConfig.getAuxiliumEndpoint() + "/user", request);
    }

    public Long getUserAvailablePoints(String uuid){
        return restTemplate.getForObject(auxiliumConfig.getAuxiliumEndpoint() + "/points/" + uuid, Long.class);
    }

    public UserDto createUser(UserDto userDto){
        Gson gson = new Gson();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String jsonContent = gson.toJson(userDto);
        HttpEntity<String> request = new HttpEntity<String>(jsonContent, headers);
        return restTemplate.postForObject(auxiliumConfig.getAuxiliumEndpoint() + "/user", request,  UserDto.class);
    }

    public ServicesDto createService(ServicesDto servicesDto){
        Gson gson = new Gson();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String jsonContent = gson.toJson(servicesDto);
        HttpEntity<String> request = new HttpEntity<String>(jsonContent, headers);
        return restTemplate.postForObject(auxiliumConfig.getAuxiliumEndpoint() + "/services", request,  ServicesDto.class);
    }

    public TransactionDto createTransaction(UserDto userDtoDto, ServicesDto servicesDto){
        Gson gson = new Gson();
        headers.setContentType(MediaType.APPLICATION_JSON);
        TransactionDto transactionDto = new TransactionDto(userDtoDto, servicesDto);
        String jsonContent = gson.toJson(transactionDto);
        HttpEntity<String> request = new HttpEntity<String>(jsonContent, headers);
        return restTemplate.postForObject(auxiliumConfig.getAuxiliumEndpoint() + "/transaction", request,  TransactionDto.class);
    }

    public void assignTransaction(UserDto userDto, Long serviceId){
        Gson gson = new Gson();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String jsonContent = gson.toJson(userDto);
        HttpEntity<String> request = new HttpEntity<String>(jsonContent, headers);
        restTemplate.put(auxiliumConfig.getAuxiliumEndpoint() + "/transaction/assign/" + serviceId, request);
    }

    public void acceptTransaction(Long serviceId){
        restTemplate.put(auxiliumConfig.getAuxiliumEndpoint() + "/transaction/accept/" + serviceId, serviceId);
    }

    public void deleteService(Long serviceId){
        restTemplate.delete(auxiliumConfig.getAuxiliumEndpoint() + "/services/" + serviceId, serviceId);
    }

    public List<EventDto> getAllEvents(){
        EventDto[] boardsResponse = restTemplate.getForObject(auxiliumConfig.getAuxiliumEndpoint() + "/events", EventDto[].class);
        return Stream.of(boardsResponse).collect(Collectors.toList());
    }

    public List<ProductDto> getAllProducts(){
        ProductDto[] boardsResponse = restTemplate.getForObject(auxiliumConfig.getAuxiliumEndpoint() + "/products", ProductDto[].class);
        return Stream.of(boardsResponse).collect(Collectors.toList());
    }

    public void collectProduct(ProductDto productDto, String uuid){
        Gson gson = new Gson();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String jsonContent = gson.toJson(productDto);
        HttpEntity<String> request = new HttpEntity<String>(jsonContent, headers);
        restTemplate.postForObject(auxiliumConfig.getAuxiliumEndpoint() + "/products/" + uuid, request,  UserDto.class);
    }

    public void collectEvent(EventDto eventDto, String uuid){
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String jsonContent = gson.toJson(eventDto);
        HttpEntity<String> request = new HttpEntity<String>(jsonContent, headers);
        restTemplate.postForObject(auxiliumConfig.getAuxiliumEndpoint() + "/events/" + uuid, request,  UserDto.class);
    }


}
