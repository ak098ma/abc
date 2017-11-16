package jp.co.applibot.abc.utils

object UserIdClaim {
  def key: String = "userId"

  def apply(userId: String): (String, String) = key -> userId
}
