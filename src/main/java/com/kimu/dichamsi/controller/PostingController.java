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

//    @GetMapping("/board")
//    public String postingBoardPage(Model model) {
//        List<PostingDTO> postings = postingService.showAllPosting();
//        model.addAttribute("postings", postings);
//        return "postingBoardPage";
//    }

//    @GetMapping("/board/write")
//    public String postingBoardWritePage() {
//        return "postingWritePage";
//    }

//    @PostMapping("/board/write")
//    public String postingWrite(@ModelAttribute PostingDTO postingDTO,
//                               @RequestPart("file") MultipartFile[] images) throws IOException {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
//        String nickname = customUserDetails.getMember().getNickname();
//        log.info("info log={}", nickname);
//        postingService.savePosting(postingDTO, images, nickname);
//        return "redirect:/posting/board";
//    }

//    @GetMapping("/board/update/{id}")
//    public String postingUpdatePage(@PathVariable("id") Long id,
//                                    Model model) {
//        //시큐리티에서 닉네임 뽑아오기
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
//        String nickname = customUserDetails.getMember().getNickname();
//
//        model.addAttribute("nickname", nickname);
//
//        PostingDTO postingDTO = postingService.showView(id);
//        log.info("update info ={}", postingDTO);
//        model.addAttribute("posting", postingDTO);
//        return "postingUpdateView";
//    }
//
//    @PostMapping("/board/update/{id}")
//    public String postingUpdate(@PathVariable("id") Long id,
//                                PostingDTO postingDTO,
//                                @RequestPart("file") MultipartFile[] images) throws IOException {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
//        String nickname = customUserDetails.getMember().getNickname();
//        log.info("info log={}", nickname);
//        log.info("update 전 DTOinfo={}", postingDTO);
//        postingDTO.setId(id);
//        log.info("update 후 DTOinfo={}", postingDTO);
//        postingService.addNewPostingImages(postingDTO, images, nickname);
//        return "redirect:/posting/board";
//    }

//    @GetMapping("/board/view/{id}")
//    public String postingViewPage(@PathVariable("id") Long id,
//                                  Model model) {
//        //시큐리티에서 닉네임 뽑아오기
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
//        String nickname = customUserDetails.getMember().getNickname();
//
//        model.addAttribute("nickname", nickname);
//
//        PostingDTO postingDTO = postingService.showView(id);
////        List<CommentDTO> comments = commentService.showComment(id);
//
//        model.addAttribute("comments", comments);
//        model.addAttribute("posting", postingDTO);
//        return "postingView";
//    }

//    @PostMapping("/board/view/delete")
//    @ResponseBody
//    public ResponseEntity<String> postingDelete(Long id) {
//        postingService.postingDelete(id);
//        if (postingService.showView(id) == null) {
//            return ResponseEntity.ok("게시글 삭제 성공");
//        } else {
//            return ResponseEntity.badRequest().body("게시글 삭제 실패");
//        }
//    }

//    @PostMapping("/image/delete/image/{imageId}")
//    @ResponseBody
//    public ResponseEntity<?> imageDelete(@PathVariable("imageId") Long imageId) {
//        boolean isDeleted = postingService.imageDelete(imageId);
//
//        System.out.println(isDeleted);
//
//        if (isDeleted) {
//            return ResponseEntity.ok("이미지 삭제 성공");
//        } else {
//            return ResponseEntity.badRequest().body("이미지 삭제 실패");
//        }
//    }

    @GetMapping("/write")
    public String writePage(Model model) {
        return "write";
    }

    @GetMapping("/update/{postId}")
    public String updatePage(@PathVariable Long postId,
                            Model model) {
        Optional<Post> post = postingService.viewPost(postId);
        model.addAttribute("post",post);
        return "update";
    }

    @PostMapping("/update/{postId}")
    public String postUpdate(@PathVariable Long postId,
                             @ModelAttribute PostDTO postDTO){
        log.info("여기까지는 오는지 = {}", postDTO);
        postDTO.setNickname(principalInfo().getNickname());
        Post post = postingService.UpdatePost(postDTO,postId);
        log.info("최종 = {}", post);
        return "redirect:/posting/view/" + postId;
    }

    @PostMapping("/image/upload")
    @ResponseBody
    public Map<String,Object> Write(
                                    MultipartRequest request){
        Map<String,Object> responseData = new HashMap<>();

        try {
            String s3Url = postingService.imageUpload(request);

            responseData.put("uploaded",true);
            responseData.put("url",s3Url);
        } catch (IOException e) {
            responseData.put("uploaded",false);
        }
        return responseData;
    }

    @PostMapping("/write")
    public String write(@ModelAttribute PostDTO postDTO){

        log.info("write1={}",postDTO);
        postDTO.setNickname(principalInfo().getNickname());
        Post post = postingService.writePost(postDTO);
        log.info("write2={}",post);
        return "redirect:/posting/view/"+post.getId();
    }

    @GetMapping("/view/{id}")
    public String modalView(@PathVariable Long id,
                            Model model){
        Optional<Post> post = postingService.viewPost(id);
        model.addAttribute("post",post);
        return "view";
    }

    @GetMapping("/main")
    public String mainPage(){
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
                String pageURL = "/posting/view/"+post.getId();
                postMap.put("pageURL",pageURL);
                responseData.add(postMap);
            }

            return ResponseEntity.ok(responseData);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/search")
    public String SearchPage(){
        return "search";
    }

    @PostMapping("/search/result")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> searchPage (@RequestParam("keyword") String keyword,
                                                                 @RequestParam(defaultValue = "1") int page,
                                                                 @RequestParam(defaultValue = "20") int size) {

        List<Post> postList = postingService.viewSearchPost(keyword,page, size);

        if (postList != null) {
            List<Map<String, Object>> responseData = new ArrayList<>();
            for (Post post : postList) {
                Map<String, Object> postMap = new HashMap<>();
                postMap.put("tags", post.getTitle());
                postMap.put("webformatURL", post.getContent());
                String pageURL = "/posting/view/"+post.getId();
                postMap.put("pageURL",pageURL);
                responseData.add(postMap);
            }

            return ResponseEntity.ok(responseData);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/like/{postId}")
    @ResponseBody
    public ResponseEntity<?> likeButton(@PathVariable Long postId){
        Post post = postingService.likeButton(postId);
        if(post != null){
            return ResponseEntity.ok(post);
        }else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("like 이벤트 실패");
        }
    }

    @PostMapping("/comment/write/{postingId}")
    @ResponseBody
    public ResponseEntity<?> commentWrite(@PathVariable Long postingId,
                                          @RequestBody CommentDTO commentDTO){
        log.info("commentDTO = {}",commentDTO);
        Comment comment = commentService.writeComment(commentDTO,principalInfo(),postingId);
        if(comment != null) {
            return ResponseEntity.ok(comment);
        }else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("댓글작성 실패");
        }
    }

    @PostMapping("/comment/view/{postingId}")
    @ResponseBody
    public ResponseEntity<?> getCommentList(@PathVariable Long postingId){
        List<Comment> commentList = commentService.getCommentList(postingId);
        if(commentList != null) {
            return ResponseEntity.ok(commentList);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("댓글 로드 실패");
        }
    }

    private Member principalInfo(){
        //시큐리티에서 닉네임 뽑아오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        return customUserDetails.getMember();
    }


}
