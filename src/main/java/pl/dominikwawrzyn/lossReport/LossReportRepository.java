package pl.dominikwawrzyn.lossReport;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LossReportRepository extends JpaRepository<LossReport, Long> {
}
