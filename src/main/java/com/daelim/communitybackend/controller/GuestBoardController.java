package com.daelim.communitybackend.controller;

import com.daelim.communitybackend.dto.response.Response;
import com.daelim.communitybackend.entity.GuestBoard;
import com.daelim.communitybackend.service.GuestBoardService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/gBoard")
public class GuestBoardController {
    @Autowired
    GuestBoardService guestBoardService;

    @GetMapping("/{target}")
    public Response<Page<GuestBoard>> getListByTarget(@PageableDefault(page = 0, size = 10, sort = "gBoardId", direction = Sort.Direction.DESC)Pageable pageable, @PathVariable String target) {
        return guestBoardService.getListByTarget(pageable, target);
    }

    @PostMapping("")
    public Response<GuestBoard> writeGBoard(@RequestBody Map<String, Object> gBoardObj) {
        return guestBoardService.writeGBoard(gBoardObj);
    }

    @PutMapping("")
    public Response<GuestBoard> updateGBoard(@RequestBody Map<String, Object> gBoardObj, HttpSession session) {
        return guestBoardService.updateGBoard(gBoardObj, session);
    }

    @DeleteMapping("/{gBoardId}")
    public Response<Object> deleteGBoard(@PathVariable Integer gBoardId, HttpSession session) {
        return guestBoardService.deleteGBoard(gBoardId, session);
    }
}
