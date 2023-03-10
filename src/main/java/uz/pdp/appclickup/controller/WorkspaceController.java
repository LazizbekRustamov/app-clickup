package uz.pdp.appclickup.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appclickup.dto.ApiResponse;
import uz.pdp.appclickup.dto.MemberDto;
import uz.pdp.appclickup.dto.WorkspaceDto;
import uz.pdp.appclickup.dto.WorkspaceRoleDto;
import uz.pdp.appclickup.entity.User;
import uz.pdp.appclickup.security.CurrentUser;
import uz.pdp.appclickup.service.WorkspaceService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/workspace")
public class WorkspaceController {

    @Autowired
    WorkspaceService workspaceService;

    @PostMapping
    @PreAuthorize(value = "hasAuthority('CAN_EDIT_WORKSPACE')")  // Bu oddiy userniyam role qosholmasligi uchun bu ishlashi uchun security configda  narsa yozish kere// ya'ni SecurityConfigni ustiga @EnableGlobalMethodSecurity(prePostEnabled = true)qoyish kerak
    private HttpEntity<?> addWorkspace(@Valid @RequestBody WorkspaceDto workspaceDto, @CurrentUser User user) {
        ApiResponse apiResponse = workspaceService.addWorkspace(workspaceDto,user);
        return ResponseEntity.status(apiResponse.isSucces() ? 200 : 409).body(apiResponse);
    }


    @PutMapping("/{id}")
    private HttpEntity<?> editWorkspace(@PathVariable Long id, @Valid @RequestBody WorkspaceDto workspaceDto) {
        ApiResponse apiResponse = workspaceService.editWorkspace(id,workspaceDto);
        return ResponseEntity.status(apiResponse.isSucces() ? 200 : 409).body(apiResponse);
    }

    @PutMapping("/changeOwner/{id}")
    private HttpEntity<?> changeOwnerWorkspace(@PathVariable Long id, @RequestParam UUID newOwnerId) {
        ApiResponse apiResponse = workspaceService.changeOwnerWorkspace(id, newOwnerId);
        return ResponseEntity.status(apiResponse.isSucces() ? 200 : 409).body(apiResponse);
    }

    @DeleteMapping("/{id}")
//    @PreAuthorize(value = "hasAuthority('CAN_EDIT_WORKSPACE')")  // Bu oddiy userniyam role qosholmasligi uchun bu ishlashi uchun security configda  narsa yozish kere// ya'ni SecurityConfigni ustiga @EnableGlobalMethodSecurity(prePostEnabled = true)qoyish kerak
    private HttpEntity<?> deleteWorkspace(@PathVariable Long id) {
        ApiResponse apiResponse = workspaceService.deleteWorkspace(id);
        return ResponseEntity.status(apiResponse.isSucces() ? 200 : 409).body(apiResponse);
    }

    @PostMapping("/addOrEditOrRemove/{id}")
    private HttpEntity<?> addOrEditOrRemoveWorkspaceUser(@PathVariable Long id,@RequestBody MemberDto memberDto) {
        ApiResponse apiResponse = workspaceService.addOrEditOrRemoveWorkspaceUser(id,memberDto);
        return ResponseEntity.status(apiResponse.isSucces() ? 200 : 409).body(apiResponse);
    }

    @PutMapping("/join")
    private HttpEntity<?> joinWorkspace(@RequestParam Long id,@CurrentUser User user) {  //@Currentuser bu sistemadagi userni olb beradi
        ApiResponse apiResponse = workspaceService.joinWorkspace(id,user);
        return ResponseEntity.status(apiResponse.isSucces() ? 200 : 409).body(apiResponse);
    }


    @GetMapping("/get/{id}")
    private HttpEntity<?> getMemberAndGuest(@PathVariable Long id) {
        List<MemberDto> members= workspaceService.getMemberAndGuest(id);
        return ResponseEntity.ok(members);
    }



    @GetMapping("/getMyWorkspace")
    private HttpEntity<?> getMyWorkspace(@CurrentUser User user){    // Sisyemadagi userni olb beradi
        List<WorkspaceDto> workspaces= workspaceService.getMyWorkspaces(user);
        return ResponseEntity.ok(workspaces);
    }



    @PostMapping("addRole")
    private HttpEntity<?> addWorkspaceRole(@RequestParam Long workspaceId,@RequestBody WorkspaceRoleDto workspaceRoleDto,@CurrentUser User user) {
        ApiResponse apiResponse = workspaceService.addWorkspaceRole(workspaceId,workspaceRoleDto,user);
        return ResponseEntity.status(apiResponse.isSucces() ? 200 : 409).body(apiResponse);
    }



    @PutMapping("/addOrRemovePermission")
    private HttpEntity<?> addOrRemovePermission(@RequestBody WorkspaceRoleDto workspaceRoleDto) {
        ApiResponse apiResponse = workspaceService.addOrRemovePermission(workspaceRoleDto);
        return ResponseEntity.status(apiResponse.isSucces() ? 200 : 409).body(apiResponse);
    }
}
