package pl.dominikwawrzyn.lossReport;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "lossReport")
public class LossReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String month;

    @Min(1900)
    private Integer year;

    private Double sumCost;


    public Double getSumCost() {
        return this.getLossReportItems().stream()
                .mapToDouble(LossReportItem::getCost)
                .sum();
    }

    @OneToMany(mappedBy = "lossReport", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LossReportItem> lossReportItems;
}