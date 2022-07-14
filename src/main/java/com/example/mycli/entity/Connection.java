package com.example.mycli.entity;

import com.example.mycli.web.SerializableSSE;
import lombok.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.persistence.*;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Connection {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "con_en")
    @SequenceGenerator(name = "con_en", sequenceName = "con_en", allocationSize = 1)
    private Long id;
    @Column(name = "user_id")
    private Long userID;
    @Column(name = "friend_id")
    private Long friendID;
    @Column(name = "emitter")
    private SerializableSSE sseEmitter;
    @Column(name = "message_history")
    private String messageHistory;
    @Column(name = "connection_status")
    private int connectionStatus;

}
