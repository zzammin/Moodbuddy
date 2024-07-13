package moodbuddy.moodbuddy.domain.quddyTI.service;

import moodbuddy.moodbuddy.domain.quddyTI.entity.QuddyTI;

public interface QuddyTIService {
    void aggregateAndSaveDiaryData();
    void saveQuddyTI(QuddyTI quddyTI);
}