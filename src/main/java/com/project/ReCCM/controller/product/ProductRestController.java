package com.project.ReCCM.controller.product;

import com.project.ReCCM.domain.product.Product;
import com.project.ReCCM.domain.product.ProductRepository;
import com.project.ReCCM.service.product.CoffeeListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RequestMapping("/api")
@org.springframework.web.bind.annotation.RestController
public class ProductRestController {

    @Autowired
    private CoffeeListService coffeeListService;
    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/coffeeList")
    public List<Product> coffeeList() {
        System.out.println("커피리스트 조회 들어옴");
        return productRepository.findAll();
    }

    @GetMapping("/searchCoffee")
    public List<Product> searchCoffee(@RequestParam("keyword") String keyword){
        return coffeeListService.searchCoffee(keyword);
    }
}
