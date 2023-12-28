package com.expandapis.user.model.request;

import lombok.Builder;

@Builder
public record AuthRequest (String username, String password){
}
