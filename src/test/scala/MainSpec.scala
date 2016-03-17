import java.io.File

import org.scalatest._


class MainSpec extends FlatSpec with Matchers {

  "It" should "create a single class correctly" in {

    val testJar = new File(getClass.getResource("/scala-case-class-exporter-test-creator_2.11-1.0.jar").toURI)
    val jodaTimeJar = new File(getClass.getResource("/joda-time-2.9.2.jar").toURI)

    val result = Main.generateClassesString(Main.Config(jars = Seq(testJar, jodaTimeJar), classes = List("Car")))

    result should equal("public class Car\n{\n    public DateTime ManufacturingCompletedDate {get; set;}\n    public int Seats {get; set;}\n    public List<Door> Doors {get; set;}\n    public string Make {get; set;}\n}")

  }

}