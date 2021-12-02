package SecureNotes;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

class DateTime {
   static String getDateTime() {
      try {
         NTPUDPClient timeClient = new NTPUDPClient();
         InetAddress inetAddress = InetAddress.getByName("time-a.nist.gov");
         try {
            TimeInfo timeInfo = timeClient.getTime(inetAddress);
            ZoneId zoneId = ZoneId.of("Etc/UTC");
            ZonedDateTime zdt = ZonedDateTime.ofInstant(new Date(timeInfo.getMessage().getTransmitTimeStamp().getTime()).toInstant(), zoneId);
            LocalDate localDate = zdt.toLocalDate();
            LocalTime localTime = zdt.toLocalTime();
            return localDate + " " + localTime.toString().substring(0, 8);
         } catch (IOException e) {
            e.printStackTrace();
         }
      } catch (UnknownHostException e) {
         e.printStackTrace();
         new TextDialog("Connection", "Check your connection and try again");
      }
      return null;
   }
}
