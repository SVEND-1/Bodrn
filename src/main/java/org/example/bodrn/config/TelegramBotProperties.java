package org.example.bodrn.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "telegram.bot")
@Getter
@Setter
public class TelegramBotProperties {
    private String token;
    private List<String> chatIds = new ArrayList<>();
}