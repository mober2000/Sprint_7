package couriertests;

import courier.*;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class CreateCourierTest {
    private final CourierGenerator generator = new CourierGenerator();
    private final String randomLogin = generator.randomLogin();

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
    }

    @Test
    @DisplayName("Create Courier Test")
    @Description("Проверяем создается ли курьер и нельзя ли его создать повторно")
    public void createCourierTest() {
        CourierClient courierClient = new CourierClient();
        ValidatableResponse createCourierResponse = courierClient.createCourier(new CreateCourierData(randomLogin, "1234", "alexeus"));
        createCourierResponse.statusCode(201).assertThat().body("ok", equalTo(true));

        ValidatableResponse createDuplicateCourierResponse = courierClient.createCourier(new CreateCourierData(randomLogin, "1234", "alexeus"));
        createDuplicateCourierResponse.statusCode(409).assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."));

        Id idCourierData = courierClient.getCourierId(new LoginCourierData(randomLogin, "1234"));
        ValidatableResponse deleteCourierResponse = courierClient.deleteCourier(idCourierData.getId());
        deleteCourierResponse.statusCode(200).assertThat().body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Create Null Fields Courier Test")
    @Description("Проверяем создается ли курьер с пустыми полями пароля или логина")
    public void createNullFieldsCourierTest() {
        CourierClient courierClient = new CourierClient();
        ValidatableResponse createNullPasswordFieldResponse = courierClient.createCourier(new CreateCourierData(randomLogin, "", "alexeus"));
        createNullPasswordFieldResponse.statusCode(400).assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));

        ValidatableResponse createNullLoginFieldResponse = courierClient.createCourier(new CreateCourierData("", "1234", "alexeus"));
        createNullLoginFieldResponse.statusCode(400).assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }
}
