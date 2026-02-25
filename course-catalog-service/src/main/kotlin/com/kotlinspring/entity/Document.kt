package com.kotlinspring.entity

import jakarta.persistence.*

@Entity
@Table(name = "documents")
class Document(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val fileName: String,
    val contentType: String,
    val size: Long,
    var storagePath: String
)