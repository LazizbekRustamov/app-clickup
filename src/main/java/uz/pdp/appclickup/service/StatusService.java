package uz.pdp.appclickup.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.pdp.appclickup.dto.ApiResponse;
import uz.pdp.appclickup.dto.StatusDto;
import uz.pdp.appclickup.entity.*;
import uz.pdp.appclickup.repository.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class StatusService {
    @Autowired
    StatusRepository statusRepository;
    @Autowired
    SpaceRepository spaceRepository;
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    TypeRepository typeRepository;


    public ApiResponse addStatus(StatusDto statusDto) {

        if (statusRepository.existsByNameAndCategoryId(statusDto.getName(), statusDto.getCategoryId())) {
            return new ApiResponse("Bunday status bu categoriyada mavjud", false);
        }

//        Optional<Space> optionalSpace = spaceRepository.findById(statusDto.getSpaceId());
//        if (!optionalSpace.isPresent()) {
//            return new ApiResponse("Bunday space mavjud emas", false);
//        }
//
        /* ************  foler space larn i olb tekshirishni orniga bitta categoriyani tekshirib olib undan space bn folder yani projectni olsa boladi  ************ */
//
//        Optional<Project> optionalProject = projectRepository.findById(statusDto.getProjectId());
//        if (!optionalProject.isPresent()) {
//            return new ApiResponse("Bunday folder mavjud emas", false);
//        }

        Optional<Type> typeOptional = typeRepository.findById(statusDto.getTypesId());
        if (!typeOptional.isPresent()) {
            return new ApiResponse("Bunday type mavjud emas", false);
        }

        Optional<Category> optionalCategory = categoryRepository.findById(statusDto.getCategoryId());
        if (!optionalCategory.isPresent()) {
            return new ApiResponse("Bunday categoriya mavjud emas", false);
        }


        Status status = new Status(
                statusDto.getName(),
                statusDto.getColor(),
                optionalCategory.get().getProject().getSpace(),
                optionalCategory.get().getProject(),
                optionalCategory.get(),
                typeOptional.get());
        statusRepository.save(status);


        return new ApiResponse("Status saqlandi", true);
    }


    public ApiResponse editStatus(StatusDto statusDto, UUID id) {

        if (statusRepository.existsByNameAndCategoryId(statusDto.getName(), statusDto.getCategoryId())) {
            return new ApiResponse("Bunday status bu categoriyada mavjud", false);
        }

        Optional<Status> optionalStatus = statusRepository.findById(id);
        if (!optionalStatus.isPresent()) {
            return new ApiResponse("Bunday status mavjud emas", false);
        }

        Optional<Type> typeOptional = typeRepository.findById(statusDto.getTypesId());
        if (!typeOptional.isPresent()) {
            return new ApiResponse("Bunday type mavjud emas", false);
        }

        Optional<Category> optionalCategory = categoryRepository.findById(statusDto.getCategoryId());
        if (!optionalCategory.isPresent()) {
            return new ApiResponse("Bunday categoriya mavjud emas", false);
        }


        Status status = optionalStatus.get();
        status.setName(statusDto.getName());
        status.setColor(statusDto.getColor());
        status.setSpace(optionalCategory.get().getProject().getSpace());
        status.setProject(optionalCategory.get().getProject());
        status.setCategory(optionalCategory.get());
        status.setTypes(typeOptional.get());

        statusRepository.save(status);

        return new ApiResponse("Status o'zgartirildi", true);
    }


    public List<Status> getStatusesByCategoryId(UUID id) {
        if (categoryRepository.existsById(id)) {
            List<Status> statuses = statusRepository.findAllByCategoryId(id);
            List<Status> statusList = new ArrayList<>();

            for (Status status : statuses) {
                Status s = new Status(status.getName(), status.getColor(), status.getTypes());
                statusList.add(s);
            }

            return statusList;
        }
        return null;
    }


    public ApiResponse deleteStatus(UUID id) {
        try {
            statusRepository.deleteById(id);
            return new ApiResponse("Status o'chirildi",true);
        }catch (Exception e){
            return new ApiResponse("O'chirishda xatolik status topilmagan!!!",false);
        }
    }
}
