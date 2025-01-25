package com.socialnetwork.shared.dto;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEvent extends Event {
    private String username;
    private String password;
}
