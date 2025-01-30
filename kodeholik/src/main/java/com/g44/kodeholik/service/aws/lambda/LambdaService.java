package com.g44.kodeholik.service.aws.lambda;

import com.g44.kodeholik.model.dto.request.lambda.CodeRequest;

public interface LambdaService {
    public String invokeLambdaFunction(CodeRequest codeRequest);
}
