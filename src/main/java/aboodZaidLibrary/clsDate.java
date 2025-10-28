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

    public clsDate(String sDate) {
        String[] vDate = sDate.split("/");
        _Day = Integer.parseInt(vDate[0]);
        _Month = Integer.parseInt(vDate[1]);
        _Year = Integer.parseInt(vDate[2]);
    }

    public clsDate(int Day, int Month, int Year) {
        _Day = Day;
        _Month = Month;
        _Year = Year;
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
        _Day = day;
    }

    public void setMonth(int month) {
        _Month = month;
    }

    public void setYear(int year) {
        _Year = year;
    }

    public static boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
    }

    public boolean isLeapYear() {
        return isLeapYear(_Year);
    }
    static int NumberOfDaysInAYear(int year)
	{
		return  isLeapYear(year) ? 365 : 364;
	}

	int NumberOfDaysInAYear()
	{
		return  NumberOfDaysInAYear(_Year);
	}
	static int NumberOfHoursInAYear(int year)
	{
		return  NumberOfDaysInAYear(year) * 24;
	}

	int NumberOfHoursInAYear()
	{
		return  NumberOfHoursInAYear(_Year);
	}
	static int NumberOfMinutesInAYear(int year)
	{
		return  NumberOfHoursInAYear(year) * 60;
	}

	int NumberOfMinutesInAYear()
	{
		return  NumberOfMinutesInAYear(_Year);
	}
	
	static int NumberOfSecondsInAYear(int year)
	{
		return  NumberOfMinutesInAYear(year) * 60;
	}
	
	int NumberOfSecondsInAYear()
	{
		return  NumberOfSecondsInAYear();
	}
	
	static int numberOfDaysInAMonth(int month, int year)
	{

		if (month < 1 || month>12)
			return  0;

		int days[] = { 31,28,31,30,31,30,31,31,30,31,30,31 };
		return (month == 2) ? (isLeapYear(year) ? 29 : 28) : days[month - 1];

	}

	int numberOfDaysInAMonth()
	{
		return numberOfDaysInAMonth(_Month, _Year);
	}
   
	
	static int numberOfHoursInAMonth(int month, int year)
	{
		return  numberOfDaysInAMonth(month, year) * 24;
	}

	int numberOfHoursInAMonth()
	{
		return  numberOfDaysInAMonth(_Month, _Year) * 24;
	}
	
	static int numberOfMinutesInAMonth(int month, int year)
	{
		return  numberOfHoursInAMonth(month, year) * 60;
	}

	int numberOfMinutesInAMonth()
	{
		return  numberOfHoursInAMonth(_Month, _Year) * 60;
	}
	
	static int numberOfSecondsInAMonth(int month, int year)
	{
		return  numberOfMinutesInAMonth(month, year) * 60;
	}

	int NumberOfSecondsInAMonth()
	{
		return  numberOfMinutesInAMonth(_Month, _Year) * 60;
	}
	
	static int dayOfWeekOrder(int day, int month, int year)
	{
		int a, y, m;
		a = (14 - month) / 12;
		y = year - a;
		m = month + (12 * a) - 2;
		
		return (day + y + (y / 4) - (y / 100) + (y / 400) + ((31 * m) / 12)) % 7;
	}

	int dayOfWeekOrder()
	{
		return dayOfWeekOrder(_Day, _Month, _Year);
	}
	
	static String dayShortName(int dayOfWeekOrder)
	{
		String arrDayNames[] = { "Sun","Mon","Tue","Wed","Thu","Fri","Sat" };

		return arrDayNames[dayOfWeekOrder];

	}

	static String dayShortName(int day, int month, int year)
	{

		String arrDayNames[] = { "Sun","Mon","Tue","Wed","Thu","Fri","Sat" };

		return arrDayNames[dayOfWeekOrder(day, month, year)];

	}

	String dayShortName()
	{

		String arrDayNames[] = { "Sun","Mon","Tue","Wed","Thu","Fri","Sat" };
		return arrDayNames[dayOfWeekOrder(_Day, _Month, _Year)];

	}
	
	static String monthShortName(int monthNumber)
	{
		String months[] = { "Jan", "Feb", "Mar",
						   "Apr", "May", "Jun",
						   "Jul", "Aug", "Sep",
						   "Oct", "Nov", "Dec"
		};

		return (months[monthNumber - 1]);
	}

	String monthShortName()
	{

		return monthShortName(_Month);
	}
	
	
	
	
	public static void printMonthCalendar(int month, int year) {
        int numberOfDays;

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
	
	
	void printMonthCalendar()
	{
		printMonthCalendar(_Month, _Year);
	}
	
	static void printYearCalendar(int year) {
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
	
	public static boolean isLastDayInMonth(clsDate date) {
	    return (date.getDay() == numberOfDaysInAMonth(date.getMonth(), date.getYear()));
	}

	public boolean isLastDayInMonth() {
	    return isLastDayInMonth(this);
	}
	
	static boolean isLastMonthInYear(int month)
	{
		return (month == 12);
	}

	public static clsDate addOneDay(clsDate date) {
	    if (isLastDayInMonth(date)) {
	        if (isLastMonthInYear(date.getMonth())) {
	            date.setMonth(1);
	            date.setDay(1);
	            date.setYear(date.getYear() + 1);
	        } else {
	            date.setDay(1);
	            date.setMonth(date.getMonth() + 1);
	        }
	    } else {
	        date.setDay(date.getDay() + 1);
	    }

	    return date;
	}
	
	public void addOneDay() {
		    clsDate updatedDate = addOneDay(this); 
		    this.setDay(updatedDate.getDay());     
		    this.setMonth(updatedDate.getMonth()); 
		    this.setYear(updatedDate.getYear());   
	}
	

}
