import io.swagger.client.ApiClient;
import io.swagger.client.ApiException;
import io.swagger.client.ApiResponse;
import io.swagger.client.api.SkiersApi;
import io.swagger.client.model.LiftRide;

public class ApiClientUseCaseTest {

        public static void main(String[] args) {
            ApiClient apiClient=new ApiClient();
            apiClient.setBasePath("http://localhost:8080/untitled_war_exploded/");
            SkiersApi skiersApi=new SkiersApi(apiClient);
            LiftRide liftRide=new LiftRide();
            liftRide.setLiftID(1);
            liftRide.setTime(1);
            try {
                ApiResponse<Void> voidApiResponse = skiersApi
                        .writeNewLiftRideWithHttpInfo(liftRide, 1, "2022", "1", 1);
                System.out.println(voidApiResponse.getStatusCode());
            } catch (ApiException e) {
                //返回400
                System.out.println("unsuccess with 400");
            }


        }

    }

