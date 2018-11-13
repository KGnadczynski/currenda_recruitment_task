package nbp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PathDate {
		
			private static final String DATE_FORMAT = "yyyy-MM-dd";
			
		    private static final ThreadLocal<DateFormat> DF = new ThreadLocal<DateFormat>() {
		    
		    	@Override public DateFormat initialValue() {
		    		return new SimpleDateFormat(DATE_FORMAT);
		    }
		  };

		  private final Date date;

		  public PathDate(Date date) {
		    this.date = date;
		  }

		  @Override public String toString() {
		    return DF.get().format(date);
		  }
}

