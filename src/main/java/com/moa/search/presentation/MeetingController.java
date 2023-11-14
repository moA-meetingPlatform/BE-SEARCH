package com.moa.search.presentation;

import com.moa.search.application.MeetingService;
import com.moa.search.domain.Meeting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/search")
public class MeetingController {

    private final MeetingService meetingService;

    @Autowired
    public MeetingController(MeetingService meetingService) {
        this.meetingService = meetingService;
    }

    @GetMapping("/related-search")
    public ResponseEntity<List<String>> getRelatedTitles(@RequestParam String keyword) {
        List<String> relatedTitles = meetingService.getRelatedMeetingTitles(keyword);
        return ResponseEntity.ok(relatedTitles);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Meeting>> searchMeetings(@RequestParam String title) {
        List<Meeting> meetings = meetingService.searchMeetingsByTitle(title);
        return ResponseEntity.ok(meetings);
    }

}
