package tests

import io.restassured.RestAssured
import net.serenitybdd.junit.runners.SerenityRunner
import org.junit.Test

import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.`hasKey`

import org.junit.runner.RunWith

const val ROOT_API_URL = "https://reqres.in/api/users?page="
const val PAGE_NUMBER = 1

@RunWith(SerenityRunner::class)
class ApiTests {
    @Test
    fun verifyTheNumberOfTotalUsers() {
        RestAssured.`when`().get(ROOT_API_URL + 1)
                .then().assertThat().statusCode(200)
                .and().body("total", `is`(12))
    }

    @Test
    fun verifyFirstIdUserNameIsGeorge()
    {
        verifyUserWithIdHasRequiredName(1,"George")
    }

    @Test
    fun verifyJsonDataObjectHasAllRequiredFields()
    {
        for(id in 1..2)
            verifyJsonDataObjectHasAllRequiredFieldsWithId(id);
    }
    fun verifyUserWithIdHasRequiredName(id: Int, first_name: String)
    {
        RestAssured.`when`().get(ROOT_API_URL + PAGE_NUMBER)
                .then().assertThat().statusCode(200)
                .and().body("data.find {it.id == $id}.first_name",  `is`(first_name))
    }
    fun verifyJsonDataObjectHasAllRequiredFieldsWithId(id: Int)
    {
        RestAssured.`when`().get(ROOT_API_URL + PAGE_NUMBER)
                .then().assertThat().statusCode(200)
                .and()
                .body("data[$id]",  `hasKey`("id"))
                .body("data[$id]",  `hasKey`("email"))
                .body("data[$id]",  `hasKey`("first_name"))
                .body("data[$id]",  `hasKey`("last_name"))
    }
}