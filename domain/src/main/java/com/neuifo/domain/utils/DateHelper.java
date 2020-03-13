package com.neuifo.domain.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class DateHelper {

    private static int sCurrentYear = -1;
    private static int sCurrentMonth = -1;

    public static final long DAY_TIME = 60 * 60 * 24 * 1000;

    // private static final DateFormat DEFAULT_FORMAT;
    private static final SimpleDateFormat TMP_FORMAT;
    //private static final DateFormat FORMAT_YYYMMDDHHMMSS = new SimpleDateFormat(DATE_FORMAT_YYYYMMDDHHMMSS);

    public static synchronized String formatDate(Date date) {
        TMP_FORMAT.applyPattern(DateType.DEFAULT_DATE_FORMAT.getValue());
        return TMP_FORMAT.format(date);
    }

    static {
        // DEFAULT_FORMAT = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        TMP_FORMAT = new SimpleDateFormat(DateType.DEFAULT_DATE_FORMAT.getValue());

        // DEFAULT_FORMAT.setTimeZone(createTimezone());
        TMP_FORMAT.setTimeZone(createTimezone());
    }

    public static TimeZone createTimezone() {
        // date.getTimeString()方法得到；该函数返回自1970年1月1日 00:00:00 GMT以来此对象表示的毫秒数。它与时区和地域没有关系(其实可以认为是GMT时间),也就是说手机上时间相同，不管是在北京和伦敦new Date().getTimeString()结果相同
        // Calendar的getInstance()方法有参数为TimeZone和Locale的重载，可以使用指定时区和语言环境获得一个日历。无参则使用默认时区和语言环境获得日历,
        // Calendar.getInstance(TimeZone.getTimeZone("GMT")).setData(new Date()), 得到的Date是当前时刻伦敦的日历,比当前时间晚8个小时

        // 获取TimeZone可以通过时区ID，如"America/New_York"，也可以通过GMT+/-hh:mm来设定
        // TimeZone.getRawOffset()方法可以用来得到当前时区的标准时间到GMT的偏移量
        // JVM默认情况下获取的就是操作系统的时区设置。因此在项目中先设置好时区
        // TimeZone.getTimeZone("Asia/Shanghai")
        // TimeZone.setDefault();
        // TimeZone.getTimeZone("Asia/Shanghai")
        return TimeZone.getTimeZone("GMT+8");
    }

    public static synchronized String formatDate(DateType targetType, Date date) {
        SimpleDateFormat dateFormat = TMP_FORMAT;
        if (date == null) {
            return "";
        }
        for (DateType dateType : DateType.values()) {
            if (targetType == dateType) {
                dateFormat.applyPattern(dateType.getValue());
                break;
            }
        }
        return dateFormat.format(date);
    }

    public static String formatDuration(int duration) {
        return String.format(Locale.CHINA, "%02d′%02d″", duration / 60000, duration % 60000 / 1000);
    }

    public static String formatToday(DateType targetType) {
        return formatDate(targetType, new Date());
        // return new SimpleDateFormat(format).format(new Date());
    }

    public static String formatDate(Date date, DateType targetType) {
        return formatDate(targetType, date);
        // return new SimpleDateFormat(format).format(date);
    }

    public static String getWeekOfDate(Date date) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    public static synchronized Date formatDate(String dateString) {
        try {
            SimpleDateFormat f = TMP_FORMAT;
            f.applyPattern(DateType.DEFAULT_DATE_FORMAT.getValue());
            return f.parse(dateString);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static synchronized Date formatDate(Date o, int dayDiff) {
        return new Date(o.getTime() + dayDiff * 24 * 3600 * 1000);
    }

    /**
     * 时间戳转特定格式时间
     *
     * @param timeStamp 时间戳
     */
    public static String formatData(DateType dataFormat, long timeStamp) {
        if (timeStamp == 0) {
            return "";
        }
        return formatData(dataFormat, new Date(timeStamp));
    }

    /**
     * 时间戳转特定格式时间
     */
    @SuppressWarnings("SimpleDateFormat")
    public static String formatData(DateType dataFormat, Date date) {
        if (date != null) {
            SimpleDateFormat format = new SimpleDateFormat(dataFormat.getValue());
            format.setTimeZone(createTimezone());
            return format.format(date);
        } else {
            return null;
        }
    }


    public static Date parseDate(long time) {
        return new Date(time);
    }


    public static synchronized Date parseDate(DateType target, String string) {
        if (string == null || string.length() == 0) {
            return null;
        }
        SimpleDateFormat f = TMP_FORMAT;

        for (DateType dateType : DateType.values()) {
            if (dateType == target) {
                f.applyPattern(dateType.getValue());
                break;
            }
        }
        try {
            return f.parse(string);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isBetweentMonth(Date start, Date end) {
        Calendar calendar1 = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"), Locale.PRC);
        calendar1.setTime(new Date());

        Calendar startCalendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"), Locale.PRC);
        startCalendar.setTime(start);

        if (calendar1.compareTo(startCalendar) < 0) {
            return false;
        }


        Calendar endCalendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"), Locale.PRC);
        endCalendar.setTime(end);
        if (calendar1.compareTo(endCalendar) > 0) {
            return false;
        }

        return true;

    }

    public static String getDateMouth(String startTime, String endTime) {
        Date startDate = parseDate(DateType.DATE_FORMAT_YYMM2, startTime);
        Date endDate = parseDate(DateType.DATE_FORMAT_YYMM2, endTime);
        if (startDate == null || endDate == null) {
            return "";
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        int starYear = calendar.get(Calendar.YEAR);
        int startMouth = calendar.get(Calendar.MONTH);
        int startDay = calendar.get(Calendar.DAY_OF_MONTH);

        calendar.setTime(endDate);
        int endYear = calendar.get(Calendar.YEAR);
        int endMouth = calendar.get(Calendar.MONTH);
        int endDay = calendar.get(Calendar.DAY_OF_MONTH);

        int offesctMouth = 30 - startDay + endDay > 15 ? 0 : -1;

        if (endYear - starYear == 0) {
            return endMouth - startMouth + offesctMouth + "个月";
        } else {
            int mouthes = endMouth + 11 - startMouth + offesctMouth + 1;
            int yearOffect = -1;

            if (mouthes > 11) yearOffect += 1;
            if (mouthes > 12) mouthes -= 12;

            String mouthTemp = mouthes == 12 ? "" : mouthes + "个月";
            String yearTemp = (endYear - starYear + yearOffect) == 0 ? "" : (endYear - starYear + yearOffect) + "年";
            return yearTemp + mouthTemp;
        }
    }


    public static boolean inCurrentMonth(Date date) {
        Calendar calendar1 = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"), Locale.PRC);
        calendar1.setTime(new Date());

        Calendar calendar2 = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"), Locale.PRC);
        calendar2.setTime(date);

        if (calendar1.get(Calendar.YEAR) != calendar2.get(Calendar.YEAR)) {
            return false;
        }

        if (calendar1.get(Calendar.MONTH) != calendar2.get(Calendar.MONTH)) {
            return false;
        }

        return true;
    }


    public static boolean inNextMonth(Date date) {

        Calendar calendar1 = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"), Locale.PRC);
        calendar1.setTime(new Date());

        Calendar calendar2 = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"), Locale.PRC);
        calendar2.setTime(date);

        if (calendar1.get(Calendar.YEAR) != calendar2.get(Calendar.YEAR)) {
            return false;
        }

        if (calendar1.get(Calendar.MONTH) + 1 == calendar2.get(Calendar.MONTH)) {
            return true;
        }
        return false;

    }


    public static int getCurrentYear() {
        if (sCurrentYear < 0) {
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"), Locale.PRC);
            calendar.setTime(new Date());
            sCurrentYear = calendar.get(Calendar.YEAR);
        }
        return sCurrentYear;
    }

    public static int getCurrentHour() {
        Calendar calendar = Calendar.getInstance(createTimezone());
        calendar.setTime(new Date());
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public static int getCurrentDayOfWeek() {
        Calendar calendar = Calendar.getInstance(createTimezone());
        calendar.setTime(new Date());
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    public static boolean isWorkDay() {
        int dayOfWeek = getCurrentDayOfWeek();
        if (dayOfWeek > 1 && dayOfWeek < 7) {
            return true;
        }
        return false;
    }

    public static boolean betweenHour(int start, int end) {
        if (start >= end) {
            new RuntimeException("start must be less than end");
        }
        int currentHour = getCurrentHour();
        if (currentHour < start || currentHour > end) {
            return false;
        }
        return true;
    }

    public static int getCurrentMonth() {
        if (sCurrentMonth < 0) {
            Calendar calendar = Calendar.getInstance(createTimezone());
            calendar.setTime(new Date(getCurrentTimeMillis()));
            sCurrentMonth = calendar.get(Calendar.MONTH);
        }
        return sCurrentMonth;
    }

    private static Date getCurrentMonthDate(int index) {
        int currentMonth = getCurrentMonth() + index;
        int currentYear = getCurrentYear();
        return DateHelper.parseDate(DateType.DATE_FORMAT_YYMM2, currentYear + "-" + currentMonth);
    }


    public static Date getCurrentMonthDate() {
        return getCurrentMonthDate(1);
    }

    public static Date getNextMonthDate() {
        return getCurrentMonthDate(2);
    }

    public static Date getDateBeforeMonths(int diff) {
        Calendar calendar = Calendar.getInstance(createTimezone());
        calendar.setTime(new Date());
        calendar.add(Calendar.MONTH, -diff);
        return calendar.getTime();
    }


    public static Date getDateBeforeDays(int diff) {
        Calendar calendar = Calendar.getInstance(createTimezone());
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, -diff);
        return calendar.getTime();
    }

    public static Date today() {
        Calendar calendar = Calendar.getInstance(createTimezone());
        calendar.setTime(new Date());
        return calendar.getTime();
    }

    public static Date yesterDay() {
        Calendar calendar = Calendar.getInstance(createTimezone());
        calendar.setTime(new Date(new Date().getTime() - DAY_TIME));
        return calendar.getTime();
    }

    public static Date getHourOfYesterday(int hour) {
        Calendar calendar = Calendar.getInstance(createTimezone());
        calendar.setTime(getDateBeforeDays(1));
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date getHourOfToday(int hour) {
        Calendar calendar = Calendar.getInstance(createTimezone());
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }


    public static Date now(long serverTime, long localTime) {
        Date now = new Date();
        now.setTime(now.getTime() + (serverTime - localTime));
        return now;
    }


    /***
     * 把当地的时间转为服务器当时的时间，一般递交数据前先把Date一类数据转换
     *
     * @param tm
     * @return
     */
    public static Date dateTimeConvertToServer(Date tm) {
        if (tm == null) return null;
        tm = new Date(tm.getTime() - getDiffTimeZoneRawOffsetStd(TimeZone.getDefault().getID())); // 转成格林威治时间
        Date d = new Date(tm.getTime() + getDiffTimeZoneRawOffsetStd("GMT+8"));
        return d;
    }

    /**
     * 计算出指定时区跟格林威治时间差
     */
    public static int getDiffTimeZoneRawOffsetStd(String timeZoneId) {
        //return TimeZone.getTimeZone(timeZoneId).getRawOffset();

        TimeZone tz = TimeZone.getTimeZone(timeZoneId);
        return tz.getOffset(GregorianCalendar.getInstance(tz).getTimeInMillis());
    }

    public static long getStringTimeMillis(String time) {
        Calendar calendar = Calendar.getInstance(createTimezone());
        if (formatDate(time) != null) {
            calendar.setTime(formatDate(time));
        } else {
            calendar.setTime(new Date());
        }
        return calendar.getTimeInMillis();
    }

    public static long getCurrentTimeMillis() {
        return System.currentTimeMillis();
    }

    public static int getDayOfMouth(Date date) {
        Calendar calendar = Calendar.getInstance(createTimezone());
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static int getDayOfYear(Date date) {
        Calendar calendar = Calendar.getInstance(createTimezone());
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_YEAR);
    }

    public static int getCurrentMonth(Date date) {
        Calendar calendar = Calendar.getInstance(createTimezone());
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH);
    }

    public static long getDayOffsect(int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, -day);
        return calendar.getTimeInMillis() / 1000 * 1000;
    }

    public static String transFerDayOfYearForConverV2(long timeMillis) {
        Calendar now = Calendar.getInstance(Locale.CHINA);
        Calendar target = Calendar.getInstance(Locale.CHINA);
        Date targetDate = new Date(timeMillis);
        target.setTime(targetDate);

        int nowDayOfYear = now.get(Calendar.DAY_OF_YEAR);
        int targetDayOfYear = target.get(Calendar.DAY_OF_YEAR);
        if (now.get(Calendar.YEAR) == target.get(Calendar.YEAR)) {
            if (nowDayOfYear - targetDayOfYear == 0) {
                return formatDate(targetDate, DateType.DATE_FORMAT_HHMM);
            } else {
                return formatDate(targetDate, DateType.DATE_FORMAT_MMDD2);
            }
        }
        return formatDate(targetDate, DateType.DATE_FORMAT_MMDD2);
    }

    public static String transFerDayOfYearForConver(long timeMillis) {
        Calendar now = Calendar.getInstance(Locale.CHINA);
        Calendar target = Calendar.getInstance(Locale.CHINA);
        Date targetDate = new Date(timeMillis);
        target.setTime(targetDate);

        int nowDayOfYear = now.get(Calendar.DAY_OF_YEAR);
        int targetDayOfYear = target.get(Calendar.DAY_OF_YEAR);
        if (now.get(Calendar.YEAR) == target.get(Calendar.YEAR)) {
            if (nowDayOfYear - targetDayOfYear == 0) {
                return "今天  " + formatDate(targetDate, DateType.DATE_FORMAT_HHMM);
            } else if (nowDayOfYear - targetDayOfYear == 1) {
                return "昨天  " + formatDate(targetDate, DateType.DATE_FORMAT_HHMM);
            } else if (nowDayOfYear - targetDayOfYear == 2) {
                return "前天  " + formatDate(targetDate, DateType.DATE_FORMAT_HHMM);
            }
        }

        return formatDate(targetDate, DateType.DATE_FORMAT_MMDDHHMM);
    }

    public static String transActiveByTime(long time) {
        //计算出现在距参数时间的分钟差。
        long interMinute = (System.currentTimeMillis() - time) / 60000;
        if (interMinute >= 60) { //过去时间
            if (interMinute < 1440) {
                return interMinute / 60 + "小时前活跃";
            } else if (interMinute < 10080) {
                return interMinute / 1440 + "天前活跃";
            } else if (interMinute < 43200) {
                return interMinute / 10080 + "周前活跃";
            } else {
                return "1个月前活跃";
            }
        } else { // 今天的未来时间 理论上不应该出现。出现了就是后端的问题。
            return "刚刚活跃";
        }
    }

    public static String transFerDayOffsetCommon(long time, String defaultValue) {
        Calendar now = Calendar.getInstance(Locale.CHINA);
        Calendar cur = Calendar.getInstance(Locale.CHINA);
        cur.setTime(new Date(time));

        int nowDayOfYear = now.get(Calendar.DAY_OF_YEAR);
        int curDayOfYear = cur.get(Calendar.DAY_OF_YEAR);

        if (now.get(Calendar.YEAR) == cur.get(Calendar.YEAR)) {
            if (nowDayOfYear == curDayOfYear) {
                if (now.getTimeInMillis() > cur.getTimeInMillis()) { //今天的过去时间
                    //计算出现在距参数时间的分钟差。
                    long interMinute =
                            (now.get(Calendar.HOUR_OF_DAY) * 60 + now.get(Calendar.MINUTE)) /*现在时间的 MinuteOfDay*/
                                    - (cur.get(Calendar.HOUR_OF_DAY) * 60 + cur.get(Calendar.MINUTE)); /*参数时间的 MinuteOfDay*/
                    if (interMinute == 0) {
                        return defaultValue;
                    } else if (interMinute < 60) {
                        return interMinute + "分钟前";
                    } else {
                        return interMinute / 60 + "小时前";
                    }
                } else { // 今天的未来时间
                    return "今天 " + DateHelper.formatData(DateType.DATE_FORMAT_HHMM, cur.getTime());
                }
            } else if (nowDayOfYear > curDayOfYear) { //过去时间
                int offsetDay = nowDayOfYear - curDayOfYear;
                return offsetDay == 1 ? "昨天" : offsetDay <= 6 ? offsetDay + "天前" : DateHelper.formatData(DateType.DATE_FORMAT_MMDD1, cur.getTime());
            } else {  //未来时间
                int offsetDay = curDayOfYear - nowDayOfYear;
                return offsetDay == 1 ? "明天 " + DateHelper.formatData(DateType.DATE_FORMAT_HHMM, cur.getTime()) : DateHelper.formatData(DateType.DATE_FORMAT_MMDDHHMM, cur.getTime());
            }
        } else {
            return DateHelper.formatData(DateType.DEFAULT_DATE_FORMAT, cur.getTime());
        }
    }

    public static int getDayOffsect(Long time) {
        int currentDay = getDayOfMouth(new Date());
        int targetDay = getDayOfMouth(new Date(time));
        return (targetDay - currentDay);
    }

    public static String getDayOffsect(Date date) {
        if (date == null) {
            return "";
        }
        int currentDay = getDayOfMouth(new Date());
        int targetDay = getDayOfMouth(date);
        if (targetDay - currentDay == 0) {
            return "今天";
        } else if (targetDay - currentDay == 1) {
            return "明天";
        } else if (targetDay - currentDay == 2) {
            return "后天";
        } else if (targetDay - currentDay == -1) {
            return "昨天";
        } else if (targetDay - currentDay == -2) {
            return "前天";
        }
        return formatDate(date, DateType.DATE_FORMAT_MMDD1);
    }

    public static int getAgeByBirth(Date birthday) {
        int age = 0;

        if (birthday == null) {
            return age;
        }

        try {
            Calendar now = Calendar.getInstance();
            now.setTime(new Date());// 当前时间

            Calendar birth = Calendar.getInstance();
            birth.setTime(birthday);

            if (birth.after(now)) {//如果传入的时间，在当前时间的后面，返回0岁
                age = 0;
            } else {
                age = now.get(Calendar.YEAR) - birth.get(Calendar.YEAR);
                if (now.get(Calendar.DAY_OF_YEAR) > birth.get(Calendar.DAY_OF_YEAR)) {
                    age += 1;
                }
            }
            return age;
        } catch (Exception e) {//兼容性更强,异常后返回数据
            return 0;
        }
    }

    /**
     * 获取给定时间的一段时间后的时间。
     *
     * @param timeMillis    要计算的时间戳。
     * @param calendarFiled 要增减的字段。
     * @param offset        要增减的值。
     * @return 返回 yyyy-MM-dd 格式的日期字符串。
     */
    public static String getSpellDate(long timeMillis, int calendarFiled, int offset) {
        Calendar time = Calendar.getInstance();
        time.setTimeInMillis(timeMillis);
        time.add(calendarFiled, offset);
        return formatDate(time.getTime());
    }
}
