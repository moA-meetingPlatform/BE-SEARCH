package com.moa.search.application;

import com.moa.search.domain.Meeting;

import java.util.List;

public interface MeetingService {
    List<String> getRelatedMeetingTitles(String keyword);

    List<Meeting> searchMeetingsByTitle(String title);
}
