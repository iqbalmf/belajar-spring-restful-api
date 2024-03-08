package net.iqbalfauzan.belajarspringbootrestfulapi.model.response;

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
@Builder
@NoArgsConstructor
public class WebResponse<T> {
    private T data;
    private String errors;
    private PagingResponse paging;
}
