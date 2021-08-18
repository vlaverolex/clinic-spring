package com.vladveretilnyk.clinic.service;

import com.vladveretilnyk.clinic.entity.Category;
import com.vladveretilnyk.clinic.entity.Doctor;
import com.vladveretilnyk.clinic.repository.CategoryRepository;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    private static final Map<String, String> DICTIONARY = new HashMap<>();

    static {
        DICTIONARY.put("Педіатр","Pediatrician");
        DICTIONARY.put("Травматолог","Traumatologist");
        DICTIONARY.put("Хірург","Surgeon");
        DICTIONARY.put("Окуліст","Ophthalmologist");
        DICTIONARY.put("Дантист","Dentist");
        DICTIONARY.put("Лор","Lor");

    }

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Category findByName(String name) {
        return LocaleContextHolder.getLocale().equals(new Locale("ua")) ?
                categoryRepository.findByName(DICTIONARY.get(name)) :
                categoryRepository.findByName(name);
    }

    public Map<Category, Set<Doctor>> findCategoryDoctors() {
        Map<Category, Set<Doctor>> categoryDoctors = new HashMap<>();
        categoryRepository.findAll().forEach(category -> categoryDoctors.put(category, category.getDoctors()));
        return categoryDoctors;
    }
}
