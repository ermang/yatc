package com.eg.yatc.auth.event;

public record UserCreatedEvent(long userId, String username) {
}
