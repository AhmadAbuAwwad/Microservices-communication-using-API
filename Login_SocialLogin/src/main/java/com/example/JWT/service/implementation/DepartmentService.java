package com.example.JWT.service.implementation;

import com.example.JWT.dto.DepartmentDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


@Service
public class DepartmentService {

    @Autowired
    WebClient webClient;

    public <T> Mono<T> getList(HttpServletRequest request, String uri,
                                   ParameterizedTypeReference<T> typeReference) {
        try {
            return webClient
                    .get()
                    .uri(uri)
                    .header("Authorization", request.getHeader("Authorization"))
                    .retrieve()
                    .bodyToMono(typeReference);
        } catch (Exception e) {
            return null;
        }
    }

    public <T> Mono<T> getResponse(HttpServletRequest request, String uri,
                                               ParameterizedTypeReference<T> typeReference) {
        try {
            return (Mono<T>) webClient
                    .get()
                    .uri(uri)
                    .header("Authorization", request.getHeader("Authorization"))
                    .retrieve()
                    .bodyToMono(typeReference);
        } catch (Exception e) {
            return null;
        }
    }

    public DepartmentDTO getDepartmentDto(HttpServletRequest request, long id) throws ExecutionException, InterruptedException {
        //  Async Api Call Using Web Client
            DepartmentDTO departmentDTO = new DepartmentDTO();
        try {
            departmentDTO = webClient.get()
                    .uri("http://localhost:8081/api/departments/getById/" + id)
                    .header("Authorization", request.getHeader("Authorization"))
                    .retrieve()
                    .bodyToMono(DepartmentDTO.class)
                    .block();
        } catch (
                HttpClientErrorException.BadRequest e) {
            System.out.println("Bad Request");
        }

        CompletableFuture<DepartmentDTO> future = webClient.get()
                .uri("http://localhost:8081/api/departments/getById/" + id)
                .header("Authorization", request.getHeader("Authorization"))
                .retrieve()
                .bodyToMono(DepartmentDTO.class)
                // specify timeout
                .timeout(Duration.ofSeconds(5L))
                // subscribe on a different thread from the given scheduler to avoid blocking as toFuture is a subscriber
                .subscribeOn(Schedulers.single())
                // subscribes to the mono and converts it to a completable future
                .toFuture();

        System.out.println("Hello1");
        System.out.println("Hello2");
        return future.join();
    }

    public DepartmentDTO createDepartmentDto(HttpServletRequest request, DepartmentDTO departmentDTO) throws ExecutionException, InterruptedException {
        try {
            departmentDTO = webClient.post()
                    .uri("http://localhost:8081/api/departments/create")
                    .header("Authorization", request.getHeader("Authorization"))
                    .bodyValue(departmentDTO)
                    .retrieve()
                    .bodyToMono(DepartmentDTO.class)
                    .block();
        } catch (
                HttpClientErrorException.BadRequest e) {
            System.out.println("Bad Request");
        }
        return departmentDTO;
    }

    public DepartmentDTO updateDepartmentDto(HttpServletRequest request, long id, DepartmentDTO departmentDTO) throws ExecutionException, InterruptedException {
        try {
            departmentDTO = webClient.put()
                    .uri("http://localhost:8081/api/departments/update/" + id)
                    .header("Authorization", request.getHeader("Authorization"))
                    .bodyValue(departmentDTO)
                    .retrieve()
                    .bodyToMono(DepartmentDTO.class)
                    .block();
        } catch (
                HttpClientErrorException.BadRequest e) {
            System.out.println("Bad Request");
        }

        return departmentDTO;
    }

    public void deleteDepartmentById(HttpServletRequest request, long id) throws ExecutionException, InterruptedException {
        try {
            webClient.delete()
                    .uri("http://localhost:8081/api/departments/delete/" + id)
                    .header("Authorization", request.getHeader("Authorization"))
                    .retrieve()
                    .bodyToMono(DepartmentDTO.class)
                    .block();
        } catch (
                HttpClientErrorException.BadRequest e) {
            System.out.println("Bad Request");
        }
    }
}
