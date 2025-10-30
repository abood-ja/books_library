package aboodZaidLibrary;
import java.time.LocalDate;
import java.lang.String;  

public class clsDate {
    
    private int _Day = 1;
    private int _Month = 1;
    private int _Year = 1900;

    
    public clsDate() {
        LocalDate today = LocalDate.now();
        _Day = today.getDayOfMonth();
        _Month = today.getMonthValue();
        _Year = today.getYear();
    }
    public clsDate(clsDate date) {
    	
        this._Day = date._Day;
        this._Month = date._Month;
        this._Year = date._Year;
    }
    public clsDate(String sDate) {
        String[] vDate = sDate.split("/");
        int day,month,year;
        try {
             day = Integer.parseInt(vDate[0]);
            month = Integer.parseInt(vDate[1]);
            year = Integer.parseInt(vDate[2]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Day, month, and year must be valid integers. Given: " + sDate);
        }
        int maxDays = clsDate.numberOfDaysInAMonth(month, year);
        if (day < 1 || day > maxDays) {
            throw new IllegalArgumentException("Day must be between 1 and " + maxDays + ". Given: " + day);
        }
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Month must be between 1 and 12. Given: " + month);
        }
        if (year < 1) {
       	 throw new IllegalArgumentException("Year must be bigger than  0. Given: " + year);
        }
        _Day = day;
        _Month = month;
        _Year = year;
    }

    public clsDate(int day, int month, int year) {
    	 int maxDays = clsDate.numberOfDaysInAMonth(month, year);
         if (day < 1 || day > maxDays) {
             throw new IllegalArgumentException("Day must be between 1 and " + maxDays + ". Given: " + day);
         }
         if (month < 1 || month > 12) {
             throw new IllegalArgumentException("Month must be between 1 and 12. Given: " + month);
         }
         if (year < 1) {
        	 throw new IllegalArgumentException("Year must be bigger than  0. Given: " + year);
         }
    	
        _Day = day;
        _Month = month;
        _Year = year;
    }

    public int getDay() {
        return _Day;
    }

    public int getMonth() {
        return _Month;
    }

    public int getYear() {
        return _Year;
    }

    public void setDay(int day) {
    	int maxDays = clsDate.numberOfDaysInAMonth(this._Month, this._Year);
    	if (day < 1 || day > maxDays) {
            throw new IllegalArgumentException("Day must be between 1 and " + maxDays + ". Given: " + day);
        }
        _Day = day;
    }

    public void setMonth(int month) {
    	 if (month < 1 || month > 12) {
             throw new IllegalArgumentException("Month must be between 1 and 12. Given: " + month);
         }
        _Month = month;
    }

    public void setYear(int year) {
    	 if (year<1) {
        	 throw new IllegalArgumentException("Year must be bigger than  0. Given: " + year);
         }
        _Year = year;
    }

    public static boolean isLeapYear(int year) {
    	 if (year<1) {
        	 throw new IllegalArgumentException("Year must be bigger than  0. Given: " + year);
         }
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }
    public static clsDate getSystemDate() {
        java.time.LocalDate now = java.time.LocalDate.now();

        int day = now.getDayOfMonth();
        int month = now.getMonthValue();
        int year = now.getYear();

        return new clsDate(day, month, year);
    }
 
    public static String getSystemDateTimeString() {
        java.time.LocalDateTime now = java.time.LocalDateTime.now();

        int day = now.getDayOfMonth();
        int month = now.getMonthValue();
        int year = now.getYear();
        int hour = now.getHour();
        int minute = now.getMinute();
        int second = now.getSecond();

        return String.format("%02d/%02d/%04d - %02d:%02d:%02d",
                             day, month, year, hour, minute, second);
    }

    public boolean isLeapYear() {
        return isLeapYear(_Year);
    }
    public static int numberOfDaysInAYear(int year)
	{
    	 if (year<1) {
        	 throw new IllegalArgumentException("Year must be bigger than  0. Given: " + year);
         }
		return  isLeapYear(year) ? 366 : 365;
	}

    public int numberOfDaysInAYear()
	{
		return  numberOfDaysInAYear(_Year);
	}
	public static int numberOfHoursInAYear(int year)
	{
		 if (year<1) {
        	 throw new IllegalArgumentException("Year must be bigger than  0. Given: " + year);
         }
		return  numberOfDaysInAYear(year) * 24;
	}

	public int numberOfHoursInAYear()
	{
		return  numberOfHoursInAYear(_Year);
	}
	public static int numberOfMinutesInAYear(int year)
	{
		 if (year<1) {
        	 throw new IllegalArgumentException("Year must be bigger than  0. Given: " + year);
         }
		return  numberOfHoursInAYear(year) * 60;
	}

	public int numberOfMinutesInAYear()
	{
		return  numberOfMinutesInAYear(_Year);
	}
	
	public static int numberOfSecondsInAYear(int year)
	{
		 if (year<1) {
        	 throw new IllegalArgumentException("Year must be bigger than  0. Given: " + year);
         }
		return  numberOfMinutesInAYear(year) * 60;
	}
	
