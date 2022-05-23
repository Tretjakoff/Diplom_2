import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.Login;
import model.User;

import static io.restassured.RestAssured.given;

public class Requests extends RestAssuredClient {

    @Step("Send POST request to /api/auth/register")
    public Response createUser(User user){
        return given()
                .spec(getBaseSpec())
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post("api/auth/register");
    }

    @Step("Send POST request to /api/auth/login")
    public Response loginUser(Login login){
        return given()
                .spec(getBaseSpec())
                .header("Content-type", "application/json")
                .and()
                .body(login)
                .when()
                .post("api/auth/login");
    }

    @Step("Send DELETE request to /api/auth/user")
    public Response deleteUser(String bearerToken){
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(bearerToken)
                .when()
                .delete("api/auth/user");
    }
}
