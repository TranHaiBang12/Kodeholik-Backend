package com.g44.kodeholik.service.aws.lambda.impl;

import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.AWSLambdaClientBuilder;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.model.InvokeResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.g44.kodeholik.model.dto.request.lambda.LambdaRequest;
import com.g44.kodeholik.service.aws.lambda.LambdaService;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class LambdaServiceImpl implements LambdaService {

    private final AWSLambda awsLambda;

    @Value("${aws.lambda.arn}")
    private String arn;

    private final ObjectMapper objectMapper;

    public LambdaServiceImpl() {
        awsLambda = AWSLambdaClientBuilder.defaultClient();
        objectMapper = new ObjectMapper();
    }

    @Override
    public String invokeLambdaFunction(LambdaRequest codeRequest) {
        try {

            // Chuyen du lieu request thanh JSON
            String payload = objectMapper.writeValueAsString(codeRequest);

            InvokeRequest invokeRequest = new InvokeRequest()
                    .withFunctionName(arn)
                    .withPayload(payload);

            InvokeResult invokeResult = awsLambda.invoke(invokeRequest);
            String result = new String(invokeResult.getPayload().array());
            result = StringEscapeUtils.unescapeJson(result);
            result = result.replace("\\", "");
            result = result.replace("\"\"", "\"");
            result = result.substring(1, result.length() - 1);
            return result;
        } catch (Exception e) {
            log.info(e.getMessage());
            return "";
        }
    }

}
