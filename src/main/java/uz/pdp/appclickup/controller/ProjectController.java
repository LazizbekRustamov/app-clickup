package uz.pdp.appclickup.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appclickup.dto.ApiResponse;
import uz.pdp.appclickup.dto.ProjectDto;
import uz.pdp.appclickup.dto.ProjectMemberDto;
import uz.pdp.appclickup.entity.Project;
import uz.pdp.appclickup.service.ProjectSevice;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/project")
public class ProjectController {
    @Autowired
    ProjectSevice projectSevice;

    @PostMapping("/addProject")
    private HttpEntity<?> addProject(@Valid @RequestBody ProjectDto projectDto) {
        ApiResponse apiResponse = projectSevice.addProject(projectDto);
        return ResponseEntity.status(apiResponse.isSucces() ? 200 : 409).body(apiResponse);
    }

    @PutMapping("/editProject/{id}")
    private HttpEntity<?> editProject(@Valid @RequestBody ProjectDto projectDto, @PathVariable UUID id) {
        ApiResponse apiResponse = projectSevice.editProject(projectDto, id);
        return ResponseEntity.status(apiResponse.isSucces() ? 200 : 409).body(apiResponse);
    }

    @GetMapping("/getProjects/{id}")
    private HttpEntity<?> getProjects(@PathVariable Long id) {
        List<Project> projects = projectSevice.getProjectsBySpace(id);
        return ResponseEntity.ok(projects);
    }

    @DeleteMapping("/delete/{id}")
    private HttpEntity<?> deleteProject(@PathVariable UUID id) {
        ApiResponse apiResponse = projectSevice.deleteProject(id);
        return ResponseEntity.status(apiResponse.isSucces() ? 200 : 409).body(apiResponse);
    }

    @PostMapping("/addOrEditOrDeleteProjectMember")  /**/
    private HttpEntity<?> addProjectMember(@Valid @RequestBody ProjectMemberDto projectMemberDto) {
        ApiResponse apiResponse = projectSevice.addProjectMember(projectMemberDto);
        return ResponseEntity.status(apiResponse.isSucces() ? 200 : 409).body(apiResponse);
    }
}
