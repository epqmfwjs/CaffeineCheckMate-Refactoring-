package com.project.ReCCM.service.product;

import com.project.ReCCM.domain.member.Member;
import com.project.ReCCM.domain.member.MemberRepository;
import com.project.ReCCM.domain.product.Favorite;
import com.project.ReCCM.domain.product.FavoriteRepository;
import com.project.ReCCM.domain.product.Product;
import com.project.ReCCM.domain.product.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    FavoriteRepository favoriteRepository;


    // 키워드를 이용한 커피 검색
    public List<Product> searchCoffee(String keyword) {
        return productRepository.searchCoffee(keyword);
    }

    // 즐겨찾기 추가
    public Favorite addFavorite(Long memberId, Long postId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        Product product = productRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시물을 찾을 수 없습니다."));

        // 이미 즐겨찾기 되어있는지 확인
        favoriteRepository.findByMemberAndProduct(member, product)
                .ifPresent(fav -> { throw new IllegalArgumentException("이미 즐겨찾기 되었습니다."); });

        Favorite favorite = new Favorite();
        favorite.setMember(member);
        favorite.setProduct(product);

        return favoriteRepository.save(favorite);
    }
    // 즐겨찾기 취소
    public void removeFavorite(Long memberId, Long postId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        Product product = productRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시물을 찾을 수 없습니다."));

        Favorite favorite = (Favorite) favoriteRepository.findByMemberAndProduct(member, product)
                .orElseThrow(() -> new IllegalArgumentException("즐겨찾기 항목을 찾을 수 없습니다."));

        favoriteRepository.delete(favorite);
    }

    // 게시글 모달 띄울때 즐겨찾기 상태 체크
    public boolean isFavorited(Long memberId, Long postId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        Product product = productRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시물을 찾을 수 없습니다."));

        return favoriteRepository.existsByMemberAndProduct(member, product);

    }
}
