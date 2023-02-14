package uz.pdp.appclickup.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appclickup.dto.ApiResponse;
import uz.pdp.appclickup.dto.CommentDto;
import uz.pdp.appclickup.entity.Comment;
import uz.pdp.appclickup.entity.User;
import uz.pdp.appclickup.security.CurrentUser;
import uz.pdp.appclickup.service.CommentService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    @Autowired
    CommentService commentService;

    @PostMapping("/add")  /*_*/
    private HttpEntity<?> addComment(@Valid @RequestBody CommentDto commentDto, @CurrentUser User user) {
        ApiResponse apiResponse = commentService.addComment(commentDto, user);
        return ResponseEntity.status(apiResponse.isSucces() ? 200 : 409).body(apiResponse);
    }


    @PutMapping("/edit/{id}")  /*_*/
    private HttpEntity<?> editMyComment(@Valid @RequestBody CommentDto commentDto, @PathVariable UUID id, @CurrentUser User user) {
        ApiResponse apiResponse = commentService.editMyComment(commentDto, id, user);
        return ResponseEntity.status(apiResponse.isSucces() ? 200 : 409).body(apiResponse);
    }


    @GetMapping("/get/{id}")  /*_*/
    private HttpEntity<?> getCommentsByTaskId(@PathVariable UUID id) {
        List<Comment> comments = commentService.getCommentsByTaskId(id);
        return ResponseEntity.ok(comments);
    }


    @DeleteMapping("/delete/{id}")   /*_*/   // Bu admin ucbun
    private HttpEntity<?> deleteComment(@PathVariable UUID id) {
        ApiResponse apiResponse = commentService.deleteComment(id);
        return ResponseEntity.status(apiResponse.isSucces() ? 200 : 409).body(apiResponse);
    }


    @DeleteMapping("/deleteMy/{id}")   /*_*/
    private HttpEntity<?> deleteMyComment(@PathVariable UUID id,@CurrentUser User user) {
        ApiResponse apiResponse = commentService.deleteMyComment(id,user);
        return ResponseEntity.status(apiResponse.isSucces() ? 200 : 409).body(apiResponse);
    }
}
