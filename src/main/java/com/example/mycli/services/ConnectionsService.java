package com.example.mycli.services;

import com.example.mycli.model.FindAllReturnIdWrap;

import javax.servlet.http.HttpServletRequest;

public interface ConnectionsService {
    void matchFromMentee(Long matchID, HttpServletRequest httpServletRequest);

    void breakMatch(Long matchID, HttpServletRequest httpServletRequest);

    void breakMatchByID(Long posterID, Long accepterID);

    FindAllReturnIdWrap getConnectionsStatusOne(HttpServletRequest httpServletRequest);

    FindAllReturnIdWrap getConnectionsStatusTwo(HttpServletRequest httpServletRequest);

    void matchFromMentor(Long matchID, HttpServletRequest httpServletRequest);
}
