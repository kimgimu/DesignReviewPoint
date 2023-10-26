package com.kimu.dichamsi.controller;

import com.kimu.dichamsi.model.PostingDTO;
import com.kimu.dichamsi.service.PostingService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequestMapping("/posting")
@Controller
public class PostingController {

    private final PostingService postingService;

    public PostingController(PostingService postingService) {
        this.postingService = postingService;
    }

    @GetMapping("/board")
    public String postingBoardPage(){

        return "postingBoardPage";
    }

    @GetMapping("/board/write")
    public String postingBoardWritePage(){
        return "postingWritePage";
    }

    @PostMapping("/board/write")
    public String postingWrite(@ModelAttribute PostingDTO postingDTO,
                               @RequestPart("images") MultipartFile[] images) throws IOException {
        postingService.savePosting(postingDTO,images);
        return "redirect:/posting/board";
    }
}
