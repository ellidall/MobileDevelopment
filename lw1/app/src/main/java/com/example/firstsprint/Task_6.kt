package com.example.firstsprint

import java.util.*

data class Student(
    val id: Int,
    var name: String,
    var age: Int,
    var points: Int
)

class StudentManager {
    private val students = mutableListOf<Student>()
    private var nextId = 1

    fun addStudent(parts: List<String>) {
        if (parts.size != 3) {
            println("Неверный формат данных студента.")
            return
        }
        val name = parts[0]
        val age = parts[1].toIntOrNull() ?: run {
            println("Возраст должен быть числом.")
            return
        }
        val points = parts[2].toIntOrNull() ?: run {
            println("Баллы должны быть числом.")
            return
        }
        students.add(Student(nextId++, name, age, points))
        println("Студент $name добавлен.")
    }

    fun removeStudent(id: Int) {
        val student = students.find { it.id == id }
        if (student != null) {
            students.remove(student)
            println("Студент ${student.name} удален.")
        } else {
            println("Студент с ID $id не найден.")
        }
    }

    fun updatePoints(id: Int, newPoints: Int) {
        val student = students.find { it.id == id }
        if (student != null) {
            student.points = newPoints
            println("Баллы студента ${student.name} обновлены на $newPoints.")
        } else {
            println("Студент с ID $id не найден.")
        }
    }

    fun renameStudent(id: Int, newName: String) {
        val student = students.find { it.id == id }
        if (student != null) {
            student.name = newName
            println("Студент переименован в $newName.")
        } else {
            println("Студент с ID $id не найден.")
        }
    }

    fun printSortedByPoints() {
        val sortedStudents = students.sortedByDescending { it.points }
        for (student in sortedStudents) {
            println("${student.name} (${student.age} лет) - ${student.points} балла")
        }
    }

    fun printSortedByNames() {
        val sortedStudents = students.sortedBy { it.name }
        for (student in sortedStudents) {
            println("${student.name} (${student.age} лет) - ${student.points} балла")
        }
    }
}

fun main() {
    val scanner = Scanner(System.`in`)
    val studentManager = StudentManager()

    println("Введите список студентов в формате '<имя> <возраст> <балл>', разделенных запятыми:")
    val input = scanner.nextLine()
    input.split(", ").forEach { studentInfo ->
        studentManager.addStudent(studentInfo.split(' '))
    }

    while (true) {
        print(">")
        val commands = scanner.nextLine().trim().split(" ")

        when (commands[0]) {
            "add" -> studentManager.addStudent(commands.drop(1))
            "remove" -> if (commands.size > 1) studentManager.removeStudent(commands[1].toInt()) else println("ID не указан.")
            "update_points" -> if (commands.size > 2) studentManager.updatePoints(commands[1].toInt(), commands[2].toInt()) else println("ID или новые баллы не указаны.")
            "rename" -> if (commands.size > 2) studentManager.renameStudent(commands[1].toInt(), commands[2]) else println("ID или новое имя не указаны.")
            "print_sort_by_points" -> studentManager.printSortedByPoints()
            "print_sort_by_names" -> studentManager.printSortedByNames()
            "exit" -> {
                println("Выход из программы.")
                return
            }
            else -> println("Неизвестная команда.")
        }
    }
}