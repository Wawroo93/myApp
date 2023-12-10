package pl.dominikwawrzyn.schedule;

import org.springframework.stereotype.Controller;


@Controller
public class ScheduleController {

    private final ScheduleRepository scheduleRepository;

    public ScheduleController(ScheduleRepository scheduleRepository) {

        this.scheduleRepository = scheduleRepository;
    }

}

