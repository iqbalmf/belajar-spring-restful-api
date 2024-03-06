package net.iqbalfauzan.belajarspringbootrestfulapi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by IqbalMF on 2024.
 * Package net.iqbalfauzan.belajarspringbootrestfulapi.model
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private String username;
    private String name;
}
