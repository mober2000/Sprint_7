package couriertests;

import courier.*;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class LoginCourierTest {
    private final CourierGenerator generator = new CourierGenerator();
    private final String randomLogin = generator.randomLogin();

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
    }

    @Test
    @DisplayName("Enable login courier test")
    @Description("Проверяем можно ли авторизоваться в аккаунте")
    public void enableLoginCourierTest() {
        CourierClient courierClient = new CourierClient();
        ValidatableResponse createCourierResponse = courierClient.createCourier(new CreateCourierData(randomLogin, "1234", "y"));
        createCourierResponse.statusCode(201).assertThat().body("ok", equalTo(true));

        ValidatableResponse loginCourierResponse = courierClient.loginCourier(new LoginCourierData(randomLogin, "1234"));
        loginCourierResponse.statusCode(200).assertThat().body("id", notNullValue());

        Id idCourierData = courierClient.getCourierId(new LoginCourierData(randomLogin, "1234"));
        ValidatableResponse deleteCourierResponse = courierClient.deleteCourier(idCourierData.getId());
        deleteCourierResponse.statusCode(200).assertThat().body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Null Fields Login Courier Test")
    @Description("Проверяем поведение api при авторизации без логина либо без пароля")
    public void nullFieldsLoginCourierTest() {
        CourierClient courierClient = new CourierClient();
        ValidatableResponse loginNullPasswordFieldResponse = courierClient.loginCourier(new LoginCourierData(randomLogin, ""));
        loginNullPasswordFieldResponse.statusCode(400).assertThat().body("message", equalTo("Недостаточно данных для входа"));

        ValidatableResponse loginNullLoginFieldResponse = courierClient.loginCourier(new LoginCourierData("", "1234"));
        loginNullLoginFieldResponse.statusCode(400).assertThat().body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Not Existing Fields Login Courier Test")
    @Description("Проверяем поведение api при авторизации с несуществующими логином или паролем")
    public void notExistingFieldsLoginCourierTest() {
        CourierClient courierClient = new CourierClient();
        ValidatableResponse createCourierResponse = courierClient.createCourier(new CreateCourierData(randomLogin, "1234", "y"));
        createCourierResponse.statusCode(201).assertThat().body("ok", equalTo(true));

        ValidatableResponse loginNotExistingLoginCourierResponse = courierClient.loginCourier(new LoginCourierData(randomLogin + "3333333", "1234"));
        loginNotExistingLoginCourierResponse.statusCode(404).assertThat().body("message", equalTo("Учетная запись не найдена"));

        ValidatableResponse loginNotExistingPasswordCourierResponse = courierClient.loginCourier(new LoginCourierData(randomLogin, "4321"));
        loginNotExistingPasswordCourierResponse.statusCode(404).assertThat().body("message", equalTo("Учетная запись не найдена"));

        Id idCourierData = courierClient.getCourierId(new LoginCourierData(randomLogin, "1234"));
        ValidatableResponse deleteCourierResponse = courierClient.deleteCourier(idCourierData.getId());
        deleteCourierResponse.statusCode(200).assertThat().body("ok", equalTo(true));
    }
}
