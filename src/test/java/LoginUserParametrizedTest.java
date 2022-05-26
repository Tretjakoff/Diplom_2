import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.Login;
import model.User;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class LoginUserParametrizedTest {
    private final Login login;
    private final Integer statusCode;
    private String bearerToken;
    Integer id;


    @Parameterized.Parameters
    public static Object[][] getNewUserData() {
        return new Object[][]{
                {new Login("myname@yandex.ru", "qwerty"), 200},
                {new Login("myname@yandex.ru", "asdfgh"), 401},
                {new Login("myname@gmail.com", "qwerty"), 401},
                {new Login("myname@yandex.ru", ""), 401},
                {new Login("", "qwerty"), 401},
                {new Login(null, "qwerty"), 401},
                {new Login("myname@yandex.ru", null), 401},
        };
    }

    public LoginUserParametrizedTest(Login login, Integer statusCode) {
        this.login = login;
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
        Thread.sleep(1000);
    }

    @Test
    @DisplayName("Login user and check response")
    @Description("Parameterized test for /api/auth/login")
    public void loginUserTest() {

        Response response = new Requests().loginUser(login);
        response.then().assertThat()
                .statusCode(statusCode);
        if (response.statusCode() != 200) {
            response.then().assertThat()
                    .body("message", Matchers.is("email or password are incorrect"));
        }


    }

}
