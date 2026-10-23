package com.suyashcodes07.tomato.controller;

import com.suyashcodes07.tomato.service.SummarizeService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class SummarizeContoller {

    private SummarizeService summarizeService;

    public SummarizeContoller(SummarizeService summarizeService) {
        this.summarizeService = summarizeService;
    }

    @PostMapping("/chat")
    public String chat(@RequestBody String message) {
        return summarizeService.chat(message);
    }
}
