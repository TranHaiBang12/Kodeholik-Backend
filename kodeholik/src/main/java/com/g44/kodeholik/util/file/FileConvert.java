package com.g44.kodeholik.util.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

public class FileConvert {
    public static File convertMultiPartToFile(MultipartFile file) throws IOException {
        if (file != null) {
            File convFile = new File(file.getOriginalFilename());
            FileOutputStream fos = new FileOutputStream(convFile);
            fos.write(file.getBytes());
            fos.close();
            return convFile;
        } else {
            return null;
        }
    }

}
