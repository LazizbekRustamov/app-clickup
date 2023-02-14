package uz.pdp.appclickup.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appclickup.dto.*;
import uz.pdp.appclickup.entity.Category;
import uz.pdp.appclickup.service.CategoryService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/category")
public class CategoryController {
    @Autowired
    CategoryService categoryService;


    @PostMapping("/addCategory")
    private HttpEntity<?> addCategory(@Valid @RequestBody CategoryDto categoryDto) {
        ApiResponse apiResponse = categoryService.addCategory(categoryDto);
        return ResponseEntity.status(apiResponse.isSucces() ? 200 : 409).body(apiResponse);
    }


    @PutMapping("/editCategory/{id}")
    private HttpEntity<?> editCategory(@Valid @RequestBody CategoryDto categoryDto, @PathVariable UUID id) {
        ApiResponse apiResponse = categoryService.editCategory(categoryDto, id);
        return ResponseEntity.status(apiResponse.isSucces() ? 200 : 409).body(apiResponse);
    }


    @GetMapping("/getCategory/{id}")
    private HttpEntity<?> getCategory(@PathVariable UUID id) {
        List<Category> category = categoryService.getByProjectCategory(id);
        return ResponseEntity.ok(category);
    }


    @GetMapping("/getArchivedCategory/{id}")
    private HttpEntity<?> getArchivedCategory(@PathVariable UUID id) {
        List<Category> category = categoryService.getByProjectArchivedCategory(id);
        return ResponseEntity.ok(category);
    }


    @DeleteMapping("/delete/{id}")
    private HttpEntity<?> deleteCategory(@PathVariable UUID id) {
        ApiResponse apiResponse = categoryService.deleteCategory(id);
        return ResponseEntity.status(apiResponse.isSucces() ? 200 : 409).body(apiResponse);
    }


    @PostMapping("/addOrEditOrDeleteCategoryMember")  /**/
    private HttpEntity<?> addCategoryMember(@Valid @RequestBody CategoryMemberDto categoryMemberDto) {
        ApiResponse apiResponse = categoryService.addCategoryMember(categoryMemberDto);
        return ResponseEntity.status(apiResponse.isSucces() ? 200 : 409).body(apiResponse);
    }
}
