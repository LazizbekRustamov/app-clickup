package uz.pdp.appclickup.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uz.pdp.appclickup.dto.*;
import uz.pdp.appclickup.entity.Priority;
import uz.pdp.appclickup.entity.Task;
import uz.pdp.appclickup.service.TaskService;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/task")
public class TaskController {
    @Autowired
    TaskService taskService;


    @PostMapping("/add")  /**/
    private HttpEntity<?> addTask(@Valid @RequestBody TaskAddOrEditDto taskDto, @RequestParam("files") List<MultipartFile> files) throws IOException {
        ApiResponse apiResponse = taskService.addTask(taskDto, files);
        return ResponseEntity.status(apiResponse.isSucces() ? 200 : 409).body(apiResponse);
    }

    @PostMapping("/addPriority/{id}")   /**/
    private HttpEntity<?> addPriorityToTask(@RequestBody Priority priority, @PathVariable UUID id) {
        ApiResponse apiResponse = taskService.addPriorityToTask(priority, id);
        return ResponseEntity.status(apiResponse.isSucces() ? 200 : 409).body(apiResponse);
    }

    @PutMapping("/deletePriority/{id}")   /**/
    private HttpEntity<?> deletePriorityFromTask(@RequestBody Priority priority, @PathVariable UUID id) {
        ApiResponse apiResponse = taskService.deletePriorityFromTask(priority, id);
        return ResponseEntity.status(apiResponse.isSucces() ? 200 : 409).body(apiResponse);
    }

    @PutMapping("/activate/{id}")   /**/
    private HttpEntity<?> activateTask(@PathVariable UUID id) {
        ApiResponse apiResponse = taskService.activateTask(id);
        return ResponseEntity.status(apiResponse.isSucces() ? 200 : 409).body(apiResponse);
    }


    @PutMapping("/edit/{id}")   /**/
    private HttpEntity<?> editTask(@PathVariable UUID id, @Valid @RequestBody TaskAddOrEditDto taskDto, @RequestParam("files") List<MultipartFile> files) throws IOException {
        ApiResponse apiResponse = taskService.editTask(id, taskDto, files);
        return ResponseEntity.status(apiResponse.isSucces() ? 200 : 409).body(apiResponse);
    }


    @GetMapping("/get/{id}")   /**/
    private HttpEntity<?> getTask(@PathVariable UUID id) {
        Task task = taskService.getTask(id);
        return ResponseEntity.status(task != null ? 200 : 409).body(task);
    }


    @GetMapping("/getList/{id}")   /**/
    private HttpEntity<?> getListTaskFromCategory(@PathVariable UUID id) {
        List<Task> tasks = taskService.getTasksFromCategory(id);
        return ResponseEntity.ok(tasks);
    }


    @DeleteMapping("/delete/{id}")  /**/
    private HttpEntity<?> deleteTask(@PathVariable UUID id){
        ApiResponse apiResponse = taskService.deletetask(id);
        return ResponseEntity.status(apiResponse.isSucces() ? 200 : 409).body(apiResponse);
    }


    @PostMapping("/addOrEditOrDeleteTaskMember")  /**/
    private HttpEntity<?> addOrEditOrDeleteTaskMember(@Valid @RequestBody TaskMemberDto taskMemberDto) {
        ApiResponse apiResponse = taskService.addOrEditOrDeleteTaskMember(taskMemberDto);
        return ResponseEntity.status(apiResponse.isSucces() ? 200 : 409).body(apiResponse);
    }


    @PostMapping("/addTag/{id}")  /**/
    private HttpEntity<?> addTag(@Valid @RequestBody TaskTagDto taskTagDto, @PathVariable UUID id) {
        ApiResponse apiResponse = taskService.addTag(taskTagDto,id);
        return ResponseEntity.status(apiResponse.isSucces() ? 200 : 409).body(apiResponse);
    }


    @PutMapping("/editOrDeleteTag/{id}")  /**/
    private HttpEntity<?> addOrDeleteTag(@Valid @RequestBody TagEditDto tagEditDto, @PathVariable Long id) {
        ApiResponse apiResponse = taskService.editOrDeleteTag(tagEditDto,id);
        return ResponseEntity.status(apiResponse.isSucces() ? 200 : 409).body(apiResponse);
    }
}
