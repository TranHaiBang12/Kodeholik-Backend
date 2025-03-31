package com.g44.kodeholik.controller.enums;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.g44.kodeholik.model.enums.problem.InputType;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/enums")
public class EnumController {
    @GetMapping("/input-types")
    public List<InputType> getInputTypes() {
        return Arrays.asList(InputType.values());
    }
}
