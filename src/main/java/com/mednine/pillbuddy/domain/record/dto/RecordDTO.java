package com.mednine.pillbuddy.domain.record.dto;

import com.mednine.pillbuddy.domain.record.entity.Record;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
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
