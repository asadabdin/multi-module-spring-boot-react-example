package org.scheduler.example.configuration;

import lombok.Getter;
import lombok.Setter;
import org.scheduler.example.model.SortDTO;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "org.task.page.default")
public class PageProperty {

    private int size;
    private int page = 10;
    private SortDTO sort;

}
