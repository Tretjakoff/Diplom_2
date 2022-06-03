import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.Login;
import model.PersonalData;
import model.User;


import static io.restassured.RestAssured.given;

public class RequestsUser extends RestAssuredClient {

    private static final String endpointUser = "api/auth/user";
    private static final String endpointLogin = "api/auth/login";
    private static final String endpointRegister = "api/auth/register";

    @Step("Send POST request to /api/auth/register")
    public Response createUser(User user){
        return given()
                .spec(getBaseSpec())
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post(endpointRegister);
    }

    @Step("Send POST request to /api/auth/login")
    public Response loginUser(Login login){
        return given()
                .spec(getBaseSpec())
                .header("Content-type", "application/json")
                .and()
                .body(login)
                .when()
                .post(endpointLogin);
    }

    @Step("Send PATCH request to /api/auth/user")
    public Response changingUserData(PersonalData personalData, String bearerToken){
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(bearerToken)
                .header("Content-type", "application/json")
                .and()
                .body(personalData)
                .when()
                .patch(endpointUser);
    }

    @Step("Send DELETE request to /api/auth/user")
    public Response deleteUser(String bearerToken){
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(bearerToken)
                .when()
                .delete(endpointUser);
    }

}
