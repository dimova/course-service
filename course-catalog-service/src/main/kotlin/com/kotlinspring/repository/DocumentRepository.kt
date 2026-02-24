package com.kotlinspring.repository

import com.kotlinspring.entity.Document
import org.springframework.data.jpa.repository.JpaRepository

interface DocumentRepository : JpaRepository<Document, Long>