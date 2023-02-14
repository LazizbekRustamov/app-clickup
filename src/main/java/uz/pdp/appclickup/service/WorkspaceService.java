package uz.pdp.appclickup.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import uz.pdp.appclickup.dto.ApiResponse;
import uz.pdp.appclickup.dto.MemberDto;
import uz.pdp.appclickup.dto.WorkspaceDto;
import uz.pdp.appclickup.dto.WorkspaceRoleDto;
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

            if (permissions.getWorkspaceRoleNames().contains(WorkspaceRoleName.ROLE_ADMIN)) {
                workspacePermissions.add(new WorkspacePermission(adminRole, permissions));
            }

            if (permissions.getWorkspaceRoleNames().contains(WorkspaceRoleName.ROLE_MEMBER)) {
                workspacePermissions.add(new WorkspacePermission(memberRole, permissions));
            }

            if (permissions.getWorkspaceRoleNames().contains(WorkspaceRoleName.ROLE_GUEST)) {
                workspacePermissions.add(new WorkspacePermission(guestRole, permissions));
            }
        }
        workspacePermissonRepository.saveAll(workspacePermissions);


        return new ApiResponse("Workspace saqlandi", true);
    }


    public ApiResponse editWorkspace(Long id, WorkspaceDto workspaceDto) {
        Optional<Workspace> optionalWorkspace = workspaceRepository.findById(id);
        if (!optionalWorkspace.isPresent()) {
            return new ApiResponse("Bunday workspace mavjud emas", false);
        }
        Workspace workspace = optionalWorkspace.get();
        workspace.setName(workspaceDto.getName());
        workspace.setColor(workspaceDto.getColor());
        workspace.setAvatar(workspaceDto.getAvatarId() == null ? null : attachmentRepository.findById(workspaceDto.getAvatarId()).orElseThrow(() -> new ResourceNotFoundException("Attachment")));

        workspaceRepository.save(workspace);

        return new ApiResponse("ozgartirildi",true);
    }


    public ApiResponse changeOwnerWorkspace(Long id, UUID newOwnerId) {
        Optional<Workspace> optionalWorkspace = workspaceRepository.findById(id);
        if (!optionalWorkspace.isPresent()) {
            return new ApiResponse("Bunday workspace mavjud emas", false);
        }

        Optional<User> optionalUser = userRepository.findById(newOwnerId);
        if (!optionalUser.isPresent()) {
            return new ApiResponse("Yangi owner uchun user mavjud emas", false);
        }

        // Workspaceda ownerni ozgartirdm
        Workspace workspace = optionalWorkspace.get();
        UUID originalOwner = workspace.getOwner().getId();
        workspace.setOwner(optionalUser.get());

        workspaceRepository.save(workspace);


        // Eski ownerni workspaceUser obyektini oldim
        Optional<WorkspaceUser> originalWorkspaceUser = workspaceUserRepository.findByUserId(originalOwner);


        // Yangi owner workspaceUser da bolmasa qoshamiz keyin esa unga eski ownerni huquqlarini biriktiramz
        Optional<WorkspaceUser> optionalWorkspaceUser = workspaceUserRepository.findByUserId(workspace.getOwner().getId());
        if (!optionalWorkspaceUser.isPresent()) {
            WorkspaceUser workspaceUser = new WorkspaceUser(
                    workspace,
                    optionalUser.get(),
                    originalWorkspaceUser.get().getWorkspaceRole(),  // Bu user aniq bor chunki workspace owner nullable false
                    new Timestamp(System.currentTimeMillis()),
                    null                                        /* email ni bilib joinWorkspacega yuvorish kere */
            );

            workspaceUserRepository.save(workspaceUser);

        } else {
            // Eski ownerni  owner rolini yangi ownerga berdm
            WorkspaceUser workspaceUser = optionalWorkspaceUser.get();
            workspaceUser.setWorkspaceRole(originalWorkspaceUser.get().getWorkspaceRole());

            workspaceUserRepository.save(workspaceUser);
        }


        // Endi eski ownerdan owner rolini olb tawladm
        Optional<WorkspaceRole> optionalRoleName = workspaceRoleRepository.findByRoleName(WorkspaceRoleName.ROLE_MEMBER.name());
        if (!optionalRoleName.isPresent()){
            return new ApiResponse("Eski owner uchun "+WorkspaceRoleName.ROLE_MEMBER.name()+" mavjud emas",false);
        }

        WorkspaceUser orgWorkspaceUser = originalWorkspaceUser.get();
        orgWorkspaceUser.setWorkspaceRole(optionalRoleName.get());

        workspaceUserRepository.save(orgWorkspaceUser);


        return new ApiResponse("Owner ozgartirildi",true);
    }


    public ApiResponse deleteWorkspace(Long id) {
        try {
            workspaceRepository.deleteById(id);
            return new ApiResponse("Ochirildi", true);
        } catch (Exception e) {
            return new ApiResponse("Xatolik", false);
        }

    }


    public ApiResponse addOrEditOrRemoveWorkspaceUser(Long id, MemberDto memberDto) {
        if (memberDto.getAddType().equals(AddType.ADD)) {
            WorkspaceUser workspaceUser = new WorkspaceUser(
                    workspaceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("id")),
                    userRepository.findById(memberDto.getId()).orElseThrow(() -> new ResourceNotFoundException("User")),
                    workspaceRoleRepository.findById(memberDto.getRoleId()).orElseThrow(() -> new ResourceNotFoundException("Role")),
                    new Timestamp(System.currentTimeMillis()),
                    null
            );

            workspaceUserRepository.save(workspaceUser);

            // To do  emailga invite habar yuvorish

        } else if (memberDto.getAddType().equals(AddType.EDIT)) {
            WorkspaceUser workspaceUser = workspaceUserRepository.findByWorkspaceIdAndUserId(id, memberDto.getId()).orElseGet(WorkspaceUser::new);
            workspaceUser.setWorkspaceRole(workspaceRoleRepository.findById(memberDto.getRoleId()).orElseThrow(() -> new ResourceNotFoundException("Role")));

            workspaceUserRepository.save(workspaceUser);

        } else if (memberDto.getAddType().equals(AddType.REMOVE)) {

            workspaceUserRepository.deleteByWorkspaceIdAndUserId(id, memberDto.getId());
        }

        return new ApiResponse("Successsssss", true);
    }


    public ApiResponse joinWorkspace(Long id, User user) {
        Optional<WorkspaceUser> optionalWorkspaceUser = workspaceUserRepository.findByWorkspaceIdAndUserId(id, user.getId());
        if (optionalWorkspaceUser.isPresent()) {
            WorkspaceUser workspaceUser = optionalWorkspaceUser.get();
            workspaceUser.setDateJoined(new Timestamp(System.currentTimeMillis()));

            workspaceUserRepository.save(workspaceUser);
            return new ApiResponse("Qoshildi", true);
        }

        return new ApiResponse("Xatolik", false);
    }


    public List<MemberDto> getMemberAndGuest(Long id) {
        List<WorkspaceUser> allByWorkspaceId = workspaceUserRepository.findAllByWorkspaceId(id);
        List<MemberDto> members = new ArrayList<>();

        for (WorkspaceUser workspaceUser : allByWorkspaceId) {
            MemberDto memberDto = mapWorkspaceUserToMemberDto(workspaceUser);
            members.add(memberDto);
        }
        return members;
    }


    public List<WorkspaceDto> getMyWorkspaces(User user) {
        List<WorkspaceUser> allByUserId = workspaceUserRepository.findAllByUserId(user.getId());
        List<WorkspaceDto> workspaces = new ArrayList<>();

        for (WorkspaceUser workspaceUser : allByUserId) {
            WorkspaceDto workspaceDto = mapWorkspaceUserToWorkspaceDto(workspaceUser);
            workspaces.add(workspaceDto);
        }
        return workspaces;

        // yoki forni bu ornini bosadi
//        return allByUserId.stream().map(workspaceUser -> mapWorkspaceUserToWorkspaceDto(workspaceUser)).collect(Collectors.toList());
    }


    public ApiResponse addOrRemovePermission(WorkspaceRoleDto workspaceRoleDto) {
        WorkspaceRole workspaceRole = workspaceRoleRepository.findById(workspaceRoleDto.getId()).orElseThrow(() -> new ResourceNotFoundException("workspace role"));
        Optional<WorkspacePermission> optionalWorkspacePermission = workspacePermissonRepository.findByWorkspaceRoleIdAndWorkspacePermissionName(workspaceRoleDto.getId(), workspaceRoleDto.getPermissionName());

        if (workspaceRoleDto.getAddType().equals(AddType.ADD)) {
            if (optionalWorkspacePermission.isPresent()) {
                return new ApiResponse("Bunday huquq mavjud", false);
            }
            WorkspacePermission workspacePermission = new WorkspacePermission(workspaceRole, workspaceRoleDto.getPermissionName());

            workspacePermissonRepository.save(workspacePermission);
            return new ApiResponse("Qo'shildi", true);
        } else if (workspaceRoleDto.getAddType().equals(AddType.REMOVE)) {
            if (optionalWorkspacePermission.isPresent()) {
                workspacePermissonRepository.delete(optionalWorkspacePermission.get());
                return new ApiResponse("O'chirildi", true);
            }
        }

        return new ApiResponse("Xatolik", false);
    }


    public ApiResponse addWorkspaceRole(Long workspaceId, WorkspaceRoleDto workspaceRoleDto, User user) {
        if (workspaceRoleRepository.existsByWorkspaceIdAndRoleName(workspaceId, workspaceRoleDto.getName())) {
            return new ApiResponse("Bunday role allaqachon mavjud", false);
        }

        WorkspaceRole workspaceRole = new WorkspaceRole();
        workspaceRole.setWorkspace(workspaceRepository.findById(workspaceId).orElseThrow(() -> new ResourceNotFoundException("workspace_id")));
        workspaceRole.setRoleName(workspaceRoleDto.getName());
        workspaceRole.setExtendsRole(workspaceRoleDto.getExtendsRole());

        workspaceRoleRepository.save(workspaceRole);

        List<WorkspacePermission> workspacePermissions = workspacePermissonRepository.findByWorkspaceRole_RoleNameAndWorkspaceRole_WorkspaceId(workspaceRole.getExtendsRole().name(), workspaceId);
        List<WorkspacePermission> newWorkspacePermissions = new ArrayList<>();
        for (WorkspacePermission workspacePermission : workspacePermissions) {
            WorkspacePermission newWorkspacePermission = new WorkspacePermission(
                    workspaceRole,
                    workspacePermission.getWorkspacePermissionName()
            );

            newWorkspacePermissions.add(newWorkspacePermission);
        }

        workspacePermissonRepository.saveAll(newWorkspacePermissions);

        return new ApiResponse("Saqlandi", true);
    }


    //  My method
    public MemberDto mapWorkspaceUserToMemberDto(WorkspaceUser workspaceUser) {
        MemberDto memberDto = new MemberDto();
        memberDto.setId(workspaceUser.getId());
        memberDto.setEmail(workspaceUser.getUser().getEmail());
        memberDto.setFullName(workspaceUser.getUser().getFullName());
        memberDto.setRoleName(workspaceUser.getWorkspaceRole().getRoleName());
        memberDto.setLastActiveTime(workspaceUser.getUser().getLastActiveTime());

        return memberDto;
    }


    public WorkspaceDto mapWorkspaceUserToWorkspaceDto(WorkspaceUser workspaceUser) {
        WorkspaceDto workspaceDto = new WorkspaceDto();
        workspaceDto.setId(workspaceUser.getWorkspace().getId());
        workspaceDto.setInitialLetter(workspaceUser.getWorkspace().getInitialLetter());
        workspaceDto.setName(workspaceUser.getWorkspace().getName());
        workspaceDto.setAvatarId(workspaceUser.getWorkspace().getAvatar() == null ? null : workspaceUser.getWorkspace().getAvatar().getId());
        workspaceDto.setColor(workspaceUser.getWorkspace().getColor());

        return workspaceDto;
    }
}
