package com.benkitoucoders.ecommerce.controllers;

import com.benkitoucoders.ecommerce.criteria.PackageCriteria;
import com.benkitoucoders.ecommerce.dtos.PackageDto;
import com.benkitoucoders.ecommerce.exceptions.EntityNotFoundException;
import com.benkitoucoders.ecommerce.services.inter.PackageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/packages")
@RequiredArgsConstructor
//@CrossOrigin(origins = "http://localhost:58213", allowCredentials = "true")
public class PackageController {

    private final PackageService packageService;

    @GetMapping
    public ResponseEntity<?> findpackagesByCriteria(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "id", required = false) Long id,
            @RequestParam(name = "active", required = false) String active,
            @RequestParam(name = "isDefault", required = false) String isDefault,
            @RequestParam(defaultValue = "0") int page, // Default page number
            @RequestParam(defaultValue = "10") int size, // Default page size
            @RequestParam(defaultValue = "id") String sortProperty, // Sort property
            @RequestParam(defaultValue = "ASC") String sortDirection
    ) throws EntityNotFoundException {
        PackageCriteria packagesCriteria = new PackageCriteria();
        packagesCriteria.setName(name);
        packagesCriteria.setId(id);
        packagesCriteria.setActive(active);
        packagesCriteria.setIsDefault(isDefault);

        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortProperty);
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(packageService.findPackagesByCriteria(packagesCriteria, pageable));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> findpackagesById(@PathVariable Long id) throws EntityNotFoundException {
        return ResponseEntity.ok(packageService.findPackagesById(id));
    }

    @PostMapping
    public ResponseEntity<?> persistpackages(@RequestBody PackageDto packagesDto) throws EntityNotFoundException, IOException {
        return ResponseEntity.ok(packageService.persistPackages(packagesDto));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> updatepackages(@PathVariable Long id, @RequestBody PackageDto packagesDto) throws EntityNotFoundException {
        return ResponseEntity.ok(packageService.updatePackages(id, packagesDto));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deletepackagesById(@PathVariable Long id) throws EntityNotFoundException {
        return ResponseEntity.ok(packageService.deletePackagesById(id));
    }
}
