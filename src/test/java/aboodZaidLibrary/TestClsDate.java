package aboodZaidLibrary;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
class TestClsDate {
static clsDate date;
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		date=new clsDate();
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testIsLeapYear_LeapYears() {
	    // Action & Assert
	    assertTrue(clsDate.isLeapYear(2000));
	    assertTrue(clsDate.isLeapYear(2004));
	}

	@Test
	void testIsLeapYear_NonLeapYears() {
	    // Action & Assert
	    assertFalse(clsDate.isLeapYear(2001));
	    assertFalse(clsDate.isLeapYear(1998));
	}
	
	@Test
	void testIsLeapYear_InValidYear() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clsDate.isLeapYear(0); 
        });
		assertTrue(exception.getMessage().contains("Year must be bigger than  0"));
	}
	
	@Test
	void testGetSystemDate() {
		//Arrange
		LocalDate today = LocalDate.now();
        clsDate myDate = new clsDate(
            today.getDayOfMonth(),  
            today.getMonthValue(),  
            today.getYear()         
        );
        //Action
        date=clsDate.getSystemDate();
        //Assert
        assertEquals(date.getDay(),myDate.getDay());
        assertEquals(date.getMonth(),myDate.getMonth());
        assertEquals(date.getYear(),myDate.getYear());
	}
	
	@Test 
	void testClsDateConstructor_DayMonthYear_InvalidDay() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new clsDate(32, 3, 2021);
        });
        assertTrue(exception.getMessage().contains("Day must be between 1 and"));
	}
	
	@Test 
	void testClsDateConstructor_DayMonthYear_InvalidMonth() {
		 IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
	            new clsDate(10, 13, 2021);
	        });
	        assertTrue(exception.getMessage().contains("Month must be between 1 and 12"));
	}
	
	@Test 
	void testClsDateConstructor_DayMonthYear_InvalidYear() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new clsDate(10, 5, 0);
        });
        assertTrue(exception.getMessage().contains("Year must be bigger than"));
	}
	
	@Test 
	void testClsDateConstructor_String_InvalidDay() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new clsDate("32/2/2022");
        });
        assertTrue(exception.getMessage().contains("Day must be between 1 and"));
	}
	
	@Test 
	void testClsDateConstructor_String_InvalidMonth() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new clsDate("2/13/2022");
        });
		assertTrue(exception.getMessage().contains("Month must be between 1 and 12"));
	}
	
	@Test 
	void testClsDateConstructor_String_InvalidYear() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new clsDate("2/8/-2022");
        });
		assertTrue(exception.getMessage().contains("Year must be bigger than"));
	}
	
	@Test
	void testNumberOfDaysInAYear_LeapYear() {
	    // Arrange & Action
	    int daysIn2000 = clsDate.numberOfDaysInAYear(2000);
	    
	    // Assert
	    assertEquals(366, daysIn2000);
	}

	@Test
	void testNumberOfDaysInAYear_NoneLeapYear() {
	    // Arrange & Action
	    int daysIn2001 = clsDate.numberOfDaysInAYear(2001);
	    
	    // Assert
	    assertEquals(365, daysIn2001);
	}
	
	@Test
	void testNumberOfDaysInYear_InvalidYear() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clsDate.numberOfDaysInAYear(0);
        });
		assertTrue(exception.getMessage().contains("Year must be bigger than  0"));
	}
	
	@Test
	void testNumberOfHoursInAYear_LeapYear() {
	    // Arrange & Action
	    int hoursIn2000 = clsDate.numberOfHoursInAYear(2000);
	    // Assert
	    assertEquals(366 * 24, hoursIn2000);
	}
	
	@Test
	void testNumberOfHoursInAYear_NoneLeapYear() {
	    // Arrange & Action
	    int hoursIn2001 = clsDate.numberOfHoursInAYear(2001);
	    // Assert
	    assertEquals(365 * 24, hoursIn2001);
	}
	
	@Test
	void testNumberOfHoursInAYear_InvalidYear() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clsDate.numberOfDaysInAYear(0);
        });
		assertTrue(exception.getMessage().contains("Year must be bigger than  0"));
	}
	
	@Test
	void testNumberOfMinutesInAYear() {
		//Arrange
		int minutesIn2000;
		int minutesIn2001;
		//Action
		minutesIn2000=clsDate.numberOfMinutesInAYear(2000);
		minutesIn2001=clsDate.numberOfMinutesInAYear(2001);
		//Assert
		assertEquals(minutesIn2000,366*24*60);
		assertEquals(minutesIn2001,365*24*60);
	}
	
	@Test
	
	void testNumberOfMinutesInAYear_InValidInput() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clsDate.numberOfDaysInAYear(0);
        });
		assertTrue(exception.getMessage().contains("Year must be bigger than  0"));
	}
	
	@Test 
	void testNumberOfSecondsInAYear() {
		//Arrange
		int secondsIn2000;
		int secondsIn2001;
		//Action
		secondsIn2000=clsDate.numberOfSecondsInAYear(2000);
		secondsIn2001=clsDate.numberOfSecondsInAYear(2001);
		//Assert
		assertEquals(secondsIn2000,366*24*60*60);
		assertEquals(secondsIn2001,365*24*60*60);
	}
	
	@Test 
	void testNumberOfSecondsInAYear_InvalidInput() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clsDate.numberOfDaysInAYear(0);
        });
		assertTrue(exception.getMessage().contains("Year must be bigger than  0"));
	}
	
	@Test
	void testNumberOfDaysInAMonth_NonLeapYear() {
		//Arrange
	    int[] expected = {31,28,31,30,31,30,31,31,30,31,30,31};
	    //Action & Assert
	    for (int month = 1; month <= 12; month++) {
	        assertEquals(expected[month-1], clsDate.numberOfDaysInAMonth(month, 2001));
	    }
	}
	
	@Test
	void testNumberOfDaysInAMonth_LeapYear() {
	    int[] expected = {31,29,31,30,31,30,31,31,30,31,30,31};
	    for (int month = 1; month <= 12; month++) {
	        assertEquals(expected[month-1], clsDate.numberOfDaysInAMonth(month, 2000));
	    }
	}
	
	@Test
	void testNumberOfDaysInAMonth_InvalidMonth() {
		//Action & Assert
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clsDate.numberOfDaysInAMonth(13, 2000);
        });

        
        assertTrue(
            exception.getMessage().contains("Month must be between"));
	}
	
	void testNumberOfDaysInAMonth_InvalidYear() {
        // Action & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clsDate.numberOfDaysInAMonth(6, -200);  // valid month, invalid year
        });

        
        assertTrue(
            exception.getMessage().contains("Year must be bigger than  0"));
    }
	
	
	
	
	@Test
	void testNumberOfHoursInAMonth_NonLeapYear(){
		//Arrange
	    int[] expected = {31*24,28*24,31*24,30*24,31*24,30*24,31*24,31*24,30*24,31*24,30*24,31*24};
	    //Action & Assert
	    for (int month = 1; month <= 12; month++) {
	        assertEquals(expected[month-1], clsDate.numberOfHoursInAMonth(month, 2001));
	    }
	}
	
	@Test
	void testNumberOfHoursInAMonth_LeapYear(){
		//Arrange
	    int[] expected = {31*24,29*24,31*24,30*24,31*24,30*24,31*24,31*24,30*24,31*24,30*24,31*24};
	    //Action & Assert
	    for (int month = 1; month <= 12; month++) {
	        assertEquals(expected[month-1], clsDate.numberOfHoursInAMonth(month, 2000));
	    }	
	}
	
	@Test
	void testNumberOfHoursInAMonth_InvalidMonth(){
		//Action & Assert
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clsDate.numberOfHoursInAMonth(13, 2000); 
        });

        
        assertTrue(
            exception.getMessage().contains("Month must be between"));
	}
	
	@Test
	void testNumberOfHoursInAMonth_InvalidYear() {
		// Action & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clsDate.numberOfHoursInAMonth(2, -200); // valid month, invalid year
        });

        
        assertTrue(
            exception.getMessage().contains("Year must be bigger than  0"));
	}
	
	@Test 
	void testNumberOfMinutesInAMonth_NonLeapYear() {
		//Arrange
	    int[] expected = {31*24*60,28*24*60,31*24*60,30*24*60,31*24*60,30*24*60,31*24*60,31*24*60,30*24*60,31*24*60,30*24*60,31*24*60};
	    //Action & Assert
	    for (int month = 1; month <= 12; month++) {
	        assertEquals(expected[month-1], clsDate.numberOfMinutesInAMonth(month, 2001));
	    }	
	}
	
	@Test 
	void testNumberOfMinutesInAMonth_LeapYear() {
		//Arrange
	    int[] expected = {31*24*60,29*24*60,31*24*60,30*24*60,31*24*60,30*24*60,31*24*60,31*24*60,30*24*60,31*24*60,30*24*60,31*24*60};
	    //Action & Assert
	    for (int month = 1; month <= 12; month++) {
	        assertEquals(expected[month-1], clsDate.numberOfMinutesInAMonth(month, 2000));
	    }
	}
	
	@Test
	void testNumberOfMinutesInAMonth_InvalidMonth() {
		//Action & Assert
				IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
		            clsDate.numberOfMinutesInAMonth(13,2000); 
		        });

		        
		        assertTrue(
		            exception.getMessage().contains("Month must be between"));
	}
	
	@Test
	void testNumberOfMinutesInAMonth_InvalidYear() {
		//Action & Assert
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clsDate.numberOfMinutesInAMonth(2,-200); 
        });

        
        assertTrue(
            exception.getMessage().contains("Year must be bigger than  0"));
	}
	
	@Test 
	void testNumberOfSecondsInAMonth_NonLeapYear() {
		//Arrange
	    int[] expected = {31*24*60*60,28*24*60*60,31*24*60*60,30*24*60*60,31*24*60*60,30*24*60*60,31*24*60*60,31*24*60*60,30*24*60*60,31*24*60*60,30*24*60*60,31*24*60*60};
	    //Action & Assert
	    for (int month = 1; month <= 12; month++) {
	        assertEquals(expected[month-1], clsDate.numberOfSecondsInAMonth(month, 2001));
	    }	
	}
	
	@Test
	void testNumberOfSecondsInAMonth_LeapYear() {
		//Arrange
	    int[] expected = {31*24*60*60,29*24*60*60,31*24*60*60,30*24*60*60,31*24*60*60,30*24*60*60,31*24*60*60,31*24*60*60,30*24*60*60,31*24*60*60,30*24*60*60,31*24*60*60};
	    //Action & Assert
	    for (int month = 1; month <= 12; month++) {
	        assertEquals(expected[month-1], clsDate.numberOfSecondsInAMonth(month, 2000));
	    }	
	}
	
	@Test
	void testNumberOfSecondsInAMonth_InvalidMonth() {
		//Action & Assert
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clsDate.numberOfSecondsInAMonth(13, 2000);
        });

        
        assertTrue(
            exception.getMessage().contains("Month must be between"));
	}
	
	@Test
	void testNumberOfSecondsInAMonth_InvalidYear() {
		//Action & Assert
				IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
		            clsDate.numberOfMinutesInAMonth(2,-200); 
		        });

		        
		        assertTrue(
		            exception.getMessage().contains("Year must be bigger than  0"));
	}
	
	@Test
	void testDayOfWeekOrder() {
	//Assert
	// January 1, 2000 -> Saturday (6)
	assertEquals(6, clsDate.dayOfWeekOrder(1, 1, 2000));

    // February 29, 2000 -> Tuesday (2) (leap year)
    assertEquals(2, clsDate.dayOfWeekOrder(29, 2, 2000));

    // March 1, 2000 -> Wednesday (3)
    assertEquals(3, clsDate.dayOfWeekOrder(1, 3, 2000));

    // December 31, 1999 -> Friday (5)
    assertEquals(5, clsDate.dayOfWeekOrder(31, 12, 1999));

    // July 4, 1776 -> Thursday (4)
    assertEquals(4, clsDate.dayOfWeekOrder(4, 7, 1776));

    // October 30, 2025 -> Thursday (4)
    assertEquals(4, clsDate.dayOfWeekOrder(30, 10, 2025));
	}
	 
	@Test
	void testDayOfWeekOrder_InvalidDay() {
		 IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
	            clsDate.dayOfWeekOrder(0, 1, 2021);
	        });
	        assertTrue(exception.getMessage().contains("Day must be between"));
		}
	
	@Test
	void testDayOfWeekOrder_InvalidMonth() {
		//Action & Assert
				IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
		            clsDate.dayOfWeekOrder(2, 13, 2000);
		        });

		        assertTrue(
		            exception.getMessage().contains("Month must be between"));
		}
	
	@Test
	void testDayOfWeekOrder_InvalidYear() {
		//Action & Assert
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clsDate.dayOfWeekOrder(2, 2, -200); 
        });

        
        assertTrue(
            exception.getMessage().contains("Year must be bigger than  0"));
		}
	
	@Test
	void testDayShortName_DayOfWeekOrder() {
		 //Arrange
		String expected[]= { "Sun","Mon","Tue","Wed","Thu","Fri","Sat"};
		//Action & Assert
		for(int day=0;day<7;day++) {
			assertEquals(clsDate.dayShortName(day),expected[day]);
		}
	}
	
	@Test
	void testDayShortName_DayOfWeekOrder_InvalidInput() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clsDate.dayShortName(-1);
        });
        assertTrue(exception.getMessage().contains("day must be from 0 to 6"));
	}
	
	@Test
	void testDayShortName_DayMonthYear() {
		//Assert
     assertEquals("Thu", clsDate.dayShortName(30, 10, 2025));
     assertEquals("Sat", clsDate.dayShortName(1, 1, 2000));
     assertEquals("Fri", clsDate.dayShortName(15, 8, 1947));
     assertEquals("Mon", clsDate.dayShortName(6, 1, 2025));
     assertEquals("Sun", clsDate.dayShortName(3, 11, 2024));
	}
	
	@Test
	void testDayShortName_DayMonthYear_InvalidDay() {
		  IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
	            clsDate.dayShortName(32, 1, 2021);  
	        });
	        assertTrue(exception.getMessage().contains("Day must be between 1 and"));
	}
	
	@Test
	void testDayShortName_DayMonthYear_InvalidMonth() {
		//Action & Assert
				IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
		            clsDate.dayShortName(2, 13, 2000);
		        });

		        assertTrue(
		            exception.getMessage().contains("Month must be between"));
	}
	
	@Test
	void testDayShortName_DayMonthYear_InvalidYear() {
		//Action & Assert
				IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
		            clsDate.dayShortName(2, 12, -2000);
		        });

		        assertTrue(
		            exception.getMessage().contains("Year must be bigger than  0"));
	}
	
	@Test
	void testMonthShortName() {
		//Arrange
		String expected[]={"Jan", "Feb", "Mar",
                "Apr", "May", "Jun",
                "Jul", "Aug", "Sep",
                "Oct", "Nov", "Dec" };
		//Action & Assert
		for(int month=1;month<=12;month++) {
			assertEquals(clsDate.monthShortName(month),expected[month-1]);
		}
	}
	
	@Test
	void testMonthShortName_InvalidInput() {
		//Action & Assert
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clsDate.monthShortName(13);
        });

        assertTrue(
            exception.getMessage().contains("Month must be between"));
	}
	
	@Test 
    public void testDaysFromTheBeginningOfTheYear() {
		assertEquals(1, clsDate.daysFromTheBeginningOfTheYear(1, 1, 2025)); // Jan 1
	    assertEquals(32, clsDate.daysFromTheBeginningOfTheYear(1, 2, 2025)); // Feb 1
	    assertEquals(59, clsDate.daysFromTheBeginningOfTheYear(28, 2, 2025)); // Feb end non-leap
	    assertEquals(61, clsDate.daysFromTheBeginningOfTheYear(1, 3, 2024)); // Leap year Feb
	    assertEquals(366, clsDate.daysFromTheBeginningOfTheYear(31, 12, 2024)); // Leap year Dec 31
	    assertEquals(365, clsDate.daysFromTheBeginningOfTheYear(31, 12, 2025)); // Non-leap Dec 31
	}
	
	@Test
	public void testDaysFromTheBeginningOfTheYear_InvalidDay() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clsDate.daysFromTheBeginningOfTheYear(33, 2, 2022);  
        });
        assertTrue(exception.getMessage().contains("Day must be between 1 and"));
	}
	
	@Test
	public void testDaysFromTheBeginningOfTheYear_InvalidMonth() {
		//Action & Assert
				IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
		            clsDate.daysFromTheBeginningOfTheYear(3, 13, 2000);
		        });

		        assertTrue(
		            exception.getMessage().contains("Month must be between"));
	}
	
	@Test
	public void testDaysFromTheBeginningOfTheYear_InvalidYear() {
		//Action & Assert
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clsDate.daysFromTheBeginningOfTheYear(2, 2, -200);
        });

        assertTrue(
            exception.getMessage().contains("Year must be bigger than  0"));
	}
	
	
	@Test
	public void testAddDays_DayInTheMiddleOfTheMonth() {
		//Arrange
	        date = new clsDate(14, 11, 2000);
	    //Action
	        date.addDays(10);
	    //Assert
	        assertEquals(24, date.getDay());
	        assertEquals(11, date.getMonth());
	        assertEquals(2000, date.getYear());
	}
	
	@Test
	public void testAddDays_EndOfYear() {
		//Arrange
	        date = new clsDate(31, 12, 2000);
	    //Action
	        date.addDays(1);
	    //Assert
	        assertEquals(1, date.getDay());
	        assertEquals(1, date.getMonth());
	        assertEquals(2001, date.getYear());
	}
	 
	@Test
	public void testAddDays_LeapYearFeb28() {
		//Arrange
	        date = new clsDate(28, 2, 2000);
	    //Action
	        date.addDays(1);
	    //Assert
	        assertEquals(29, date.getDay());
	        assertEquals(2, date.getMonth());
	        assertEquals(2000, date.getYear());
	}
	
	@Test
	public void testAddDays_NonLeapYearFeb28() {
		//Arrange
	        date = new clsDate(28, 2, 2001);
	   //Action
	        date.addDays(1);
       //Assert
	        assertEquals(1, date.getDay());
	        assertEquals(3, date.getMonth());
	        assertEquals(2001, date.getYear());
	}
	
	@Test
	public void testAddDays_EndOfMonth() {
		//Arrange
	        date = new clsDate(31, 1, 2001);
	   //Action
	        date.addDays(1);
       //Assert
	        assertEquals(1, date.getDay());
	        assertEquals(2, date.getMonth());
	        assertEquals(2001, date.getYear());
	}
	
	 @Test
     public void testDate1BeforeDate2_DayDifferenceTrue() {
		 //Arrange
	        clsDate date1 = new clsDate(1, 1, 2020);
	        clsDate date2 = new clsDate(2, 1, 2020);
	     //Assert
	        assertTrue(clsDate.isDate1BeforeDate2(date1, date2));
	 }

	 @Test
	 void testDate1BeforeDate2_DayDifferenceFalse() {
		//Arrange
	        clsDate date1 = new clsDate(1, 1, 2020);
	        clsDate date2 = new clsDate(2, 1, 2020);
	      //Assert
	        assertFalse(clsDate.isDate1BeforeDate2(date2, date1));
	 }

	 @Test
	 void testDate1BeforeDate2_MonthDifference() {
		//Arrange
	        clsDate date1 = new clsDate(1, 1, 2020);
	        clsDate date2 = new clsDate(1, 2, 2020);
	      //Assert
	        assertTrue(clsDate.isDate1BeforeDate2(date1, date2));
	 }

	 @Test
	 void testDate1BeforeDate2_YearDifference() {
		//Arrange
	        clsDate date1 = new clsDate(1, 1, 2020);
	        clsDate date2 = new clsDate(1, 1, 2021);
	      //Assert
	        assertTrue(clsDate.isDate1BeforeDate2(date1, date2));
	 }

	 @Test
	 void testDate1BeforeDate2_SameDate() {
		//Arrange
	        clsDate date1 = new clsDate(1, 1, 2020);
	        clsDate date2 = new clsDate(1, 1, 2020);
	      //Assert
	        assertFalse(clsDate.isDate1BeforeDate2(date1, date2));
	 }
	 
	 @Test
	 void testIsDate1EqualDate2_SameDate() {
		 //Arrange 
		 clsDate date1 = new clsDate(1, 1, 2020);
	     clsDate date2 = new clsDate(1, 1, 2020);
	     //Assert
	        assertTrue(clsDate.isDate1EqualDate2(date1, date2));
	 }
	
	 @Test
	 void testIsDate1EqualDate2_Date1BeforeDate2() {
		 //Arrange 
		 clsDate date1 = new clsDate(1, 1, 2019);
	     clsDate date2 = new clsDate(1, 1, 2020);
	     //Assert
	        assertFalse(clsDate.isDate1EqualDate2(date1, date2));
	 }
	 
	 @Test
	 void testIsDate1EqualDate2_Date2BeforeDate1() {
		 //Arrange 
		 clsDate date1 = new clsDate(1, 1, 2020);
	     clsDate date2 = new clsDate(1, 1, 2019);
	     //Assert
	     assertFalse(clsDate.isDate1EqualDate2(date1, date2));
	 }
	 
	 @Test
	 void testGetDifferenceInDays_SameDate() {
		//Arrange 
		 clsDate date1 = new clsDate(1, 1, 2020);
	     clsDate date2 = new clsDate(1, 1, 2020);
	   //Assert
	     assertEquals(clsDate.getDifferenceInDays(date1, date2),0);
	 }
	 
	 @Test
	 void testGetDifferenceInDays_DifferenceInDays_date1before() {
		//Arrange 
		 clsDate date1 = new clsDate(1, 1, 2020);
	     clsDate date2 = new clsDate(10, 1, 2021);
	   //Assert
	     assertEquals(clsDate.getDifferenceInDays(date1, date2),375);
	 }
	 
	 @Test
	 void testGetDifferenceInDays_DifferenceInDays_date2before() {
		//Arrange 
		 clsDate date1 = new clsDate(10, 1, 2021);
	     clsDate date2 = new clsDate(1, 1, 2020);
	   //Assert
	     assertEquals(clsDate.getDifferenceInDays(date1, date2),-375);
	 }
	 
	 @Test
	 void testGetDifferenceInDays_DifferenceInMonths_date1before() {
		//Arrange 
		 clsDate date1 = new clsDate(1, 1, 2020);
	     clsDate date2 = new clsDate(1, 2, 2020);
	   //Assert
	     assertEquals(clsDate.getDifferenceInDays(date1, date2),31);
	 }
	 
	 @Test
	 void testGetDifferenceInDays_DifferenceInMonths_date2before() {
		//Arrange 
		 clsDate date1 = new clsDate(1, 2, 2020);
	     clsDate date2 = new clsDate(1, 1, 2020);
	   //Assert
	     assertEquals(clsDate.getDifferenceInDays(date1, date2),-31);
	 }
	 
	 @Test
	 void testGetDifferenceInDays_DifferenceInYears_date1before_LeapYear() {
		//Arrange 
		 clsDate date1 = new clsDate(1, 1, 2020);
	     clsDate date2 = new clsDate(1, 1, 2021);
	   //Assert
	     assertEquals(clsDate.getDifferenceInDays(date1, date2),366);
	 }
	 
	 @Test
	 void testGetDifferenceInDays_DifferenceInYears_date1before_NonLeapYear() {
		//Arrange 
		 clsDate date1 = new clsDate(1, 1, 2022);
	     clsDate date2 = new clsDate(1, 1, 2023);
	   //Assert
	     assertEquals(clsDate.getDifferenceInDays(date1, date2),365);
	 }
	 
	 @Test
	 void testGetDifferenceInDays_DifferenceInYears_date2before_LeapYear() {
		//Arrange 
		 clsDate date1 = new clsDate(1, 1, 2021);
	     clsDate date2 = new clsDate(1, 1, 2020);
	   //Assert
	     assertEquals(clsDate.getDifferenceInDays(date1, date2),-366);
	 }
	 
	 @Test
	 void testGetDifferenceInDays_DifferenceInYears_date2before_NonLeapYear() {
		//Arrange 
		 clsDate date1 = new clsDate(1, 1, 2023);
	     clsDate date2 = new clsDate(1, 1, 2022);
	   //Assert
	     assertEquals(clsDate.getDifferenceInDays(date1, date2),-365);
	 }
	 
	 @Test
	 void testDateToString() {
		 //Arrange
		 String dateString;
		 date=new clsDate(10,12,2023);
		 //Action 
		 dateString=date.DateToString();
		 //Assert
		 assertEquals(dateString,"10/12/2023");
		 
	 }


    // Add these test methods to your TestClsDate class to achieve 100% coverage

