package com.fortysevendeg.mvessel.util

case class ConnectionValues(name: String, params: Map[String, String])

trait ConnectionStringParser {

  val urlRegex = "jdbc:sqlite:(:?[\\/\\.\\-_A-Za-z0-9]+:?)(\\?([A-Za-z0-9]+=[A-Za-z0-9]+)((\\&([A-Za-z0-9]+=[A-Za-z0-9]+))*)?)?".r

  def parseConnectionString(connectionString: String): Option[ConnectionValues] = {
    Option(connectionString) flatMap { c =>
      val matcher = urlRegex.pattern.matcher(c)
      matcher.matches() match {
        case true =>
          Some(ConnectionValues(
            name = matcher.group(1),
            params = readParams(matcher.group(3), matcher.group(4))))
        case _ =>
          None
      }
    }
  }

  private[this] def readParams(firstParam: String, lastParams: String): Map[String, String] =
    (Option(firstParam), Option(lastParams)) match {
      case (Some(f), Some(t)) if f.length > 0 => paramsToMap(f) ++ paramsToMap(cleanParams(t): _*)
      case (Some(f), None) if f.length > 0 => paramsToMap(f)
      case _ => Map.empty
    }

  private[this] def paramsToMap(params: String*): Map[String, String] =
    Map(params map { p =>
      val Array(name, value) = p.split("=")
      name -> value
    }: _*)

  private[this] def cleanParams(params: String): Seq[String] =
    params.split("\\&").filter(_.length > 0)

}
