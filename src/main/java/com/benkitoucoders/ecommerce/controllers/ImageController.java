package com.benkitoucoders.ecommerce.controllers;

import com.benkitoucoders.ecommerce.services.inter.ImageService;
import com.benkitoucoders.ecommerce.dtos.ImageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
//@CrossOrigin(origins = "http://localhost:58213", allowCredentials = "true")
public class ImageController {

    private final ImageService imageService;

    @GetMapping
    public ResponseEntity<List<ImageDto>> getImagesByQuery(@RequestParam(name = "imageId", required = false) Long imageId,
                                                           @RequestParam(name = "imageName", required = false) String imageName,
                                                           @RequestParam(name = "imageType", required = false) String imageType,
                                                           @RequestParam(name = "imageFilePath", required = false) String imageFilePath,
                                                           @RequestParam(name = "productId", required = false) Long productId,
                                                           @RequestParam(name = "categoryId", required = false) Long categoryId
    ) {
        return ResponseEntity.ok().body(imageService.getImagesByQuery(imageId, imageName, imageType, imageFilePath, productId, categoryId));
    }

    @GetMapping("/{imageId}")
    public ResponseEntity<ImageDto> getImageById(@PathVariable Long imageId) {
        return ResponseEntity.ok().body(imageService.getImageById(imageId));
    }
    @PostMapping
    public ResponseEntity<ImageDto> addImage(@RequestBody ImageDto imageDto) {
        return ResponseEntity.ok().body(imageService.addImage(imageDto));
    }

    @PutMapping("/{imageId}")
    public ResponseEntity<ImageDto> updateImage(@PathVariable Long imageId, @RequestBody ImageDto imageDto) {
        return ResponseEntity.ok().body(imageService.updateImage(imageId, imageDto));
    }

    @DeleteMapping("/{imageId}")
    public ResponseEntity<?> deleteImageById(@PathVariable Long imageId) {
        return ResponseEntity.ok().body(imageService.deleteImageById(imageId));
    }

    @DeleteMapping("/byProduct/{productId}")
    public ResponseEntity<?> deleteImageByProductId(@PathVariable Long productId) throws IOException {
        return ResponseEntity.ok().body(imageService.deleteImagesByProductId(productId));
    }

    @DeleteMapping("/byCategory/{categoryId}")
    public ResponseEntity<?> deleteImageByCategoryId(@PathVariable Long categoryId) throws IOException {
        return ResponseEntity.ok().body(imageService.deleteImageByCategoryId(categoryId));
    }

    @PostMapping("/{productId}/uploadImages")
    public ResponseEntity<?> uploadImages(
            @RequestParam(name = "images", required = true) List<MultipartFile> images,
            @PathVariable(name = "productId", required = true) Long productId) throws IOException {

        imageService.uploadProductImages(images, productId);
        return ResponseEntity.ok(null);
    }

    @PostMapping("/uploadImage")
    public ResponseEntity<?> uploadCategoryImage(
            @RequestParam(name = "image", required = true) MultipartFile image,
            @RequestParam(name = "categoryId", required = true) Long categoryId) throws IOException {
        imageService.uploadCategoryImage(image, categoryId);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/getImages")
    public List<String> getImagesFromAwsInFolder(@RequestParam(name = "folderPath", required = false) String folderPath) throws IOException {
        folderPath = folderPath.replace('_', '/');
        return imageService.getImagesFromAwsInFolder(folderPath);
    }
}
