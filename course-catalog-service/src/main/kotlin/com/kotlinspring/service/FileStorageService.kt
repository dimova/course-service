package com.kotlinspring.service

import com.kotlinspring.entity.Document
import com.kotlinspring.repository.DocumentRepository
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.nio.file.*
import java.util.*

@Service
class FileStorageService(
    private val documentRepository: DocumentRepository
) {

    private val uploadDir: Path = Paths.get("/tmp/uploads")

    init {
        Files.createDirectories(uploadDir)
    }

    fun store(file: MultipartFile): Document {

        require(!file.isEmpty) { "Cannot store empty file." }
        require(file.size <= 10_000_000) { "File too large" }

        val allowedTypes = setOf("image/png", "image/jpeg", "application/pdf")
        require(file.contentType in allowedTypes) { "Invalid file type" }

        val originalName = file.originalFilename ?: "file"
        val safeFileName = UUID.randomUUID().toString() + "_" +
                Paths.get(originalName).fileName.toString()

        val targetLocation = uploadDir.resolve(safeFileName).normalize()

        // Prevent path traversal attack
        require(targetLocation.parent == uploadDir) {
            "Invalid file path"
        }

        Files.copy(
            file.inputStream,
            targetLocation,
            StandardCopyOption.REPLACE_EXISTING
        )

        val document = Document(
            fileName = safeFileName,
            contentType = file.contentType ?: "application/octet-stream",
            size = file.size,
            storagePath = targetLocation.toString()
        )

        return documentRepository.save(document)
    }

    fun load(documentId: Long): Path {
        val doc = documentRepository.findById(documentId)
            .orElseThrow { RuntimeException("File not found") }

        return Paths.get(doc.storagePath)
    }
}