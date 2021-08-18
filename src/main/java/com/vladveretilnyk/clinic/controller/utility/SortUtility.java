package com.vladveretilnyk.clinic.controller.utility;

import org.springframework.data.domain.Sort;

import java.util.Optional;

public class SortUtility {
    private SortUtility() {
    }

    public static Sort getSort(String sort, String direction) {
        sort = Optional.ofNullable(sort).orElse("id");
        direction = Optional.ofNullable(direction).orElse("DESC");

        return Sort.by(Sort.Direction.valueOf(direction), sort);
    }
}
