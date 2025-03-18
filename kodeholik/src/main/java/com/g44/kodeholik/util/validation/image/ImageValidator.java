package com.g44.kodeholik.util.validation.image;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class ImageValidator implements ConstraintValidator<ValidImage, MultipartFile> {

    private static final List<String> ALLOWED_EXTENSIONS = List.of("png", "jpeg", "jpg");

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null) {
            return true;
        }
        String filename = file.getOriginalFilename();
        String extension = filename.substring(filename.lastIndexOf(".") + 1);
        return ALLOWED_EXTENSIONS.contains(extension);
    }
}
