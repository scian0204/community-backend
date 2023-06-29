package com.daelim.communitybackend.service;

import com.daelim.communitybackend.dto.response.Error;
import com.daelim.communitybackend.dto.response.Response;
import com.daelim.communitybackend.entity.GuestBoard;
import com.daelim.communitybackend.repository.GuestBoardRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class GuestBoardService {
    ObjectMapper objMpr = new ObjectMapper();
    @Autowired
    private GuestBoardRepository guestBoardRepository;

    public Response<Page<GuestBoard>> getListByTarget(Pageable pageable, String target) {
        Response<Page<GuestBoard>> res = new Response<>();
        Page<GuestBoard> guestBoards = guestBoardRepository.findAllByTarget(pageable, target);
        res.setData(guestBoards);

        return res;
    }

    public Response<GuestBoard> writeGBoard(Map<String, Object> gBoardObj) {
        Response<GuestBoard> res = new Response<>();
        GuestBoard guestBoard = objMpr.convertValue(gBoardObj, GuestBoard.class);
        res.setData(guestBoardRepository.save(guestBoard));

        return res;
    }

    public Response<GuestBoard> updateGBoard(Map<String, Object> gBoardObj, HttpSession session) {
        Response<GuestBoard> res = new Response<>();
        GuestBoard guestBoardObj = objMpr.convertValue(gBoardObj, GuestBoard.class);
        Optional<GuestBoard> guestBoardOptional = guestBoardRepository.findById(guestBoardObj.getGBoardId());

        if (guestBoardOptional.isPresent()) {
            GuestBoard guestBoard = guestBoardOptional.get();
            if (guestBoard.getUserId().equals(session.getAttribute("userId"))) {
                guestBoard.setContent(guestBoardObj.getContent());
                res.setData(guestBoardRepository.save(guestBoard));
            } else {
                Error error = new Error();
                error.setErrorId(1);
                error.setMessage("해당 방명록을 작성한 유저와 다름");
                res.setError(error);
            }
        } else {
            Error error = new Error();
            error.setErrorId(0);
            error.setMessage("해당 방명록이 존재하지 않음");
            res.setError(error);
        }

        return res;
    }

    public Response<Object> deleteGBoard(Integer gBoardId, HttpSession session) {
        Response<Object> res = new Response<>();
        Optional<GuestBoard> guestBoardOptional = guestBoardRepository.findById(gBoardId);

        if (guestBoardOptional.isPresent()) {
            GuestBoard guestBoard = guestBoardOptional.get();
            if (guestBoard.getUserId().equals(session.getAttribute("userId"))) {
                guestBoardRepository.delete(guestBoard);
            } else {
                Error error = new Error();
                error.setErrorId(1);
                error.setMessage("해당 방명록을 작성한 유저와 다름");
                res.setError(error);
            }
        } else {
            Error error = new Error();
            error.setErrorId(0);
            error.setMessage("해당 방명록이 존재하지 않음");
            res.setError(error);
        }

        return res;
    }
}
