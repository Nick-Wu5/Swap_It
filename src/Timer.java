public class Timer {
    private int timer; //timer that goes off before taking a picture
    private int currentMonth; //today's month, range from 1-12
    private int currentDay; //today's day, range from 1-31
    private int currentYear; //today's year and the year picture should be sent, max is 2024 inclusive
    private int currentTime; //time at the current moment
    private int calculatedMonth;//calculated month to send a picture to a certain user
    private int calculatedDay;//calculated day to send a picture to a cetain user
    private int calculatedYear;//calculated year to send a picture to a certain user
    private int calculatedTime;//calculated time to send a picture to a certain user

    public Timer(int timer, int currentMonth, int currentDay, int currentYear, int currentTime,
                 int calculatedMonth, int calculatedDay, int calculatedYear, int calculatedTime) {
        this.timer = timer;
        this.currentMonth = currentMonth;
        this.currentDay = currentDay;
        this.currentYear = currentYear;
        this.currentTime = currentTime;
        this.calculatedMonth = calculatedMonth;
        this.calculatedDay = calculatedDay;
        this.calculatedYear = calculatedYear;
        this.calculatedTime = calculatedTime;
    }

    // getters and setters!
    public int getTimer() {
        return timer;
    }

    public int getCurrentMonth() {
        return currentMonth;
    }

    public int getCurrentDay() {
        return currentDay;
    }

    public int getCurrentYear() {
        return currentYear;
    }

    public int getCurrentTime() {
        return currentTime;
    }

    public int getCalculatedMonth() {
        return calculatedMonth;
    }

    public int getCalculatedDay() {
        return calculatedDay;
    }

    public int getCalculatedYear() {
        return calculatedYear;
    }

    public int getCalculatedTime() {
        return calculatedTime;
    }

    public void setCurrentMonth(int currentMonth) {
        this.currentMonth = currentMonth;
    }

    public void setCurrentDay(int currentDay) {
        this.currentDay = currentDay;
    }

    public void setCurrentYear(int currentYear) {
        this.currentYear = currentYear;
    }

    public void setCurrentTime(int currentTime) {
        this.currentTime = currentTime;
    }

    public void setCalculatedMonth(int calculatedMonth) {
        this.calculatedMonth = calculatedMonth;
    }

    public void setCalculatedDay(int calculatedDay) {
        this.calculatedDay = calculatedDay;
    }

    public void setCalculatedYear(int calculatedYear) {
        this.calculatedYear = calculatedYear;
    }

    public void setCalculatedTime(int calculatedTime) {
        this.calculatedTime = calculatedTime;
    }
}
//note to self: keep in mind to make specific exception methods & prints to the user