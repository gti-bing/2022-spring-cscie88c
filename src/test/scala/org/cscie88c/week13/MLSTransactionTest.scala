package org.cscie88c.week13

import org.cscie88c.testutils.{ StandardTest }
import scala.util.{Try, Success, Failure}

class MLSTransactionTest extends StandardTest{
  "MLSTransaction" should {
      "load and clean raw CSV data file" in {
        val csvRow = "1,71475884,ACT,874900,,1/25/2013,,,2726,,24 Crescent C095/Multi007,Derry,NH,03038,,12,7,7422,138,18000,Stephen Trefethen,\"Summerview Real Estate, LLC\",(603) 432-5453,\"Call List Office, Appointment Required\",\"12,324 sq ft Gross area, High Quality Building Across the street from Pinkerton High School.7,422 sq. ft of living space, includes 6 garges , hardwood floors, and custom built-ins, Victorian Hall ways. Could be easily set up and sold as condos. Next to Newell Meadows and ALWAYS full!! Don't miss out! Newer roofs & paint. NOI 10%  $64,830.06 NET QUALITY PROPERTY. Owner / Broker interest.\",,,6,,,,,Pinkerton Acad.,,MF,Crescent C095/Multi007,24,24,http://media.mlspin.com/photo.aspx?mls=71475884"
        val actualResult = MLSTransaction(csvRow)
        val expectedResult = Some(MLSTransaction(1,71475884,"ACT",874900,("").toLongOption,"1/25/2013","","24 Crescent C095/Multi007","Derry","NH","03038","MF"))
        actualResult shouldBe expectedResult

        val csvRowInvalid = "1,71475884,ACT,874900,,1/25/2013,"
        val actualResultInvalid = MLSTransaction(csvRowInvalid)
        val expectedResultInvalid = None
        actualResultInvalid shouldBe expectedResultInvalid
    } 
  }
}



