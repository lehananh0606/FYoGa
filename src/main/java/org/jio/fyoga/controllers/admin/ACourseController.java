package org.jio.fyoga.controllers.admin;/*  Welcome to Jio word
    @author: Jio
    Date: 7/15/2023
    Time: 4:51 PM
    
    ProjectName: FYoGa
    Jio: I wish you always happy with coding <3
*/

import jakarta.servlet.http.HttpSession;
import org.jio.fyoga.entity.Account;
import org.jio.fyoga.entity.Class;
import org.jio.fyoga.entity.Course;
import org.jio.fyoga.entity.Role;
import org.jio.fyoga.model.AccountDTO;
import org.jio.fyoga.model.ClassDTO;
import org.jio.fyoga.model.CourseDTO;
import org.jio.fyoga.service.ICourseService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

@Controller
@RequestMapping("/FYoGa/Login/ADMIN/Course")
public class ACourseController {

    @Autowired
    ICourseService courseService;
    @GetMapping("")
    public String getCourses(Model model) {
//        List<Class> classListON = classService.findByStatus(1);
//        model.addAttribute("CLASS_ON", classListON);
//
//        List<Class> classListOff = classService.findByStatus(0);
//        model.addAttribute("CLASS_OFF", classListOff);
        List<Course> courseList = courseService.findAll();
        model.addAttribute("COURSELIST", courseList);
        return "admin/pageCourseAdmin";
    }

    // /FYoGa/Login/ADMIN/Course/CreateOrEdit
    @GetMapping("/CreateOrEdit")
    public String showCreateOrEdit(@RequestParam int isEdit,
                                   @RequestParam(name = "CourseID",required = false, defaultValue = "-1") String R_CourseID,
                                   Model model){
        int CourseID = Integer.parseInt(R_CourseID);
        CourseDTO courseDTO = CourseDTO.builder().build();
        // xu ly edit
        if(isEdit == 1 && CourseID >= 0){

            Course courseEntity = courseService.findById(CourseID).orElseThrow();
            BeanUtils.copyProperties(courseEntity, courseDTO);
            courseDTO.setIsEdit(true);
        }

        //xu ly CREATE
        if(isEdit == 0){
            courseDTO.setIsEdit(false);
        }
        model.addAttribute("COURSEDTO", courseDTO);
//        model.addAttribute("COURSES", courses);
//        model.addAttribute("ACCOUNTS", accounts);

        return "admin/createCourse";

    }

    // /FYoGa/Login/ADMIN/Course/CreateOrEdit
    @PostMapping("/CreateOrEdit")
    public String UserOrUpdate (HttpSession session, @ModelAttribute("COURSEDTO") CourseDTO courseDTO, RedirectAttributes ra
            , @RequestParam("file") MultipartFile file){
        Course courseEntity = new Course();

        if(courseDTO.getIsEdit()){

            courseEntity = courseService.findById(courseDTO.getCourseID()).orElseThrow();
            courseEntity.setName(courseDTO.getName());
            courseEntity.setDescription(courseDTO.getDescription());
            courseEntity.setSummary(courseDTO.getSummary());



        }else {
//        xử lý tạo mới
            // copy tu model sang entity
            Account account = (Account) session.getAttribute("USER");
            courseEntity.setAdmin(account);
            BeanUtils.copyProperties(courseDTO, courseEntity);
        }

        // xu ly img
        try {
            courseService.saveIMGAccount(file, courseEntity);
        } catch (IOException e) {
            // Xử lý lỗi nếu cần
        }


        //save
        courseService.save(courseEntity);
        ra.addFlashAttribute("MSG","Save successfully!!!");
        return "redirect:/FYoGa/Login/ADMIN/Course";

    }



    //xử lý lấy ảnh
    @RequestMapping("/downloads-png")
    public ResponseEntity<?> downloadPngCourse(@RequestParam(defaultValue = "") int courseID) {
        byte[] pngData = courseService.getIMGById(courseID);
        if (pngData != null) {
            ByteArrayResource resource = new ByteArrayResource(pngData);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=image.png")
                    .contentType(MediaType.IMAGE_PNG)
                    .body( resource);
        }
        // Xử lý trường hợp tệp tin không tồn tại
        return ResponseEntity.notFound().build();
    }
}
