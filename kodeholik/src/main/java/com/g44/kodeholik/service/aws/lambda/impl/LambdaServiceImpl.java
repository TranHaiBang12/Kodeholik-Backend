package com.g44.kodeholik.service.aws.lambda.impl;

import java.io.IOException;

import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.model.InvokeResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.g44.kodeholik.model.dto.request.lambda.LambdaRequest;
import com.g44.kodeholik.service.aws.lambda.LambdaService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class LambdaServiceImpl implements LambdaService {

    private final AWSLambda awsLambda;

    @Value("${aws.lambda.arn}")
    private String arn;

    @Value("${aws.s3.region}")
    private String region;

    private final ObjectMapper objectMapper;

    public LambdaServiceImpl() {
        awsLambda = AWSLambdaClientBuilder
                .standard()
                .withRegion(Regions.AP_SOUTHEAST_1)
                .defaultClient();
        objectMapper = new ObjectMapper();
    }

    @Override
    public String invokeLambdaFunction(LambdaRequest codeRequest) {
        try {
            String payload = objectMapper.writeValueAsString(codeRequest);
            log.info(payload);
            InvokeRequest invokeRequest = new InvokeRequest()
                    .withFunctionName(arn)
                    .withPayload(payload);

            InvokeResult invokeResult = awsLambda.invoke(invokeRequest);
            String result = new String(invokeResult.getPayload().array());
            // log.info(result);
            result = StringEscapeUtils.unescapeJson(result);
            result = result.replace("\\", "");
            result = result.replace("\"\"", "\"");
            result = result.substring(1, result.length() - 1);
            // log.info(result);
            return result;
        } catch (Exception e) {
            log.info(e.getMessage());
            return "";
        }

    }

}
