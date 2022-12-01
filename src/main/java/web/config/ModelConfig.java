package web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import web.model.AreaChecker;
import web.model.AreaCheckerImpl;

@Configuration
public class ModelConfig {

    @Bean
    public AreaChecker areaChecker() {
        return new AreaCheckerImpl();
    }

}
