package uz.pdp.appclickup.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.pdp.appclickup.dto.ApiResponse;
import uz.pdp.appclickup.dto.ProjectDto;
import uz.pdp.appclickup.dto.ProjectMemberDto;
import uz.pdp.appclickup.entity.*;
import uz.pdp.appclickup.entity.enums.AddType;
import uz.pdp.appclickup.repository.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProjectSevice {
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    SpaceRepository spaceRepository;
    @Autowired
    AccesTypeRepository accesTypeRepository;
    @Autowired
    ProjectUserRepository projectUserRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    SpaceUserRepository spaceUserRepository;


    public ApiResponse addProject(ProjectDto projectDto) {

        if (!projectRepository.existsByNameAndSpaceId(projectDto.getName(), projectDto.getSpaceId())){
            return new ApiResponse("Bunday folder mavjud", false);
        }

        Optional<Space> optionalSpace = spaceRepository.findById(projectDto.getSpaceId());
        if (!optionalSpace.isPresent()) {
            return new ApiResponse("Bunday space mavjud emas", false);
        }

        Optional<AccesTypes> optionalAccesTypes = accesTypeRepository.findById(projectDto.getAccesTypeId());
        if (!optionalAccesTypes.isPresent()) {
            return new ApiResponse("Bunday acces type mavjud emas", false);
        }

        Project project = new Project(
                projectDto.getName(),
                projectDto.getColor(),
                optionalSpace.get(),
                optionalAccesTypes.get(),
                projectDto.isArchived());

        projectRepository.save(project);

        return new ApiResponse("Folder saqlandi", true);
    }

    public ApiResponse editProject(ProjectDto projectDto, UUID id) {
        if (!projectRepository.existsByNameAndSpaceId(projectDto.getName(), projectDto.getSpaceId())){
            return new ApiResponse("Bunday folder mavjud", false);
        }

        Optional<Project> optionalProject = projectRepository.findById(id);
        if (!optionalProject.isPresent()) {
            return new ApiResponse("Bunday folder mavjud emas", false);
        }

        Optional<Space> optionalSpace = spaceRepository.findById(projectDto.getSpaceId());
        if (!optionalSpace.isPresent()) {
            return new ApiResponse("Bunday space mavjud emas", false);
        }

        Optional<AccesTypes> optionalAccesTypes = accesTypeRepository.findById(projectDto.getAccesTypeId());
        if (!optionalAccesTypes.isPresent()) {
            return new ApiResponse("Bunday acces type mavjud emas", false);
        }

        Project project = optionalProject.get();
        project.setName(projectDto.getName());
        project.setColor(projectDto.getColor());
        project.setSpace(optionalSpace.get());
        project.setAccesTypes(optionalAccesTypes.get());
        project.setArchived(projectDto.isArchived());

        projectRepository.save(project);


        return new ApiResponse("Folder o'zgartirildi", true);
    }


    public List<Project> getProjectsBySpace(Long id) {
        if (spaceRepository.existsById(id)) {
            List<Project> projectList = projectRepository.findAllBySpaceId(id);
            List<Project> projects = new ArrayList<>();

            for (Project project : projectList) {
                Project p = new Project(
                        project.getName(),
                        project.getColor(),
                        project.getAccesTypes(),
                        project.isArchived());
                projects.add(p);
            }

            return projects;
        }
        return null;
    }

    public ApiResponse deleteProject(UUID id) {
        try {
            projectRepository.deleteById(id);
            return new ApiResponse("Folder o'chirildi", true);
        } catch (Exception e) {
            return new ApiResponse("Error" + e.getMessage(), false);
        }
    }

    public ApiResponse addProjectMember(ProjectMemberDto projectMemberDto) {
        if (projectMemberDto.getAddType().equals(AddType.ADD)) {

            Optional<User> optionalUser = userRepository.findById(projectMemberDto.getUserId());
            if (!optionalUser.isPresent()) {
                return new ApiResponse("Bunday user royxatdan otmagan", false);
            }

            Optional<Project> optionalProject = projectRepository.findById(projectMemberDto.getProjectId());
            if (!optionalProject.isPresent()) {
                return new ApiResponse("Bunday folder mavjud emas", false);
            }

            if (!spaceUserRepository.existsByUserId(projectMemberDto.getUserId())) {
                return new ApiResponse("Bunday user spaceda mavjud emas", false);
            }


            ProjectUser projectUser = new ProjectUser(optionalProject.get(), optionalUser.get());
            projectUserRepository.save(projectUser);

            return new ApiResponse("Folerga member qoshildi", true);
        }
//        else if (projectMemberDto.getAddType().equals(AddType.EDIT)) {}
        else if (projectMemberDto.getAddType().equals(AddType.REMOVE)) {
            try {
                projectUserRepository.deleteByUserIdAndProjectId(projectMemberDto.getUserId(), projectMemberDto.getProjectId());
                return new ApiResponse("Folderdan user ochirildi", true);
            } catch (Exception e) {
                return new ApiResponse("Folderda userni ochirishda error", false);
            }
        } else {
            return new ApiResponse("Errorr", false);
        }
    }
}
