package aboodZaidLibrary;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestClsEncryption {
    static clsEncryption encryption;
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		encryption=new clsEncryption();
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
	void testEncryptText() {
		//Arrange
		String string1="abcd";
		String string2="bcde";
		//Action
		string1=clsEncryption.encryptText(string1, 1);
		//Assert
		assertEquals(string1,string2);
	}
	
	@Test
	void testEncryptText_InvalidEncryptionKey() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clsEncryption.encryptText("1234", -1);
        });
		assertTrue(exception.getMessage().contains("Encryption key must not be negative."));
	}
	
	@Test
	void testDecryptText() {
		//Arrange
		String string1="1234";
		String string2="";
		//Action
		string2=clsEncryption.encryptText(string1,1);
		string2=clsEncryption.decryptText(string2, 1);
		//Assert
		assertEquals(string1,string2);
	}
	
	@Test
	void testDecryptText_InvalidEncryptionKey() {
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            clsEncryption.decryptText("1234", -1);
        });
		assertTrue(exception.getMessage().contains("Encryption key must not be negative."));
	}

}
