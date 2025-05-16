package com.sploit.socialnetwork.auth.payload.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LogoutResponse {

    private String message;

    @JsonIgnore
    private transient String cleanJwtCookie;

    @JsonIgnore
    private transient String cleanRefreshCookie;
}
