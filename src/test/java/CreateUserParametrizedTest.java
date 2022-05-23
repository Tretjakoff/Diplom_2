import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.User;
import org.hamcrest.Matchers;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class CreateUserParametrizedTest {
    private final User user;
    private final String message;
    private static String bearerToken;
    Integer id;

    @Parameterized.Parameters
    public static Object[][] getNewUserData() {
        return new Object[][]{
                {new User("diplom@yandex.ru", "qwerty", "Diplom"), "User already exists"},
                {new User("", "qwerty", "Diplom"),  "Email, password and name are required fields"},
                {new User("diplom@yandex.ru", "", "Diplom"),  "Email, password and name are required fields"},
                {new User("diplom@yandex.ru", "qwerty", ""),  "Email, password and name are required fields"},
                {new User(null, "qwerty", "Diplom"),  "Email, password and name are required fields"},
                {new User("diplom@yandex.ru", null, "Diplom"),  "Email, password and name are required fields"},
                {new User("diplom@yandex.ru", "qwerty", null),  "Email, password and name are required fields"},
        };
    }

    public CreateUserParametrizedTest(User user, String message) {
        this.user = user;
        this.message = message;
    }

    @Before
    public void createNewUser() {
        Response response = new Requests().createUser(new User("diplom@yandex.ru", "qwerty", "Diplom"));
        response.then().assertThat()
                .statusCode(200);
        if(response.statusCode() == 200) {
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
    @DisplayName("Create user and check response")
    @Description("Parameterized test for /api/auth/register")
    public void createUserTest(){

        Response response = new Requests().createUser(user);
        response.then().assertThat()
                .body("message", Matchers.is(message));

    }

}
