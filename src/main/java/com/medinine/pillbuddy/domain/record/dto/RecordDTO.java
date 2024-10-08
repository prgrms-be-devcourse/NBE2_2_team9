package com.medinine.pillbuddy.domain.record.dto;

import com.medinine.pillbuddy.domain.record.entity.Record;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecordDTO {
    private Long recordId;
    private LocalDateTime date;
    private String medicationName;
    private String taken;

    public RecordDTO(Record record) {
        this.recordId = record.getId();
        this.date = record.getDate();
        this.medicationName = record.getUserMedication().getName();
        this.taken = record.getTaken().toString();
    }
}
