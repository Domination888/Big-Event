package com.domination.service;

import com.domination.pojo.Category;

import java.util.List;

public interface CategoryService {
    // 新增分类
    void add(Category category);
    //列表查询
    List<Category> List();
    //id查分类
    Category findById(Integer id);

    void update(Category category);

    void delete(Integer id);
}
