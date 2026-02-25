package com.kotlinspring.entity

import jakarta.persistence.*


@Entity
@Table(name = "COURSES")
data class Course(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Int?,
    /* val name: String,
     val category: String*/
    var name: String,
    var category: String,
    @ManyToOne(
        fetch = FetchType.LAZY
    )
    @JoinColumn(name = "INSTRUCTOR_ID", nullable = false)
    val instructor: Instructor? = null,

    var description: String? = null,
    var fileName: String? = null
){
    override fun toString(): String {
        val instructorId = instructor?.id
        return "Course(id=$id, name='$name', category='$category', instructorId=$instructorId, description=$description, fileName=$fileName)"
    }
}