// ============== Missing Tests for Constructors ==============

    @Test
    void testClsDateConstructor_CopyConstructor() {
        // Arrange
        clsDate original = new clsDate(15, 6, 2023);

        // Action
        clsDate copy = new clsDate(original);

        // Assert
        assertEquals(15, copy.getDay());
        assertEquals(6, copy.getMonth());
        assertEquals(2023, copy.getYear());
    }

    @Test
    void testClsDateConstructor_String_ValidDate() {
        // Action
        clsDate date = new clsDate("15/6/2023");

        // Assert
        assertEquals(15, date.getDay());
        assertEquals(6, date.getMonth());
        assertEquals(2023, date.getYear());
    }

    @Test
    void testClsDateConstructor_String_InvalidFormat() {
        // Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new clsDate("invalid/date/format");
        });
        assertTrue(exception.getMessage().contains("Day, month, and year must be valid integers"));
    }

    @Test
    void testClsDateConstructor_DayMonthYear_ValidDate() {
        // Action
        clsDate date = new clsDate(15, 6, 2023);

        // Assert
        assertEquals(15, date.getDay());
        assertEquals(6, date.getMonth());
        assertEquals(2023, date.getYear());
    }

// ============== Missing Tests for Setters ==============

    @Test
    void testSetDay_ValidDay() {
        // Arrange
        clsDate date = new clsDate(1, 1, 2023);

        // Action
        date.setDay(15);

        // Assert
        assertEquals(15, date.getDay());
    }

    @Test
    void testSetDay_InvalidDay() {
        // Arrange
        clsDate date = new clsDate(1, 1, 2023);

        // Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            date.setDay(32);
        });
        assertTrue(exception.getMessage().contains("Day must be between 1 and"));
    }

    @Test
    void testSetMonth_ValidMonth() {
        // Arrange
        clsDate date = new clsDate(1, 1, 2023);

        // Action
        date.setMonth(6);

        // Assert
        assertEquals(6, date.getMonth());
    }

    @Test
    void testSetMonth_InvalidMonth() {
        // Arrange
        clsDate date = new clsDate(1, 1, 2023);

        // Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            date.setMonth(13);
        });
        assertTrue(exception.getMessage().contains("Month must be between 1 and 12"));
    }

    @Test
    void testSetYear_ValidYear() {
        // Arrange
        clsDate date = new clsDate(1, 1, 2023);

        // Action
        date.setYear(2025);

        // Assert
        assertEquals(2025, date.getYear());
    }

    @Test
    void testSetYear_InvalidYear() {
        // Arrange
        clsDate date = new clsDate(1, 1, 2023);

        // Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            date.setYear(0);
        });
        assertTrue(exception.getMessage().contains("Year must be bigger than"));
    }

