package courier;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class CourierClient {

    @Step("Создаем курьера")
    public ValidatableResponse createCourier(CreateCourierData createCourierData) {
        return given().log().all()
                .header("Content-type", "application/json")
                .and()
                .body(createCourierData)
                .when()
                .post("/api/v1/courier")
                .then().log().all();
    }

    @Step("Авторизуемся в личном кабинете курьера")
    public ValidatableResponse loginCourier(LoginCourierData loginCourierData) {
        return given().log().all()
                .header("Content-type", "application/json")
                .and()
                .body(loginCourierData)
                .when()
                .post("/api/v1/courier/login")
                .then().log().all();
    }

    @Step("Авторизуемся и достаем из тела ответа ID нашего курьера")
    public Id getCourierId(LoginCourierData loginCourierData) {
        return given()
                .header("Content-type", "application/json")
                .body(loginCourierData)
                .post("/api/v1/courier/login")
                .body()
                .as(Id.class);
    }

    @Step("Удаляем курьера по ID")
    public ValidatableResponse deleteCourier(int idCourier) {
        return given().log().all()
                .header("Content-type", "application/json")
                .and()
                .body("{\"id\": " + idCourier + "}")
                .when()
                .delete("/api/v1/courier/" + idCourier)
                .then().log().all();
    }
}
