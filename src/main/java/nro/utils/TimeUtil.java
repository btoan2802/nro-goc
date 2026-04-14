package nro.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;

/**
 *
 * @author Phong Vũ
 * @copyright Phong Vũ
 *
 */
public class TimeUtil {

    public static final byte SECOND = 1;
    public static final byte MINUTE = 2;
    public static final byte HOUR = 3;
    public static final byte DAY = 4;
    public static final byte WEEK = 5;
    public static final byte MONTH = 6;
    public static final byte YEAR = 7;

    /**
     *
     * @param d1 thời gian bắt đầu
     * @param d2 thời gian kết thúc
     * @param type loại
     * @return khoảng cách thời gian theo loại
     */
    public static long diffDate(Date d1, Date d2, byte type) {
        long timeDiff = Math.abs(d1.getTime() - d2.getTime());
        switch (type) {
            case SECOND:
                return (timeDiff / 1000);
            case MINUTE:
                return (timeDiff / (60 * 1000) % 60);
            case HOUR:
                return (timeDiff / (60 * 60 * 1000) % 24);
            case DAY:
                return (timeDiff / (24 * 60 * 60 * 1000));
            case WEEK:
                return (timeDiff / (7 * 24 * 60 * 60 * 1000));
            case MONTH:
                return (timeDiff / (30 * 24 * 60 * 60 * 1000));
            case YEAR:
                return (timeDiff / (365 * 24 * 60 * 60 * 1000));
            default:
                return 0;
        }
    }
    
    public static long stringToTime(String time, String format) throws Exception {
      SimpleDateFormat fm = new SimpleDateFormat(format);

      try {
         return fm.parse(time).getTime();
      } catch (ParseException var4) {
         throw new Exception("Thời gian không hợp lệ");
      }
   }

    public static boolean isTimeNowInRangex(String d1, String d2, String format) throws Exception {
        SimpleDateFormat fm = new SimpleDateFormat(format);
        try {
            long time1 = fm.parse(d1).getTime();
            long time2 = fm.parse(d2).getTime();
            long now = fm.parse(fm.format(new Date())).getTime();
            return now > time1 && now < time2;
        } catch (Exception e) {
            throw new Exception("Thời gian không hợp lệ");
        }
    }

    public static String getTimeLeft(long timeLeft) {
        if (timeLeft <= 0) {
            return "0 giây";
        }
        long minutes = timeLeft / 60000; // Convert milliseconds to minutes
        if (minutes > 0) {
            return minutes + " phút";
        } else {
            long seconds = (timeLeft % 60000) / 1000; // Convert remaining milliseconds to seconds
            return seconds + " giây";
        }
    }

    public static int getCurrDay() {
        Date date = new Date();
        return date.getDay();
    }

    public static int getCurrHour() {
        Date date = new Date();
        return date.getHours();
    }

    public static int getCurrMin() {
        Date date = new Date();
        return date.getMinutes();
    }

    public static String getTimeLeft(long lastTime, int secondTarget) {
        int secondPassed = (int) ((System.currentTimeMillis() - lastTime) / 1000);
        int secondsLeft = secondTarget - secondPassed;
        if (secondsLeft < 0) {
            secondsLeft = 0;
        }
        return secondsLeft > 60 ? (secondsLeft / 60) + " phút" : secondsLeft + " giây";
    }

    public static int getMinLeft(long lastTime, int secondTarget) {
        int secondPassed = (int) ((System.currentTimeMillis() - lastTime) / 1000);
        int secondsLeft = secondTarget - secondPassed;
        if (secondsLeft < 0) {
            secondsLeft = 0;
        }
        int minLeft = 0;
        if (secondsLeft > 0 && secondsLeft <= 60) {
            minLeft = 1;
        } else if (secondsLeft > 60) {
            minLeft = secondsLeft / 60;
        }
        return minLeft;
    }

    public static int getSecondLeft(long lastTime, int secondTarget) {
        int secondPassed = (int) ((System.currentTimeMillis() - lastTime) / 1000);
        int secondsLeft = secondTarget - secondPassed;
        if (secondsLeft < 0) {
            secondsLeft = 0;
        }
        return secondsLeft;
    }

    public static long getTime(String time, String format) throws Exception {
        SimpleDateFormat fm = new SimpleDateFormat(format);
        try {
            return fm.parse(time).getTime();
        } catch (ParseException ex) {
            throw new Exception("Thời gian không hợp lệ");
        }
    }

    public static String getTimeNow(String format) {
        SimpleDateFormat fm = new SimpleDateFormat(format);
        return fm.format(new Date());
    }

    public static String getTimeBeforeCurrent(int subTime, String format) {
        SimpleDateFormat fm = new SimpleDateFormat(format);
        Date date = new Date(System.currentTimeMillis() - subTime);
        return fm.format(date);
    }

    public static void main(String[] args) {
        System.out.println(getTimeBeforeCurrent(21 * 24 * 60 * 60 * 1000, "yyyy-MM-dd"));
    }

    public static String formatTime(Date time, String format) {
        SimpleDateFormat fm = new SimpleDateFormat(format);
        return fm.format(time);
    }

