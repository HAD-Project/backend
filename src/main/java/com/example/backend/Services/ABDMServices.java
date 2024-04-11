package com.example.backend.Services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.backend.Models.AccessTokenModel;
import com.example.backend.Models.AuthInit;
import com.example.backend.Models.ConfirmAuthModel;
import com.example.backend.Models.PatientProfileModel;
import com.example.backend.Models.ResendAuthModel;
import com.example.backend.Models.SessionModel;
import com.example.backend.Models.TxnIDModel;
import com.example.backend.Models.XTokenModel;
import com.example.backend.Models.abdm.RegisteredFacilities.GetRegisteredFacilities;
import com.google.gson.Gson;

import lombok.Getter;
import lombok.Setter;
import reactor.core.publisher.Mono;

@Service
@Getter
@Setter
public class ABDMServices {
    
    @Value("${api.clientId}")
    private String apiClientId;

    @Value("${api.clientSecret}")
    private String apiClientSecret;

    private String rsaKey;

    private String accessToken;

    private String xToken;

    private String facId;

    private String facName;


    private static final String BASE_URI = "https://healthidsbx.abdm.gov.in";
    private static final String BASE_PATH = "api";

    public void getPublicKey() {
        WebClient webClient = WebClient.create();
        String keyMono = webClient.get()
                                      .uri("https://healthidsbx.abdm.gov.in/api/v1/auth/cert")
                                      .retrieve()
                                      .bodyToMono(String.class).block();
        setRsaKey(keyMono); 
    }

        

