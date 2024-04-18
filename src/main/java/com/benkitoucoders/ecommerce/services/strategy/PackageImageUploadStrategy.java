package com.benkitoucoders.ecommerce.services.strategy;

import com.benkitoucoders.ecommerce.dao.ImageDao;
import com.benkitoucoders.ecommerce.entities.Image;
import com.benkitoucoders.ecommerce.exceptions.EntityAlreadyExistsException;
import com.benkitoucoders.ecommerce.mappers.ImageMapper;
import com.benkitoucoders.ecommerce.services.strategy.inter.ImageUploadStrategy;
import com.benkitoucoders.ecommerce.services.strategy.inter.ImagesUploadStrategy;
import com.benkitoucoders.ecommerce.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PackageImageUploadStrategy implements ImageUploadStrategy, ImagesUploadStrategy {
    private final ImageDao imageDao;
    private final ImageMapper imageMapper;

    @Override
    public void uploadImage(MultipartFile file, Long packageId) throws IOException {
        String folderPath = Constants.IMAGE_FOLDER_PATH + "packages/package_" + packageId + "/";
        String filePath = folderPath + file.getOriginalFilename();

        Optional<Image> existingImage = imageDao.findByFilePath(filePath);
        existingImage.ifPresent(image -> {
            throw new EntityAlreadyExistsException(String.format("This image with name %s already exists", file.getOriginalFilename()));
        });

        Image image = imageDao.save(
                Image.builder()
                        .name(file.getOriginalFilename())
                        .packageId(packageId)
                        .productId(null)
                        .categoryId(null)
                        .type(file.getContentType())
                        .filePath(filePath).build());

        // Create the package's folder if it doesn't exist
        Path path = Paths.get(folderPath);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        if (image != null) {
            file.transferTo(new File(filePath));
            //return "file uploaded successfully: " + filePath;
        } else {
            //return "Failed to save image entity.";
        }
    }

    @Override
    public void uploadImages(List<MultipartFile> images, Long packageId) throws IOException {
        for (MultipartFile image : images) {
            uploadImage(image, packageId);
        }
    }

}
