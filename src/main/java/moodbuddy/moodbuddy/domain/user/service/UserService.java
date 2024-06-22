package moodbuddy.moodbuddy.domain.user.service;

import moodbuddy.moodbuddy.domain.user.dto.UserResponseDTO;

public interface UserService {
    UserResponseDTO mainPage(Long userId);
}
