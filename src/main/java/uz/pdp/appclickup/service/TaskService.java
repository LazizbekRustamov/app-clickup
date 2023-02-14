package uz.pdp.appclickup.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.pdp.appclickup.dto.*;
import uz.pdp.appclickup.entity.*;
import uz.pdp.appclickup.entity.enums.AddType;
import uz.pdp.appclickup.repository.*;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TaskService {
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    StatusRepository statusRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    PriorityRepository priorityRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    TaskUserRepository taskUserRepository;
    @Autowired
    CategoryUserRepository categoryUserRepository;
    @Autowired
    WorkspaceRepository workspaceRepository;

    public String uploadDirectory = "E:\\Yuklanganlar";
    @Autowired
    private TagRepository tagRepository;


    public ApiResponse addTask(TaskAddOrEditDto taskDto, List<MultipartFile> files) throws IOException {

        if (taskRepository.existsByNameAndCategoryId(taskDto.getName(), taskDto.getCategoryId())) {
            return new ApiResponse("Bunday task allaqachon mavjud", false);
        }

        Optional<Category> optionalCategory = categoryRepository.findById(taskDto.getCategoryId());
        if (!optionalCategory.isPresent()) {
            return new ApiResponse("Bunday categoriya mavjud emas", false);
        }

        Optional<Status> optionalStatus = statusRepository.findByIdAndCategoryId(taskDto.getStatusId(), taskDto.getCategoryId());
        if (!optionalStatus.isPresent()) {
            return new ApiResponse("Bu categoriyada bunday status mavjud emas", false);
        }

        List<Attachment> attachments = new ArrayList<>();
        for (MultipartFile file : files) {
            Attachment attachment = new Attachment();
            attachment.setName(file.getName());
            attachment.setOriginalName(file.getOriginalFilename());
            attachment.setSize(file.getSize());
            attachment.setContentType(file.getContentType());

            byte[] bytes = file.getBytes();
            Path path = Paths.get("attachments/" + file.getOriginalFilename());
            Files.write(path, bytes);

            attachments.add(attachment);
        }


        Task task = new Task(
                taskDto.getName(),
                taskDto.getDescription(),
                optionalStatus.get(),
                optionalCategory.get(),
                null,
                taskDto.getParentTaskId() == null ? null : taskRepository.findById(taskDto.getParentTaskId()).orElseThrow(() -> new ResourceNotFoundException("parent task id si da xatolik")),
                taskDto.getDueDate(),
                null,
                attachments
        );

        taskRepository.save(task);


        return new ApiResponse();
    }


    public ApiResponse addPriorityToTask(Priority priority, UUID id) {

        Optional<Task> optionalTask = taskRepository.findById(id);
        if (!optionalTask.isPresent()) {
            return new ApiResponse("Bunday task mavjud emas", false);
        }

        if (taskRepository.existsByIdAndPriorityName(id, priority.getName())) {
            return new ApiResponse("Bunday priority mavjud", false);
        }

        Task task = optionalTask.get();
        task.setPriority(priority);

        taskRepository.save(task);

        return new ApiResponse("Priority saqlandi", true);
    }


    public ApiResponse deletePriorityFromTask(Priority priority, UUID id) {

        Optional<Task> optionalTask = taskRepository.findById(id);
        if (!optionalTask.isPresent()) {
            return new ApiResponse("Bunday task mavjud emas", false);
        }

        Task task = optionalTask.get();
        task.setPriority(null);

        taskRepository.save(task);

        return new ApiResponse("Priority o'chirildi", true);
    }


    public ApiResponse activateTask(UUID id) {

        Optional<Task> optionalTask = taskRepository.findById(id);
        if (!optionalTask.isPresent()) {
            return new ApiResponse("Bunday task mavjud emas", false);
        }

        Task task = optionalTask.get();
        task.setActivedDate(new Timestamp(System.currentTimeMillis()));

        taskRepository.save(task);

        return new ApiResponse("Task aktivlashtirildi", true);
    }


    public ApiResponse editTask(UUID id, TaskAddOrEditDto taskDto, List<MultipartFile> files) throws IOException {

        Optional<Task> optionalTask = taskRepository.findById(id);
        if (!optionalTask.isPresent()) {
            return new ApiResponse("Bunday task mavjud emas", false);
        }

        if (taskRepository.existsByNameAndCategoryId(taskDto.getName(), taskDto.getCategoryId())) {
            return new ApiResponse("Bunday task allaqachon mavjud", false);
        }


        List<Attachment> attachments = new ArrayList<>();
        for (MultipartFile file : files) {
            Attachment attachment = new Attachment();
            attachment.setName(file.getName());
            attachment.setOriginalName(file.getOriginalFilename());
            attachment.setSize(file.getSize());
            attachment.setContentType(file.getContentType());

            byte[] bytes = file.getBytes();
            Path path = Paths.get("attachments/" + file.getOriginalFilename());
            Files.write(path, bytes);

            attachments.add(attachment);
        }


        Task task = optionalTask.get();
        task.setName(taskDto.getName());
        task.setDescription(taskDto.getDescription());
        task.setStatus(statusRepository.findById(taskDto.getStatusId()).orElseThrow(() -> new ResourceNotFoundException("Status topilmadi")));
        task.setCategory(categoryRepository.findById(taskDto.getCategoryId()).orElseThrow(() -> new ResourceNotFoundException("Category topilmadi")));
        if (taskDto.getParentTaskId() != null) {
            task.setParentTask(taskRepository.findById(taskDto.getParentTaskId()).orElseThrow(() -> new ResourceNotFoundException("Parent task topilmadi")));
        }
        if (taskDto.getDueDate() != null) {
            task.setDueDate(taskDto.getDueDate());
        }
        if (!attachments.isEmpty()) {
            task.setAttachmentList(attachments);
        }

        taskRepository.save(task);
        return new ApiResponse("Task o'zgartirildi", true);
    }


    public Task getTask(UUID id) {

        Optional<Task> optionalTask = taskRepository.findById(id);
        if (optionalTask.isPresent()) {
            Task task = optionalTask.get();
            return new Task(
                    task.getName(),
                    task.getDescription(),
                    task.getPriority(),
                    task.getDueDate(),
                    task.getActivedDate(),
                    task.getAttachmentList(),
                    task.getTags()
            );
        }
        return null;
    }


    public List<Task> getTasksFromCategory(UUID id) {
        try {
            List<Task> tasks = taskRepository.findAllByCategoryId(id);
            List<Task> taskList = new ArrayList<>();

            for (Task task : tasks) {
                Task t = new Task(
                        task.getName(),
                        task.getDescription(),
                        task.getPriority(),
                        task.getDueDate(),
                        task.getActivedDate(),
                        task.getAttachmentList(),
                        task.getTags()
                );
                taskList.add(t);
            }
            return taskList;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }


    public ApiResponse deletetask(UUID id) {

        Optional<Task> optionalTask = taskRepository.findById(id);
        if (!optionalTask.isPresent()) {
            return new ApiResponse("Bunday task mavjud emas", false);
        }

        Task task = optionalTask.get();

        taskRepository.deleteById(id);

        List<Attachment> attachmentList = task.getAttachmentList();
        for (Attachment attachment : attachmentList) {

            Path path = Paths.get(uploadDirectory + "/" + attachment.getOriginalName());
            if (Files.exists(path)) {
                try {
                    Files.delete(path);
                } catch (IOException e) {
                    throw new ResourceNotFoundException("Failed to delete attachment with id " + id, e);
                }
            }
        }
        return new ApiResponse("Attachment o'chirildi", true);
    }


    public ApiResponse addOrEditOrDeleteTaskMember(TaskMemberDto taskMemberDto) {

        if (taskMemberDto.getAddType().equals(AddType.ADD)) {

            Optional<User> optionalUser = userRepository.findById(taskMemberDto.getUserId());
            if (!optionalUser.isPresent()) {
                return new ApiResponse("Bunday user royxatdan otmagan", false);
            }

            Optional<Task> optionalTask = taskRepository.findById(taskMemberDto.getTaskId());
            if (!optionalTask.isPresent()) {
                return new ApiResponse("Bunday task mavjud emas", false);
            }

            if (!categoryUserRepository.existsByUserId(taskMemberDto.getUserId())) {
                return new ApiResponse("Bunday user categoryda mavjud emas", false);
            }

            TaskUser taskUser = new TaskUser(optionalTask.get(), optionalUser.get());
            taskUserRepository.save(taskUser);

            return new ApiResponse("User taskka qoshildi", true);
        }
//        else if (taskMemberDto.getAddType().equals(AddType.EDIT)) {}
        else if (taskMemberDto.getAddType().equals(AddType.REMOVE)) {
            try {
                taskUserRepository.deleteByUserIdAndTaskId(taskMemberDto.getUserId(), taskMemberDto.getTaskId());
                return new ApiResponse("Taskdan user ochirildi", true);
            } catch (Exception e) {
                return new ApiResponse("Taskda userni ochirishda error", false);
            }
        } else {
            return new ApiResponse("Errorr", false);
        }
    }


    public ApiResponse addTag(TaskTagDto taskTagDto, UUID id) {
        Optional<Task> optionalTask = taskRepository.findById(id);
        if (!optionalTask.isPresent()) {
            return new ApiResponse("Bu id lik task topilmadi", false);
        }


        List<TagDto> tagDtoList = taskTagDto.getTagDtoList();
        List<Tag> tagList = new ArrayList<>();

        for (TagDto tagDto : tagDtoList) {
            Tag tag = new Tag(
                    tagDto.getName(),
                    tagDto.getColor(),
                    workspaceRepository.findById(tagDto.getWorkspaceId()).
                            orElseThrow(() -> new ResourceNotFoundException(tagDto.getWorkspaceId() + " id lik workspace topilmadi"))
            );
            tagList.add(tag);
        }
        List<Tag> tags = tagRepository.saveAll(tagList);


        Task task = optionalTask.get();
        task.setTags(tags);

        taskRepository.save(task);


        return null;
    }


    public ApiResponse editOrDeleteTag(TagEditDto tagEditDto, Long id) {
        if (tagEditDto.getAddType().equals(AddType.EDIT)) {

            Optional<Tag> optionalTag = tagRepository.findById(id);
            if (!optionalTag.isPresent()){
                return new ApiResponse("Bunday tag mavjud emas",false);
            }

            if (tagRepository.existsByNameAndColor(tagEditDto.getName(), tagEditDto.getColor())) {
                return new ApiResponse("Bunday nomli va rangli tag mavjud",false);
            }

            Tag tag = optionalTag.get();
            tag.setName(tagEditDto.getName());
            tag.setColor(tagEditDto.getColor());

            tagRepository.save(tag);

            return new ApiResponse("Tag ozgartirildi", true);
        } else if (tagEditDto.getAddType().equals(AddType.REMOVE)) {
            try {
                tagRepository.deleteById(id);
                return new ApiResponse("Tag o'chirildi", true);
            } catch (Exception e) {
                return new ApiResponse("Tagni ochirishda xatolik", false);
            }
        } else {
            return new ApiResponse("Tag ozgartirish yoki ochirishda xatolik", false);
        }
    }
}

