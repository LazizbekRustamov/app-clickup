package uz.pdp.appclickup.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.pdp.appclickup.dto.ApiResponse;
import uz.pdp.appclickup.entity.Attachment;
import uz.pdp.appclickup.repository.AttachmentRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class AttachmentService {

    @Autowired
    AttachmentRepository attachmentRepository;

    public String uploadDirectory = "E:\\Yuklanganlar";

    public Attachment saveAttachment(MultipartFile file) {
        try {
            Attachment attachment = new Attachment();
            attachment.setName(file.getOriginalFilename());
            attachment.setOriginalName(file.getOriginalFilename());
            attachment.setSize(file.getSize());
            attachment.setContentType(file.getContentType());

            byte[] bytes = file.getBytes();
            Path path = Paths.get(uploadDirectory + "/" + file.getOriginalFilename());
            Files.write(path, bytes);

            return attachmentRepository.save(attachment);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save attachment", e);
        }
    }


    public Attachment getAttachment(UUID id) {
        return attachmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attachment with id " + id + " not found"));
    }

    public List<Attachment> getAttachments() {
        return attachmentRepository.findAll();
    }

    public ApiResponse deleteAttachment(UUID id) {
        Attachment attachment = attachmentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Attachment not found with id " + id));
        Path path = Paths.get(uploadDirectory + "/" + attachment.getOriginalName());
        if (Files.exists(path)) {
            try {
                Files.delete(path);
            } catch (IOException e) {
                throw new ResourceNotFoundException("Failed to delete attachment with id " + id, e);
            }
        }
        attachmentRepository.deleteById(id);
        return new ApiResponse("File o'chirildi", true);
    }
}
