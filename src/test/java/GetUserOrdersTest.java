import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.User;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class GetUserOrdersTest {
    private static String bearerToken;

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
        Thread.sleep(1000);
    }

    @Test
    @DisplayName("Get user orders and check response")
    @Description("Parameterized test for /api/orders")
    public void getUserOrdersTest() {
        Response response = new Requests().getUserOrders(bearerToken);
        response.then().assertThat()
                .statusCode(200)
                .body("success", Matchers.is(true));
    }

    @Test
    @DisplayName("Get user orders and check response without authorization")
    @Description("Parameterized test for /api/orders")
    public void getUserOrdersWithoutAuthTest() {
        Response response = new Requests().getUserOrders("");
        response.then().assertThat()
                .statusCode(401)
                .body("success", Matchers.is(false))
                .body("message", Matchers.is("You should be authorised"));

    }
}
