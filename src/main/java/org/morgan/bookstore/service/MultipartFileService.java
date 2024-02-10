package org.morgan.bookstore.service;

import org.springframework.http.MediaType;
import jakarta.persistence.EntityNotFoundException;
import org.morgan.bookstore.enums.ImageType;
import org.morgan.bookstore.exception.ImageNotValidException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

@Service
public class MultipartFileService {

    private static final String FULL_PATH = "D:/My Projects/BookStore/backend/web/images/";

    public String uploadImage(MultipartFile file, ImageType type) {
        validate(file);
        String imageName = generateImageName(file.getOriginalFilename());
        String imagePath = switch (type) {
            case COVER ->  "cover/"+imageName;
            case THUMBNAIL -> "thumbnail/"+imageName;
        };

        Path path = Paths.get(FULL_PATH+imagePath);

        try (InputStream inputStream = file.getInputStream()){
            Files.copy(inputStream,path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return imagePath;
    }

    public byte[] downloadImage(String imagePath) {
        String path = FULL_PATH+imagePath;
        try {
            return Files.readAllBytes(new File(path).toPath());
        } catch (IOException e) {
            throw new EntityNotFoundException(e);
        }
    }

    public MediaType getContentType(String imagePath) {
        String extension = imagePath.substring(imagePath.lastIndexOf('.') + 1).toLowerCase();
        return switch (extension) {
            case "jpg", "jpeg" -> MediaType.IMAGE_JPEG;
            case "png" -> MediaType.IMAGE_PNG;
            default -> MediaType.APPLICATION_OCTET_STREAM;
        };
    }
    public void deleteImage(String imagePath) {
        Path path = Paths.get(FULL_PATH+imagePath);
        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new EntityNotFoundException(e);
        }
    }



    private static void validate(MultipartFile file) {
        if (file == null) {
            throw new ImageNotValidException("No image file was uploaded.");
        }

        String contentType = file.getContentType();
        String[] allowedExtensions = {"image/jpeg", "image/jpg", "image/png"};

        if (!Arrays.asList(allowedExtensions).contains(contentType)) {
            throw new ImageNotValidException(
                    String.format("Invalid image format. Only %s formats are allowed.", String.join(", ", allowedExtensions)));
        }

    }


    private  String generateImageName(String fileName) {
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        String randomUUID = UUID.randomUUID().toString();
        String extension = getExtension(fileName);

        return timestamp + "_" + randomUUID + "." + extension;
    }

    private String getExtension(String filename) {
        return filename.substring(filename.lastIndexOf(".") + 1);
    }

}
