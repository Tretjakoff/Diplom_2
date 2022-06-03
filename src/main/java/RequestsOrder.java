import io.qameta.allure.Step;
import io.restassured.response.Response;
import model.Order;

import java.util.List;

import static io.restassured.RestAssured.given;

public class RequestsOrder extends RestAssuredClient{
    private static final String endpointOrder = "api/orders";
    private static final String endpointIngredients = "api/ingredients";

    @Step("Send POST request to /api/orders")
    public Response orderCreation(Order order, String bearerToken) {
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(bearerToken)
                .header("Content-type", "application/json")
                .and()
                .body(order)
                .when()
                .post(endpointOrder);
    }

    @Step("Send GET request to /api/ingredients")
    public List<String> returnIngredients(){
        return given()
                .spec(getBaseSpec())
                .get(endpointIngredients)
                .jsonPath().getList("data._id");
    }

    @Step("Send GET request to /api/orders")
    public Response getUserOrders(String bearerToken){
        return given()
                .spec(getBaseSpec())
                .auth().oauth2(bearerToken)
                .when()
                .get(endpointOrder);
    }
}
