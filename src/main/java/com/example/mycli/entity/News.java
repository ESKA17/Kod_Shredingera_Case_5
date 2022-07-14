package com.example.mycli.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "news_table")
@Builder
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "news_en")
    @SequenceGenerator(name = "news_en", sequenceName = "news_en", allocationSize = 1)
    private Long id;

    @Column(name = "user_id")
    private Long userID;

    @Column(name = "news")
    private String news;

    @Column(name = "accepter" )
    private Long accepter_id;

    @Column(name = "accepted")
    private Boolean accepted;

    @Column(name = "date")
    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate date;

    @Column(name = "finished")
    private Boolean finished;
}
