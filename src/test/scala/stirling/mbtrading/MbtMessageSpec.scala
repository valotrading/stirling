package stirling.mbtrading

import java.nio.ByteBuffer
import org.joda.time.DateTime
import org.scalatest.WordSpec
import org.scalatest.matchers.MustMatchers
import silvertip.{PartialMessageException, GarbledMessageException}
import stirling.mbtrading.MbtMessage._
import stirling.mbtrading.MbtMessageTag._
import stirling.mbtrading.MbtMessageType._

class MbtMessageSpec extends WordSpec with MustMatchers with MbtMessageFixtures {
  "MbtMessage" when {
    "formatting" must {
      "format valid messages with one or more fields" in {
        val message = MbtMessage(Login)
          .set(Password, "password") .set(Username, "username")
        message.format must equal("L|101=password;100=username\n")
      }
      "format valid messages with zero fields" in {
        MbtMessage(Heartbeat).format must equal("9|\n")
      }
    }
    "merging" must {
      "merge copies messages from the given message" in {
        MbtMessage(Login)
          .set(Username, "username")
        .merge(MbtMessage(Login)
          .set(Password, "password")
        ) must equal(MbtMessage(Login)
          .set(Username, "username")
          .set(Password, "password")
        )
      }
      "throw an IllegalArgumentException if message types differ" in {
        intercept[IllegalArgumentException] {
          MbtMessage(Login)
          .merge(MbtMessage(Subscription))
        }
      }
    }
    "parsing" must {
      "parse non-partial messages" in {
        val message = parse(strToByteBuffer("L|2041=A;8055=B;2004=C\n"))
        message.msgType must equal(Login)
        message.fields must equal(Map(LastAsk -> "C", InfoMsgFrom -> "B", ContractSize -> "A"))
      }
      "parse partial messages" in {
        evaluating {
          parse(strToByteBuffer("L|2041=A;8055=B"))
        } must produce [PartialMessageException]
      }
    }
  }
}

trait MbtMessageFixtures {
  def strToByteBuffer(message: String) = {
    val buffer = ByteBuffer.allocate(32)
    buffer.put(message.getBytes)
    buffer.flip
    buffer
  }
}
