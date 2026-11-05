package anuong.cantho.backend.config;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CauHinhWebsiteWebConfig implements WebMvcConfigurer{

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {

    Path uploadDir = Paths.get(
        "uploads",
        "cau-hinh"
    ).toAbsolutePath().normalize();

    String resourceLocation = uploadDir.toUri().toString();

    registry
        .addResourceHandler("/uploads/cau-hinh/**")
        .addResourceLocations(resourceLocation);
  }

}