	public int numberOfSecondsInAYear()
	{
		return  numberOfSecondsInAYear(_Year);
	}
	
	public static int numberOfDaysInAMonth(int month, int year)
	{

		if (month < 1 || month>12)
			{
			throw new IllegalArgumentException("Month must be between 1 and 12. Given: " + month);
			}

		int days[] = { 31,28,31,30,31,30,31,31,30,31,30,31 };
		return (month == 2) ? (isLeapYear(year) ? 29 : 28) : days[month - 1];

	}

	public int numberOfDaysInAMonth()
	{
		return numberOfDaysInAMonth(_Month, _Year);
	}
   
	
	public static int numberOfHoursInAMonth(int month, int year)
	{
		return  numberOfDaysInAMonth(month, year) * 24;
	}

	public int numberOfHoursInAMonth()
	{
		return  numberOfDaysInAMonth(_Month, _Year) * 24;
	}
	
	public static int numberOfMinutesInAMonth(int month, int year)
	{
		return  numberOfHoursInAMonth(month, year) * 60;
	}

	public int numberOfMinutesInAMonth()
	{
		return  numberOfHoursInAMonth(_Month, _Year) * 60;
	}
	
	public static int numberOfSecondsInAMonth(int month, int year)
	{
		return  numberOfMinutesInAMonth(month, year) * 60;
	}

	public int numberOfSecondsInAMonth()
	{
		return  numberOfMinutesInAMonth(_Month, _Year) * 60;
	}
	
	public static int dayOfWeekOrder(int day, int month, int year)
	{
		int maxDays = clsDate.numberOfDaysInAMonth(month, year);
        if (day < 1 || day > maxDays) {
            throw new IllegalArgumentException("Day must be between 1 and " + maxDays + ". Given: " + day);
        }
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Month must be between 1 and 12. Given: " + month);
        }
        if (year < 1) {
       	 throw new IllegalArgumentException("Year must be bigger than  0. Given: " + year);
        }
		int a, y, m;
		a = (14 - month) / 12;
		y = year - a;
		m = month + (12 * a) - 2;
		
