package uz.pdp.appclickup.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import uz.pdp.appclickup.dto.ApiResponse;
import uz.pdp.appclickup.entity.Attachment;
import uz.pdp.appclickup.service.AttachmentService;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/attachment")
public class AttachmentController {

    @Autowired
    AttachmentService attachmentService;

    @PostMapping("/upload")
    public HttpEntity<?> saveAttachment(@RequestParam("file") MultipartFile file) {
        Attachment attachment = attachmentService.saveAttachment(file);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(attachment.getId()).toUri();

        return ResponseEntity.created(location).body(attachment);
    }


    @GetMapping("/{id}")
    public HttpEntity<?> getAttachment(@PathVariable UUID id) {
        Attachment attachment = attachmentService.getAttachment(id);
        return ResponseEntity.ok(attachment);
    }


    @GetMapping
    public List<Attachment> getAttachments() {
        return attachmentService.getAttachments();
    }

    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteAttachment(@PathVariable UUID id) {
        ApiResponse apiResponse = attachmentService.deleteAttachment(id);
        return ResponseEntity.status(apiResponse.isSucces() ? 200 : 409).body(apiResponse);
    }
}
