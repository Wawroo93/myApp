package pl.dominikwawrzyn.schedule;

import java.util.ArrayList;
import java.util.List;

public class ScheduleForm {

    private List<Schedule> schedules;

    public List<Schedule> getSchedules() {
        if (schedules == null) {
            schedules = new ArrayList<>();
        }
        return schedules;
    }

    public void setSchedules(List<Schedule> schedules) {
        this.schedules = schedules;
    }
}
