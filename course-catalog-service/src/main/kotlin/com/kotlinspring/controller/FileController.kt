package com.kotlinspring.controller

import com.kotlinspring.entity.Document
import com.kotlinspring.service.FileStorageService
import org.springframework.core.io.UrlResource
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/files")
class FileController(
    private val fileStorageService: FileStorageService
) {

    @PostMapping("/upload")
    fun upload(@RequestParam("file") file: MultipartFile): Document {
        return fileStorageService.store(file)
    }

    @GetMapping("/{id}")
    fun download(@PathVariable id: Long): ResponseEntity<UrlResource> {
        val path = fileStorageService.load(id)
        val resource = UrlResource(path.toUri())

        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(resource)
    }
    @GetMapping("/debug/tmp")
    fun tmp(): String {
        return System.getProperty("java.io.tmpdir")
    }
}