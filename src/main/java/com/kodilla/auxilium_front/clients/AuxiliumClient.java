package com.kodilla.auxilium_front.clients;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kodilla.auxilium_front.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class AuxiliumClient {

    @Autowired
    private RestTemplate restTemplate;

    private HttpHeaders headers = new HttpHeaders();


    public Stream<String> getCities(){
        ServicesDto[] boardsResponse = restTemplate.getForObject("http://localhost:8080/v1/services", ServicesDto[].class);
        return Stream.of(boardsResponse).map(s -> s.getCity()).distinct();
    }

    public List<ServicesDto> getAllServices(){
        ServicesDto[] boardsResponse = restTemplate.getForObject("http://localhost:8080/v1/services", ServicesDto[].class);
        return Stream.of(boardsResponse).collect(Collectors.toList());
    }

    public List<ServicesDto> getAllServicesOwnedByUser(String uuid){
        TransactionDto[] boardsResponse = restTemplate.getForObject("http://localhost:8080/v1/transaction/owned/" + uuid
                , TransactionDto[].class);
        return Stream.of(boardsResponse).map(t -> t.getServicesDto()).collect(Collectors.toList());
    }

    public List<ServicesDto> getAllServicesAssignedToUser(String uuid){
        TransactionDto[] boardsResponse = restTemplate.getForObject("http://localhost:8080/v1/transaction/serviced/" + uuid
                , TransactionDto[].class);
        return Stream.of(boardsResponse).map(t -> t.getServicesDto()).collect(Collectors.toList());
    }

    public List<ServicesDto> getServicesFilteredByCity(String city){
        ServicesDto[] boardsResponse = restTemplate.getForObject("http://localhost:8080/v1/services/city/" + city, ServicesDto[].class);
        return Stream.of(boardsResponse).collect(Collectors.toList());
    }

    public List<ServicesDto> getServicesFilteredByType(String type){
        ServicesDto[] boardsResponse = restTemplate.getForObject("http://localhost:8080/v1/services/type/" + type, ServicesDto[].class);
        return Stream.of(boardsResponse).collect(Collectors.toList());
    }

    public List<ServicesDto> getServicesFilteredByCityAndType(String city, String type){
        ServicesDto[] boardsResponse = restTemplate.getForObject("http://localhost:8080/v1/services/filter/" + city + "/" + type, ServicesDto[].class);
        return Stream.of(boardsResponse).collect(Collectors.toList());
    }

    public boolean login(String password, String email){
        return restTemplate.getForObject("http://localhost:8080/v1/user/login?password=" + password + "&email=" + email, Boolean.class);
    }

    public UserDto getUserByLoginData(String password, String email){
        return restTemplate.getForObject("http://localhost:8080/v1/user?password=" + password + "&email=" + email, UserDto.class);
    }

    public UserDto getUserByUUID(String uuid){
        return restTemplate.getForObject("http://localhost:8080/v1/user/" + uuid, UserDto.class);
    }

    public void changeUserData(UserDto userDto){
        Gson gson = new Gson();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String jsonContent = gson.toJson(userDto);
        HttpEntity<String> request = new HttpEntity<String>(jsonContent, headers);
        restTemplate.put("http://localhost:8080/v1/user", request);
    }

    public Long getUserAvailablePoints(String uuid){
        return restTemplate.getForObject("http://localhost:8080/v1/points/" + uuid, Long.class);
    }

    public UserDto createUser(UserDto userDto){
        Gson gson = new Gson();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String jsonContent = gson.toJson(userDto);
        HttpEntity<String> request = new HttpEntity<String>(jsonContent, headers);
        return restTemplate.postForObject("http://localhost:8080/v1/user", request,  UserDto.class);
    }

    public ServicesDto createService(ServicesDto servicesDto){
        Gson gson = new Gson();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String jsonContent = gson.toJson(servicesDto);
        HttpEntity<String> request = new HttpEntity<String>(jsonContent, headers);
        return restTemplate.postForObject("http://localhost:8080/v1/services", request,  ServicesDto.class);
    }

    public TransactionDto createTransaction(UserDto userDtoDto, ServicesDto servicesDto){
        Gson gson = new Gson();
        headers.setContentType(MediaType.APPLICATION_JSON);
        TransactionDto transactionDto = new TransactionDto(userDtoDto, servicesDto);
        String jsonContent = gson.toJson(transactionDto);
        HttpEntity<String> request = new HttpEntity<String>(jsonContent, headers);
        return restTemplate.postForObject("http://localhost:8080/v1/transaction", request,  TransactionDto.class);
    }

    public void assignTransaction(UserDto userDto, Long serviceId){
        Gson gson = new Gson();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String jsonContent = gson.toJson(userDto);
        HttpEntity<String> request = new HttpEntity<String>(jsonContent, headers);
        restTemplate.put("http://localhost:8080/v1/transaction/assign/" + serviceId, request);
    }

    public void acceptTransaction(Long serviceId){
        restTemplate.put("http://localhost:8080/v1/transaction/accept/" + serviceId, serviceId);
    }

    public void deleteService(Long serviceId){
        restTemplate.delete("http://localhost:8080/v1/services/" + serviceId, serviceId);
    }

    public List<EventDto> getAllEvents(){
        EventDto[] boardsResponse = restTemplate.getForObject("http://localhost:8080/v1/events", EventDto[].class);
        return Stream.of(boardsResponse).collect(Collectors.toList());
    }

    public List<ProductDto> getAllProducts(){
        ProductDto[] boardsResponse = restTemplate.getForObject("http://localhost:8080/v1/products", ProductDto[].class);
        return Stream.of(boardsResponse).collect(Collectors.toList());
    }

    public void collectProduct(ProductDto productDto, String uuid){
        Gson gson = new Gson();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String jsonContent = gson.toJson(productDto);
        HttpEntity<String> request = new HttpEntity<String>(jsonContent, headers);
        restTemplate.postForObject("http://localhost:8080/v1/products/" + uuid, request,  UserDto.class);
    }

    public void collectEvent(EventDto eventDto, String uuid){
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String jsonContent = gson.toJson(eventDto);
        HttpEntity<String> request = new HttpEntity<String>(jsonContent, headers);
        restTemplate.postForObject("http://localhost:8080/v1/events/" + uuid, request,  UserDto.class);
    }


}
