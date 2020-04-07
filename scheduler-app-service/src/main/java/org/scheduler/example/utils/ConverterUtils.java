package org.scheduler.example.utils;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import static org.springframework.beans.BeanUtils.*;

@Slf4j
@UtilityClass
public class ConverterUtils {

    public static <T> T mapProperties(@NonNull Object source, Class<T> targetClass, String... ignoreProperties) {
        log.debug("Converting {}", source);
        T target = instantiateClass(targetClass);
        copyProperties(source, target, ignoreProperties);
        log.debug("After conversion {}", target);
        return target;
    }
}
