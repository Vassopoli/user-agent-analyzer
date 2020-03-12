package br.com.uol.pagseguro.poc.useragent

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class UserAgentApplication

fun main(args: Array<String>) {
	runApplication<UserAgentApplication>(*args)
}
