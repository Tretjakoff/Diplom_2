import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.Order;
import model.User;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

@RunWith(Parameterized.class)
public class OrderCreationParametrizedTest {
    private static final Requests requests = new Requests();
    private final Order order;
    private final Integer statusCode;
    private static String bearerToken;

    @Parameterized.Parameters
    public static Object[][] getNewUserData() {
        return new Object[][]{
                {new Order(Arrays.asList(requests.returnIngredients().get(0), requests.returnIngredients().get(1))), 200},
                {new Order(Arrays.asList(requests.returnIngredients().get(1), requests.returnIngredients().get(2))), 200},
                {new Order(Arrays.asList(requests.returnIngredients().get(0), requests.returnIngredients().get(1))), 200},
                {new Order(null), 400},
                {new Order(Arrays.asList()), 400},
                {new Order(Arrays.asList("qwert5678iuhgd", "asdfghjkl0987")), 500},
                {new Order(Arrays.asList("fjvmbhjkjuhyki5646")), 500},
        };
    }

    public OrderCreationParametrizedTest(Order order, Integer statusCode) {
        this.order = order;
        this.statusCode = statusCode;
    }

    @Before
    public void createNewUser() {
        Response response = new Requests().createUser(new User("myname@yandex.ru", "qwerty", "Myname"));
        response.then().assertThat()
                .statusCode(200);
        if (response.statusCode() == 200) {
            bearerToken = response.jsonPath().getString("accessToken").replace("Bearer ", "");
        }
    }

    @After
    public void cleanUp() throws InterruptedException {
        Response response = new Requests().deleteUser(bearerToken);
        response.then().assertThat()
                .statusCode(202)
                .body("message", Matchers.is("User successfully removed"));
        Thread.sleep(3000);
    }

    @Test
    @DisplayName("Order creation and check response")
    @Description("Parameterized test for /api/orders")
    public void changingUserDataTest() {
        Response response = new Requests().orderCreation(order, bearerToken);
        response.then().assertThat()
                .statusCode(statusCode);
        if (response.statusCode() == 200) {
            response.then().assertThat()
                    .body("order.number", Matchers.notNullValue());
            Assert.assertNotNull(response.jsonPath().get("order.ingredients"));
        } else if (response.statusCode() == 400) {
            response.then().assertThat()
                    .body("message", Matchers.is("Ingredient ids must be provided"));
        } else {
            Assert.assertEquals(response.getStatusLine(), "HTTP/1.1 500 Internal Server Error");
        }
    }

    @Test
    @DisplayName("Order creation and check response without authorization")
    @Description("Parameterized test for /api/orders")
    public void changingUserDataWithoutAuthTest() {
        Response response = new Requests().orderCreation(order, "");
        response.then().assertThat()
                .statusCode(statusCode);
        if (response.statusCode() == 200) {
            response.then().assertThat()
                    .body("order.number", Matchers.notNullValue());
            Assert.assertNull(response.jsonPath().get("order.ingredients"));
        } else if (response.statusCode() == 400) {
            response.then().assertThat()
                    .body("message", Matchers.is("Ingredient ids must be provided"));
        } else {
            Assert.assertEquals(response.getStatusLine(), "HTTP/1.1 500 Internal Server Error");
        }
    }
}
