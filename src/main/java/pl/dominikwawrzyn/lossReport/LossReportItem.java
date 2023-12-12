package pl.dominikwawrzyn.lossReport;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "lossReportItem")
public class LossReportItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private Double cost;

    @ManyToOne
    @JoinColumn(name = "loss_report_id")
    private LossReport lossReport;
}