package uz.pdp.appclickup.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import uz.pdp.appclickup.dto.ApiResponse;
import uz.pdp.appclickup.dto.SpaceDto;
import uz.pdp.appclickup.dto.SpaceMemberDto;
import uz.pdp.appclickup.entity.*;
import uz.pdp.appclickup.entity.enums.AddType;
import uz.pdp.appclickup.repository.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SpaceService {
    @Autowired
    SpaceRepository spaceRepository;
    @Autowired
    ClickAppsRepository clickAppsRepository;
    @Autowired
    ViewRepository viewRepository;
    @Autowired
    WorkspaceRepository workspaceRepository;
    @Autowired
    AttachmentRepository attachmentRepository;
    @Autowired
    AccesTypeRepository accesTypeRepository;
    @Autowired
    WorkspaceUserRepository workspaceUserRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    SpaceUserRepository spaceUserRepository;


    public ApiResponse addSpace(SpaceDto spaceDto, User user) {
        if (spaceRepository.existsByWorkspaceIdAndName(spaceDto.getWorkspaceId(), spaceDto.getName())) {
            return new ApiResponse("Bunday space allaqachon mavjud", false);
        }

        Optional<Workspace> optionalWorkspace = workspaceRepository.findById(spaceDto.getWorkspaceId());
        if (!optionalWorkspace.isPresent()) {
            return new ApiResponse("Mavjud bolmagan ishxonaga bo'lim(space) qoshib bolmaydi", false);
        }

        Optional<AccesTypes> optionalAccesTypes = accesTypeRepository.findById(spaceDto.getAccesType_id());
        if (!optionalAccesTypes.isPresent()) {
            return new ApiResponse("aaaaaaaaa", false);
        }

        List<View> views = new ArrayList<>();
        for (View view : spaceDto.getViews()) {
            Optional<View> optionalView = viewRepository.findByViews(view.getViews());
            optionalView.ifPresent(views::add);
        }

        List<ClickApps> clickApps = new ArrayList<>();
        for (ClickApps clickApp : spaceDto.getClickApps()) {
            Optional<ClickApps> optionalClickApps = clickAppsRepository.findByClickAppsEnum(clickApp.getClickAppsEnum());
            optionalClickApps.ifPresent(clickApps::add);
        }


        Space space = new Space();
        space.setName(spaceDto.getName());
        space.setColor(spaceDto.getColor());
        space.setWorkspace(optionalWorkspace.get());
        space.setAvatar(spaceDto.getAvatarId() == null ? null : attachmentRepository.findById(spaceDto.getAvatarId()).orElseThrow(() -> new ResourceNotFoundException("Avatar")));
        space.setAccesTypes(optionalAccesTypes.get());
        space.setView(views);
        space.setClickApps(clickApps);
        space.setOwner(user);

        spaceRepository.save(space);

        return new ApiResponse("Saqlandi", true);
    }


    public List<Space> getMySpace(User user) {
        try {
            List<Space> spaces = spaceRepository.findAllByOwnerId(user.getId());
            List<Space> spaceList = new ArrayList<>();

            for (Space space : spaces) {
                Space s = new Space(
                        space.getName(),
                        space.getColor(),
                        space.getInitialLetter(),
                        space.getAvatar(),
                        space.getAccesTypes()
                );
                spaceList.add(s);
            }

            return spaceList;
        } catch (Exception e) {
            return null;
        }
    }


    public List<Space> getAllSpaces(Long id) {
        if (workspaceRepository.existsById(id)) {
            List<Space> spaces = spaceRepository.findByWorkspaceId(id);
            List<Space> secSpace = new ArrayList<>();


            for (Space space : spaces) {
                Space s = new Space(
                        space.getName(),
                        space.getColor(),
                        space.getInitialLetter(),
                        space.getAvatar(),
                        space.getAccesTypes());
                secSpace.add(s);
            }

            return secSpace;
        }
        return null;
    }


    public ApiResponse deleteSpace(Long id) {
        if (spaceRepository.existsById(id)) {
            spaceRepository.deleteById(id);
            return new ApiResponse("O'chirildi", true);
        }
        return new ApiResponse("Bunday space mavjud emas", false);
    }


    public ApiResponse editSpace(Long id, SpaceDto spaceDto) {

        if (spaceRepository.existsByWorkspaceIdAndName(spaceDto.getWorkspaceId(), spaceDto.getName())) {
            return new ApiResponse("Bunday space allaqachon mavjud", false);
        }

        Optional<Space> optionalSpace = spaceRepository.findById(id);
        if (!optionalSpace.isPresent()) {
            return new ApiResponse("Bunday space mavjud emas", false);
        }


        Optional<AccesTypes> optionalAccesTypes = accesTypeRepository.findById(spaceDto.getAccesType_id());
        if (!optionalAccesTypes.isPresent()){
            return new ApiResponse("truuuuuuuuuuuuuuuuu",false);
        }


        List<View> views = new ArrayList<>();
        for (View view : spaceDto.getViews()) {
            Optional<View> optionalView = viewRepository.findByViews(view.getViews());
            optionalView.ifPresent(views::add);
        }

        List<ClickApps> clickApps = new ArrayList<>();
        for (ClickApps clickApp : spaceDto.getClickApps()) {
            Optional<ClickApps> optionalClickApps = clickAppsRepository.findByClickAppsEnum(clickApp.getClickAppsEnum());
            optionalClickApps.ifPresent(clickApps::add);
        }


        Space space = optionalSpace.get();
        space.setName(spaceDto.getName());
        space.setColor(spaceDto.getColor());
        space.setAvatar(spaceDto.getAvatarId() == null ? null : attachmentRepository.findById(spaceDto.getAvatarId()).orElseThrow(() -> new ResourceNotFoundException("Avatar")));
        space.setAccesTypes(optionalAccesTypes.get());
        space.setView(views);
        space.setClickApps(clickApps);

        spaceRepository.save(space);

        return new ApiResponse("O'zgartirildi", true);
    }

    public ApiResponse addSpaceMember(SpaceMemberDto spaceMemberDto) {
        if (spaceMemberDto.getAddType().equals(AddType.ADD)) {

            Optional<Space> optionalSpace = spaceRepository.findById(spaceMemberDto.getSpaceId());
            if(!optionalSpace.isPresent()){
                return new ApiResponse("Bunday space mavjud emas",false);
            }

            Optional<User> optionalUser = userRepository.findById(spaceMemberDto.getUserId());
            if(!optionalUser.isPresent()){
                return new ApiResponse("Bunday user royxatdan otmagan",false);
            }

            if (!workspaceUserRepository.existsByUserId(spaceMemberDto.getUserId())){
                return new ApiResponse("Workspacega buday user qoshilmagan",false);
            }

            SpaceUser spaceUser = new SpaceUser(
                    optionalSpace.get(),
                    optionalUser.get());
            spaceUserRepository.save(spaceUser);


            return new ApiResponse("Spacega member qoshildi",true);
        }
//        else if (projectMemberDto.getAddType().equals(AddType.EDIT)) {}
        else if (spaceMemberDto.getAddType().equals(AddType.REMOVE)) {
            try {
                spaceUserRepository.deleteByUserIdAndSpaceId(spaceMemberDto.getUserId(), spaceMemberDto.getSpaceId());
                return new ApiResponse("Spacedan member ochirildi", true);
            }catch (Exception e){
                return new ApiResponse("O'chirishda error", false);
            }
        } else {
            return new ApiResponse("Errorr", false);
        }
    }
}
