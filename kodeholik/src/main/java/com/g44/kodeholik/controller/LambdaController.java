package com.g44.kodeholik.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.g44.kodeholik.model.dto.request.lambda.CodeRequest;
import com.g44.kodeholik.service.aws.lambda.LambdaService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class LambdaController {

    private final LambdaService lambdaService;

    @PostMapping("/compile")
    public String compileCode(@RequestBody CodeRequest codeRequest) {
        return lambdaService.invokeLambdaFunction(codeRequest);
    }

}
