package com.g44.kodeholik.service.aws.lambda;

import com.g44.kodeholik.model.dto.request.lambda.LambdaRequest;

public interface LambdaService {
    public String invokeLambdaFunction(LambdaRequest codeRequest);
}
