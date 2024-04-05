package com.example.backend.Services;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import javax.print.attribute.standard.Media;

import org.apache.coyote.BadRequestException;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.backend.Models.abdm.ABDMAuthRequest;
import com.example.backend.Models.abdm.ABDMAuthResponse;
import com.example.backend.Models.abdm.auth.patient.PatientAuthOnInitReq;
import com.example.backend.Models.abdm.auth.patient.PatienthAuthOnInitRes;
import com.example.backend.Repositories.PatientRepository;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClientResponse;

@Service
@Setter
@Getter
@PropertySource("classpath:application-dev.properties")
public class ABDMServices_Shrutik {
    private String RSAKey;

    private String hipAuthToken;
    private String hipRefreshToken;

    @Value("${clientId}")
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private String clientId;

    @Value("${clientSecret}")
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private String clientSecret;

    public void getRsaPublicKey() throws URISyntaxException {
        WebClient webClient = WebClient.create();
        String key = webClient
        .get()
        .uri(new URI("https://healthidsbx.abdm.gov.in/api/v1/auth/cert"))
        .retrieve()
        .bodyToMono(String.class)
        .block();
        setRSAKey(key);
    }

    public Mono<ABDMAuthResponse> authWithGateway() throws URISyntaxException {

        ABDMAuthRequest req = new ABDMAuthRequest();
        req.setClientId(clientId);
        req.setClientSecret(clientSecret);
        WebClient webClient = WebClient.create();
        
        Mono<ABDMAuthResponse> mono = 
            webClient
            .post()
            .uri(new URI("https://dev.abdm.gov.in/gateway/v0.5/sessions"))
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(req)
            .retrieve()
            .bodyToMono(ABDMAuthResponse.class);
        return mono;
    }

    ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            StringBuilder sb = new StringBuilder("Request: \n");
            //append clientRequest method and url
            clientRequest
              .headers()
              .forEach((name, values) -> values.forEach(value -> sb.append(name + " " + value + "\n")));
            sb.append(clientRequest.body().toString());
            System.out.println(sb);
            return Mono.just(clientRequest);
        });
    }

    public Mono<PatienthAuthOnInitRes> initPatientAuth(String authMethod, String healthId) throws Exception {
        PatientAuthOnInitReq req = new PatientAuthOnInitReq();
        req.setAuthMethod(authMethod);
        req.setHealthid(healthId);
        System.out.println("body: " + req.getAuthMethod() + " " + req.getHealthid());
        WebClient webClient = WebClient.builder()
            .filters(exchangeFiltersFunction -> {
                exchangeFiltersFunction.add(logRequest());
            })
            .build();

        try {
            Mono<PatienthAuthOnInitRes> mono = this.authWithGateway()
                .flatMap(authTokenObj -> {
                    try {
                        return webClient
                                .post()
                                .uri(new URI("https://healthidsbx.abdm.gov.in/api/v1/auth/init"))
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("User-Agent", "HAD_ABDM")
                                .header("Authorization", "Bearer " + authTokenObj.getAccessToken())
                                .bodyValue(req)
                                .retrieve()
                                .bodyToMono(PatienthAuthOnInitRes.class);
                    } catch (URISyntaxException e) {
                        System.out.println(e.getMessage());
                        return Mono.error(e);
                    }
                    catch (Exception e) {
                        System.out.println(e.getLocalizedMessage());
                        return Mono.error(e);
                    }
                });
            return mono;
        }
        catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            return Mono.error(e);
        }
    }

}
