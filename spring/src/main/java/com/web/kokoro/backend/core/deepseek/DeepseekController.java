package com.web.kokoro.backend.core.deepseek;

import com.mobile.hotel.model.CompletionsRequest;
import com.web.kokoro.backend.base.Result;
import com.web.kokoro.backend.core.user.UserService;
import jakarta.validation.Valid;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/deepseek")
public class DeepseekController {
    private final DeepseekService deepseekService;
    DeepseekController(@Autowired DeepseekService deepseekService){
        this.deepseekService = deepseekService;
    }
    private final ExecutorService executor = Executors.newCachedThreadPool();

    @RequestMapping(value  = "/chat/completions",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter completions(@Valid @RequestBody CompletionsRequest requestBody) throws IOException {
        SseEmitter emitter = new SseEmitter(0L);
        emitter.send(SseEmitter.event()
                .name("starting")
        );
        deepseekService.requestChat(requestBody, new Function1<String, Unit>() {
            @SneakyThrows
            @Override
            public Unit invoke(String s) {
                emitter.send(SseEmitter.event()
                        .data(s)
                );
                return null;
            }
        }, new Function0<Unit>() {
            @Override
            @SneakyThrows
            public Unit invoke() {
                emitter.send(SseEmitter.event()
                        .name("complete"));

                emitter.complete();
                return null;
            }
        });
        return emitter;
    }


}
