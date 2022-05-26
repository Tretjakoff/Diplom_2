import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.PersonalData;
import model.User;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class ChangingUserDataParametrizedTest {
    private final PersonalData personalData;
    private static String bearerToken;
    Integer id;

    @Parameterized.Parameters
    public static Object[][] getNewUserData() {
        return new Object[][]{
                {new PersonalData("monday@yandex.ru", "Monday")},
                {new PersonalData("", "Tuesday")},
                {new PersonalData("wednesday@yandex.ru", "")},
                {new PersonalData(null, "Thursday")},
                {new PersonalData("friday@yandex.ru", null)},
                {new PersonalData(null, null)},
                {new PersonalData("", "")},
        };
    }

    public ChangingUserDataParametrizedTest(PersonalData personalData) {
        this.personalData = personalData;
    }

    @Before
    public void createNewUser() {
        Response response = new Requests().createUser(new User("diplom@yandex.ru", "qwerty", "Diplom"));
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
    @DisplayName("Changing user data and check response")
    @Description("Parameterized test for /api/auth/user")
    public void changingUserDataTest() {
        Response response = new Requests().changingUserData(personalData, bearerToken);
        if (response.statusCode() == 200) {
            response.then().assertThat()
                    .body("success", Matchers.is(true));
        } else {
            response.then().assertThat()
                    .body("message", Matchers.is("User with such email already exists"));
        }
    }

    @Test
    @DisplayName("Changing user data and check response without authorization")
    @Description("Parameterized test for /api/auth/user")
    public void changingUserDataWithoutAuthTest() {
        Response response = new Requests().changingUserData(personalData, "");
        response.then().assertThat()
                .body("message", Matchers.is("You should be authorised"));
    }
}
