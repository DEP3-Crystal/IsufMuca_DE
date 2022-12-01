import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession

object IdNameAgeFriendsDataset {

  case class Person(id: Int, name: String, age: Int, friends: Int)

  def main(args: Array[String]): Unit = {

    Logger.getLogger("org").setLevel(Level.ERROR)


    val spark: SparkSession = SparkSession
      .builder()
      .master("local[*]")
      .appName("IdNameAgeFriends")
      .getOrCreate()

    import spark.implicits._

    val schemaPeople = spark.read
      .option("header", "true")
      .option("inferSchema", "true")
      .csv("src/main/resources/dataset.csv")
      .as[Person]

    schemaPeople.printSchema()
    schemaPeople.createOrReplaceTempView("people")
    val twentyAndOlderPeople = spark.sql("SELECT id,name,age,friends FROM people WHERE age>=20")
    val twentyAndOlderPeopleList = twentyAndOlderPeople.collect()

    twentyAndOlderPeopleList.foreach(e => println(e.toString()))

    spark.stop()
  }
}


