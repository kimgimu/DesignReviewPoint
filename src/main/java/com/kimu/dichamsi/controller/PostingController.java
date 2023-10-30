package com.kimu.dichamsi.controller;

import com.kimu.dichamsi.model.Comment;
import com.kimu.dichamsi.model.CommentDTO;
import com.kimu.dichamsi.model.CustomUserDetails;
import com.kimu.dichamsi.model.PostingDTO;
import com.kimu.dichamsi.service.CommentService;
import com.kimu.dichamsi.service.PostingService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RequestMapping("/posting")
@Controller
public class PostingController {

    private final PostingService postingService;
    private final CommentService commentService;

    public PostingController(PostingService postingService, CommentService commentService) {
        this.postingService = postingService;
        this.commentService = commentService;
    }

    @GetMapping("/board")
    public String postingBoardPage(Model model){
        List<PostingDTO> postings = postingService.showAllPosting();
        model.addAttribute("postings",postings);
        return "postingBoardPage";
    }

    @GetMapping("/board/write")
    public String postingBoardWritePage(){
        return "postingWritePage";
    }

    @PostMapping("/board/write")
    public String postingWrite(@ModelAttribute PostingDTO postingDTO,
                               @RequestPart("file") MultipartFile[] images) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        postingService.savePosting(postingDTO,images);
        return "redirect:/posting/board";
    }

    @GetMapping("/board/view/{id}")
    public String postingViewPage(@PathVariable("id") Long id,
                                  Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("userEmail",authentication.getName());
        PostingDTO postingDTO = postingService.showView(id);
        List<CommentDTO> comments = commentService.showComment(id);
        model.addAttribute("comments",comments);
        model.addAttribute("posting",postingDTO);
        return "postingView";
    }

//    @PostMapping("/comment")
//    public @ResponseBody ResponseBody commentWrite(String userEmail){
//
//        return ;
//    }

}
