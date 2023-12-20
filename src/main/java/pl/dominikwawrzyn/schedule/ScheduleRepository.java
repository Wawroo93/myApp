package pl.dominikwawrzyn.schedule;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;


public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findAllByEmployeeId(Long employeeId);

    List<Schedule> findAllByDay(LocalDate day);

}