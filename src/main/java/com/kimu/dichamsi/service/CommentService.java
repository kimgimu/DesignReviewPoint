package com.kimu.dichamsi.service;

import com.kimu.dichamsi.execption.AppException;
import com.kimu.dichamsi.execption.ErrorCode;
import com.kimu.dichamsi.model.Comment;
import com.kimu.dichamsi.model.CommentDTO;
import com.kimu.dichamsi.model.Member;
import com.kimu.dichamsi.model.Posting;
import com.kimu.dichamsi.repository.CommentRepository;
import com.kimu.dichamsi.repository.MemberReository;
import com.kimu.dichamsi.repository.PostingRepositoy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostingRepositoy postingRepositoy;
    private final MemberReository memberReository;
    public CommentService(CommentRepository commentRepository, PostingRepositoy postingRepositoy, MemberReository memberReository) {
        this.commentRepository = commentRepository;
        this.postingRepositoy = postingRepositoy;
        this.memberReository = memberReository;
    }

    public List<CommentDTO> showComment(Long postingID){
        List<Comment> comments = commentRepository.findByPostingId(postingID);
        List<CommentDTO> commentDTOs = new ArrayList<>();
        for(Comment comment : comments){
            commentDTOs.add(comment.toDTO());
        }
        return commentDTOs;
    }

    public boolean commentWrite(CommentDTO commentDTO){
        Comment comment = commentDTO.toEntity();
        Optional<Member> member = memberReository.findByNickname(commentDTO.getMemberName());
        Optional<Posting> posting = postingRepositoy.findById(commentDTO.getPostingId());

        comment.setMember(member.get());
        comment.setPosting(posting.get());

        Comment savedComment = commentRepository.save(comment);

        if (savedComment != null) {
            return true;
        } else {
            throw new AppException(ErrorCode.COMMENT_NOT_SAVED, "댓글 저장 실패");
        }
    }

    public boolean commentDelete(CommentDTO commentDTO){
        // 댓글을 찾아서 삭제
        Comment comment = commentRepository.findById(commentDTO.getCommentId())
                .orElse(null);

        System.out.println("댓글"+comment);

        if (comment != null) {
            commentRepository.delete(comment);
            return true; // 삭제 성공
        } else {
            return false; // 삭제 실패
        }
    }

}
