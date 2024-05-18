package ru.alex.testcasebankapp.config;

import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.annotation.RequestParamMethodArgumentResolver;
import ru.alex.testcasebankapp.model.entity.PaginationEntity;
import ru.alex.testcasebankapp.model.user.User;
import ru.alex.testcasebankapp.util.SearchParam;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class SearchParamResolver extends RequestParamMethodArgumentResolver {

    public SearchParamResolver(boolean useDefaultResolution) {
        super(useDefaultResolution);
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(SearchParam.class);
    }

    @Override
    protected Object resolveName(String name, MethodParameter parameter, NativeWebRequest request) throws Exception {
        int pageNumber = Integer.parseInt(Optional.ofNullable(request.getParameter("page-number")).orElse("0"));

        int pageSize = Integer.parseInt(Optional.ofNullable(request.getParameter("page-size")).orElse("20"));

        String propertySort = Optional.ofNullable(request.getParameter("sort-param")).orElse("");

        if (propertySort.equals("emails") || propertySort.equals("phones") ||
                !Arrays.stream(User.class.getDeclaredFields())
                        .map(Field::getName).collect(Collectors.toSet()).contains(propertySort)) {
            propertySort = "";
        }
        return PaginationEntity.builder()
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .propertySort(propertySort)
                .build();
    }


}
