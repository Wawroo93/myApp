package pl.dominikwawrzyn.lossReport;

import javax.persistence.*;
import lombok.*;


import java.time.LocalDate;

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

    private Double weight;

    private Integer quantity;

    private LocalDate eventDate;

    @ManyToOne
    @JoinColumn(name = "loss_report_id")
    private LossReport lossReport;

}