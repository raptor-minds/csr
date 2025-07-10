package com.blockchain.csr.controller;

import com.blockchain.csr.model.entity.Photo;
import com.blockchain.csr.repository.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/photos")
public class PhotoController {
    @Autowired
    private PhotoRepository photoRepository;

    @GetMapping("/{id}")
    public Photo getPhotoById(@PathVariable Long id) {
        return photoRepository.findById(id).orElse(null);
    }
}