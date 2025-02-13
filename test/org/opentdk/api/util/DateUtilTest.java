package org.opentdk.api.util;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.DateTimeException;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;

public class DateUtilTest {

    @Test
    public void compare() {
        Assert.assertEquals(DateUtil.compare("20.03.2019", "2019-03-20"), 0);
        Assert.assertEquals(DateUtil.compare("20.03.2019", "2019-03-19"), 1);
        Assert.assertEquals(DateUtil.compare("20.03.2019", "2019-04-20"), -1);
        Assert.assertEquals(DateUtil.compare("20.03.2018", "2019-03-20"), -1);
        Assert.assertEquals(DateUtil.compare("2019-03-19", "2019-03-20"), -1);
        Assert.assertEquals(DateUtil.compare("2019-03-20", "2019-03-20"), 0);
        int result = -999;
        try {
            result = DateUtil.compare("Irgendetwas", "2019-03-20");
        } catch (DateTimeException ignored) {

        }
        Assert.assertEquals(result, -999);

        Assert.assertEquals(DateUtil.compare("20210329", "2021-03-29"), 0);
        Assert.assertEquals(DateUtil.compare("23:45:31", "23:45:30"), 1);
        Assert.assertEquals(DateUtil.compare("2021.03.29 12:00:56", "2021.03.29 12:00:56.001002003"), -1);
        Assert.assertEquals(DateUtil.compare("20210329 15:14:22", "20210330"), -1);
        Assert.assertEquals(DateUtil.compare("2021-03-30", "15:14:22.123"), -1);
        Assert.assertEquals(DateUtil.compare("15:14:22.123456", "2021-03-10.15:14:22"), 1);
        Assert.assertEquals(DateUtil.compare("2021-03-10.15:14:22", "2021-03-10.15:14:22.175"), -1);
    }

    @Test
    public void diff() {
        Assert.assertEquals(DateUtil.diff("2020-12-31", "20210102", ChronoUnit.DAYS), 2);
        Assert.assertEquals(DateUtil.diff("23:59:59", "00.00.00", ChronoUnit.DAYS), 0);
        Assert.assertEquals(DateUtil.diff("2021-04-01-12.30.00.000000", "2021-04-05-23.30.00.000000", ChronoUnit.DAYS), 4);
        Assert.assertEquals(DateUtil.diff("2021-04-01-12.30.00.000000", "2020-04-01-12:30:00,000", ChronoUnit.DAYS), 365);

        Assert.assertEquals(DateUtil.diff("2021/03/29", "2021-03-30", ChronoUnit.MILLIS), 86400000);
        Assert.assertEquals(DateUtil.diff("14:15:32", "14:15:30", ChronoUnit.MILLIS), 2000);
        Assert.assertEquals(DateUtil.diff("2021-01-01 12:30:45.000000", "2021-01-01 12:30:46.100000", ChronoUnit.MILLIS), 1100);
        Assert.assertEquals(DateUtil.diff("2021-03-30 01:00:00.000", "2021/03/30", ChronoUnit.MILLIS), 3600000);
    }

    @Test
    public void get() {
        Assert.assertEquals(DateUtil.get("31.12.2018", "yyyyMMdd"), "20181231");
        Assert.assertEquals(DateUtil.get("20000113", "yyyy-MM-dd"), "2000-01-13");
        Assert.assertEquals(DateUtil.get("1999-02-28", "dd-MM-yyyy"), "28-02-1999");
        Assert.assertEquals(DateUtil.get("2020.11.02", "dd.MM.yyyy"), "02.11.2020");
        Assert.assertEquals(DateUtil.get("12/24/1985", "yyyy.MM.dd"), "1985.12.24");
        Assert.assertEquals(DateUtil.get("24-12-1985", "MM/dd/yyyy"), "12/24/1985");

        String date = "12/31/2018";
        Assert.assertEquals(DateUtil.get(date, "yyyyMMdd"), "20181231");
        Assert.assertEquals(DateUtil.get(date, "yyyy-MM-dd"), "2018-12-31");
        Assert.assertEquals(DateUtil.get(date, "dd-MM-yyyy"), "31-12-2018");
        Assert.assertEquals(DateUtil.get(date, "dd.MM.yyyy"), "31.12.2018");
        Assert.assertEquals(DateUtil.get(date, "yyyy.MM.dd"), "2018.12.31");
        Assert.assertEquals(DateUtil.get(date, "MM/dd/yyyy"), "12/31/2018");

        Assert.assertEquals(DateUtil.get("31.12.2018", -42, "yyyy-MM-dd", ChronoUnit.DAYS), "2018-11-19");
        Assert.assertEquals(DateUtil.get(date, -42, "yyyy-MM-dd", ChronoUnit.DAYS), "2018-11-19");
        Assert.assertEquals(DateUtil.get("2021/01/01", "yyyy/MM/dd"), "2021/01/01");
        Assert.assertEquals(DateUtil.get(1289375173771L, "yyyyMMdd"), "20101110");

        Assert.assertEquals(DateUtil.get("2021.04.04 12:00:00.000000000", 2, "yyyy.MM.dd HH:mm:ss.SSSSSSSSS", ChronoUnit.NANOS), "2021.04.04 12:00:00.000000002");
        Assert.assertEquals(DateUtil.get("2021.01.01 12:00:00", 2, "yyyyMMdd", ChronoUnit.MICROS), "20210101");
        Assert.assertEquals(DateUtil.get("2021-01-01-00:00:00,000", 2, "yyyy-MM-dd-HH:mm:ss.SSSSSS", ChronoUnit.MILLIS), "2021-01-01-00:00:00.002000");
        Assert.assertEquals(DateUtil.get("2021.04.04 12:00:30", 2, "yyyyMMdd HH:mm:ss", ChronoUnit.SECONDS), "20210404 12:00:32");
        Assert.assertEquals(DateUtil.get("2021.04.04 12:00:30", 2, "yyyyMMdd HH:mm:ss", ChronoUnit.MINUTES), "20210404 12:02:30");
        Assert.assertEquals(DateUtil.get("2021.04.04 12:00:30", 2, "yyyyMMdd", ChronoUnit.HOURS), "20210404");
        Assert.assertEquals(DateUtil.get("2021.04.04 12:00:30", -2, "yyyyMMdd", ChronoUnit.DAYS), "20210402");
        Assert.assertEquals(DateUtil.get("2021.04.04 12:00:30", 2, "yyyyMMdd", ChronoUnit.WEEKS), "20210418");
        Assert.assertEquals(DateUtil.get("2021.04.04 12:00:30", 2, "yyyyMMdd", ChronoUnit.MONTHS), "20210604");
        Assert.assertEquals(DateUtil.get("2021.04.04 12:00:30", 2, "dd.MM.yyyy", ChronoUnit.YEARS), "04.04.2023");
    }

