package com.project.ReCCM.controller.custom;

import com.project.ReCCM.Repository.custom.CustomPostRequestDto;
import com.project.ReCCM.domain.custom.Custom;
import com.project.ReCCM.domain.custom.CustomRepository;
import com.project.ReCCM.service.custom.CustomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/api")
@RestController
public class CustomRestController {


    @Autowired
    private CustomService customService;
    @Autowired
    private CustomRepository customRepository;

    private static final String UPLOAD_DIR = "upload-dir/";

    public void CustomService() {
        // 디렉토리가 없으면 생성
        File dir = new File(UPLOAD_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    @GetMapping("/customList")
    public List<Custom> customList() {
        System.out.println("커스텀리스트 조회 들어옴");
        return customService.getAllCustomPosts();
    }

    @GetMapping("/searchCustom")
    public List<Custom> searchCustom(@RequestParam("keyword") String keyword){
        return customService.searchCoffee(keyword);
    }

    @PostMapping("/createCustom")
    public ResponseEntity<Map<String, String>> createCustom(@RequestParam("customTitle") String customTitle,
                                                            @RequestParam("customContent") String customContent,
                                                            @RequestParam("customImages") MultipartFile[] imgReal) {
        try {
            System.out.println(customTitle + " : " + customContent + " : " + imgReal);
            // 서비스에서 게시글 생성 로직 호출
            customService.createCustomPost(customTitle, customContent, imgReal);
//                    customPostRequestDto.getCustomTitle(),
//                    customPostRequestDto.getCustomContent(),
//                    customPostRequestDto.getImgReal());


            // 응답을 JSON 형식으로 반환
            Map<String, String> response = new HashMap<>();
            response.put("message", "게시글이 등록되었습니다.");

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            e.printStackTrace(); // 에러 로그 출력
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "게시글 등록에 실패했습니다.");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }


}
