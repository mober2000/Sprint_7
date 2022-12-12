import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CreateCourier {
    private final CourierGenerator generator = new CourierGenerator();
    private String randomLogin = generator.randomLogin();

    CreateCourierData createCourierData = new CreateCourierData(randomLogin, "1234", "alexey");
    CreateCourierData nullPasswordCreateCourierData = new CreateCourierData(randomLogin, "", "alexeus");
    CreateCourierData nullLoginCreateCourierData = new CreateCourierData("", "1234", "alexeus");

    ValidatableResponse responseCreate =
            given().log().all()
                    .header("Content-type", "application/json")
                    .and()
                    .body(createCourierData)
                    .when()
                    .post("/api/v1/courier")
                    .then().log().all()
                    .assertThat().body("ok", equalTo(true))
                    .and()
                    .statusCode(201);

    ValidatableResponse responseDuplicate =
            given().log().all()
                    .header("Content-type", "application/json")
                    .and()
                    .body(createCourierData)
                    .when()
                    .post("/api/v1/courier")
                    .then().log().all()
                    .assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."))
                    .and()
                    .statusCode(409);


    ValidatableResponse responseUnablePassword =
            given().log().all()
                    .header("Content-type", "application/json")
                    .and()
                    .body(nullPasswordCreateCourierData)
                    .when()
                    .post("/api/v1/courier")
                    .then().log().all()
                    .assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"))
                    .and()
                    .statusCode(400);

    ValidatableResponse responseUnableLogin =
            given().log().all()
                    .header("Content-type", "application/json")
                    .and()
                    .body(nullLoginCreateCourierData)
                    .when()
                    .post("/api/v1/courier")
                    .then().log().all()
                    .assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"))
                    .and()
                    .statusCode(400);
}
