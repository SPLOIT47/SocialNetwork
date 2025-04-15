package com.sploit.socialnetwork.auth.payload.response;

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
    private transient String cleanJwtCookie;
    private transient String cleanRefreshCookie;
}