// ============== Missing Tests for Instance Methods ==============

    @Test
    void testIsLeapYear_InstanceMethod() {
        // Arrange
        clsDate leapYearDate = new clsDate(1, 1, 2000);
        clsDate nonLeapYearDate = new clsDate(1, 1, 2001);

        // Assert
        assertTrue(leapYearDate.isLeapYear());
        assertFalse(nonLeapYearDate.isLeapYear());
    }

    @Test
    void testNumberOfDaysInAYear_InstanceMethod() {
        // Arrange
        clsDate leapYearDate = new clsDate(1, 1, 2000);
        clsDate nonLeapYearDate = new clsDate(1, 1, 2001);

        // Assert
        assertEquals(366, leapYearDate.numberOfDaysInAYear());
        assertEquals(365, nonLeapYearDate.numberOfDaysInAYear());
    }

    @Test
    void testNumberOfHoursInAYear_InstanceMethod() {
        // Arrange
        clsDate date = new clsDate(1, 1, 2000);

        // Assert
        assertEquals(366 * 24, date.numberOfHoursInAYear());
    }

    @Test
    void testNumberOfMinutesInAYear_InstanceMethod() {
        // Arrange
        clsDate date = new clsDate(1, 1, 2000);

        // Assert
        assertEquals(366 * 24 * 60, date.numberOfMinutesInAYear());
    }

    @Test
    void testNumberOfSecondsInAYear_InstanceMethod() {
        // Arrange
        clsDate date = new clsDate(1, 1, 2000);

        // Assert
        assertEquals(366 * 24 * 60 * 60, date.numberOfSecondsInAYear());
    }

    @Test
    void testNumberOfDaysInAMonth_InstanceMethod() {
        // Arrange
        clsDate date = new clsDate(1, 2, 2000);

        // Assert
        assertEquals(29, date.numberOfDaysInAMonth());
    }

    @Test
    void testNumberOfHoursInAMonth_InstanceMethod() {
        // Arrange
        clsDate date = new clsDate(1, 2, 2000);

        // Assert
        assertEquals(29 * 24, date.numberOfHoursInAMonth());
    }

    @Test
    void testNumberOfMinutesInAMonth_InstanceMethod() {
        // Arrange
        clsDate date = new clsDate(1, 2, 2000);

        // Assert
        assertEquals(29 * 24 * 60, date.numberOfMinutesInAMonth());
    }

    @Test
    void testNumberOfSecondsInAMonth_InstanceMethod() {
        // Arrange
        clsDate date = new clsDate(1, 2, 2000);

        // Assert
        assertEquals(29 * 24 * 60 * 60, date.numberOfSecondsInAMonth());
    }

    @Test
    void testDayOfWeekOrder_InstanceMethod() {
        // Arrange
        clsDate date = new clsDate(1, 1, 2000);

        // Assert
        assertEquals(6, date.dayOfWeekOrder());
    }

    @Test
    void testDayShortName_InstanceMethod() {
        // Arrange
        clsDate date = new clsDate(1, 1, 2000);

        // Assert
        assertEquals("Sat", date.dayShortName());
    }

    @Test
    void testMonthShortName_InstanceMethod() {
        // Arrange
        clsDate date = new clsDate(1, 6, 2000);

        // Assert
        assertEquals("Jun", date.monthShortName());
    }

    @Test
    void testDaysFromTheBeginningOfTheYear_InstanceMethod() {
        // Arrange
        clsDate date = new clsDate(1, 2, 2025);

        // Assert
        assertEquals(32, date.daysFromTheBeginningOfTheYear());
    }

