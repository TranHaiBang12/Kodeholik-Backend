package com.g44.kodeholik.util.validation.file;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.log4j.Log4j2;

import org.springframework.web.multipart.MultipartFile;

@Log4j2
public class MaxFileSizeValidator implements ConstraintValidator<MaxFileSize, MultipartFile> {

    private long maxSize;

    @Override
    public void initialize(MaxFileSize constraintAnnotation) {
        this.maxSize = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null || file.isEmpty()) {
            return true; // Có thể xử lý logic khác nếu bạn muốn bắt buộc phải có file
        }
        log.info(file.getSize());
        return file.getSize() <= maxSize;
    }
}
