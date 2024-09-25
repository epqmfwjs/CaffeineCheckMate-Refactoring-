package com.project.ReCCM.controller.custom;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.ReCCM.Repository.custom.CommentResponseDto;
import com.project.ReCCM.Repository.custom.CustomPostRequestDto;
import com.project.ReCCM.Repository.custom.CustomResponseDto;
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
import java.util.stream.Collectors;

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

/*    @GetMapping("/customList")
    public List<Custom> customList() {
        System.out.println("커스텀리스트 조회 들어옴");
        return customService.getAllCustomPosts();
    }*/
    
    // 리스트 결과 가져오기
    @GetMapping("/customList")
    public ResponseEntity<List<CustomResponseDto>> customList() {
        try {
            List<Custom> customPosts = customService.getAllCustomPosts();

            // Custom 객체를 CustomResponseDto로 변환
            List<CustomResponseDto> response = customPosts.stream()
                    .map(custom -> new CustomResponseDto(
                            custom.getId(),
                            custom.getCustomTitle(),
                            custom.getCustomContent(),
                            custom.getImgReal(),
                            custom.getMember().getMemberId(),
                            custom.getCreatedDate(),
                            custom.getLikesCount()))
                    .collect(Collectors.toList());
            System.out.println("커스텀리스트 조회 결과: " + response);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.err.println("커스텀 리스트를 가져오는 중 오류 발생: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/searchCustom")
    public List<Custom> searchCustom(@RequestParam("keyword") String keyword){
        return customService.searchCoffee(keyword);
    }
    
    // 게시글 작성
    @PostMapping("/createCustom")
    public ResponseEntity<Map<String, String>> createCustom(@RequestParam("customTitle") String customTitle,
                                                            @RequestParam("customContent") String customContent,
                                                            @RequestParam("memberPK") Long memberPK,
                                                            @RequestParam("customImages") MultipartFile[] imgReal) {
        try {
            System.out.println(memberPK + " : " + customTitle + " : " + customContent + " : " + imgReal);
            // 서비스에서 게시글 생성 로직 호출
            customService.createCustomPost(memberPK, customTitle, customContent, imgReal);

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
                                                            @RequestParam("text") String text,
                                                            @RequestParam("memberPK") Long memberPK) {
        System.out.println("댓글저장 postId " + " : "+ postId);
        System.out.println("댓글저장 memberPK " + " : "+ memberPK);

        CommentResponseDto createdComment = commentService.createComment(memberPK, postId, text);
        return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
    }

    // 댓글 조회 (GET)
    @GetMapping("/comments")
    public ResponseEntity<List<CommentResponseDto>> getComments(@RequestParam("postId") Long postId) {
        System.out.println("댓글조회들어옴 postId " + " : "+ postId);
        List<CommentResponseDto> comments = commentService.getCommentsByPostId(postId);

        System.out.println("댓글 조회 서비스 다녀온 후  " + " : " + comments.toString());

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
    public ResponseEntity<Integer> toggleLike(@RequestBody LikeRequestDto likeRequestDto) {
        System.out.println("postId: " + likeRequestDto.getPostId() + ", memberId: " + likeRequestDto.getMemberId());

        // 좋아요 토글
        countService.toggleLike(likeRequestDto.getPostId(), likeRequestDto.getMemberId());

        // 현재 게시물의 좋아요 수를 가져옴
        int currentLikesCount = customRepository.findById(likeRequestDto.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("게시물이 존재하지 않습니다."))
                .getLikesCount();

        // 현재 좋아요 수 반환
        return ResponseEntity.ok(currentLikesCount);
    }

    @GetMapping("/likeStatus")
    public ResponseEntity<Map<String, Object>> getLikeStatus(@RequestParam Long postId, @RequestParam Long memberId) {
        boolean hasLiked = countService.checkLikeStatus(postId, memberId); // 사용자 좋아요 상태 체크
        Map<String, Object> response = new HashMap<>();
        response.put("hasLiked", hasLiked);
        return ResponseEntity.ok(response);
    }
}