// ============== Missing Tests for Static Methods ==============

    @Test
    void testGetSystemDateTimeString() {
        // Action
        String dateTime = clsDate.getSystemDateTimeString();

        // Assert
        assertNotNull(dateTime);
        assertTrue(dateTime.matches("\\d{2}/\\d{2}/\\d{4} - \\d{2}:\\d{2}:\\d{2}"));
    }

    @Test
    void testDateToString_Static() {
        // Arrange
        clsDate date = new clsDate(10, 12, 2023);

        // Action
        String dateString = clsDate.DateToString(date);

        // Assert
        assertEquals("10/12/2023", dateString);
    }

    @Test
    void testGetDateFromDayOrderInYear() {
        // Action
        clsDate date = clsDate.getDateFromDayOrderInYear(32, 2025);

        // Assert
        assertEquals(1, date.getDay());
        assertEquals(2, date.getMonth());
        assertEquals(2025, date.getYear());
    }

    @Test
    void testGetDateFromDayOrderInYear_LastDayOfYear() {
        // Action
        clsDate date = clsDate.getDateFromDayOrderInYear(365, 2023);

        // Assert
        assertEquals(31, date.getDay());
        assertEquals(12, date.getMonth());
        assertEquals(2023, date.getYear());
    }

    @Test
    void testGetDateFromDayOrderInYear_LeapYear() {
        // Action
        clsDate date = clsDate.getDateFromDayOrderInYear(366, 2024);

        // Assert
        assertEquals(31, date.getDay());
        assertEquals(12, date.getMonth());
        assertEquals(2024, date.getYear());
    }

// ============== Missing Tests for addDays ==============

    @Test
    void testAddDays_NegativeDays() {
        // Arrange
        clsDate date = new clsDate(15, 6, 2023);

        // Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            date.addDays(-5);
        });
        assertTrue(exception.getMessage().contains("days must be positive"));
    }

    @Test
    void testAddDays_MultipleMonths() {
        // Arrange
        clsDate date = new clsDate(1, 1, 2023);

        // Action
        date.addDays(90);

        // Assert
        assertEquals(1, date.getDay());
        assertEquals(4, date.getMonth());
        assertEquals(2023, date.getYear());
    }

    @Test
    void testAddDays_CrossingYears() {
        // Arrange
        clsDate date = new clsDate(1, 12, 2023);

        // Action
        date.addDays(40);

        // Assert
        assertEquals(10, date.getDay());
        assertEquals(1, date.getMonth());
        assertEquals(2024, date.getYear());
    }

// ============== Missing Tests for Date Comparison ==============

    @Test
    void testIsDateBeforeDate2_InstanceMethod_True() {
        // Arrange
        clsDate date1 = new clsDate(1, 1, 2020);
        clsDate date2 = new clsDate(2, 1, 2020);

        // Assert
        assertTrue(date1.isDateBeforeDate2(date2));
    }

    @Test
    void testIsDateBeforeDate2_InstanceMethod_False() {
        // Arrange
        clsDate date1 = new clsDate(2, 1, 2020);
        clsDate date2 = new clsDate(1, 1, 2020);

        // Assert
        assertFalse(date1.isDateBeforeDate2(date2));
    }

    @Test
    void testIsDateEqualDate2_InstanceMethod_True() {
        // Arrange
        clsDate date1 = new clsDate(1, 1, 2020);
        clsDate date2 = new clsDate(1, 1, 2020);

        // Assert
        assertTrue(date1.isDateEqualDate2(date2));
    }

    @Test
    void testIsDateEqualDate2_InstanceMethod_False() {
        // Arrange
        clsDate date1 = new clsDate(1, 1, 2020);
        clsDate date2 = new clsDate(2, 1, 2020);

        // Assert
        assertFalse(date1.isDateEqualDate2(date2));
    }

    @Test
    void testGetDifferenceInDays_InstanceMethod() {
        // Arrange
        clsDate date1 = new clsDate(1, 1, 2020);
        clsDate date2 = new clsDate(10, 1, 2020);

        // Assert
        assertEquals(9, date1.getDifferenceInDays(date2));
    }

// ============== Missing Tests for Print Methods ==============

    @Test
    void testPrintMonthCalendar_StaticMethod() {
        // This test verifies the method doesn't throw exceptions
        assertDoesNotThrow(() -> {
            clsDate.printMonthCalendar(6, 2023);
        });
    }

    @Test
    void testPrintMonthCalendar_InstanceMethod() {
        // Arrange
        clsDate date = new clsDate(1, 6, 2023);

        // Assert
        assertDoesNotThrow(() -> {
            date.printMonthCalendar();
        });
    }

    @Test
    void testPrintMonthCalendar_InvalidMonth() {
        // Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clsDate.printMonthCalendar(13, 2023);
        });
        assertTrue(exception.getMessage().contains("Month must be between 1 and 12"));
    }

    @Test
    void testPrintMonthCalendar_InvalidYear() {
        // Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clsDate.printMonthCalendar(6, 0);
        });
        assertTrue(exception.getMessage().contains("Year must be bigger than"));
    }

    @Test
    void testPrintYearCalendar_StaticMethod() {
        // This test verifies the method doesn't throw exceptions
        assertDoesNotThrow(() -> {
            clsDate.printYearCalendar(2023);
        });
    }

    @Test
    void testPrintYearCalendar_InstanceMethod() {
        // Arrange
        clsDate date = new clsDate(1, 1, 2023);

        // Assert
        assertDoesNotThrow(() -> {
            date.printYearCalendar();
        });
    }

    @Test
    void testPrintYearCalendar_InvalidYear() {
        // Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clsDate.printYearCalendar(0);
        });
        assertTrue(exception.getMessage().contains("Year must be bigger than"));
    }

