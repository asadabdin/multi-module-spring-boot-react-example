package org.scheduler.example.configuration;

import com.google.common.base.Predicates;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.SingletonBeanRegistry;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.PostConstruct;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.ArrayUtils.isNotEmpty;

@Configuration
@EnableSwagger2
@RequiredArgsConstructor
public class SwaggerConfiguration {

    private final ConfigurableApplicationContext context;

    //Default Docket to show all
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(metaData())
                .groupName("All")
                .forCodeGeneration(Boolean.TRUE)
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .paths(PathSelectors.any())
                .paths(Predicates.not(PathSelectors.regex("/error.*")))
                .build();
    }

    //Creating Docket Dynamically per Rest Controller
    @PostConstruct
    public void postConstruct() throws ClassNotFoundException {
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AnnotationTypeFilter(RestController.class));
        for (BeanDefinition beanDef : provider.findCandidateComponents("org.scheduler.example.controller")) {
            Class<?> cl = Class.forName(beanDef.getBeanClassName());
            RequestMapping requestMapping = cl.getAnnotation(RequestMapping.class);
            if (nonNull(requestMapping) && (isNotEmpty(requestMapping.value()) || isNotEmpty(requestMapping.path()))) {
                String resource_url = isNotEmpty(requestMapping.value()) ? requestMapping.value()[0] : requestMapping.path()[0];
                SingletonBeanRegistry beanRegistry = context.getBeanFactory();
                Docket docket = new Docket(DocumentationType.SWAGGER_2)
                        .groupName(StringUtils.isNotEmpty(requestMapping.name()) ? requestMapping.name() : resource_url)
                        .apiInfo(metaData())
                        .forCodeGeneration(Boolean.TRUE)
                        .select()
                        .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                        .paths(PathSelectors.regex(resource_url + ".*"))
                        .paths(Predicates.not(PathSelectors.regex("/error.*")))
                        .build();
                beanRegistry.registerSingleton(cl.getSimpleName() + "_docket_api", docket);
            }
        }
    }

    private ApiInfo metaData() {
        return new ApiInfoBuilder()
                .title("Task Services")
                .description("Endpoints related to Task Services")
                .version("1.0")
                .contact(new Contact("Asad Abdin", "", "asadabdin@gmail.com"))
                .build();
    }
}
