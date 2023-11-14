package com.moa.search.infrastructure;

import com.moa.search.domain.Meeting;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {

    // 연관 검색어를 위한 메서드
    @Query("SELECT m.meetingTitle FROM Meeting m WHERE m.meetingTitle LIKE %:keyword%")
    List<String> findRelatedTitles(@Param("keyword") String keyword);

    // 실제 검색 결과를 위한 메서드
    List<Meeting> findByMeetingTitleContaining(String title);
}
