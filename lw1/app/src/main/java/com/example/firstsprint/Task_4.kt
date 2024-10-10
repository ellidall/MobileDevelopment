package com.example.lw1

import java.util.*

data class Word(val value: String)
data class Context(val name: String)
data class Translate(val value: String)

typealias TranslationList = MutableList<Translate>
typealias ContextMap = MutableMap<Context, TranslationList>
typealias Dictionary = MutableMap<Word, ContextMap>

class Translator {
    private val dictionary: Dictionary = mutableMapOf()

    fun add(word: Word, context: Context, translate: Translate): Boolean {
        val contextMap = dictionary.getOrPut(word) { mutableMapOf() }
        val translations = contextMap.getOrPut(context) { mutableListOf() }
        return translations.add(translate)
    }

    fun remove(word: Word, context: Context, translate: Translate) {
        val translations = dictionary[word]?.get(context)
        translations?.remove(translate)

        if (translations != null && translations.isEmpty()) {
            dictionary[word]?.remove(context)
        }
    }

    fun getTranslate(word: Word): ContextMap {
        return dictionary[word] ?: mutableMapOf()
    }

    fun words(): Dictionary {
        return dictionary
    }
}

fun main() {
    val scanner = Scanner(System.`in`)
    val translator = Translator()
    println("Добро пожаловать в словарь. Введите команду или 'exit' для выхода")

    while (true) {
        print("> ")
        val input = scanner.nextLine().trim()
        val commands = input.split(" ")

        when (commands[0]) {
            "add" -> if (commands.size == 4) {
                val word = commands[1]
                val context = commands[2]
                val translation = commands[3]
                if (translator.add(
                        Word(commands[1]),
                        Context(commands[2]),
                        Translate(commands[3])
                    )
                ) {
                    println("Добавлен перевод '${translation}' для слова '${word}' в контексте '${context}'.")
                } else {
                    println("Перевод '${translation}' уже существует для слова '${word}' в контексте '${context}'.")
                }
            } else {
                println("Неверный формат команды. Используйте: add <word> <context> <translate>")
            }

            "remove" -> if (commands.size == 4) {
                try {
                    translator.remove(
                        Word(commands[1]),
                        Context(commands[2]),
                        Translate(commands[3])
                    )
                } catch (e: Exception) {
                    println("Произошла ошибка при удалении перевода: ${e.message}. Слово отсутствует в словаре.")
                }
            } else {
                println("Неверный формат команды. Используйте: remove <word> <context> <translate>")
            }

            "translate" -> if (commands.size == 2) {
                val word = commands[1]
                val translations = translator.getTranslate(Word(word))
                if (translations.isNotEmpty()) {
                    println("Переводы слова \"${word}\":")
                    for ((context, translates) in translations) {
                        println("В контексте \"${context.name}\": ${translates.joinToString(", ") { it.value }}")
                    }
                } else {
                    println("Нет переводов для слова \"${word}\".")
                }
            } else {
                println("Неверный формат команды. Используйте: translate <word>")
            }

            "print" -> {
                val allWords = translator.words()
                if (allWords.isNotEmpty()) {
                    for ((word, contextMap) in allWords) {
                        println("-----------------------------------")
                        println("Переводы слова \"${word.value}\":")
                        for ((context, translates) in contextMap) {
                            println("В контексте \"${context.name}\": ${translates.joinToString(", ") { it.value }}")
                        }
                        println("-----------------------------------")
                    }
                } else {
                    println("Словарь пуст.")
                }
            }

            "exit" -> {
                println("Выход из программы.")
                return
            }

            else -> println("Неизвестная команда. Попробуйте снова.")
        }
    }
}