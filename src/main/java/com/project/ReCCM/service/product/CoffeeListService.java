package com.project.ReCCM.service.product;

import com.project.ReCCM.domain.product.Product;
import com.project.ReCCM.domain.product.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CoffeeListService {
    @Autowired
    private ProductRepository productRepository;

    // 키워드를 이용한 커피 검색
    public List<Product> searchCoffee(String keyword) {
        return productRepository.searchCoffee(keyword);
    }
}
