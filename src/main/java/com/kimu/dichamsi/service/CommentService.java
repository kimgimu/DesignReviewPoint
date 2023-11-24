package com.kimu.dichamsi.service;

import com.kimu.dichamsi.model.Comment;
import com.kimu.dichamsi.model.CommentDTO;
import com.kimu.dichamsi.model.Member;
import com.kimu.dichamsi.model.Post;
import com.kimu.dichamsi.repository.CommentRepository;
import com.kimu.dichamsi.repository.MemberReository;
import com.kimu.dichamsi.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;
    private final MemberReository memberReository;
    private final PostRepository postRepository;
    public CommentService(CommentRepository commentRepository, MemberReository memberReository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.memberReository = memberReository;
        this.postRepository = postRepository;
    }

    public Comment writeComment(CommentDTO commentDTO,Member member,Long postId){
        Optional<Post> post  = postRepository.findById(postId);
        Comment commentBefore = commentDTO.toEntity(member,post.get());
        return commentRepository.save(commentBefore);
    }

    public List<Comment> getCommentList(Long postingId) {
        return commentRepository.findByPostId(postingId);
    }

    public Optional<Comment> deleteComment(Long commentId){
        commentRepository.deleteById(commentId);
        return commentRepository.findById(commentId);
    }

//    public List<CommentDTO> showComment(Long postingID){
//        List<Comment> comments = commentRepository.findByPostingId(postingID);
//        List<CommentDTO> commentDTOs = new ArrayList<>();
//        for(Comment comment : comments){
//            commentDTOs.add(comment.toDTO());
//        }
//        return commentDTOs;
//    }

//    public boolean commentWrite(CommentDTO commentDTO){
//        Comment comment = commentDTO.toEntity();
//        Optional<Member> member = memberReository.findByNickname(commentDTO.getMemberName());
//        Optional<Posting> posting = postingRepositoy.findById(commentDTO.getPostingId());
//
//        comment.setMember(member.get());
//        comment.setPosting(posting.get());
//
//        Comment savedComment = commentRepository.save(comment);
//
//        if (savedComment != null) {
//            return true;
//        } else {
//            throw new AppException(ErrorCode.COMMENT_NOT_SAVED, "댓글 저장 실패");
//        }
//    }

//    public boolean commentDelete(CommentDTO commentDTO){
//        // 댓글을 찾아서 삭제
//        Comment comment = commentRepository.findById(commentDTO.getCommentId())
//                .orElse(null);
//
//        System.out.println("댓글"+comment);
//
//        if (comment != null) {
//            commentRepository.delete(comment);
//            return true; // 삭제 성공
//        } else {
//            return false; // 삭제 실패
//        }
//    }

}
