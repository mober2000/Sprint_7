import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import order.orderlist.OrderList;
import org.junit.Before;
import org.junit.Test;


import static io.restassured.RestAssured.given;


public class ReturnOrdersListTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Test
    public void CreateOrder() {
        OrderList ordersList =
                 given()
                        .when()
                        .get("/api/v1/orders")
                        .body()
                .as(OrderList.class);

    }

}
