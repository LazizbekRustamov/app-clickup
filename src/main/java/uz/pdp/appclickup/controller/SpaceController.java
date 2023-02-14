package uz.pdp.appclickup.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appclickup.dto.ApiResponse;
import uz.pdp.appclickup.dto.ProjectMemberDto;
import uz.pdp.appclickup.dto.SpaceDto;
import uz.pdp.appclickup.dto.SpaceMemberDto;
import uz.pdp.appclickup.entity.Space;
import uz.pdp.appclickup.entity.User;
import uz.pdp.appclickup.security.CurrentUser;
import uz.pdp.appclickup.service.SpaceService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/space")
public class SpaceController {

    @Autowired
    SpaceService spaceService;


    @PostMapping("/addSpace")
    private HttpEntity<?> addSpace(@RequestBody SpaceDto spaceDto, @CurrentUser User user){
        ApiResponse apiResponse = spaceService.addSpace(spaceDto,user);
        return ResponseEntity.status(apiResponse.isSucces()?200:409).body(apiResponse);
    }

    @GetMapping("/getMySpace")
    private HttpEntity<?> getMySpace(@CurrentUser User user){
        List<Space> spaces = spaceService.getMySpace(user);
        return ResponseEntity.ok(spaces);
    }



    @GetMapping("/getAllSpaces")
    private HttpEntity<?> getAllSpaces(@RequestParam Long id){
        List<Space> spaces = spaceService.getAllSpaces(id);
        return ResponseEntity.status(spaces == null ?409:200).body(spaces);
    }


    @DeleteMapping("/delete/{id}")
    private HttpEntity<?> deleteSpace(@PathVariable Long id){
        ApiResponse apiResponse = spaceService.deleteSpace(id);
        return ResponseEntity.status(apiResponse.isSucces()?200:409).body(apiResponse);
    }


    @PutMapping("/edit/{id}")
    private HttpEntity<?> editSpace(@PathVariable Long id,@RequestBody SpaceDto spaceDto){
        ApiResponse apiResponse = spaceService.editSpace(id,spaceDto);
        return ResponseEntity.status(apiResponse.isSucces()?200:409).body(apiResponse);
    }


    @PostMapping("/addOrEditOrDeleteSpaceMember")  /**/
    private HttpEntity<?> addEditRevomeSpaceMember(@Valid @RequestBody SpaceMemberDto spaceMemberDto) {
        ApiResponse apiResponse = spaceService.addSpaceMember(spaceMemberDto);
        return ResponseEntity.status(apiResponse.isSucces() ? 200 : 409).body(apiResponse);
    }
}
