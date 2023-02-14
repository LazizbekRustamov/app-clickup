package uz.pdp.appclickup.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import uz.pdp.appclickup.dto.ApiResponse;
import uz.pdp.appclickup.dto.CategoryDto;
import uz.pdp.appclickup.dto.CategoryMemberDto;
import uz.pdp.appclickup.entity.*;
import uz.pdp.appclickup.entity.enums.AddType;
import uz.pdp.appclickup.repository.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CategoryService {
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    AccesTypeRepository accesTypeRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ProjectUserRepository projectUserRepository;
    @Autowired
    CategoryUserRepository categoryUserRepository;
    @Autowired
    StatusRepository statusRepository;
    @Autowired
    TypeRepository typeRepository;

    public ApiResponse addCategory(CategoryDto categoryDto) {

        if (!categoryRepository.existsByNameAndProjectId(categoryDto.getName(), categoryDto.getProjectId())) {
            return new ApiResponse("Bunday categoriya mavjud", false);
        }

        Optional<Project> optionalProject = projectRepository.findById(categoryDto.getProjectId());
        if (!optionalProject.isPresent()) {
            return new ApiResponse("Bunday folder mavjud emas", false);
        }

        Optional<AccesTypes> optionalAccesTypes = accesTypeRepository.findById(categoryDto.getAccesTypesId());
        if (!optionalAccesTypes.isPresent()) {
            return new ApiResponse("Bunday acces type mavjud emas", false);
        }


        Category category = new Category(
                categoryDto.getName(),
                categoryDto.getColor(),
                optionalProject.get(),
                optionalAccesTypes.get(),
                categoryDto.isArchived());
        categoryRepository.save(category);

        statusRepository.save(new Status("Yangi Ish", "Qizil", category.getProject().getSpace(),
                category.getProject(), category, typeRepository.findById(1).orElseThrow(() -> new ResourceNotFoundException("Status yoq"))));

        statusRepository.save(new Status("Jarayonda", "Ko'k", category.getProject().getSpace(),
                category.getProject(), category, typeRepository.findById(2).orElseThrow(() -> new ResourceNotFoundException("Status yoq"))));

        statusRepository.save(new Status("Tugallandi", "Yashil", category.getProject().getSpace(),
                category.getProject(), category, typeRepository.findById(3).orElseThrow(() -> new ResourceNotFoundException("Status yoq"))));


        return new ApiResponse("Categoriya saqlandi", true);
    }


    public ApiResponse editCategory(CategoryDto categoryDto, UUID id) {

        if (!categoryRepository.existsByNameAndProjectId(categoryDto.getName(), categoryDto.getProjectId())) {
            return new ApiResponse("Bunday categoriya mavjud", false);
        }

        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (!optionalCategory.isPresent()) {
            return new ApiResponse("Bunday categoriya mavjud emas", false);
        }

        Optional<Project> optionalProject = projectRepository.findById(categoryDto.getProjectId());
        if (!optionalProject.isPresent()) {
            return new ApiResponse("Bunday folder mavjud emas", false);
        }

        Optional<AccesTypes> optionalAccesTypes = accesTypeRepository.findById(categoryDto.getAccesTypesId());
        if (!optionalAccesTypes.isPresent()) {
            return new ApiResponse("Bunday acces type mavjud emas", false);
        }

        Category category = optionalCategory.get();
        category.setName(categoryDto.getName());
        category.setColor(categoryDto.getColor());
        category.setProject(optionalProject.get());
        category.setAccesTypes(optionalAccesTypes.get());
        category.setArchived(categoryDto.isArchived());

        categoryRepository.save(category);

        return new ApiResponse("O'zgartirildi", true);
    }


    public List<Category> getByProjectCategory(UUID id) {
        if (projectRepository.existsById(id)) {
            List<Category> categories = categoryRepository.findAllByProjectId(id);
            List<Category> categoryList = new ArrayList<>();

            for (Category category : categories) {
                if (!category.isArchived()) {
                    Category c = new Category(
                            category.getName(),
                            category.getColor(),
                            category.getAccesTypes(),
                            false
                    );
                    categoryList.add(c);
                }
            }
            return categoryList;
        }
        return null;
    }


    public List<Category> getByProjectArchivedCategory(UUID id) {
        if (projectRepository.existsById(id)) {
            List<Category> categories = categoryRepository.findAllByProjectId(id);
            List<Category> categoryList = new ArrayList<>();

            for (Category category : categories) {
                if (category.isArchived()) {
                    Category c = new Category(
                            category.getName(),
                            category.getColor(),
                            category.getAccesTypes(),
                            true
                    );
                    categoryList.add(c);
                }
            }
            return categoryList;
        }
        return null;
    }


    public ApiResponse deleteCategory(UUID id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
            return new ApiResponse("Categori o'chirildi", true);
        } else {
            return new ApiResponse("Erorr", false);
        }
    }


    public ApiResponse addCategoryMember(CategoryMemberDto categoryMemberDto) {
        if (categoryMemberDto.getAddType().equals(AddType.ADD)) {

            Optional<User> optionalUser = userRepository.findById(categoryMemberDto.getUserId());
            if (!optionalUser.isPresent()) {
                return new ApiResponse("Bunday user royxatdan otmagan", false);
            }

            Optional<Category> optionalCategory = categoryRepository.findById(categoryMemberDto.getCategoryId());
            if (!optionalCategory.isPresent()) {
                return new ApiResponse("Bunday categori mavjud emas", false);
            }

            if (!projectUserRepository.existsByUserId(categoryMemberDto.getUserId())) {
                return new ApiResponse("Bunday user folderda mavjud emas", false);
            }

            CategoryUser categoryUser = new CategoryUser(optionalCategory.get(), optionalUser.get());
            categoryUserRepository.save(categoryUser);

            return new ApiResponse("User categoriyaga qoshildi", true);
        }
//        else if (categoryMemberDto.getAddType().equals(AddType.EDIT)) {}
        else if (categoryMemberDto.getAddType().equals(AddType.REMOVE)) {
            try {
                categoryUserRepository.deleteByUserIdAndCategoryId(categoryMemberDto.getUserId(), categoryMemberDto.getCategoryId());
                return new ApiResponse("Categorydan user ochirildi", true);
            } catch (Exception e) {
                return new ApiResponse("Categoryda userni ochirishda error", false);
            }
        } else {
            return new ApiResponse("Errorr", false);
        }
    }
}