    @Test
    public void getFirstOf() {
        Assert.assertEquals(DateUtil.getFirstOf("04.04.2021", ChronoField.DAY_OF_WEEK, "yyyy-MM-dd"), "2021-03-29");
        Assert.assertEquals(DateUtil.getFirstOf("2021.04.04 12:00:30", ChronoField.DAY_OF_MONTH, "yyyy.MM.dd"), "2021.04.01");
        Assert.assertEquals(DateUtil.getFirstOf("2021.04.04 12:00:30", ChronoField.DAY_OF_YEAR, "dd.MM.yyyy"), "01.01.2021");
        Assert.assertEquals(DateUtil.getFirstOf(ChronoField.NANO_OF_DAY, "HH.mm.ss"), "00.00.00");
        Assert.assertEquals(DateUtil.getFirstOf("2021-04-04", ChronoField.NANO_OF_DAY, "yyyy-MM-dd-HH.mm.ss.SSSSSS"), "2021-04-04-00.00.00.000000");
    }

    @Test
    public void getLastOf() {
        Assert.assertEquals(DateUtil.getLastOf("04.04.2021", ChronoField.DAY_OF_WEEK, "yyyy-MM-dd"), "2021-04-04");
        Assert.assertEquals(DateUtil.getLastOf("2021.04.04 12:00:30", ChronoField.DAY_OF_MONTH, "yyyy.MM.dd"), "2021.04.30");
        Assert.assertEquals(DateUtil.getLastOf("2021.04.04 12:00:30", ChronoField.DAY_OF_YEAR, "dd.MM.yyyy"), "31.12.2021");
        Assert.assertEquals(DateUtil.getLastOf(ChronoField.NANO_OF_DAY, "HH:mm:ss"), "23:59:59");
        Assert.assertEquals(DateUtil.getLastOf("2021-04-04", ChronoField.NANO_OF_DAY, "yyyy-MM-dd-HH.mm.ss.SSSSSS"), "2021-04-04-23.59.59.999999");
    }

    @Test
    public void getMillis() {
        Assert.assertEquals(13, DateUtil.getLengthMilliseconds());
    }

    @Test
    public void getNumber() {
        Assert.assertEquals(DateUtil.getNumber("04.04.2021", ChronoField.DAY_OF_WEEK), 7);
        Assert.assertEquals(DateUtil.getNumber("2021.04.04 12:00:30", ChronoField.DAY_OF_MONTH), 4);
        Assert.assertEquals(DateUtil.getNumber("2021.04.04 12:00:30", ChronoField.DAY_OF_YEAR), 94);
        Assert.assertEquals(DateUtil.getNumber("2021.04.04 12:00:30.999", ChronoField.MILLI_OF_SECOND), 999);
    }

    @Test
    public void findDate() {
        String input = "Das Event findet am 15.02.2025 um 10:00:30 Uhr statt.";
        Assert.assertEquals(DateUtil.findDate(input).get(), "15.02.2025");

        input = "Treffen am 2023-08-15 14:45.";
        Assert.assertEquals(DateUtil.findDate(input).get(), "2023-08-15 14:45");

        input = "Backup wurde am 12/31/2024 durchgeführt.";
        Assert.assertEquals(DateUtil.findDate(input).get(), "12/31/2024");

        input =  "Die Uhrzeit ist 23:59.";
        Assert.assertEquals(DateUtil.findDate(input).get(), "23:59");
    }
}
