package com.kimu.dichamsi.service;

import com.kimu.dichamsi.model.Comment;
import com.kimu.dichamsi.model.CommentDTO;
import com.kimu.dichamsi.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public List<CommentDTO> showComment(Long postingID){
        List<Comment> comments = commentRepository.findByPostingId(postingID);
        List<CommentDTO> commentDTOs = new ArrayList<>();
        for(Comment comment : comments){
            commentDTOs.add(comment.toDTO());
        }
        return commentDTOs;
    }

}
