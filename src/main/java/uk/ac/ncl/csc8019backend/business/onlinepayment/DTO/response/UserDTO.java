package uk.ac.ncl.csc8019backend.business.onlinepayment.DTO.response;

import lombok.Data;

/**
 * User information (obtained from user module)
 */
@Data
public class UserDTO {

    private Long id;
    private String phone;          // Used as customerID for HorsePay
    private String name;
    private Integer loyaltyCount;  // Successful pickup count (for 10th order free judgment)
}