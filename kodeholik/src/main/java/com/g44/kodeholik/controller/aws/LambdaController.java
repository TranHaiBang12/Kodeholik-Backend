package com.g44.kodeholik.controller.aws;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.g44.kodeholik.model.dto.request.lambda.LambdaRequest;
import com.g44.kodeholik.service.CompileService;
import com.g44.kodeholik.service.aws.lambda.LambdaService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/lambda")
public class LambdaController {

    private final LambdaService lambdaService;

    @PostMapping("/test-compile")
    public String testCompile(@RequestBody LambdaRequest lambdaRequest) throws IOException, InterruptedException {
        log.info(lambdaRequest.getTestCases());
        // return lambdaService.invokeLambdaFunction(lambdaRequest);
        return CompileService.compileAndRun(lambdaRequest.getCode(),
                lambdaRequest.getTestCases(),
                lambdaRequest.getLanguage(), lambdaRequest.getFunctionSignature(),
                lambdaRequest.getReturnType());
    }

}
