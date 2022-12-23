package ordertests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import order.OrdersClient;
import order.orderlist.OrderList;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ReturnOrdersListTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Test
    @DisplayName("Check order list")
    @Description("Проверяем, что нам возвращается список заказов")
    public void checkOrderList() {
        OrdersClient ordersClient = new OrdersClient();
        OrderList orderListBody = ordersClient.checkOrderListAndGetLimit();
        assertEquals(10, orderListBody.getPageInfo().getLimit());
    }
}
