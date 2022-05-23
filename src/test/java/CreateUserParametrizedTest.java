import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.User;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class CreateUserParametrizedTest {
    private final User user;
    private final Integer statusCode;
    private String bearerToken;
    Integer id;

    @Parameterized.Parameters
    public static Object[][] getNewUserData() {
        return new Object[][]{
                {new User("praktik@y.ru", "qwerty", "Pr"), 200},
                {new User("praktikum@yandex.ru", "qwerty", "Praktikum"), 403},
                {new User("", "qwerty", "Praktikum"), 403},
                {new User("praktikum@yandex.ru", "", "Praktikum"), 403},
                {new User("praktikum@yandex.ru", "qwerty", ""), 403},
                {new User(null, "qwerty", "Praktikum"), 403},
                {new User("praktikum@yandex.ru", null, "Praktikum"), 403},
                {new User("praktikum@yandex.ru", "qwerty", null), 403},
        };
    }

    public CreateUserParametrizedTest(User user, Integer statusCode) {
        this.user = user;
        this.statusCode = statusCode;
    }


    @After
    public void cleanUp() {
        if (statusCode == 200) {
            Response response = new Requests().deleteUser(bearerToken);
            response.then().assertThat()
                    .statusCode(202)
                    .body("message", Matchers.is("User successfully removed"));
        }
    }

    @Test
    @DisplayName("Create courier and check response")
    @Description("Parameterized test for /api/auth/register")
    public void createUserTest() throws InterruptedException {

        Response response = new Requests().createUser(user);
        response.then().assertThat()
                .statusCode(statusCode);
        if(statusCode == 200) {
            bearerToken = response.jsonPath().getString("accessToken").replace("Bearer ", "");
        }
        Thread.sleep(1000);

    }

}
