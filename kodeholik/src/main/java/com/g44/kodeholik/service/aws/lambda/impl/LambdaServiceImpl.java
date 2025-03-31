package com.g44.kodeholik.service.aws.lambda.impl;

import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.model.InvokeResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.g44.kodeholik.exception.BadRequestException;
import com.g44.kodeholik.model.dto.request.lambda.LambdaRequest;
import com.g44.kodeholik.model.dto.request.lambda.ResponseResult;
import com.g44.kodeholik.service.aws.lambda.LambdaService;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class LambdaServiceImpl implements LambdaService {

    private final AWSLambda awsLambda;

    @Value("${aws.lambda.arn}")
    private String arn;

    @Value("${aws.s3.region}")
    private String region;

    private final ObjectMapper objectMapper;

    @Override
    public String invokeLambdaFunction(LambdaRequest codeRequest) {
        String result = "";
        String notFormatted = "";
        try {
            String payload = objectMapper.writeValueAsString(codeRequest);
            log.info(payload);
            InvokeRequest invokeRequest = new InvokeRequest()
                    .withFunctionName(arn)
                    .withPayload(payload);

            InvokeResult invokeResult = awsLambda.invoke(invokeRequest);
            result = new String(invokeResult.getPayload().array());
            notFormatted = result;
            result = StringEscapeUtils.unescapeJson(result);
            result = result.replace("\\", "");
            result = result.substring(1, result.length() - 1);

            result = result.replace("\"[", "[");
            result = result.replace("]\"", "]");
            result = result.replace("\"{", "{");
            result = result.replace("}\"", "}");

            result = result.replace("\"\"", "\"");
            log.info(result);

            objectMapper.readValue(result, ResponseResult.class);
            return result;
        } catch (Exception e) {
            log.info(e.getMessage());
            notFormatted = notFormatted.replace("\\", "");
            notFormatted = notFormatted.substring(1, notFormatted.length() - 1);
            notFormatted = notFormatted.replace("\"\"", "\"");
            log.info(notFormatted);
            return notFormatted;
        }

    }

}