		return (day + y + (y / 4) - (y / 100) + (y / 400) + ((31 * m) / 12)) % 7;
	}

	public int dayOfWeekOrder()
	{
		return dayOfWeekOrder(_Day, _Month, _Year);
	}
	
	public static String dayShortName(int dayOfWeekOrder)
	{
		if(dayOfWeekOrder>6||dayOfWeekOrder<0) {
			throw new IllegalArgumentException("day must be from 0 to 6  . Given: " + dayOfWeekOrder);
		}
		String arrDayNames[] = { "Sun","Mon","Tue","Wed","Thu","Fri","Sat" };

		return arrDayNames[dayOfWeekOrder];

	}

	public static String dayShortName(int day, int month, int year)
	{
		int maxDays = clsDate.numberOfDaysInAMonth(month, year);
        if (day < 1 || day > maxDays) {
            throw new IllegalArgumentException("Day must be between 1 and " + maxDays + ". Given: " + day);
        }
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Month must be between 1 and 12. Given: " + month);
        }
        if (year < 1) {
       	 throw new IllegalArgumentException("Year must be bigger than  0. Given: " + year);
        }
		String arrDayNames[] = { "Sun","Mon","Tue","Wed","Thu","Fri","Sat" };

		return arrDayNames[dayOfWeekOrder(day, month, year)];
	}

	public String dayShortName()
	{

		String arrDayNames[] = { "Sun","Mon","Tue","Wed","Thu","Fri","Sat" };
		return arrDayNames[dayOfWeekOrder(_Day, _Month, _Year)];

	}
	
	public static String monthShortName(int monthNumber)
	{
		
        if (monthNumber < 1 || monthNumber > 12) {
            throw new IllegalArgumentException("Month must be between 1 and 12. Given: " + monthNumber);
        }
        
		String months[] = { "Jan", "Feb", "Mar",
						   "Apr", "May", "Jun",
						   "Jul", "Aug", "Sep",
						   "Oct", "Nov", "Dec"
		};

		return (months[monthNumber - 1]);
	}

	public String monthShortName()
	{

		return monthShortName(_Month);
	}
	
	public static void printMonthCalendar(int month, int year) {
        int numberOfDays;
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Month must be between 1 and 12. Given: " + month);
        }
        if (year < 1) {
       	 throw new IllegalArgumentException("Year must be bigger than  0. Given: " + year);
        }
        // Index of the day from 0 to 6
        int current = dayOfWeekOrder(1, month, year);
        
        numberOfDays = numberOfDaysInAMonth(month, year);
        System.out.printf("\n  _______________%s_______________\n\n", monthShortName(month));
        System.out.println("  Sun  Mon  Tue  Wed  Thu  Fri  Sat");
        int i;
        for (i = 0; i < current; i++) {
            System.out.print("     ");
        }
        for (int j = 1; j <= numberOfDays; j++) {
            System.out.printf("%5d", j);

            if (++i == 7) {
                i = 0;
                System.out.println();
            }
        }
        System.out.println("\n  _________________________________");
    }
	
	
	public void printMonthCalendar()
	{
		printMonthCalendar(_Month, _Year);
	}
	
	public static void printYearCalendar(int year) {
		if (year < 1) {
	       	 throw new IllegalArgumentException("Year must be bigger than  0. Given: " + year);
	        }
        System.out.println("\n  _________________________________\n");
        System.out.printf("           Calendar - %d\n", year);
        System.out.println("  _________________________________\n");

        for (int i = 1; i <= 12; i++) {
            printMonthCalendar(i, year);
        }
    }
	
	public void printYearCalendar() {
        System.out.println("\n  _________________________________\n");
        System.out.printf("           Calendar - %d\n", _Year);
        System.out.println("  _________________________________\n");

        for (int i = 1; i <= 12; i++) {
            printMonthCalendar(i, _Year);
        }
    }
	public static int daysFromTheBeginningOfTheYear(int day, int month, int year) {
		 int maxDays = clsDate.numberOfDaysInAMonth(month, year);
         if (day < 1 || day > maxDays) {
             throw new IllegalArgumentException("Day must be between 1 and " + maxDays + ". Given: " + day);
         }
         if (month < 1 || month > 12) {
             throw new IllegalArgumentException("Month must be between 1 and 12. Given: " + month);
         }
         if (year < 1) {
        	 throw new IllegalArgumentException("Year must be bigger than  0. Given: " + year);
         }
	    int totalDays = 0;

	    for (int i = 1; i < month; i++) {
	        totalDays += numberOfDaysInAMonth(i, year);
	    }

	    totalDays += day;

	    return totalDays;
	}
	public int daysFromTheBeginningOfTheYear() {
	    int totalDays = 0;
	    for (int i = 1; i < _Month; i++) {
	        totalDays += numberOfDaysInAMonth(i, _Year);
	    }

	    totalDays += _Day;

	    return totalDays;
	}
	
	public static clsDate getDateFromDayOrderInYear(int dateOrderInYear, int year) {
	    clsDate date = new clsDate();
	    int remainingDays = dateOrderInYear;
	    int monthDays = 0;

	    date.setYear(year); 
	    date.setMonth(1);

	    while (true) {
	        monthDays = numberOfDaysInAMonth(date.getMonth(), year);

	        if (remainingDays > monthDays) {
	            remainingDays -= monthDays;
	            date.setMonth(date.getMonth()+1);
	        } else {
	            date.setDay(remainingDays);
	            break;
	        }
	    }

	    return date;
	}
	
	
	public void addDays(int days) {
		if (days<0) {
           throw new IllegalArgumentException("days must be positive. Given: " + days);
		}
	    int remainingDays = days + daysFromTheBeginningOfTheYear(_Day, _Month, _Year);
	    int monthDays = 0;

	    _Month = 1;

	    while (true) {
	        monthDays = numberOfDaysInAMonth(_Month, _Year);

	        if (remainingDays > monthDays) {
	            remainingDays -= monthDays;
	            _Month++;

	            if (_Month > 12) {
	                _Month = 1;
	                _Year++;
	            }
	        } else {
	            _Day = remainingDays;
	            break;
	        }
	    }
	}
	
	
	public static boolean isDate1BeforeDate2(clsDate date1, clsDate date2) {
	    return (date1.getYear() < date2.getYear()) 
	        ? true 
	        : (date1.getYear() == date2.getYear() 
	            ? (date1.getMonth() < date2.getMonth() 
	                ? true 
	                : (date1.getMonth() == date2.getMonth() 
	                    ? date1.getDay() < date2.getDay() 
	                    : false)) 
	            : false);
	}
	
	public boolean isDateBeforeDate2(clsDate date2) {
	 
	    return isDate1BeforeDate2(this, date2);
	}
	
	public static boolean isDate1EqualDate2(clsDate date1, clsDate date2) {
	    return (date1.getYear() == date2.getYear()) 
	        ? (date1.getMonth() == date2.getMonth() 
	            ? (date1.getDay() == date2.getDay() 
	                ? true 
	                : false) 
	            : false) 
	        : false;
	}
	public boolean isDateEqualDate2(clsDate date2) {
	    return isDate1EqualDate2(this, date2);
	}
	
	public static int getDifferenceInDays(clsDate date1, clsDate date2) {
	    
	    clsDate d1 = new clsDate(date1);
	    clsDate d2 = new clsDate(date2);

	    int days = 0;
	    int swapFlag = 1;

	    if (!clsDate.isDate1BeforeDate2(d1, d2)) {
	        clsDate temp = d1;
	        d1 = d2;
	        d2 = temp;
	        swapFlag = -1;
	    }

	    // Count days
	    while (clsDate.isDate1BeforeDate2(d1, d2)) {
	        d1.addDays(1);
	        days++;
	    }

	 
	    return days * swapFlag;
	}
	
	public int getDifferenceInDays(clsDate date2)
	{
		return getDifferenceInDays(this, date2);
	}
	
	

}
	