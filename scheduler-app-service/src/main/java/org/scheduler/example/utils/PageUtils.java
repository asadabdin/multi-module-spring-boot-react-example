package org.scheduler.example.utils;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.scheduler.example.model.PageRequestDTO;
import org.scheduler.example.model.SortDTO;
import org.scheduler.example.model.SortOrderDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.scheduler.example.model.SortOrderDTO.Direction.ASC;
import static org.scheduler.example.utils.ConverterUtils.mapProperties;

@UtilityClass
public class PageUtils {

    public static Pageable pageable(@NonNull PageRequestDTO<?> pageRequestDTO) {
        return pageable(pageRequestDTO.getPage(), pageRequestDTO.getPageSize(), pageRequestDTO.getSort());
    }

    public static Pageable pageable(int page, int size, SortDTO sort) {
        return ofNullable(sort)
                .map(SortDTO::getOrders)
                .map(sortOrderDTOS -> sortOrderDTOS.stream().map(PageUtils::getOrder).collect(toList()))
                .map(Sort::by)
                .map(sortDomain -> PageRequest.of(page, size, sortDomain))
                .orElseGet(() -> PageRequest.of(page, size));
    }

    private static Sort.Order getOrder(SortOrderDTO sortOrderDTO) {
        return sortOrderDTO.getDirection() == ASC ? Sort.Order.by(sortOrderDTO.getProperty()) : Sort.Order.desc(sortOrderDTO.getProperty());
    }

}