// ============== Edge Cases and Additional Coverage ==============

    @Test
    void testAddDays_Zero() {
        // Arrange
        clsDate date = new clsDate(15, 6, 2023);

        // Action
        date.addDays(0);

        // Assert
        assertEquals(15, date.getDay());
        assertEquals(6, date.getMonth());
        assertEquals(2023, date.getYear());
    }

    @Test
    void testIsDate1BeforeDate2_MonthAndYearSame_Date1After() {
        // Arrange
        clsDate date1 = new clsDate(15, 6, 2023);
        clsDate date2 = new clsDate(10, 6, 2023);

        // Assert
        assertFalse(clsDate.isDate1BeforeDate2(date1, date2));
    }

    @Test
    void testIsDate1BeforeDate2_YearDifferent_MonthAndDaySame() {
        // Arrange
        clsDate date1 = new clsDate(15, 6, 2022);
        clsDate date2 = new clsDate(15, 6, 2023);

        // Assert
        assertTrue(clsDate.isDate1BeforeDate2(date1, date2));
    }

    // ============== Tests for isDate1BeforeDate2 - Missing branches ==============

    @Test
    void testIsDate1BeforeDate2_SameYear_Date1MonthGreater() {
        // Tests the false branch when years are equal but date1.month > date2.month
        clsDate date1 = new clsDate(1, 6, 2023);
        clsDate date2 = new clsDate(1, 5, 2023);

        assertFalse(clsDate.isDate1BeforeDate2(date1, date2));
    }

    @Test
    void testIsDate1BeforeDate2_SameYearAndMonth_Date1DayGreater() {
        // Tests the false branch when year and month are equal but date1.day > date2.day
        clsDate date1 = new clsDate(15, 6, 2023);
        clsDate date2 = new clsDate(10, 6, 2023);

        assertFalse(clsDate.isDate1BeforeDate2(date1, date2));
    }

    @Test
    void testIsDate1BeforeDate2_Date1YearGreater() {
        // Tests the false branch when date1.year > date2.year
        clsDate date1 = new clsDate(1, 1, 2024);
        clsDate date2 = new clsDate(1, 1, 2023);

        assertFalse(clsDate.isDate1BeforeDate2(date1, date2));
    }

    // ============== Tests for isDate1EqualDate2 - Missing branches ==============

    @Test
    void testIsDate1EqualDate2_SameYear_DifferentMonth() {
        // Tests false branch when years equal but months differ
        clsDate date1 = new clsDate(1, 5, 2023);
        clsDate date2 = new clsDate(1, 6, 2023);

        assertFalse(clsDate.isDate1EqualDate2(date1, date2));
    }

    @Test
    void testIsDate1EqualDate2_SameYearAndMonth_DifferentDay() {
        // Tests false branch when year and month equal but days differ
        clsDate date1 = new clsDate(10, 6, 2023);
        clsDate date2 = new clsDate(15, 6, 2023);

        assertFalse(clsDate.isDate1EqualDate2(date1, date2));
    }

    @Test
    void testIsDate1EqualDate2_DifferentYear() {
        // Tests false branch when years differ
        clsDate date1 = new clsDate(1, 1, 2022);
        clsDate date2 = new clsDate(1, 1, 2023);

        assertFalse(clsDate.isDate1EqualDate2(date1, date2));
    }

    // ============== Tests for getDifferenceInDays - Swap flag branches ==============

    @Test
    void testGetDifferenceInDays_Date1AfterDate2_SwapFlagActivated() {
        // Tests the swap branch when date1 is after date2
        // This ensures swapFlag = -1 branch is covered
        clsDate date1 = new clsDate(10, 1, 2023);
        clsDate date2 = new clsDate(1, 1, 2023);

        int difference = clsDate.getDifferenceInDays(date1, date2);

        assertEquals(-9, difference);
    }

    @Test
    void testGetDifferenceInDays_Date2AfterDate1_NoSwap() {
        // Tests the normal case where date1 is before date2
        // This ensures swapFlag = 1 branch is covered
        clsDate date1 = new clsDate(1, 1, 2023);
        clsDate date2 = new clsDate(10, 1, 2023);

        int difference = clsDate.getDifferenceInDays(date1, date2);

        assertEquals(9, difference);
    }

    // ============== Tests for addDays - Month wraparound branches ==============

    @Test
    void testAddDays_WrapsToNextYear() {
        // Tests the branch where _Month > 12 and wraps to next year
        clsDate date = new clsDate(15, 12, 2023);
        date.addDays(30); // Should go into January 2024

        assertEquals(14, date.getDay());
        assertEquals(1, date.getMonth());
        assertEquals(2024, date.getYear());
    }

    @Test
    void testAddDays_StaysInSameMonth() {
        // Tests the else branch where remainingDays <= monthDays
        clsDate date = new clsDate(1, 6, 2023);
        date.addDays(5);

        assertEquals(6, date.getDay());
        assertEquals(6, date.getMonth());
        assertEquals(2023, date.getYear());
    }

    @Test
    void testAddDays_ExactlyEndOfMonth() {
        // Tests boundary condition
        clsDate date = new clsDate(1, 1, 2023);
        date.addDays(30); // Should be exactly Jan 31

        assertEquals(31, date.getDay());
        assertEquals(1, date.getMonth());
        assertEquals(2023, date.getYear());
    }

    // ============== Tests for getDateFromDayOrderInYear - Branch coverage ==============

    @Test
    void testGetDateFromDayOrderInYear_FirstDayOfYear() {
        // Tests minimal case
        clsDate date = clsDate.getDateFromDayOrderInYear(1, 2023);

        assertEquals(1, date.getDay());
        assertEquals(1, date.getMonth());
        assertEquals(2023, date.getYear());
    }

    @Test
    void testGetDateFromDayOrderInYear_MidYear() {
        // Tests loop continues through multiple months
        clsDate date = clsDate.getDateFromDayOrderInYear(100, 2023);

        assertEquals(10, date.getDay());
        assertEquals(4, date.getMonth());
        assertEquals(2023, date.getYear());
    }

    @Test
    void testGetDateFromDayOrderInYear_LeapYearDay60() {
        // Tests leap year February 29
        clsDate date = clsDate.getDateFromDayOrderInYear(60, 2024);

        assertEquals(29, date.getDay());
        assertEquals(2, date.getMonth());
        assertEquals(2024, date.getYear());
    }

    // ============== Tests for numberOfDaysInAMonth - February branches ==============

    @Test
    void testNumberOfDaysInAMonth_FebruaryLeapYear() {
        // Tests (month == 2) && isLeapYear(year) branch
        int days = clsDate.numberOfDaysInAMonth(2, 2024);
        assertEquals(29, days);
    }

    @Test
    void testNumberOfDaysInAMonth_FebruaryNonLeapYear() {
        // Tests (month == 2) && !isLeapYear(year) branch
        int days = clsDate.numberOfDaysInAMonth(2, 2023);
        assertEquals(28, days);
    }

    @Test
    void testNumberOfDaysInAMonth_NotFebruary() {
        // Tests else branch where month != 2
        int days = clsDate.numberOfDaysInAMonth(7, 2023);
        assertEquals(31, days);
    }

    // ============== Tests for isLeapYear - All branches ==============

    @Test
    void testIsLeapYear_DivisibleBy400() {
        // Tests (year % 400 == 0) branch - true
        assertTrue(clsDate.isLeapYear(2000));
        assertTrue(clsDate.isLeapYear(1600));
    }

    @Test
    void testIsLeapYear_DivisibleBy100ButNot400() {
        // Tests (year % 100 == 0) but not (year % 400 == 0) - false
        assertFalse(clsDate.isLeapYear(1900));
        assertFalse(clsDate.isLeapYear(2100));
    }

    @Test
    void testIsLeapYear_DivisibleBy4ButNot100() {
        // Tests (year % 4 == 0 && year % 100 != 0) - true
        assertTrue(clsDate.isLeapYear(2024));
        assertTrue(clsDate.isLeapYear(2004));
    }

    @Test
    void testIsLeapYear_NotDivisibleBy4() {
        // Tests default false case
        assertFalse(clsDate.isLeapYear(2023));
        assertFalse(clsDate.isLeapYear(2019));
    }

    // ============== Tests for dayOfWeekOrder calculation branches ==============

    @Test
    void testDayOfWeekOrder_February() {
        // Tests month < 3 branch affecting the calculation
        int order = clsDate.dayOfWeekOrder(15, 2, 2023);
        assertEquals(3, order); // Wednesday
    }

    @Test
    void testDayOfWeekOrder_March() {
        // Tests month >= 3 branch
        int order = clsDate.dayOfWeekOrder(1, 3, 2023);
        assertEquals(3, order); // Wednesday
    }

    // ============== Edge cases for constructors ==============

    @Test
    void testClsDateConstructor_String_LeapYearFeb29() {
        // Valid leap year date
        clsDate date = new clsDate("29/2/2024");
        assertEquals(29, date.getDay());
        assertEquals(2, date.getMonth());
        assertEquals(2024, date.getYear());
    }

    @Test
    void testClsDateConstructor_String_InvalidLeapYearFeb29() {
        // Invalid Feb 29 in non-leap year
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new clsDate("29/2/2023");
        });
        assertTrue(exception.getMessage().contains("Day must be between 1 and"));
    }

    @Test
    void testClsDateConstructor_DayMonthYear_LeapYearFeb29() {
        // Valid leap year date
        clsDate date = new clsDate(29, 2, 2024);
        assertEquals(29, date.getDay());
    }

    @Test
    void testClsDateConstructor_DayMonthYear_InvalidLeapYearFeb29() {
        // Invalid Feb 29 in non-leap year
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new clsDate(29, 2, 2023);
        });
        assertTrue(exception.getMessage().contains("Day must be between 1 and"));
    }

    // ============== Tests for setter validations ==============

    @Test
    void testSetDay_BoundaryMaxDay() {
        // Test setting day to max valid day
        clsDate date = new clsDate(1, 1, 2023);
        date.setDay(31);
        assertEquals(31, date.getDay());
    }

    @Test
    void testSetDay_BoundaryMinDay() {
        // Test setting day to min valid day
        clsDate date = new clsDate(15, 1, 2023);
        date.setDay(1);
        assertEquals(1, date.getDay());
    }

    @Test
    void testSetDay_InvalidZero() {
        clsDate date = new clsDate(15, 1, 2023);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            date.setDay(0);
        });
        assertTrue(exception.getMessage().contains("Day must be between 1 and"));
    }

    // ============== Tests for various month boundaries ==============



    @Test
    void testGetDifferenceInDays_SpanningLeapYear() {
        clsDate date1 = new clsDate(1, 2, 2024);
        clsDate date2 = new clsDate(1, 3, 2024);

        int difference = clsDate.getDifferenceInDays(date1, date2);
        assertEquals(29, difference); // February has 29 days in 2024
    }

    @Test
    void testGetDifferenceInDays_SpanningNonLeapYear() {
        clsDate date1 = new clsDate(1, 2, 2023);
        clsDate date2 = new clsDate(1, 3, 2023);

        int difference = clsDate.getDifferenceInDays(date1, date2);
        assertEquals(28, difference); // February has 28 days in 2023
    }

    @Test
    void testClsDateConstructor_NoArgs_UsesSystemDate() {
        // This ensures the default constructor branches are covered
        clsDate date = new clsDate();
        assertNotNull(date);
        assertTrue(date.getDay() >= 1 && date.getDay() <= 31);
        assertTrue(date.getMonth() >= 1 && date.getMonth() <= 12);
        assertTrue(date.getYear() >= 2024);
    }

    // ============== Tests for isDate1BeforeDate2 - All remaining branches ==============

    @Test
    void testIsDate1BeforeDate2_YearEqual_MonthEqual_DayEqual() {
        // Tests all equal - should return false
        clsDate date1 = new clsDate(15, 6, 2023);
        clsDate date2 = new clsDate(15, 6, 2023);
        assertFalse(clsDate.isDate1BeforeDate2(date1, date2));
    }

    @Test
    void testIsDate1BeforeDate2_YearEqual_MonthLess_DayGreater() {
        // Year equal, month1 < month2 (should be true regardless of day)
        clsDate date1 = new clsDate(25, 5, 2023);
        clsDate date2 = new clsDate(1, 6, 2023);
        assertTrue(clsDate.isDate1BeforeDate2(date1, date2));
    }

    @Test
    void testIsDate1BeforeDate2_YearEqual_MonthEqual_DayLess() {
        // Year equal, month equal, day1 < day2
        clsDate date1 = new clsDate(10, 6, 2023);
        clsDate date2 = new clsDate(15, 6, 2023);
        assertTrue(clsDate.isDate1BeforeDate2(date1, date2));
    }

    // ============== Tests for isDate1EqualDate2 - All remaining branches ==============

    @Test
    void testIsDate1EqualDate2_AllFieldsEqual() {
        // Tests the true branch through all conditions
        clsDate date1 = new clsDate(15, 6, 2023);
        clsDate date2 = new clsDate(15, 6, 2023);
        assertTrue(clsDate.isDate1EqualDate2(date1, date2));
    }

    // ============== Tests for getDifferenceInDays - Loop branches ==============

    @Test
    void testGetDifferenceInDays_EqualDates_NoLoop() {
        // Tests when while condition is false immediately
        clsDate date1 = new clsDate(15, 6, 2023);
        clsDate date2 = new clsDate(15, 6, 2023);
        assertEquals(0, clsDate.getDifferenceInDays(date1, date2));
    }

    @Test
    void testGetDifferenceInDays_OneDayDifference() {
        // Tests while loop executes exactly once
        clsDate date1 = new clsDate(15, 6, 2023);
        clsDate date2 = new clsDate(16, 6, 2023);
        assertEquals(1, clsDate.getDifferenceInDays(date1, date2));
    }

    @Test
    void testGetDifferenceInDays_Date1AfterDate2_Negative() {
        // Ensures negative result when date1 > date2
        clsDate date1 = new clsDate(20, 6, 2023);
        clsDate date2 = new clsDate(15, 6, 2023);
        assertEquals(-5, clsDate.getDifferenceInDays(date1, date2));
    }

    // ============== Tests for addDays - Loop exit branches ==============

    @Test
    void testAddDays_ExactlyOneMonth() {
        // Tests adding exact number of days to reach next month
        clsDate date = new clsDate(15, 1, 2023);
        date.addDays(17); // Should be Feb 1
        assertEquals(1, date.getDay());
        assertEquals(2, date.getMonth());
        assertEquals(2023, date.getYear());
    }

    @Test
    void testAddDays_MultipleYears() {
        // Tests wrapping through multiple years
        clsDate date = new clsDate(1, 1, 2023);
        date.addDays(730); // 730 days from Jan 1, 2023
        // 2023 has 365 days, 2024 has 366 days (leap year)
        // 730 = 365 + 365, but 2024 is leap so: 365 (2023) + 365 (into 2024) = Dec 31, 2024
        assertEquals(31, date.getDay());
        assertEquals(12, date.getMonth());
        assertEquals(2024, date.getYear());
    }

    @Test
    void testDaysFromTheBeginningOfTheYear_InvalidMonth_LessThan1() {
        // Tests the month < 1 branch
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clsDate.daysFromTheBeginningOfTheYear(15, 0, 2023);
        });
        assertTrue(exception.getMessage().contains("Month must be between 1 and 12"));
    }

    @Test
    void testDaysFromTheBeginningOfTheYear_InvalidMonth_GreaterThan12() {
        // Tests the month > 12 branch
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clsDate.daysFromTheBeginningOfTheYear(15, 13, 2023);
        });
        assertTrue(exception.getMessage().contains("Month must be between 1 and 12"));
    }

    @Test
    void testDaysFromTheBeginningOfTheYear_InvalidYear_Zero() {
        // Tests the year < 1 branch
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clsDate.daysFromTheBeginningOfTheYear(15, 6, 0);
        });
        assertTrue(exception.getMessage().contains("Year must be bigger than  0"));
    }

    @Test
    void testDaysFromTheBeginningOfTheYear_InvalidYear_Negative() {
        // Tests the year < 1 branch with negative value
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clsDate.daysFromTheBeginningOfTheYear(15, 6, -100);
        });
        assertTrue(exception.getMessage().contains("Year must be bigger than  0"));
    }

    @Test
    void testDaysFromTheBeginningOfTheYear_ValidInputs_AllBranchesPass() {
        // Tests when all validations pass
        int days = clsDate.daysFromTheBeginningOfTheYear(15, 6, 2023);
        assertEquals(166, days); // Jan(31) + Feb(28) + Mar(31) + Apr(30) + May(31) + 15 = 166
    }

    @Test
    void testAddDays_FromLeapYearFeb28To29() {
        // Tests leap year boundary
        clsDate date = new clsDate(28, 2, 2024);
        date.addDays(1);
        assertEquals(29, date.getDay());
        assertEquals(2, date.getMonth());
        assertEquals(2024, date.getYear());
    }

    @Test
    void testDayOfWeekOrder_InvalidMonth_LessThan1() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clsDate.dayOfWeekOrder(15, 0, 2023);
        });
        assertTrue(exception.getMessage().contains("Month must be between 1 and 12"));
    }

    @Test
    void testDayOfWeekOrder_InvalidMonth_GreaterThan12() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clsDate.dayOfWeekOrder(15, 13, 2023);
        });
        assertTrue(exception.getMessage().contains("Month must be between 1 and 12"));
    }

    @Test
    void testDayOfWeekOrder_InvalidYear_Zero() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clsDate.dayOfWeekOrder(15, 6, 0);
        });
        assertTrue(exception.getMessage().contains("Year must be bigger than  0"));
    }

    @Test
    void testDayOfWeekOrder_InvalidYear_Negative() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clsDate.dayOfWeekOrder(15, 6, -100);
        });
        assertTrue(exception.getMessage().contains("Year must be bigger than  0"));
    }

    @Test
    void testDayOfWeekOrder_ValidInputs_AllBranchesPass() {
        int order = clsDate.dayOfWeekOrder(15, 6, 2023);
        assertTrue(order >= 0 && order <= 6);
    }

    // Add these tests to TestClsDate.java:

    @Test
    void testClsDateConstructor_IntParams_InvalidMonth_LessThan1() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new clsDate(15, 0, 2023);
        });
        assertTrue(exception.getMessage().contains("Month must be between 1 and 12"));
    }

    @Test
    void testClsDateConstructor_IntParams_InvalidMonth_GreaterThan12() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new clsDate(15, 13, 2023);
        });
        assertTrue(exception.getMessage().contains("Month must be between 1 and 12"));
    }

    @Test
    void testClsDateConstructor_IntParams_InvalidYear_Zero() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new clsDate(15, 6, 0);
        });
        assertTrue(exception.getMessage().contains("Year must be bigger than"));
    }

    @Test
    void testClsDateConstructor_IntParams_InvalidYear_Negative() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new clsDate(15, 6, -2023);
        });
        assertTrue(exception.getMessage().contains("Year must be bigger than"));
    }

    // Add these tests to TestClsDate.java:

    @Test
    void testNumberOfHoursInAYear_InvalidYear_Zero() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clsDate.numberOfHoursInAYear(0);
        });
        assertTrue(exception.getMessage().contains("Year must be bigger than  0"));
    }

    // Add these tests to TestClsDate.java:

    @Test
    void testNumberOfSecondsInAYear_InvalidYear_Zero() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clsDate.numberOfSecondsInAYear(0);
        });
        assertTrue(exception.getMessage().contains("Year must be bigger than  0"));
    }

    @Test
    void testNumberOfSecondsInAYear_InvalidYear_Negative() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clsDate.numberOfSecondsInAYear(-100);
        });
        assertTrue(exception.getMessage().contains("Year must be bigger than  0"));
    }
    @Test
    void testNumberOfHoursInAYear_InvalidYear_Negative() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clsDate.numberOfHoursInAYear(-100);
        });
        assertTrue(exception.getMessage().contains("Year must be bigger than  0"));
    }
    // Add ALL these tests to TestClsDate.java:

    // Tests for numberOfHoursInAYear:


