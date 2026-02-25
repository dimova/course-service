package com.kotlinspring.controller

import com.kotlinspring.entity.Document
import com.kotlinspring.service.FileStorageService
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.UrlResource
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Paths

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
        val fileName = fileStorageService.getDocumentFileName(id)
        val resource = UrlResource(path.toUri())

        return ResponseEntity.ok()
            .header("Content-Disposition", "attachment; filename=\"$fileName\"")
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(resource)
    }

    @GetMapping("/serve/**")
    fun serveFile(): ResponseEntity<FileSystemResource> {
        // Extract the filename from the request path
        val request = org.springframework.web.context.request.RequestContextHolder.getRequestAttributes()
            as? org.springframework.web.context.request.ServletRequestAttributes
        val requestPath = request?.request?.requestURI ?: return ResponseEntity.badRequest().build()

        // Extract the file path after /files/serve/
        val filePath = requestPath.substringAfter("/files/serve/")

        if (filePath.isEmpty()) {
            return ResponseEntity.badRequest().build()
        }

        val file = Paths.get("/tmp/uploads", filePath)

        // Prevent path traversal attacks
        if (!file.normalize().startsWith(Paths.get("/tmp/uploads").normalize())) {
            return ResponseEntity.badRequest().build()
        }

        if (!Files.exists(file)) {
            return ResponseEntity.notFound().build()
        }

        val resource = FileSystemResource(file)
        val contentType = Files.probeContentType(file) ?: "application/octet-stream"

        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(contentType))
            .header("Content-Disposition", "inline; filename=\"${file.fileName}\"")
            .body(resource)
    }

    @GetMapping("/debug/tmp")
    fun tmp(): String {
        return System.getProperty("java.io.tmpdir")
    }
}