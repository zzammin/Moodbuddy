package moodbuddy.moodbuddy.domain.diary.service;

import moodbuddy.moodbuddy.domain.diary.dto.DiaryRequestDTO;
import moodbuddy.moodbuddy.domain.diary.dto.DiaryResponseDTO;

public interface DiaryService {

    DiaryResponseDTO monthlyCalendar(Long userId, DiaryRequestDTO.CalendarMonthDTO calendarMonthDTO);

    DiaryResponseDTO summary(Long userId, DiaryRequestDTO.CalendarSummaryDTO calendarSummaryDTO);
}
