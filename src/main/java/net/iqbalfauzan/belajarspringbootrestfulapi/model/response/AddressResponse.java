package net.iqbalfauzan.belajarspringbootrestfulapi.model.response;


import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressResponse {
    private String id;
    private String street;
    private String city;
    private String province;
    private String country;
    private String postalCode;
}
