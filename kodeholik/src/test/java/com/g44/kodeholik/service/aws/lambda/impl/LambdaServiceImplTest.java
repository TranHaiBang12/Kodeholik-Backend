package com.g44.kodeholik.service.aws.lambda.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import com.amazonaws.services.lambda.AWSLambda;
import com.amazonaws.services.lambda.model.InvokeRequest;
import com.amazonaws.services.lambda.model.InvokeResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.g44.kodeholik.exception.BadRequestException;
import com.g44.kodeholik.model.dto.request.lambda.LambdaRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class LambdaServiceImplTest {

    @Mock
    private AWSLambda awsLambda;

    @InjectMocks
    private LambdaServiceImpl lambdaServiceImpl;

    @Mock
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        ReflectionTestUtils.setField(lambdaServiceImpl, "arn",
                "arn:aws:lambda:ap-southeast-1:490004628975:function:compiler-code");
        ReflectionTestUtils.setField(lambdaServiceImpl, "region", "ap-southeast-1");
        ReflectionTestUtils.setField(lambdaServiceImpl, "awsLambda", awsLambda);

    }

    @Test
    public void testInvokeLambdaFunction() throws Exception {
        // Mock input request
        LambdaRequest request = new LambdaRequest();
        request.setCode("");
        request.setFunctionSignature("");
        request.setLanguage("Java");
        request.setReturnType("int");
        request.setTestCases(new ArrayList<>());

        // Mock InvokeResult trả về dữ liệu từ Lambda
        ByteBuffer byteBuffer = ByteBuffer.wrap("{\"result\":\"success\"}"
                .getBytes());
        InvokeResult mockResult = new InvokeResult()
                .withPayload(byteBuffer);
        when(awsLambda.invoke(any(InvokeRequest.class)))
                .thenReturn(mockResult);

        // Gọi phương thức cần test
        String response = lambdaServiceImpl.invokeLambdaFunction(request);

        // Kiểm tra kết quả trả về đúng format
        assertEquals("\"result\":\"success\"", response);

        // Kiểm tra xem phương thức `invoke` đã được gọi đúng với request
        verify(awsLambda, times(1))
                .invoke(any(InvokeRequest.class));
    }

    @Test
    public void testInvokeLambdaFunction_Exception() throws Exception {
        LambdaRequest lambdaRequest = new LambdaRequest();

        when(awsLambda.invoke(any(InvokeRequest.class))).thenThrow(
                new RuntimeException("Error"));

        BadRequestException badRequestException = assertThrows(
                BadRequestException.class,
                () -> lambdaServiceImpl.invokeLambdaFunction(lambdaRequest));
        assertEquals("Request on lambda failed", badRequestException
                .getMessage());
        assertEquals("Request on lambda failed", badRequestException
                .getDetails());
    }
}