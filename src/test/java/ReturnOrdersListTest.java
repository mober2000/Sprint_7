import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;

import order.orderlist.OrderList;
import org.junit.Before;
import org.junit.Test;


import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;


public class ReturnOrdersListTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Check order list")
    @Description("Проверяем, что нам возвращается список заказов")
    public void CheckOrderList() {
        OrderList ordersList =
                 given()
                         .header("Content-type", "application/json")
                         .when()
                         .get("/api/v1/orders?limit=10&page=0")
                         .body()
                         .as(OrderList.class);
        assertEquals(10, ordersList.getPageInfo().getLimit());

    }



}
