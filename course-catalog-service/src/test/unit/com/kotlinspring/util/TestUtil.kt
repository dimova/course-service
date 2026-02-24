package com.kotlinspring.util

import com.kotlinspring.dto.CourseDTO
import com.kotlinspring.entity.Course
import com.kotlinspring.entity.Instructor

/*fun courseDTO(
    id: Int? = null,
    name: String = "Build RestFul APis using Spring Boot and Kotlin",
    category: String = "Dilip Sundarraj",
) = CourseDTO(
    id,
    name,
    category
)*/


fun courseEntityList() = listOf(
    Course(null,
        "Build RestFul APis using SpringBoot and Kotlin", "Development", null,
        "API-focused Spring Boot course"),
    Course(null,
        "Build Reactive Microservices using Spring WebFlux/SpringBoot", "Development"
        , null,
        "Reactive microservices with WebFlux"
    ),
    Course(null,
        "Wiremock for Java Developers", "Development" , null,
        "Wiremock fundamentals and testing"
    )
)

fun courseDTO(
    id: Int? = null,
    name: String = "Build RestFul APis using Spring Boot and Kotlin",
    category: String = "Development",
    instructorId: Int? = 1,
    description: String? = "API-focused Spring Boot course"
) = CourseDTO(
    id,
    name,
    category,
    instructorId,
    description
)

fun courseEntityList(instructor: Instructor? = null) = listOf(
    Course(null,
        "Build RestFul APis using SpringBoot and Kotlin", "Development",
        instructor,
        "API-focused Spring Boot course"),
    Course(null,
        "Build Reactive Microservices using Spring WebFlux/SpringBoot", "Development"
        ,instructor,
        "Reactive microservices with WebFlux"
    ),
    Course(null,
        "Wiremock for Java Developers", "Development" ,
        instructor,
        "Wiremock fundamentals and testing")
)

fun instructorEntity(name : String = "Dilip Sundarraj")
= Instructor(null, name)
