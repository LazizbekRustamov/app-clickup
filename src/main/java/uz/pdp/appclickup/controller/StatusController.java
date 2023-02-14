package uz.pdp.appclickup.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.pdp.appclickup.dto.ApiResponse;
import uz.pdp.appclickup.dto.StatusDto;
import uz.pdp.appclickup.entity.Status;
import uz.pdp.appclickup.service.StatusService;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/status")
public class StatusController {

    @Autowired
    StatusService statusService;


    @PostMapping("/addStatus")
    private HttpEntity<?> addStatus(@Valid @RequestBody StatusDto statusDto) {
        ApiResponse apiResponse = statusService.addStatus(statusDto);
        return ResponseEntity.status(apiResponse.isSucces() ? 200 : 409).body(apiResponse);
    }

    @PutMapping("/editStatus/{id}")
    private HttpEntity<?> editStatus(@Valid @RequestBody StatusDto statusDto, @PathVariable UUID id) {
        ApiResponse apiResponse = statusService.editStatus(statusDto,id);
        return ResponseEntity.status(apiResponse.isSucces() ? 200 : 409).body(apiResponse);
    }

    @GetMapping("/getStatusesByCategory/{id}")
    private HttpEntity<?> getStatusesByCategoryID(@PathVariable UUID id) {
        List<Status> statuses = statusService.getStatusesByCategoryId(id);
        return ResponseEntity.ok(statuses);
    }

    @DeleteMapping("/delete/{id}")
    private HttpEntity<?> deleteStatus(@PathVariable UUID id) {
        ApiResponse apiResponse = statusService.deleteStatus(id);
        return ResponseEntity.status(apiResponse.isSucces() ? 200 : 409).body(apiResponse);
    }
}
