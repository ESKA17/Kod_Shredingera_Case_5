package com.example.mycli.services;

import com.example.mycli.entity.Connection;
import com.example.mycli.web.SerializableSSE;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ConnectionsService {
    void matchFromMentee(Long matchID, HttpServletRequest httpServletRequest);

    void breakMatch(Long matchID, HttpServletRequest httpServletRequest);

    void breakMatchByID(Long posterID, Long accepterID);

    List<Long> getConnectionsStatusOne(HttpServletRequest httpServletRequest);

    List<Long> getConnectionsStatusTwo(HttpServletRequest httpServletRequest);

    void matchFromMentor(Long matchID, HttpServletRequest httpServletRequest);
}
