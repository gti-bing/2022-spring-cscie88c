package org.cscie88c.week13

import org.cscie88c.testutils.{ StandardTest }
import scala.util.{Try, Success, Failure}
import cats.implicits._

class AverageTransactionAggregateTest extends StandardTest{
    "AverageTransactionAggregate" should {
       "use monoid to perform aggregation" in {
           val dataA = AverageTransactionAggregate("2016-01-MF",100000,10)
           val dataB = AverageTransactionAggregate("2016-01-MF",300000,15)
           val data = List(dataA,dataB)
           
           val expectedResult = AverageTransactionAggregate("2016-01-MF",400000,25)
           val actualResult = data.reduce( _ |+| _)
           actualResult shouldBe expectedResult
       }
    }

}