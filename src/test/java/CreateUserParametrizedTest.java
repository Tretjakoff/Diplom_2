import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.User;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static org.apache.http.HttpStatus.*;

@RunWith(Parameterized.class)
public class CreateUserParametrizedTest {
    private final User user;
    private final String message;
    private static String bearerToken;

    @Parameterized.Parameters(name = "user, message: {0} {1}")
    public static Object[][] getNewUserData() {
        return new Object[][]{
                {new User("diplom@yandex.ru", "qwerty", "Diplom"), "User already exists"},
                {new User("", "qwerty", "Diplom"), "Email, password and name are required fields"},
                {new User("diplom@yandex.ru", "", "Diplom"), "Email, password and name are required fields"},
                {new User("diplom@yandex.ru", "qwerty", ""), "Email, password and name are required fields"},
                {new User(null, "qwerty", "Diplom"), "Email, password and name are required fields"},
                {new User("diplom@yandex.ru", null, "Diplom"), "Email, password and name are required fields"},
                {new User("diplom@yandex.ru", "qwerty", null), "Email, password and name are required fields"},
        };
    }

    public CreateUserParametrizedTest(User user, String message) {
        this.user = user;
        this.message = message;
    }

    @Before
    public void createNewUser() {
        Response response = new RequestsUser().createUser(new User("diplom@yandex.ru", "qwerty", "Diplom"));
        response.then().assertThat()
                .statusCode(SC_OK);
        if (response.statusCode() == 200) {
            bearerToken = response.jsonPath().getString("accessToken").replace("Bearer ", "");
        }
    }

    @After
    public void cleanUp() throws InterruptedException {
        Response response = new RequestsUser().deleteUser(bearerToken);
        response.then().assertThat()
                .statusCode(SC_ACCEPTED)
                .body("message", Matchers.is("User successfully removed"));
        Thread.sleep(3000);
    }


    @Test
    @DisplayName("Create user and check response")
    @Description("Parameterized test for /api/auth/register")
    public void createUserTest() {

        Response response = new RequestsUser().createUser(user);
        response.then().assertThat()
                .body("message", Matchers.is(message));

    }

}
