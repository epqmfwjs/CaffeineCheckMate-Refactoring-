package com.project.ReCCM.controller.custom;

import com.project.ReCCM.Repository.custom.CommentResponseDto;
import com.project.ReCCM.Repository.custom.CustomPostRequestDto;
import com.project.ReCCM.Repository.custom.LikeRequestDto;
import com.project.ReCCM.domain.custom.Custom;
import com.project.ReCCM.domain.custom.CustomRepository;
import com.project.ReCCM.service.custom.CommentService;
import com.project.ReCCM.service.custom.CountService;
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
    private final CustomRepository customRepository;

    @Autowired
    private final CustomService customService;

    @Autowired
    private final CommentService commentService;

    @Autowired
    private final CountService countService;

    private static final String UPLOAD_DIR = "upload-dir/";

    public CustomRestController(CustomRepository customRepository, CustomService customService, CommentService commentService, CountService countService) {
        this.customRepository = customRepository;
        this.customService = customService;
        this.commentService = commentService;
        this.countService = countService;
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
        System.out.println("댓글저장" + " : "+ postId);
        CommentResponseDto createdComment = commentService.createComment(postId, text);
        return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
    }

    // 댓글 조회 (GET)
    @GetMapping("/comments")
    public ResponseEntity<List<CommentResponseDto>> getComments(@RequestParam("postId") Long postId) {
        System.out.println("댓글조회들어옴" + " : "+ postId);
        List<CommentResponseDto> comments = commentService.getCommentsByPostId(postId);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    //좋아요 관련
/*
    @PostMapping("/favorites/{postId}")
    public ResponseEntity<?> favorites(@PathVariable Long postId) {
        System.out.println("게시물번호" + " : "+ postId);
        countService.favorites(postId);
        return ResponseEntity.ok().body("favorites 등록되었습니다."); // 성공 응답
    }

    //좋아요로 만들었지만 즐겨찾기에 이용해야겠음
    @PostMapping("/like/{postId}")
    public ResponseEntity<?> likePost(@PathVariable Long postId) {
        System.out.println("게시물번호" + " : "+ postId);
        countService.likeNum(postId);
        return ResponseEntity.ok().body("좋아요가 등록되었습니다."); // 성공 응답
    }
*/

    // 게시글에 대한 좋아요/취소 기능
    @PostMapping("/like")
    public void toggleLike(@RequestBody LikeRequestDto likeRequestDto) {
        countService.toggleLike(likeRequestDto.getPostId(), likeRequestDto.getMemberId());
    }
}