    public void initiateSession() {
        try {
            SessionModel session = new SessionModel(apiClientId, apiClientSecret);
            URL url = new URL("https://dev.abdm.gov.in/gateway/v0.5/sessions");

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setDoOutput(true);

            Gson gson = new Gson();
            String requestBody = gson.toJson(session);

            try (OutputStream os = httpURLConnection.getOutputStream()) {
                byte[] input = requestBody.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            
            int responseCode = httpURLConnection.getResponseCode();

            BufferedReader reader;
            if (responseCode == HttpURLConnection.HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            } else {
                reader = new BufferedReader(new InputStreamReader(httpURLConnection.getErrorStream()));
            }

            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            AccessTokenModel accessTokenModel = gson.fromJson(response.toString(), AccessTokenModel.class);

            setAccessToken(accessTokenModel.getAccessToken());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public TxnIDModel initializeAuth(AuthInit authInit) {
        try {
            initiateSession();
            URL url = new URL(BASE_URI + "/" + BASE_PATH + "/v1/auth/init");
            System.out.println(authInit.getHealthid());
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setRequestProperty("Authorization", "Bearer " + getAccessToken());
            System.out.println(getAccessToken());
            httpURLConnection.setDoOutput(true);
            // System.out.println("here");
            Gson gson = new Gson();
            String requestBody = gson.toJson(authInit);
            System.out.println(requestBody);
            // System.out.println(authInit.getAuthMethod());
            // System.out.println(authInit.getHealthid());
            // System.out.println(requestBody.toString());
            try (OutputStream os = httpURLConnection.getOutputStream()) {
                byte[] input = requestBody.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            
            int responseCode = httpURLConnection.getResponseCode();
            System.out.println(responseCode);
            BufferedReader reader;
            if (responseCode == HttpURLConnection.HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            } else {
                reader = new BufferedReader(new InputStreamReader(httpURLConnection.getErrorStream()));
            }

            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            // System.out.println("here");
            System.out.println(response.toString());
            TxnIDModel txnIDModel = gson.fromJson(response.toString(), TxnIDModel.class);

            return txnIDModel;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public TxnIDModel resendAuth(ResendAuthModel resendAuth) {
        try {
            initiateSession();
            URL url = new URL(BASE_URI + "/" + BASE_PATH + "/v1/auth/resendAuthOTP");

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setRequestProperty("Authorization", "Bearer " + getAccessToken());
            httpURLConnection.setDoOutput(true);
            System.out.println(resendAuth.getAuthMethod());
            System.out.println(resendAuth.getTxnId());
            Gson gson = new Gson();
            String requestBody = gson.toJson(resendAuth);

            try (OutputStream os = httpURLConnection.getOutputStream()) {
                byte[] input = requestBody.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            
            int responseCode = httpURLConnection.getResponseCode();
            System.out.println(responseCode);
            BufferedReader reader;
            if (responseCode == HttpURLConnection.HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            } else {
                reader = new BufferedReader(new InputStreamReader(httpURLConnection.getErrorStream()));
            }

            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            System.out.println(response.toString());

            TxnIDModel txnIDModel = gson.fromJson(response.toString(), TxnIDModel.class);

            return txnIDModel;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public XTokenModel confirmAuth(ConfirmAuthModel confirmAuthModel, String authType) {
        try {
            initiateSession();
            URL url;
            if (authType == "AADHAAR") {
                url = new URL(BASE_URI + "/" + BASE_PATH + "/v1/auth/confirmWithAadhaarOTP");
            } else {
                url = new URL(BASE_URI + "/" + BASE_PATH + "/v1/auth/confirmWithMobileOTP");
            }
            
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setRequestProperty("Authorization","Bearer " + getAccessToken());
            httpURLConnection.setDoOutput(true);

            Gson gson = new Gson();
            String requestBody = gson.toJson(confirmAuthModel);

            try (OutputStream os = httpURLConnection.getOutputStream()) {
                byte[] input = requestBody.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            
            int responseCode = httpURLConnection.getResponseCode();

            BufferedReader reader;
            if (responseCode == HttpURLConnection.HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            } else {
                reader = new BufferedReader(new InputStreamReader(httpURLConnection.getErrorStream()));
            }

            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            System.out.println(responseCode);
            System.out.println(response.toString());
            XTokenModel accessTokenModel = gson.fromJson(response.toString(), XTokenModel.class);
            setXToken(accessTokenModel.getToken());

            return accessTokenModel;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public PatientProfileModel fetchProfile() {
        try {
            initiateSession();
            URL url = new URL(BASE_URI + "/" + BASE_PATH + "/v2/account/profile");
            
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setRequestProperty("Authorization","Bearer " + getAccessToken());
            httpURLConnection.setRequestProperty("X-Token", "Bearer " + getXToken());
            // httpURLConnection.setDoOutput(false);

            Gson gson = new Gson();
            // String requestBody = gson.toJson(confirmAuthModel);

            // try (OutputStream os = httpURLConnection.getOutputStream()) {
            //     byte[] input = requestBody.getBytes("utf-8");
            //     os.write(input, 0, input.length);
            // }
            
            int responseCode = httpURLConnection.getResponseCode();

            BufferedReader reader;
            if (responseCode == HttpURLConnection.HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            } else {
                reader = new BufferedReader(new InputStreamReader(httpURLConnection.getErrorStream()));
            }

            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            System.out.println(responseCode);
            System.out.println(response.toString());
            PatientProfileModel patientProfileModel = gson.fromJson(response.toString(), PatientProfileModel.class);

            return patientProfileModel;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void fetchRegisteredFacilities() {
        try {
            initiateSession();
            URL url = new URL("https://dev.abdm.gov.in/gateway/v1/bridges/getServices");

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setRequestProperty("Authorization","Bearer " + getAccessToken());

            Gson gson = new Gson();

            int responseCode = httpURLConnection.getResponseCode();

            BufferedReader reader;
            if (responseCode == HttpURLConnection.HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            } else {
                reader = new BufferedReader(new InputStreamReader(httpURLConnection.getErrorStream()));
            }

            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            System.out.println(responseCode);
            System.out.println(response.toString());
            GetRegisteredFacilities getRegisteredFacilities = gson.fromJson(response.toString(), GetRegisteredFacilities.class);
            List<com.example.backend.Models.abdm.RegisteredFacilities.Service> facList = getRegisteredFacilities.getServices();
            setFacId(facList.get(0).getId());
            setFacName(facList.get(0).getName());     

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    

}
