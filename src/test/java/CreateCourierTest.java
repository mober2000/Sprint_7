import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CreateCourierTest {
    private final CourierGenerator generator = new CourierGenerator();

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
    }


    @Test
    public void createCourierTest() {

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

        CreateCourierData createDuplicateCourierData = new CreateCourierData(randomLogin, "12345", "alexeus");
        ValidatableResponse responseDuplicate =
                given().log().all()
                        .header("Content-type", "application/json")
                        .and()
                        .body(createDuplicateCourierData)
                        .when()
                        .post("/api/v1/courier")
                        .then().log().all()
                        .assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."))
                        .and()
                        .statusCode(409);
    }

        @Test
        public void createNullFieldsCourierTest() {
            String randomLogin = generator.randomLogin();

            CreateCourierData nullPasswordCreateCourierData = new CreateCourierData(randomLogin, "", "alexeus");
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

            CreateCourierData nullLoginCreateCourierData = new CreateCourierData("", "1234", "alexeus");
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

    }
