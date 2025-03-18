package com.g44.kodeholik.controller.openai;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.g44.kodeholik.service.openai.OpenAIService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/openai")
public class OpenAIController {

    private final OpenAIService openAIService;

    @GetMapping("/test")
    public ResponseEntity<String> getContentEmailReminder() {
        return ResponseEntity.ok(openAIService.generateContentEmailReminder(
                "Hãy tạo một email nhắc nhở Bằng vào học sau khi đã không học trong 5 ngày liên tiếp. Bạn có thể viết với thái độ nghiêm túc, động viên cậu ấy tiếp tục học, không cần viết những phần như chào hay trân trọng, chỉ tập trung vào nội dung"));
    }

}
