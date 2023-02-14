package uz.pdp.appclickup.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.pdp.appclickup.dto.ApiResponse;
import uz.pdp.appclickup.dto.CommentDto;
import uz.pdp.appclickup.entity.Comment;
import uz.pdp.appclickup.entity.Task;
import uz.pdp.appclickup.entity.User;
import uz.pdp.appclickup.repository.CategoryRepository;
import uz.pdp.appclickup.repository.CommentRepository;
import uz.pdp.appclickup.repository.TaskRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CommentService {
    @Autowired
    CommentRepository commentRepository;
    @Autowired
    TaskRepository taskRepository;


    public ApiResponse addComment(CommentDto commentDto, User user) {

        Optional<Task> optionalTask = taskRepository.findById(commentDto.getTaskId());
        if (!optionalTask.isPresent()) {
            return new ApiResponse("Bunday task mavjud emas", false);
        }

        Comment comment = new Comment(commentDto.getText(), optionalTask.get(), user);

        commentRepository.save(comment);

        return new ApiResponse("Comment qoshildi", true);
    }


    public ApiResponse editMyComment(CommentDto commentDto, UUID id, User user) {

        Optional<Comment> optionalComment = commentRepository.findById(id);
        if (!optionalComment.isPresent()) {
            return new ApiResponse("Bunday comment mavjud emas", false);
        }

        if (user.getId() != optionalComment.get().getOwner().getId()) {
            return new ApiResponse("Bu sizning kommentariyangiz emas", false);
        }

        Optional<Task> optionalTask = taskRepository.findById(commentDto.getTaskId());
        if (!optionalTask.isPresent()) {
            return new ApiResponse("Bunday task mavjud emas", false);
        }

        Comment comment = optionalComment.get();
        comment.setText(commentDto.getText());
        comment.setTask(optionalTask.get());

        commentRepository.save(comment);

        return new ApiResponse("Comment o'zgartirildi", true);
    }


    public List<Comment> getCommentsByTaskId(UUID id) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (!optionalTask.isPresent()) {
            return new ArrayList<>();
        }

        List<Comment> comments = commentRepository.findAllByTaskId(id);
        List<Comment> commentList = new ArrayList<>();
        for (Comment comment : comments) {
            Comment c = new Comment(comment.getText());
            commentList.add(c);
        }

        return commentList;
    }


    public ApiResponse deleteComment(UUID id) {
        try {
            commentRepository.deleteById(id);
            return new ApiResponse("Komment ochirildi", true);
        } catch (Exception e) {
            return new ApiResponse("Bunday komment mavjud emas", false);
        }
    }

    public ApiResponse deleteMyComment(UUID id, User user) {
        Optional<Comment> optionalComment = commentRepository.findById(id);
        if (!optionalComment.isPresent()) {
            return new ApiResponse("Bunday comment mavjud emas", false);
        }

        if (user.getId() != optionalComment.get().getOwner().getId()){
            return new ApiResponse("Sizda bunday comment mavjud emas", false);
        }

        commentRepository.deleteById(id);
        return new ApiResponse("Komment ochirildi", true);
    }
}
