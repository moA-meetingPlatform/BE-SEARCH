package com.moa.search.domain;

import com.moa.global.common.BaseCreateDateTime;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "meeting")
public class Meeting extends BaseCreateDateTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", length = 150)
    private String meetingTitle;


}
