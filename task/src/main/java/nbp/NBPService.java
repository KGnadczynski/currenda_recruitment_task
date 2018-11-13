package nbp;

import java.util.Date;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface NBPService {

	
		  @GET("exchangerates/rates/{table}/{code}/{startDate}/{endDate}")
		  Call<GetExchangeRatesResponse> getExchangeRates(@Path("table") String table,
				  										  @Path("code") String code,
				  										  @Path("startDate") PathDate startDate,
				  										  @Path("endDate") PathDate endDate);
}
  
