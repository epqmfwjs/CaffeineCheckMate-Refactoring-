package com.project.ReCCM.controller.product;

import com.project.ReCCM.Repository.product.FavoriteRequestDto;
import com.project.ReCCM.Repository.product.FavoriteResponseDto;
import com.project.ReCCM.domain.product.Product;
import com.project.ReCCM.domain.product.ProductRepository;
import com.project.ReCCM.service.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/api")
@org.springframework.web.bind.annotation.RestController
public class ProductRestController {

    @Autowired
    private ProductService productService;
    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/coffeeList")
    public List<Product> coffeeList() {
        System.out.println("커피리스트 조회 들어옴");
       return productRepository.findAll();
    }

    @GetMapping("/searchCoffee")
    public List<Product> searchCoffee(@RequestParam("keyword") String keyword){
        return productService.searchCoffee(keyword);
    }


    // 즐겨찾기 추가
    @PostMapping("/favorite")
    public ResponseEntity<?> addFavorite(@RequestBody FavoriteRequestDto request) {
        productService.addFavorite(request.getMemberId(), request.getPostId());

        Map<String, String> response = new HashMap<>();
        response.put("message", "즐겨찾기에 추가되었습니다.");

        return ResponseEntity.ok(response);
    }


    //즐겨찾기 취소
    @DeleteMapping("/favorite")
    public ResponseEntity<?> removeFavorite(@RequestBody FavoriteRequestDto request) {
        productService.removeFavorite(request.getMemberId(), request.getPostId());

        Map<String, String> response = new HashMap<>();
        response.put("message", "즐겨찾기에서 제거되었습니다.");

        return ResponseEntity.ok(response);
    }

    // 즐겨찾기 유무 판단
    @GetMapping("favoriteStatus")
    public ResponseEntity<FavoriteResponseDto> getFavoriteStatus(@RequestParam("postId") Long postId,@RequestParam("memberId") Long memberId) {
        System.out.println("postId  : " + postId + "  memberId : " + memberId);
        boolean isFavorited = productService.isFavorited(memberId,postId);
        System.out.println("isFavorited : " + isFavorited);
        return ResponseEntity.ok(new FavoriteResponseDto(isFavorited));
    }

}
