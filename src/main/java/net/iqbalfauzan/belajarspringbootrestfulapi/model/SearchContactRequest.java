package net.iqbalfauzan.belajarspringbootrestfulapi.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class SearchContactRequest {
    private String name;
    private String email;
    private String phone;
    @NotNull
    private int page;
    @NotNull
    private int size;
}
