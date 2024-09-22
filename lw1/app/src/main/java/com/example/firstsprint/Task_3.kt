package com.example.lw1

fun main() {
    println("Введите команду или 'exit' для выхода")

    while (true) {
        print("> ")
        val input = readlnOrNull() ?: continue
        val command = input.split(" ")

        when (command[0]) {
            "sum" -> if (command.size == 3) sum(
                command[1].toDouble(),
                command[2].toDouble()
            )

            "subtract" -> if (command.size == 3) subtract(
                command[1].toDouble(),
                command[2].toDouble()
            )

            "divide" -> if (command.size == 3) divide(
                command[1].toDouble(),
                command[2].toDouble()
            )

            "multiply" -> if (command.size == 3) multiply(
                command[1].toDouble(),
                command[2].toDouble()
            )

            "pow" -> if (command.size == 3) pow(
                command[1].toDouble(),
                command[2].toDouble()
            )

            "max" -> if (command.size > 1) max(command.drop(1))
            "min" -> if (command.size > 1) min(command.drop(1))
            "print_list" -> if (command.size > 1) printList(command.drop(1))
            "print_about" -> if (command.size == 3) printAbout(
                command[1],
                command[2].toInt()
            )

            "exit" -> {
                return
            }

            else -> println("Неизвестная команда. Попробуйте снова.")
        }
    }
}

fun sum(a: Double, b: Double) {
    println("Сумма: $a + $b = ${a + b}")
}

fun subtract(a: Double, b: Double) {
    val result = a - b
    println("Вычитание: $a - $b = $result")
}

fun divide(a: Double, b: Double) {
    if (b != 0.0) {
        val result: Double = a / b
        println("Деление: $a / $b = $result")
    } else {
        println("Ошибка: Деление на ноль!")
    }
}

fun multiply(a: Double, b: Double) {
    val result = a * b
    println("Умножение: $a * $b = $result")
}

fun pow(a: Double, b: Double) {
    val result = Math.pow(a, b)
    println("$a в степени $b равно $result")
}

fun max(numbers: List<String>) {
    val intList = numbers.map { it.toDouble() }
    val maxNum = intList.maxOrNull()
    println("Максимальное число: $maxNum")
}

fun min(numbers: List<String>) {
    val intList = numbers.map { it.toDouble() }
    val minNum = intList.minOrNull()
    println("Минимальное число: $minNum")
}

fun printList(numbers: List<String>) {
    val intList = numbers.map { it.toDouble() }.sorted()
    val resultString = intList.joinToString(" < ")
    println("Отсортированный список: $resultString")
}

fun printAbout(name: String, age: Int) {
    println("Привет, меня зовут $name, мне $age лет, через 5 лет мне будет ${age + 5} лет.")
}