package pl.currenda;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;

import nbp.GetExchangeRatesResponse;
import nbp.NBPService;
import nbp.PathDate;
import nbp.Rate;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ExchangeRates 
{
	private static final String API_NBP_URL = "http://api.nbp.pl/api/";
	private static final String DATE_FORMAT = "yyyy-MM-dd";
	private static final String TABLE_NAME = "C";
	private NBPService nbpService;
	private String currencyCode;
	private Date startDate;
	private Date endDate;
	
    public static void main( String[] args )
    {
    	ExchangeRates exchangeRates = new ExchangeRates();
    	exchangeRates.initRetrofit();
    	exchangeRates.getUserInputData();
    	exchangeRates.getExchangeRates();
        
    }
    
    public void initRetrofit() {
    	Retrofit retrofit = new Retrofit.Builder()
    		    .baseUrl(API_NBP_URL)
    		    .addConverterFactory(GsonConverterFactory.create())
    		    .build();

    		this.nbpService = retrofit.create(NBPService.class);
    		
    }
    
    public void getUserInputData() {
    	System.out.println( "Witaj w programie Currenda" );
    	this.getUserCurrencyCode();
    	this.getUserInputStartDate();
    	this.getUserInputEndDate();
        
    }
    
    public void getUserCurrencyCode() {
    	
        System.out.println( "Podaj kod waluty: " );
        
        Scanner scanner = new Scanner(System.in);
        this.currencyCode = scanner.nextLine();
    }
    
    
    
    public void getUserInputStartDate() {
    	
    	Scanner scanner = new Scanner(System.in);
        System.out.println("Podaj date poczatkowa w formacie: " + DATE_FORMAT);
        
        try {
        	this.startDate = this.getDateFromString(scanner.nextLine());
        }
        catch (ParseException e) {
        	System.out.println("Bledny format daty!");
        	this.getUserInputStartDate();
        }
        
    }
    
    public void getUserInputEndDate() {
    	
    	Scanner scanner = new Scanner(System.in);
        System.out.println("Podaj date koncowa w formacie: " + DATE_FORMAT);
        
        try {
        	this.endDate = this.getDateFromString(scanner.nextLine());
        }
        catch (ParseException e) {
        	System.out.println("Bledny format daty!");
        	this.getUserInputEndDate();
        }
        
    }
    
    public void getExchangeRates() {
    	this.nbpService.getExchangeRates(TABLE_NAME, this.currencyCode, new PathDate(this.startDate), new PathDate(this.endDate)).enqueue(new Callback<GetExchangeRatesResponse>() {
			
			public void onResponse(Call<GetExchangeRatesResponse> call, Response<GetExchangeRatesResponse> response) {
				if(response.isSuccessful()) {
					ExchangeRates.this.getExchangeRatesSuccess(response.body());
				}
				else {
					ExchangeRates.this.getExchangeRatesFail();
				}
				
			}
			
			public void onFailure(Call<GetExchangeRatesResponse> call, Throwable t) {
				ExchangeRates.this.getExchangeRatesFail();
				
			}
		});
    }
    
    private void getExchangeRatesSuccess(GetExchangeRatesResponse getExchangeRatesResponse) {
    	
    	double average = this.getExchangeRateAverage(getExchangeRatesResponse.rates);
    	System.out.printf("srednia wynosi:  %,.4f\n", average);
    	
    	double standardDeviation = this.getStandardDeviation(getExchangeRatesResponse.rates);
    	System.out.printf("odchylenie standardowe wynosi:  %,.4f\n", standardDeviation);

    	
    }
    
    private void getExchangeRatesFail() {
    	System.out.println("cos poszlo nie tak!");
    }
    
    private double getExchangeRateAverage(Rate[] rates) {
    	double sum = 0;
    	
    	for(Rate rate : rates) {
    		sum += rate.bid;
    		
    	}
    	
    	return sum/rates.length;
    }
    
    private double getStandardDeviation(Rate[] rates) {
    	double askSum = 0;
    	double askPowSum = 0;
    	for(Rate rate : rates) {
    		askSum += rate.ask;
    		askPowSum += Math.pow(rate.ask, 2);
    	}
    	
    	return Math.sqrt((askPowSum / rates.length) - Math.pow(askSum/rates.length,2));
    }
    
    public Date getDateFromString(String dateString) throws ParseException {
    	DateFormat format = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
    	return format.parse(dateString);
    }
}
