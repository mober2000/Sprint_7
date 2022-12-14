import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import order.CreateOrderData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class CreateOrderTests {

    private List<String> color;

    public CreateOrderTests(List<String> color) {
        this.color = color;
    }

    @Parameterized.Parameters
    public static Object[][] dataGen() {
        return new Object[][] {
                {List.of("GRAY", "BLACK")},
                {List.of("GRAY")},
                {List.of("")},
        };
    }


    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
    }

    @Test
    @DisplayName("Create Order")
    @Description("Проверяем создается ли заказ с двумя цветами, одним цветом, без указания цвета")
    public void CreateOrder() {
        CreateOrderData createOrderData = new CreateOrderData("Alexey","Chebanov","Проспект мира, дом 5 корпус А", "Сокольники", "+7 999 888 77 66", 5, "07-04-2000", "Просьба везди аккуратно", color);
        ValidatableResponse responseCreate =
                given().log().all()
                        .header("Content-type", "application/json")
                        .and()
                        .body(createOrderData)
                        .when()
                        .post("/api/v1/orders")
                        .then().log().all()
                        .assertThat().body("track", notNullValue())
                        .and()
                        .statusCode(201);
    }
}
