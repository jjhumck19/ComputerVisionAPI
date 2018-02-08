package com.jhu.azureapi;

import static org.junit.Assert.*;

import org.junit.Test;


public class ComputerVisionAPITest {

	ComputerVisionAPI api = new ComputerVisionAPI();
	@Test
	public void testChangeRegion() {
		api.changeRegion("westus2");
		String expected = "https://westus2.api.cognitive.microsoft.com/vision/v1.0/";
		assertEquals(expected, api.getUriBase());
	}

}
