package moodbuddy.moodbuddy.domain.calendar.service;

import moodbuddy.moodbuddy.domain.calendar.dto.CalendarResponseDTO;

public interface CalendarService {

    CalendarResponseDTO mainPage(String kakaoId);

    CalendarResponseDTO monthlyCalendar(String month);

    CalendarResponseDTO diarySummary(String day);
}
