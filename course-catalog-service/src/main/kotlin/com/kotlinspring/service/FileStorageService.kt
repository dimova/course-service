package com.kotlinspring.service

import com.kotlinspring.entity.Document
import com.kotlinspring.repository.DocumentRepository
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.nio.file.*


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
        val safeFileName = Paths.get(originalName).fileName.toString()

        // First, save the document metadata to get the ID
        val document = Document(
            fileName = safeFileName,
            contentType = file.contentType ?: "application/octet-stream",
            size = file.size,
            storagePath = "" // Temporary, will update after getting ID
        )

        val savedDocument = documentRepository.save(document)

        // Now use the document ID as the storage name
        val storageFileName = safeFileName
        val targetLocation = uploadDir.resolve(storageFileName).normalize()

        // Prevent path traversal attack
        require(targetLocation.parent == uploadDir) {
            "Invalid file path"
        }

        Files.copy(
            file.inputStream,
            targetLocation,
            StandardCopyOption.REPLACE_EXISTING
        )

        // Update the document with the actual storage path
        savedDocument.storagePath = targetLocation.toString()
        return documentRepository.save(savedDocument)
    }

    fun load(documentId: Long): Path {
        val doc = documentRepository.findById(documentId)
            .orElseThrow { RuntimeException("File not found with id: $documentId") }

        val path = Paths.get(doc.storagePath)
        if (!Files.exists(path)) {
            throw RuntimeException("File not found at path: ${doc.storagePath}")
        }

        return path
    }

    fun getDocumentFileName(documentId: Long): String {
        val doc = documentRepository.findById(documentId)
            .orElseThrow { RuntimeException("File not found with id: $documentId") }

        return doc.fileName
    }
}