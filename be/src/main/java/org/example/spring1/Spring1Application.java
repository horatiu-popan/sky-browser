package org.example.spring1;

import org.example.spring1.item.model.Item;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

import java.time.LocalDate;

@SpringBootApplication
@ConfigurationPropertiesScan
public class Spring1Application {

  public static void main(String[] args) {
    SpringApplication.run(Spring1Application.class, args);
  }


}
