package uk.ac.ncl.csc8019backend.system.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.ac.ncl.csc8019backend.system.entity.Role;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfoResponse {
    private String username;
    private String email;
    private Role role;
}
