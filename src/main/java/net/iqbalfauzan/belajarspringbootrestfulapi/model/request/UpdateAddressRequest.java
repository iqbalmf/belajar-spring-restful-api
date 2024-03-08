package net.iqbalfauzan.belajarspringbootrestfulapi.model.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by IqbalMF on 2024.
 * Package net.iqbalfauzan.belajarspringbootrestfulapi.model.request
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateAddressRequest {

    @NotBlank
    @JsonIgnore
    private String contactId;
    @JsonIgnore
    @NotBlank
    private String addressId;

    @Size(max = 200)
    private String street;
    @Size(max = 100)
    private String city;
    @Size(max = 100)
    private String province;
    @Size(max = 100)
    @NotBlank
    private String country;
    @Size(max = 10)
    private String postalCode;
}
