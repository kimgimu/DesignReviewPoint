package com.kimu.dichamsi.controller;

import com.kimu.dichamsi.model.Comment;
import com.kimu.dichamsi.model.CommentDTO;
import com.kimu.dichamsi.model.CustomUserDetails;
import com.kimu.dichamsi.model.PostingDTO;
import com.kimu.dichamsi.service.CommentService;
import com.kimu.dichamsi.service.PostingService;
import org.springframework.http.ResponseEntity;
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
        //시큐리티에서 닉네임 뽑아오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        String nickname = customUserDetails.getMember().getNickname();

        model.addAttribute("nickname",nickname);

        PostingDTO postingDTO = postingService.showView(id);
        List<CommentDTO> comments = commentService.showComment(id);

        model.addAttribute("comments",comments);
        model.addAttribute("posting",postingDTO);
        return "postingView";
    }

    @PostMapping("/comment/write")
    @ResponseBody
    public ResponseEntity<?> commentWrite(CommentDTO commentDTO){
        boolean isSaved = commentService.commentWrite(commentDTO);
        if (isSaved) {
            return ResponseEntity.ok(commentDTO);
        } else {
            return ResponseEntity.badRequest().body("댓글 저장 실패");
        }
    }

    @PostMapping("/comment/delete")
    @ResponseBody
    public ResponseEntity<?> commentDelete(CommentDTO commentDTO){
        boolean isDeleted = commentService.commentDelete(commentDTO);

        System.out.println(isDeleted);

        if (isDeleted) {
            return ResponseEntity.ok("댓글 삭제 성공");
        } else {
            return ResponseEntity.badRequest().body("댓글 삭제 실패");
        }
    }


}
