// Automating testing API https://reqres.in/api/users?page=1
// Used Serenity framework with Maven build automation
// Language: Kotlin
package tests

import io.restassured.RestAssured
import net.serenitybdd.junit.runners.SerenityRunner
import org.junit.Test

import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.hasKey

import org.junit.runner.RunWith

const val ROOT_API_URL = "https://reqres.in/api/users?page="
const val PAGE_NUMBER = 1
const val MAX_USERS = 12
// В задании были указаны довольные простые проверки потому решил не плодить
// множество различных классов с параметрами и т.д.
@RunWith(SerenityRunner::class)
class ApiTests {
    // проверка, что сервис всегда отдает фиксированное кол-во юзеров суммарно по всем страницам
    // тут можно либо вручную обходить все страницы и подсчитывать общее количество
    // либо проверить значение в поле total с константным значением 12
    @Test
    fun verifyTheNumberOfTotalUsers() {
        RestAssured.`when`().get(ROOT_API_URL + PAGE_NUMBER)
                .then().assertThat().statusCode(200)
                .and().body("total", `is`(MAX_USERS))
    }
    // валидация значений полей JSON структуры респонза, например проверяем, что юзера с id=1
    // зовут “George”
    // Тут сделал метод принимающий параметры которые должны быть проверены (в данном случае
    // id и имя)
    @Test
    fun verifyFirstIdUserNameIsGeorge()
    {
        verifyUserWithIdHasRequiredName(1,"George")
        verifyUserWithIdHasRequiredName(2,"Janet")
        verifyUserWithIdHasRequiredName(3,"Emma")
    }
    // валидация структуры объекта data в респонзе (т.е. что все обязательные поля всегда
    // присутствуют. Мы полагаем, что все поля mandatory)
    // Проходим все записи с указаной страницы и проверям что у всех присутствуют требуемые поля
    @Test
    fun verifyJsonDataObjectHasAllRequiredFields()
    {
        for(id in 1..2)
            verifyJsonDataObjectHasAllRequiredFieldsWithId(id)
    }
    // Параметризированные методы
    // Нахождение пользователи по id и проверка соответствия
    fun verifyUserWithIdHasRequiredName(id: Int, first_name: String)
    {
        RestAssured.`when`().get(ROOT_API_URL + PAGE_NUMBER)
                .then().assertThat().statusCode(200)
                .and().body("data.find {it.id == $id}.first_name",  `is`(first_name))
    }
    // Проверка что у записи Х присутсвуют все требуемые поля
    fun verifyJsonDataObjectHasAllRequiredFieldsWithId(id: Int)
    {
        RestAssured.`when`().get(ROOT_API_URL + PAGE_NUMBER)
                .then().assertThat().statusCode(200)
                .and()
                .body("data[$id]",  hasKey("id"))
                .body("data[$id]",  hasKey("email"))
                .body("data[$id]",  hasKey("first_name"))
                .body("data[$id]",  hasKey("last_name"))
    }
}