// Add these tests to TestClsDate.java:

    @Test
    void testDayShortName_DayMonthYear_InvalidMonth_LessThan1() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clsDate.dayShortName(15, 0, 2023);
        });
        assertTrue(exception.getMessage().contains("Month must be between 1 and 12"));
    }

    @Test
    void testDayShortName_DayMonthYear_InvalidMonth_GreaterThan12() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clsDate.dayShortName(15, 13, 2023);
        });
        assertTrue(exception.getMessage().contains("Month must be between 1 and 12"));
    }
// Add these tests to TestClsDate.java to cover both branches of day validation:

    @Test
    void testDaysFromTheBeginningOfTheYear_InvalidDay_LessThan1() {
        // Tests the day < 1 branch
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clsDate.daysFromTheBeginningOfTheYear(0, 6, 2023);
        });
        assertTrue(exception.getMessage().contains("Day must be between 1 and"));
    }

    @Test
    void testDaysFromTheBeginningOfTheYear_InvalidDay_GreaterThanMax() {
        // Tests the day > maxDays branch
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clsDate.daysFromTheBeginningOfTheYear(32, 6, 2023); // June has 30 days
        });
        assertTrue(exception.getMessage().contains("Day must be between 1 and"));
    }

    @Test
    void testDaysFromTheBeginningOfTheYear_InvalidDay_Feb30NonLeap() {
        // Tests day > maxDays for February in non-leap year
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clsDate.daysFromTheBeginningOfTheYear(30, 2, 2023); // Feb has 28 days
        });
        assertTrue(exception.getMessage().contains("Day must be between 1 and"));
    }
    @Test
    void testDayShortName_DayMonthYear_InvalidYear_Zero() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clsDate.dayShortName(15, 6, 0);
        });
        assertTrue(exception.getMessage().contains("Year must be bigger than  0"));
    }


    // Add these tests to TestClsDate.java to cover both branches of month validation:

    @Test
    void testPrintMonthCalendar_InvalidMonth_LessThan1() {
        // Tests the month < 1 branch
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clsDate.printMonthCalendar(0, 2023);
        });
        assertTrue(exception.getMessage().contains("Month must be between 1 and 12"));
    }


    // Add these tests to TestClsDate.java to cover both branches of monthNumber validation:

    @Test
    void testMonthShortName_InvalidMonth_LessThan1() {
        // Tests the monthNumber < 1 branch
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clsDate.monthShortName(0);
        });
        assertTrue(exception.getMessage().contains("Month must be between 1 and 12"));
    }

    // Add these tests to TestClsDate.java to cover both branches of dayOfWeekOrder validation:
// Add these tests to TestClsDate.java to cover both branches of day validation:

    @Test
    void testDayOfWeekOrder_InvalidDay_LessThan1() {
        // Tests the day < 1 branch
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clsDate.dayOfWeekOrder(0, 6, 2023);
        });
        assertTrue(exception.getMessage().contains("Day must be between 1 and"));
    }

    // Add these tests to TestClsDate.java to cover both branches of day validation:

    @Test
    void testClsDateConstructor_String_InvalidDay_LessThan1() {
        // Tests the day < 1 branch
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new clsDate("0/6/2023");
        });
        assertTrue(exception.getMessage().contains("Day must be between 1 and"));
    }

    @Test
    void testClsDateConstructor_String_InvalidDay_GreaterThanMax() {
        // Tests the day > maxDays branch
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new clsDate("32/6/2023"); // June has 30 days
        });
        assertTrue(exception.getMessage().contains("Day must be between 1 and"));
    }

    @Test
    void testClsDateConstructor_String_InvalidDay_Feb30() {
        // Tests day > maxDays for February
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new clsDate("30/2/2023"); // Feb has 28 days in 2023
        });
        assertTrue(exception.getMessage().contains("Day must be between 1 and"));
    }
    @Test
    void testClsDateConstructor_IntParams_InvalidDay_LessThan1() {
        // Tests the day < 1 branch
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new clsDate(0, 6, 2023);
        });
        assertTrue(exception.getMessage().contains("Day must be between 1 and"));
    }

    @Test
    void testClsDateConstructor_IntParams_InvalidDay_GreaterThanMax() {
        // Tests the day > maxDays branch
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new clsDate(32, 6, 2023); // June has 30 days
        });
        assertTrue(exception.getMessage().contains("Day must be between 1 and"));
    }

    @Test
    void testClsDateConstructor_IntParams_InvalidDay_Feb30() {
        // Tests day > maxDays for February
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new clsDate(30, 2, 2023); // Feb has 28 days in 2023
        });
        assertTrue(exception.getMessage().contains("Day must be between 1 and"));
    }
    @Test
    void testDayShortName_DayMonthYear_InvalidDay_LessThan1() {
        // Tests the day < 1 branch
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clsDate.dayShortName(0, 6, 2023);
        });
        assertTrue(exception.getMessage().contains("Day must be between 1 and"));
    }

    @Test
    void testDayShortName_DayMonthYear_InvalidDay_GreaterThanMax() {
        // Tests the day > maxDays branch
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clsDate.dayShortName(32, 6, 2023); // June has 30 days
        });
        assertTrue(exception.getMessage().contains("Day must be between 1 and"));
    }

    @Test
    void testDayShortName_DayMonthYear_InvalidDay_Feb30() {
        // Tests day > maxDays for February
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clsDate.dayShortName(30, 2, 2023); // Feb has 28 days in 2023
        });
        assertTrue(exception.getMessage().contains("Day must be between 1 and"));
    }

    @Test
    void testSetMonth_InvalidMonth_LessThan1() {
        // Tests the month < 1 branch
        clsDate date = new clsDate(15, 6, 2023);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            date.setMonth(0);
        });
        assertTrue(exception.getMessage().contains("Month must be between 1 and 12"));
    }

    @Test
    void testSetMonth_InvalidMonth_GreaterThan12() {
        // Tests the month > 12 branch
        clsDate date = new clsDate(15, 6, 2023);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            date.setMonth(13);
        });
        assertTrue(exception.getMessage().contains("Month must be between 1 and 12"));
    }
    @Test
    void testNumberOfDaysInAMonth_InvalidMonth_LessThan1() {
        // Tests the month < 1 branch
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clsDate.numberOfDaysInAMonth(0, 2023);
        });
        assertTrue(exception.getMessage().contains("Month must be between 1 and 12"));
    }

    @Test
    void testNumberOfDaysInAMonth_InvalidMonth_GreaterThan12() {
        // Tests the month > 12 branch
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clsDate.numberOfDaysInAMonth(13, 2023);
        });
        assertTrue(exception.getMessage().contains("Month must be between 1 and 12"));
    }
    @Test
    void testDayOfWeekOrder_InvalidDay_GreaterThanMax() {
        // Tests the day > maxDays branch
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clsDate.dayOfWeekOrder(32, 6, 2023); // June has 30 days
        });
        assertTrue(exception.getMessage().contains("Day must be between 1 and"));
    }

    @Test
    void testDayOfWeekOrder_InvalidDay_Feb30() {
        // Tests day > maxDays for February
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clsDate.dayOfWeekOrder(30, 2, 2023); // Feb has 28 days in 2023
        });
        assertTrue(exception.getMessage().contains("Day must be between 1 and"));
    }
    @Test
    void testDayShortName_DayOfWeekOrder_LessThan0() {
        // Tests the dayOfWeekOrder < 0 branch
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clsDate.dayShortName(-1);
        });
        assertTrue(exception.getMessage().contains("day must be from 0 to 6"));
    }

    @Test
    void testDayShortName_DayOfWeekOrder_GreaterThan6() {
        // Tests the dayOfWeekOrder > 6 branch
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clsDate.dayShortName(7);
        });
        assertTrue(exception.getMessage().contains("day must be from 0 to 6"));
    }

    @Test
    void testMonthShortName_InvalidMonth_GreaterThan12() {
        // Tests the monthNumber > 12 branch
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clsDate.monthShortName(13);
        });
        assertTrue(exception.getMessage().contains("Month must be between 1 and 12"));
    }
    @Test
    void testPrintMonthCalendar_InvalidMonth_GreaterThan12() {
        // Tests the month > 12 branch
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clsDate.printMonthCalendar(13, 2023);
        });
        assertTrue(exception.getMessage().contains("Month must be between 1 and 12"));
    }
    @Test
    void testDayShortName_DayMonthYear_InvalidYear_Negative() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clsDate.dayShortName(15, 6, -2023);
        });
        assertTrue(exception.getMessage().contains("Year must be bigger than  0"));
    }

    // Tests for numberOfMinutesInAYear:
    @Test
    void testNumberOfMinutesInAYear_InvalidYear_Zero() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clsDate.numberOfMinutesInAYear(0);
        });
        assertTrue(exception.getMessage().contains("Year must be bigger than  0"));
    }

    @Test
    void testNumberOfMinutesInAYear_InvalidYear_Negative() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clsDate.numberOfMinutesInAYear(-100);
        });
        assertTrue(exception.getMessage().contains("Year must be bigger than  0"));
    }
    @Test
    void testAddDays_FromLeapYearFeb29ToMarch1() {
        // Tests leap year Feb 29 to March 1
        clsDate date = new clsDate(29, 2, 2024);
        date.addDays(1);
        assertEquals(1, date.getDay());
        assertEquals(3, date.getMonth());
        assertEquals(2024, date.getYear());
    }

    // ============== Tests for getDateFromDayOrderInYear - Loop branches ==============
    @Test
    void testClsDateConstructor_String_InvalidMonth_LessThan1() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new clsDate("15/0/2023");
        });
        assertTrue(exception.getMessage().contains("Month must be between 1 and 12"));
    }

    @Test
    void testClsDateConstructor_String_InvalidMonth_GreaterThan12() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new clsDate("15/13/2023");
        });
        assertTrue(exception.getMessage().contains("Month must be between 1 and 12"));
    }

    @Test
    void testClsDateConstructor_String_InvalidYear_Zero() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new clsDate("15/6/0");
        });
        assertTrue(exception.getMessage().contains("Year must be bigger than"));
    }

    @Test
    void testClsDateConstructor_String_InvalidYear_Negative() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new clsDate("15/6/-2023");
        });
        assertTrue(exception.getMessage().contains("Year must be bigger than"));
    }



    @Test
    void testGetDateFromDayOrderInYear_ExactlyEndOfMonth() {
        // Tests when remainingDays equals monthDays
        clsDate date = clsDate.getDateFromDayOrderInYear(31, 2023); // Jan 31
        assertEquals(31, date.getDay());
        assertEquals(1, date.getMonth());
        assertEquals(2023, date.getYear());
    }

    @Test
    void testGetDateFromDayOrderInYear_CrossMultipleMonths() {
        // Tests loop continues through several months
        clsDate date = clsDate.getDateFromDayOrderInYear(200, 2023);
        assertEquals(19, date.getDay());
        assertEquals(7, date.getMonth());
        assertEquals(2023, date.getYear());
    }

    @Test
    void testGetDateFromDayOrderInYear_Day366LeapYear() {
        // Tests last day of leap year
        clsDate date = clsDate.getDateFromDayOrderInYear(366, 2024);
        assertEquals(31, date.getDay());
        assertEquals(12, date.getMonth());
        assertEquals(2024, date.getYear());
    }

    // ============== Tests for numberOfDaysInAMonth - Array access branches ==============

    @Test
    void testNumberOfDaysInAMonth_AllMonthsNonLeapYear() {
        // Ensures all array indices are accessed
        int[] expected = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        for (int month = 1; month <= 12; month++) {
            assertEquals(expected[month - 1], clsDate.numberOfDaysInAMonth(month, 2023));
        }
    }

    @Test
    void testNumberOfDaysInAMonth_Month1() {
        assertEquals(31, clsDate.numberOfDaysInAMonth(1, 2023));
    }

    @Test
    void testNumberOfDaysInAMonth_Month3() {
        assertEquals(31, clsDate.numberOfDaysInAMonth(3, 2023));
    }

    @Test
    void testNumberOfDaysInAMonth_Month4() {
        assertEquals(30, clsDate.numberOfDaysInAMonth(4, 2023));
    }

    @Test
    void testNumberOfDaysInAMonth_Month12() {
        assertEquals(31, clsDate.numberOfDaysInAMonth(12, 2023));
    }

    // ============== Tests for dayOfWeekOrder - Formula branches ==============

    @Test
    void testDayOfWeekOrder_January() {
        // Tests January (month = 1) in formula
        int order = clsDate.dayOfWeekOrder(1, 1, 2023);
        assertEquals(0, order); // Sunday
    }

    @Test
    void testDayOfWeekOrder_December() {
        // Tests December (month = 12) in formula
        int order = clsDate.dayOfWeekOrder(31, 12, 2023);
        assertEquals(0, order); // Sunday
    }

    @Test
    void testDayOfWeekOrder_VariousDaysInMonth() {
        // Tests different days in same month
        clsDate date1 = new clsDate(1, 6, 2023);
        clsDate date2 = new clsDate(15, 6, 2023);
        clsDate date3 = new clsDate(30, 6, 2023);

        assertTrue(date1.dayOfWeekOrder() >= 0 && date1.dayOfWeekOrder() <= 6);
        assertTrue(date2.dayOfWeekOrder() >= 0 && date2.dayOfWeekOrder() <= 6);
        assertTrue(date3.dayOfWeekOrder() >= 0 && date3.dayOfWeekOrder() <= 6);
    }

    // ============== Tests for daysFromTheBeginningOfTheYear - Loop branches ==============

    @Test
    void testDaysFromTheBeginningOfTheYear_FirstDay() {
        // Tests when loop doesn't execute (month = 1)
        assertEquals(1, clsDate.daysFromTheBeginningOfTheYear(1, 1, 2023));
    }

    @Test
    void testDaysFromTheBeginningOfTheYear_LastDayOfMonth() {
        // Tests with last day of various months
        assertEquals(31, clsDate.daysFromTheBeginningOfTheYear(31, 1, 2023));
        assertEquals(59, clsDate.daysFromTheBeginningOfTheYear(28, 2, 2023));
    }

    @Test
    void testDaysFromTheBeginningOfTheYear_MidYear() {
        // Tests loop through multiple months
        assertEquals(182, clsDate.daysFromTheBeginningOfTheYear(1, 7, 2023));
    }

    // ============== Tests for instance method branches ==============

    @Test
    void testInstanceMethod_DaysFromBeginningOfYear() {
        clsDate date = new clsDate(1, 7, 2023);
        assertEquals(182, date.daysFromTheBeginningOfTheYear());
    }

    @Test
    void testInstanceMethod_GetDifferenceInDays() {
        clsDate date1 = new clsDate(1, 1, 2023);
        clsDate date2 = new clsDate(10, 1, 2023);
        assertEquals(9, date1.getDifferenceInDays(date2));
    }

    @Test
    void testInstanceMethod_IsDateBeforeDate2_True() {
        clsDate date1 = new clsDate(1, 1, 2023);
        clsDate date2 = new clsDate(10, 1, 2023);
        assertTrue(date1.isDateBeforeDate2(date2));
    }

    @Test
    void testInstanceMethod_IsDateBeforeDate2_False() {
        clsDate date1 = new clsDate(10, 1, 2023);
        clsDate date2 = new clsDate(1, 1, 2023);
        assertFalse(date1.isDateBeforeDate2(date2));
    }

    @Test
    void testInstanceMethod_IsDateEqualDate2_True() {
        clsDate date1 = new clsDate(1, 1, 2023);
        clsDate date2 = new clsDate(1, 1, 2023);
        assertTrue(date1.isDateEqualDate2(date2));
    }

    @Test
    void testInstanceMethod_IsDateEqualDate2_False() {
        clsDate date1 = new clsDate(1, 1, 2023);
        clsDate date2 = new clsDate(2, 1, 2023);
        assertFalse(date1.isDateEqualDate2(date2));
    }

    // ============== Tests for edge cases in validation ==============

    @Test
    void testConstructor_Day1() {
        clsDate date = new clsDate(1, 6, 2023);
        assertEquals(1, date.getDay());
    }

    @Test
    void testConstructor_Day31() {
        clsDate date = new clsDate(31, 1, 2023);
        assertEquals(31, date.getDay());
    }

    @Test
    void testConstructor_Month1() {
        clsDate date = new clsDate(15, 1, 2023);
        assertEquals(1, date.getMonth());
    }

    @Test
    void testConstructor_Month12() {
        clsDate date = new clsDate(15, 12, 2023);
        assertEquals(12, date.getMonth());
    }

    @Test
    void testConstructor_Year1() {
        clsDate date = new clsDate(15, 6, 1);
        assertEquals(1, date.getYear());
    }

    // ============== Tests for setter boundary values ==============

    @Test
    void testSetMonth_Boundary1() {
        clsDate date = new clsDate(15, 6, 2023);
        date.setMonth(1);
        assertEquals(1, date.getMonth());
    }

    @Test
    void testSetMonth_Boundary12() {
        clsDate date = new clsDate(15, 6, 2023);
        date.setMonth(12);
        assertEquals(12, date.getMonth());
    }

    @Test
    void testSetYear_Boundary1() {
        clsDate date = new clsDate(15, 6, 2023);
        date.setYear(1);
        assertEquals(1, date.getYear());
    }

    @Test
    void testSetDay_February29LeapYear() {
        clsDate date = new clsDate(1, 2, 2024);
        date.setDay(29);
        assertEquals(29, date.getDay());
    }

    // ============== Tests for String constructor edge cases ==============

    @Test
    void testStringConstructor_SingleDigitDay() {
        clsDate date = new clsDate("5/6/2023");
        assertEquals(5, date.getDay());
        assertEquals(6, date.getMonth());
        assertEquals(2023, date.getYear());
    }

    @Test
    void testStringConstructor_SingleDigitMonth() {
        clsDate date = new clsDate("15/3/2023");
        assertEquals(15, date.getDay());
        assertEquals(3, date.getMonth());
        assertEquals(2023, date.getYear());
    }

    // ============== Tests for remaining uncovered branches ==============

    @Test
    void testAddDays_CrossingFebruaryNonLeap() {
        clsDate date = new clsDate(15, 2, 2023);
        date.addDays(20); // Should go to March
        assertEquals(7, date.getDay());
        assertEquals(3, date.getMonth());
        assertEquals(2023, date.getYear());
    }

    @Test
    void testAddDays_CrossingFebruaryLeap() {
        clsDate date = new clsDate(15, 2, 2024);
        date.addDays(20); // Should go to March
        assertEquals(6, date.getDay());
        assertEquals(3, date.getMonth());
        assertEquals(2024, date.getYear());
    }

    @Test
    void testAddDays_LargeDayCount() {
        clsDate date = new clsDate(1, 1, 2020);
        date.addDays(1000);
        // Should be somewhere in 2022
        assertEquals(2022, date.getYear());
    }

    @Test
    void testDayShortName_AllDaysOfWeek() {
        String[] expected = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (int i = 0; i < 7; i++) {
            assertEquals(expected[i], clsDate.dayShortName(i));
        }
    }

    @Test
    void testMonthShortName_AllMonths() {
        String[] expected = {"Jan", "Feb", "Mar", "Apr", "May", "Jun",
                "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        for (int i = 1; i <= 12; i++) {
            assertEquals(expected[i - 1], clsDate.monthShortName(i));
        }
    }

    // ============== Tests for copy constructor branches ==============

    @Test
    void testCopyConstructor_AllFields() {
        clsDate original = new clsDate(15, 6, 2023);
        clsDate copy = new clsDate(original);

        assertEquals(original.getDay(), copy.getDay());
        assertEquals(original.getMonth(), copy.getMonth());
        assertEquals(original.getYear(), copy.getYear());

        // Ensure they are independent
        copy.setDay(20);
        assertEquals(15, original.getDay());
        assertEquals(20, copy.getDay());
    }
	
	 
}