    public static String formatTime(long time, String format) {
        SimpleDateFormat fm = new SimpleDateFormat(format);
        return fm.format(new Date(time));
    }

    public static String getTimeAgo(int timeRemainS) {
        if (timeRemainS < 0) {
            timeRemainS = 0;
        }

        int num = 0;

        if (timeRemainS >= 60) {
            num = timeRemainS / 60;
            timeRemainS %= 60;
        }

        int num2 = 0;

        if (num >= 60) {
            num2 = num / 60;
            num %= 60;
        }

        int num3 = 0;

        if (num2 >= 24) {
            num3 = num2 / 24;
            num2 %= 24;
        }

        String text = "";

        if (num3 > 0) {
            text += num3 + " ngày ";
            text += num2 + " giờ ";
            text += num + " phút ";
            text += timeRemainS + " giây";
        } else if (num2 > 0) {
            text += num2 + " giờ ";
            text += num + " phút ";
            text += timeRemainS + " giây";
        } else if (num > 0) {
            text += num + " phút ";
            text += (timeRemainS > 0 ? timeRemainS + " giây" : "");
        } else {
            text += (timeRemainS > 0 ? timeRemainS + " giây" : "0 giây");
        }

        return text;
    }

    public static String getTimeFromSecondToString(int timeRemainS) {
        if (timeRemainS < 0) {
            timeRemainS = 0;
        }

        int num = 0;

        if (timeRemainS >= 60) {
            num = timeRemainS / 60;
            timeRemainS %= 60;
        }

        int num2 = 0;

        if (num >= 60) {
            num2 = num / 60;
            num %= 60;
        }

        int num3 = 0;

        if (num2 >= 24) {
            num3 = num2 / 24;
            num2 %= 24;
        }

        String text = "";

        if (num3 > 0) {
            text += num3 + " ngày ";
            text += num2 + " giờ ";
            text += num + " phút ";
            text += timeRemainS + " giây";
        } else if (num2 > 0) {
            text += num2 + " giờ ";
            text += num + " phút ";
            text += timeRemainS + " giây";
        } else if (num > 0) {
            text += num + " phút ";
            text += (timeRemainS > 0 ? timeRemainS + " giây" : "");
        } else {
            text += (timeRemainS > 0 ? timeRemainS + " giây" : "0 giây");
        }

        return text;
    }

    public static String getTimeRemaining(long startTime, int seconds) {
        long currentTime = System.currentTimeMillis();
        long endTime = startTime + (seconds * 1000);
        long timeDiff = endTime - currentTime;

        if (timeDiff <= 0) {
            return "Đã qua thời gian";
        }

        int days = (int) (timeDiff / (24 * 60 * 60 * 1000));
        int hours = (int) ((timeDiff / (60 * 60 * 1000)) % 24);
        int minutes = (int) ((timeDiff / (60 * 1000)) % 60);

        StringBuilder sb = new StringBuilder();
        if (days > 0) {
            sb.append(days).append(" ngày, ");
        }
        if (hours > 0) {
            sb.append(hours).append(" giờ, ");
        }
        if (minutes > 0) {
            sb.append(minutes).append(" phút");
        }

        String remainingTime = sb.toString().trim();
        if (remainingTime.isEmpty()) {
            remainingTime = "Ít hơn 1 phút";
        }

        return remainingTime;
    }

    public static String secToTime(int sec) {
        int seconds = sec % 60;
        int minutes = sec / 60;
        if (minutes >= 60) {
            int hours = minutes / 60;
            minutes %= 60;
            if (hours >= 24) {
                int days = hours / 24;
                return String.format("%d days %02d:%02d:%02d", days, hours % 24, minutes, seconds);
            }
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        }
        return String.format("%02d:%02d", minutes, seconds);
    }

     public static boolean timeBoss(int hour) {
    if (hour >= 0 && hour <= 23 && hour % 2 == 0) {
        return true;
    }
    return false;
}
     
    public static boolean isDuaTopSmTuan(String time_create) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(time_create, formatter);

        LocalDateTime currentDateTime = LocalDateTime.now();
        WeekFields weekFields = WeekFields.of(Locale.getDefault());

        int currentWeekNumber = currentDateTime.get(weekFields.weekOfWeekBasedYear());
        int targetWeekNumber = dateTime.get(weekFields.weekOfWeekBasedYear());

        if (targetWeekNumber != currentWeekNumber) {
            return false; // time_create không thuộc vào tuần hiện tại
        }

        DayOfWeek dayOfWeek = dateTime.getDayOfWeek();
        int hour = dateTime.getHour();
        int minute = dateTime.getMinute();

        boolean isFriday = dayOfWeek == DayOfWeek.FRIDAY && hour >= 18;
        boolean isSaturday = dayOfWeek == DayOfWeek.SATURDAY;
        boolean isSunday = dayOfWeek == DayOfWeek.SUNDAY && (hour < 23 || (hour == 23 && minute == 59));

        return isFriday || isSaturday || isSunday;
    }

    public static boolean isYesterday(long currentTimeMillis) {
        long currentTime = System.currentTimeMillis();
        long todayStart = (currentTime / (24 * 60 * 60 * 1000)) * (24 * 60 * 60 * 1000);
        return currentTimeMillis < todayStart;
    }
}
