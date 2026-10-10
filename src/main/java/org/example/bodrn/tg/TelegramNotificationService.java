package org.example.bodrn.tg;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.bodrn.config.TelegramBotProperties;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class TelegramNotificationService {

    private final TelegramBotProperties props;
    private final RestTemplate restTemplate = new RestTemplate();

    public void sendToAll(String text) {
        if (props.getToken() == null || props.getChatIds().isEmpty()) return;
        String url = "https://api.telegram.org/bot" + props.getToken() + "/sendMessage";
        for (String chatId : props.getChatIds()) {
            try {
                MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
                body.add("chat_id", chatId);
                body.add("text", text);
                body.add("parse_mode", "HTML");
                restTemplate.postForEntity(url, body, String.class);
            } catch (Exception e) {
                log.error("Не удалось отправить сообщение в Telegram chatId={}: {}", chatId, e.getMessage());
            }
        }
    }

}