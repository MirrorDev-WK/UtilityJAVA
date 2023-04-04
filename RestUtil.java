package th.co.ncr.connector.utils;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;
import th.co.ncr.connector.config.RestTemplateConfig;

@Component
@Slf4j
public class RestUtil {
	
	@Autowired private RestTemplateConfig restConfig;

	public <T>ResponseEntity<T> postRequestHttp(String endpointUrl, Object requestBody, Class<T> responseType) throws Exception {
		log.info("start : Post Request " + endpointUrl);
	    	
    	HttpHeaders headers = new HttpHeaders();
    	headers.setContentType(MediaType.APPLICATION_JSON);
    	
    	if (!restConfig.getBasicCred().equalsIgnoreCase("")) {
    		headers.set("Authorization", "Basic " + restConfig.getBasicCred());
    	}
    	
    	HttpEntity<Object> request = new HttpEntity<>(requestBody, headers);
    	
    	if(request.getHeaders() != null) {
    		log.info("Headers: {}", request.getHeaders());
    	}
    	
    	if(request.getBody() != null) {
    		log.info("Body: {}", request.getBody());
    	}
    	
    	RestTemplate restTemplate = restConfig.getRestTemplate();
    	ResponseEntity<T> response = restTemplate.postForEntity(endpointUrl, request, responseType);
        log.info("Response: {}", response);
    	log.info("Status: {}", response.getStatusCode());
    	
    	return response;
    }
	    
    public <T>ResponseEntity<T> postRequestHttp(String endpointUrl, Object requestBody, Map<String, String> requestHeader, Class<T> responseType) throws Exception {
        log.info("start: Post Request " + endpointUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        if (!restConfig.getBasicCred().equalsIgnoreCase("")) {
    		headers.set("Authorization", "Basic " + restConfig.getBasicCred());
    	}
        
        if (requestHeader != null && requestHeader.size() > 0) {
	        for (Map.Entry<String, String> entry : requestHeader.entrySet()) {
				headers.set(entry.getKey(), entry.getValue());
			}
        }

        HttpEntity<Object> request;
        if (requestBody != null) {
        	request = new HttpEntity<>(requestBody, headers);
        } else {
        	request = new HttpEntity<>(headers);
        }
        
        if (request.getHeaders() != null) {
        	log.debug("Headers: {}", request.getHeaders());
        }
        
        if (request.getBody() != null) {
        	log.debug("Body: {}", request.getBody());
        }

        RestTemplate restTemplate = restConfig.getRestTemplate();
        ResponseEntity<T> response = restTemplate.postForEntity(endpointUrl, request, responseType);
        log.info("Response: {}", response);
    	log.info("Status: {}", response.getStatusCode());

        return response;
    }
}
