package battleships

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackages = ["battleships", "service", "repository"])
class BattleshipsApplication

fun main(args: Array<String>) {
    runApplication<BattleshipsApplication>(*args)
}
