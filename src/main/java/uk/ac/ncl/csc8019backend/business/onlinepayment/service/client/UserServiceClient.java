package uk.ac.ncl.csc8019backend.business.onlinepayment.service.client;


import uk.ac.ncl.csc8019backend.business.onlinepayment.DTO.response.UserDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * User service client - calls user module API
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class UserServiceClient {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${user.service.url:http://localhost:8080}")
    private String userServiceUrl;

    /**
     * Get user details by ID
     *
     * @param userId User ID
     * @return User DTO or null if not found
     */
    public UserDTO getUser(Long userId) {
        try {
            String url = userServiceUrl + "/api/users/" + userId;
            ResponseEntity<UserDTO> response = restTemplate.getForEntity(url, UserDTO.class);
            return response.getBody();
        } catch (Exception e) {
            log.error("Failed to get user: {}", userId, e);
            return null;
        }
    }

    /**
     * Increment user loyalty count (after successful payment)
     *
     * @param userId User ID
     * @return Whether increment succeeded
     */
    public boolean incrementLoyalty(Long userId) {
        try {
            String url = userServiceUrl + "/api/users/" + userId + "/loyalty";
            ResponseEntity<Map> response = restTemplate.postForEntity(url, null, Map.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            log.error("Failed to increment loyalty: {}", userId, e);
            return false;
        }
    }

    /**
     * Reset user loyalty count (after 10th free order)
     *
     * @param userId User ID
     * @return Whether reset succeeded
     */
    public boolean resetLoyalty(Long userId) {
        try {
            String url = userServiceUrl + "/api/users/" + userId + "/loyalty/reset";
            ResponseEntity<Map> response = restTemplate.postForEntity(url, null, Map.class);
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            log.error("Failed to reset loyalty: {}", userId, e);
            return false;
        }
    }
}
