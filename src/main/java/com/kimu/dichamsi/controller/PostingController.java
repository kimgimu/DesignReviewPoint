package com.kimu.dichamsi.controller;

import com.kimu.dichamsi.model.*;
import com.kimu.dichamsi.service.CommentService;
import com.kimu.dichamsi.service.PostingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import java.io.IOException;
import java.util.*;

@RequestMapping("/posting")
@Controller
@Slf4j
public class PostingController {

    private final PostingService postingService;
    private final CommentService commentService;

    public PostingController(PostingService postingService, CommentService commentService) {
        this.postingService = postingService;
        this.commentService = commentService;
    }

    @GetMapping("/write")
    public String writePage(Model model) {
        return "write";
    }

    @GetMapping("/update/{postId}")
    public String updatePage(@PathVariable Long postId,
                             Model model) {
        Optional<Post> post = postingService.viewPost(postId);
        model.addAttribute("post", post);
        return "update";
    }

    @PostMapping("/update/{postId}")
    public String postUpdate(@PathVariable Long postId,
                             @ModelAttribute PostDTO postDTO) {
        postDTO.setNickname(principalInfo().getNickname());
        List<Comment> commentList = commentService.getCommentList(postId);
        Post post = postingService.UpdatePost(postDTO, postId, commentList);
        return "redirect:/posting/view/" + postId;
    }

    @PostMapping("/image/upload")
    @ResponseBody
    public Map<String, Object> write(
            MultipartRequest request) {
        Map<String, Object> responseData = new HashMap<>();

        try {
            String s3Url = postingService.imageUpload(request);

            responseData.put("uploaded", true);
            responseData.put("url", s3Url);
        } catch (IOException e) {
            responseData.put("uploaded", false);
        }
        return responseData;
    }

    @PostMapping("/write")
    public String write(@ModelAttribute PostDTO postDTO) {

        log.info("write1={}", postDTO);
        postDTO.setNickname(principalInfo().getNickname());
        Post post = postingService.writePost(postDTO);
        log.info("write2={}", post);
        return "redirect:/posting/view/" + post.getId();
    }

    @GetMapping("/view/{id}")
    public String modalView(@PathVariable Long id,
                            Model model) {
        Optional<Post> post = postingService.viewPost(id);
        model.addAttribute("post", post);
        return "view";
    }

    @PostMapping("/view/delete/{id}")
    public ResponseEntity<String> deletePost(@PathVariable Long id) {
        Optional<Post> post = postingService.deletePost(id);
        if (post.isEmpty()) {
            log.info("삭제 완료 리디렉션 실행");
            // 성공 시 JSON 응답
            return ResponseEntity.ok("{\"redirect\": \"/posting/main\"}");
        }
        // 실패 시 JSON 응답
        return ResponseEntity.ok("{\"redirect\": \"/view/" + id + "\"}");
    }

    @GetMapping("/main")
    public String mainPage(Model model) {
        List<TopSeven> topSevenList = postingService.topSevenPost();
        log.info("topSeven = {}",topSevenList);
        model.addAttribute("topSevenList",topSevenList);
        return "main";
    }

    @PostMapping("/main")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> mainPage(@RequestParam(defaultValue = "1") int page,
                                                              @RequestParam(defaultValue = "20") int size) {

        List<Post> postList = postingService.viewAllPost(page, size);

        if (postList != null) {
            List<Map<String, Object>> responseData = new ArrayList<>();
            for (Post post : postList) {
                Map<String, Object> postMap = new HashMap<>();
                postMap.put("tags", post.getTitle());
                postMap.put("webformatURL", post.getContent());
                String pageURL = "/posting/view/" + post.getId();
                postMap.put("pageURL", pageURL);
                responseData.add(postMap);
            }

            return ResponseEntity.ok(responseData);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/search/main")
    public String search(@RequestParam(name = "keyword") String keyword,Model model){
        model.addAttribute("keyword",keyword);
        return "search";
    }

    @PostMapping("/search")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> searchPage(@RequestParam("keyword") String keyword,
                                                                @RequestParam(defaultValue = "1") int page,
                                                                @RequestParam(defaultValue = "20") int size) {

        List<Post> postList = postingService.viewSearchPost(keyword, page, size);
        log.info("postList 출력 = {}",postList);

        if (postList != null) {
            List<Map<String, Object>> responseData = new ArrayList<>();
            for (Post post : postList) {
                Map<String, Object> postMap = new HashMap<>();
                postMap.put("tags", post.getTitle());
                postMap.put("webformatURL", post.getContent());
                String pageURL = "/posting/view/" + post.getId();
                postMap.put("pageURL", pageURL);
                responseData.add(postMap);
            }
            return ResponseEntity.ok(responseData);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @PostMapping("/like/{postId}")
    @ResponseBody
    public ResponseEntity<?> likeButton(@PathVariable Long postId) {
        Post post = postingService.likeButton(postId);
        if (post != null) {
            return ResponseEntity.ok(post);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("like 이벤트 실패");
        }
    }

    @PostMapping("/comment/write/{postingId}")
    @ResponseBody
    public ResponseEntity<?> commentWrite(@PathVariable Long postingId,
                                          @RequestBody CommentDTO commentDTO) {
        log.info("commentDTO = {}", commentDTO);

        Comment comment = commentService.writeComment(commentDTO, principalInfo(), postingId);
        if (comment != null) {
            return ResponseEntity.ok(comment);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("댓글작성 실패");
        }
    }

    @PostMapping("/comment/view/{postingId}")
    @ResponseBody
    public ResponseEntity<?> getCommentList(@PathVariable Long postingId) {
        List<Comment> commentList = commentService.getCommentList(postingId);
        if (commentList != null) {
            return ResponseEntity.ok(commentList);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("댓글 로드 실패");
        }
    }

    @PostMapping("/comment/view/delete/{commentId}")
    @ResponseBody
    public ResponseEntity<?> deleteComment(@PathVariable Long commentId) {
        Optional<Comment> comment = commentService.deleteComment(commentId);
        log.info("댓글 유무 확인 = {}", comment);
        if (comment.isEmpty()) {
            log.info("값이 없다면 댓글 삭제 성공하는 조건");
            return ResponseEntity.ok("댓글 삭제 성공");
        } else {
            log.info("값이 있다면 댓글 삭제 실패하는 조건");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("댓글 삭제 실패");
        }
    }

    private Member principalInfo() {
        //시큐리티에서 닉네임 뽑아오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        return customUserDetails.getMember();
    }


}
