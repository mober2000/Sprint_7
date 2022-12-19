package order;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import order.orderlist.OrderList;

import static io.restassured.RestAssured.given;

public class OrdersClient {
    @Step("Создаем заказ")
    public ValidatableResponse createOrder(CreateOrderData createOrderData) {
        return given().log().all()
                .header("Content-type", "application/json")
                .and()
                .body(createOrderData)
                .when()
                .post("/api/v1/orders")
                .then().log().all();
    }

    @Step("Получение списка заказов(лимит на 10 заказов)")
    public OrderList checkOrderListAndGetLimit() {
        return given()
                .header("Content-type", "application/json")
                .when()
                .get("/api/v1/orders?limit=10&page=0")
                .body()
                .as(OrderList.class);
    }
}
