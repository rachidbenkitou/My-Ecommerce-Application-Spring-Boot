package com.Benkitou_Miraouy_Coders.ecommerce.services;

import com.Benkitou_Miraouy_Coders.ecommerce.dao.CategoryDao;
import com.Benkitou_Miraouy_Coders.ecommerce.dtos.CategoryDto;
import com.Benkitou_Miraouy_Coders.ecommerce.entities.Category;
import com.Benkitou_Miraouy_Coders.ecommerce.exceptions.EntityAlreadyExistsException;
import com.Benkitou_Miraouy_Coders.ecommerce.exceptions.EntityNotFoundException;
import com.Benkitou_Miraouy_Coders.ecommerce.mappers.CategoryMapper;
import com.Benkitou_Miraouy_Coders.ecommerce.services.inter.CategoryServiceInter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class CategoryService implements CategoryServiceInter {
    private final CategoryDao categoryDao;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryDto> getCategoriesByQuery(Long id, String name) {
        return categoryDao.findAllCategoryIdsAndNames(id, name);
    }
    @Override
    public CategoryDto getCategoryById(Long id) {
        Optional<Category> category = categoryDao.findById(id);

        if (category.isPresent()) {
            return categoryMapper.modelToDto(category.get());

        } else {
            throw new EntityNotFoundException(String.format("The category with the id %d is not found.", id));
        }
    }
    @Override
    public CategoryDto addCategory(CategoryDto categoryDto) {
        if (categoryDao.existsByName(categoryDto.getName())) {
            throw new EntityAlreadyExistsException(String.format("The category with the name %s is already exists", categoryDto.getName()));
        }
        categoryDto.setId(null);

        return categoryMapper.modelToDto(categoryDao.save(categoryMapper.dtoToModel(categoryDto)));
    }
    @Override
    public CategoryDto updateCategory(Long id, CategoryDto categoryDto) {
        CategoryDto oldCategoryDto = getCategoryById(id);
        categoryDto.setId(oldCategoryDto.getId());
        Category updatedCategory = categoryDao.save(categoryMapper.dtoToModel(categoryDto));
        return categoryMapper.modelToDto(updatedCategory);
    }
    @Override
    public void deleteCategoryById(Long id) {
        getCategoryById(id);
        categoryDao.deleteById(id);
    }

}
