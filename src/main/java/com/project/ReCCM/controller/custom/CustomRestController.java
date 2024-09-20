package com.project.ReCCM.controller.custom;

import com.project.ReCCM.Repository.custom.CommentResponseDto;
import com.project.ReCCM.Repository.custom.CustomPostRequestDto;
import com.project.ReCCM.domain.custom.Custom;
import com.project.ReCCM.domain.custom.CustomRepository;
import com.project.ReCCM.service.custom.CommentService;
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

    @Autowired
    private final CommentService commentService;

    private static final String UPLOAD_DIR = "upload-dir/";

    public CustomRestController(CommentService commentService) {
        this.commentService = commentService;
    }

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

    // 댓글 작성 (POST)
    @PostMapping("/comments")
    public ResponseEntity<CommentResponseDto> createComment(@RequestParam("postId") Long postId,
                                                            @RequestParam("text") String text) {
        CommentResponseDto createdComment = commentService.createComment(postId, text);
        return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
    }

    // 댓글 조회 (GET)
    @GetMapping("/comments")
    public ResponseEntity<List<CommentResponseDto>> getComments(@RequestParam("postId") Long postId) {
        System.out.println("댓글조회들어옴" + " : "+postId);
        List<CommentResponseDto> comments = commentService.getCommentsByPostId(postId);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }


}
