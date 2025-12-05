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


	
	 
}
