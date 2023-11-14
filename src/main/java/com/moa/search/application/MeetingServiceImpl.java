package com.moa.search.application;

import com.moa.search.domain.Meeting;
import com.moa.search.infrastructure.MeetingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MeetingServiceImpl implements MeetingService {
    private MeetingRepository meetingRepository;

    @Autowired
    public void MeetingService(MeetingRepository meetingRepository) {
        this.meetingRepository = meetingRepository;
    }

    public List<String> getRelatedMeetingTitles(String keyword) {
        return meetingRepository.findRelatedTitles(keyword);
    }

    public List<Meeting> searchMeetingsByTitle(String title) {
        return meetingRepository.findByMeetingTitleContaining(title);
    }
}
