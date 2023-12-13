package pl.dominikwawrzyn.lossReport;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LossReportItemRepository extends JpaRepository<LossReportItem, Long> {

    List<LossReportItem> findAllByLossReport(LossReport lossReport);

}