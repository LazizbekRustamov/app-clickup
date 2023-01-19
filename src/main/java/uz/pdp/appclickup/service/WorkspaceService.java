package uz.pdp.appclickup.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import uz.pdp.appclickup.dto.ApiResponse;
import uz.pdp.appclickup.dto.MemberDto;
import uz.pdp.appclickup.dto.WorkspaceDto;
import uz.pdp.appclickup.entity.*;
import uz.pdp.appclickup.entity.enums.AddType;
import uz.pdp.appclickup.entity.enums.WorkspacePermissionName;
import uz.pdp.appclickup.entity.enums.WorkspaceRoleName;
import uz.pdp.appclickup.repository.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class WorkspaceService {
    @Autowired
    WorkspaceRepository workspaceRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    AttachmentRepository attachmentRepository;
    @Autowired
    WorkspaceUserRepository workspaceUserRepository;
    @Autowired
    WorkspaceRoleRepository workspaceRoleRepository;
    @Autowired
    WorkspacePermissonRepository workspacePermissonRepository;


    public ApiResponse addWorkspace(WorkspaceDto workspaceDto, User user) {
//        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal(); // Bu tizimdagi userni aniqlash uchun  buni ishini @CurrentUser anotatsiyasi ham bajaradi
        if (workspaceRepository.existsByOwnerIdAndName(user.getId(), workspaceDto.getName())) {
            return new ApiResponse("Sizda bunday workspace mavjud", false);
        }
        Workspace workspace = new Workspace(
                workspaceDto.getName(),
                workspaceDto.getColor(),
                user,
                workspaceDto.getAvatarId() == null ? null : attachmentRepository.findById(workspaceDto.getAvatarId()).orElseThrow(() -> new ResourceNotFoundException("Attachment"))
        );

        workspaceRepository.save(workspace);


        WorkspaceRole ownerRole = workspaceRoleRepository.save(new WorkspaceRole(
                workspace,
                WorkspaceRoleName.ROLE_OWNER.name(),
                null
        ));


        WorkspaceRole adminRole = workspaceRoleRepository.save(new WorkspaceRole(workspace, WorkspaceRoleName.ROLE_ADMIN.name(), null));
        WorkspaceRole memberRole = workspaceRoleRepository.save(new WorkspaceRole(workspace, WorkspaceRoleName.ROLE_MEMBER.name(), null));
        WorkspaceRole guestRole = workspaceRoleRepository.save(new WorkspaceRole(workspace, WorkspaceRoleName.ROLE_GUEST.name(), null));


        workspaceUserRepository.save(new WorkspaceUser(
                workspace,
                user,
                ownerRole,
                new Timestamp(System.currentTimeMillis()),  // Bu hozirgi vaqt
                new Timestamp(System.currentTimeMillis())
        ));



        WorkspacePermissionName[] values = WorkspacePermissionName.values();
        List<WorkspacePermission> workspacePermissions = new ArrayList<>();
        for (WorkspacePermissionName permissions : values) {
            WorkspacePermission workspacePermission = new WorkspacePermission(
                    ownerRole,
                    permissions);

            workspacePermissions.add(workspacePermission);

            if (permissions.getWorkspaceRoleNames().contains(WorkspaceRoleName.ROLE_ADMIN)){
                workspacePermissions.add(new WorkspacePermission(adminRole,permissions));
            }

            if (permissions.getWorkspaceRoleNames().contains(WorkspaceRoleName.ROLE_MEMBER)){
                workspacePermissions.add(new WorkspacePermission(memberRole,permissions));
            }

            if (permissions.getWorkspaceRoleNames().contains(WorkspaceRoleName.ROLE_GUEST)){
                workspacePermissions.add(new WorkspacePermission(guestRole,permissions));
            }
        }
        workspacePermissonRepository.saveAll(workspacePermissions);


        return new ApiResponse("Workspace saqlandi", true);
    }


    public ApiResponse editWorkspace(WorkspaceDto workspaceDto) {
        return null;
    }


    public ApiResponse changeOwnerWorkspace(Long id, UUID ownerId) {
        return new ApiResponse();
    }


    public ApiResponse deleteWorkspace(Long id) {
        try {
            workspaceRepository.deleteById(id);
            return new ApiResponse("Ochirildi", true);
        } catch (Exception e) {
            return new ApiResponse("Xatolik", false);
        }

    }



    public ApiResponse addOrEditOrRemoveWorkspace(Long id, MemberDto memberDto) {
        if(memberDto.getAddType().equals(AddType.ADD)){
            WorkspaceUser workspaceUser = new WorkspaceUser(
                    workspaceRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("id")),
                    userRepository.findById(memberDto.getId()).orElseThrow(()->new ResourceNotFoundException("User")),
                    workspaceRoleRepository.findById(memberDto.getRoleId()).orElseThrow(()->new ResourceNotFoundException("Role")),
                    new Timestamp(System.currentTimeMillis()),
                    null
            );

            workspaceUserRepository.save(workspaceUser);

            // To do  emailga invite habar yuvorish

        }else if(memberDto.getAddType().equals(AddType.EDIT)){
            WorkspaceUser workspaceUser = workspaceUserRepository.findByWorkspaceIdAndUserId(id,memberDto.getId()).orElseGet(WorkspaceUser::new);
            workspaceUser.setWorkspaceRole(workspaceRoleRepository.findById(memberDto.getRoleId()).orElseThrow(()->new ResourceNotFoundException("Role")));

            workspaceUserRepository.save(workspaceUser);

        }else if(memberDto.getAddType().equals(AddType.REMOVE)){

            workspaceUserRepository.deleteByWorkspaceIdAndUserId(id,memberDto.getId());
        }

        return new ApiResponse("Successsssss",true);
    }

    public ApiResponse joinWorkspace(Long id, User user) {
        Optional<WorkspaceUser> optionalWorkspaceUser = workspaceUserRepository.findByWorkspaceIdAndUserId(id, user.getId());
        if (optionalWorkspaceUser.isPresent()){
            WorkspaceUser workspaceUser = optionalWorkspaceUser.get();
            workspaceUser.setDateJoined(new Timestamp(System.currentTimeMillis()));

            workspaceUserRepository.save(workspaceUser);
            return new ApiResponse("Qoshildi",true);
        }

        return new ApiResponse("Xatolik",false);
    }
}
