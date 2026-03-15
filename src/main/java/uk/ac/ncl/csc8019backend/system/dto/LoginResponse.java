package uk.ac.ncl.csc8019backend.system.dto;

/**
 * Response body for successful login (API contract instead of Map).
 */
public class LoginResponse {

    private String token;
    private String tokenHead;

    public LoginResponse() {
    }

    public LoginResponse(String token, String tokenHead) {
        this.token = token;
        this.tokenHead = tokenHead;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenHead() {
        return tokenHead;
    }

    public void setTokenHead(String tokenHead) {
        this.tokenHead = tokenHead;
    }
}
