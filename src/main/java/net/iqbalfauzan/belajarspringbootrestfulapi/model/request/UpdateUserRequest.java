package net.iqbalfauzan.belajarspringbootrestfulapi.model.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by IqbalMF on 2024.
 * Package net.iqbalfauzan.belajarspringbootrestfulapi.model
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUserRequest {
    @Size(max = 100)
    private String name;
    @Size(max = 100)
    private String password;
}
