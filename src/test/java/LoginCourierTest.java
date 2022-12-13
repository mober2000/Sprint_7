import courier.CourierGenerator;
import courier.CreateCourierData;
import courier.LoginCourierData;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class LoginCourierTest {
    private final CourierGenerator generator = new CourierGenerator();

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
    }

    @Test
    public void enableLoginCourierTest() {
        String randomLogin = generator.randomLogin();

        CreateCourierData createCourierData = new CreateCourierData(randomLogin, "1234", "alexey");
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

        LoginCourierData loginCourierData = new LoginCourierData(randomLogin, "1234");
        ValidatableResponse responseLogin =
                given().log().all()
                        .header("Content-type", "application/json")
                        .and()
                        .body(loginCourierData)
                        .when()
                        .post("/api/v1/courier/login")
                        .then().log().all()
                        .assertThat().body("id", notNullValue())
                        .and()
                        .statusCode(200);
    }

        @Test
        public void nullFieldsLoginCourierTest(){
            String randomLogin = generator.randomLogin();

            LoginCourierData nullLoginFieldData = new LoginCourierData("", "1234");
            ValidatableResponse responseUnableLogin =
                    given().log().all()
                            .header("Content-type", "application/json")
                            .and()
                            .body(nullLoginFieldData)
                            .when()
                            .post("/api/v1/courier/login")
                            .then().log().all()
                            .assertThat().body("message", equalTo("Недостаточно данных для входа"))
                            .and()
                            .statusCode(400);

            LoginCourierData nullPasswordFieldData = new LoginCourierData(randomLogin, "");
            ValidatableResponse responseUnablePassword =
                    given().log().all()
                            .header("Content-type", "application/json")
                            .and()
                            .body(nullPasswordFieldData)
                            .when()
                            .post("/api/v1/courier/login")
                            .then().log().all()
                            .assertThat().body("message", equalTo("Недостаточно данных для входа"))
                            .and()
                            .statusCode(400);
        }

    @Test
    public void notExistingFieldsLoginCourierTest(){
        String randomLogin = generator.randomLogin();

        CreateCourierData createCourierData = new CreateCourierData(randomLogin, "1234", "alexey");
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

        LoginCourierData notExistingLoginFieldData = new LoginCourierData(randomLogin + "3333333", "1234");
        ValidatableResponse responseNotExistingLogin =
                given().log().all()
                        .header("Content-type", "application/json")
                        .and()
                        .body(notExistingLoginFieldData)
                        .when()
                        .post("/api/v1/courier/login")
                        .then().log().all()
                        .assertThat().body("message", equalTo("Учетная запись не найдена"))
                        .and()
                        .statusCode(404);

        LoginCourierData notExistingPasswordFieldData = new LoginCourierData(randomLogin, "4321");
        ValidatableResponse responseNotExistingPassword =
                given().log().all()
                        .header("Content-type", "application/json")
                        .and()
                        .body(notExistingPasswordFieldData)
                        .when()
                        .post("/api/v1/courier/login")
                        .then().log().all()
                        .assertThat().body("message", equalTo("Учетная запись не найдена"))
                        .and()
                        .statusCode(404);
    }
}
