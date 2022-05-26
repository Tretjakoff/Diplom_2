import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.Login;
import model.Order;
import model.PersonalData;
import model.User;

import java.util.List;

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

    @Step("Send PATCH request to /api/auth/user")
    public Response changingUserData(PersonalData personalData, String bearerToken){
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(bearerToken)
                .header("Content-type", "application/json")
                .and()
                .body(personalData)
                .when()
                .patch("api/auth/user");
    }

    @Step("Send POST request to /api/orders")
    public Response orderCreation(Order order, String bearerToken) {
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(bearerToken)
                .header("Content-type", "application/json")
                .and()
                .body(order)
                .when()
                .post("api/orders");
    }

    @Step("Send GET request to /api/ingredients")
    public List<String> returnIngredients(){
        return given()
                .spec(getBaseSpec())
                .get("api/ingredients")
                .jsonPath().getList("data._id");
    }

    @Step("Send GET request to /api/orders")
    public Response getUserOrders(String bearerToken){
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(bearerToken)
                .when()
                .get("api/orders");
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
