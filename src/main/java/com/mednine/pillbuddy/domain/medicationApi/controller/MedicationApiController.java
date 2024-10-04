package com.mednine.pillbuddy.domain.medicationApi.controller;

import com.mednine.pillbuddy.domain.medicationApi.dto.JsonForm;
import com.mednine.pillbuddy.domain.medicationApi.dto.MedicationDTO;
import com.mednine.pillbuddy.domain.medicationApi.dto.MedicationForm;
import com.mednine.pillbuddy.domain.medicationApi.service.MedicationApiService;
import com.mednine.pillbuddy.global.exception.ErrorCode;
import com.mednine.pillbuddy.global.exception.PillBuddyCustomException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search")
@Slf4j
public class MedicationApiController {
    private final MedicationApiService medicationApiService;

    @GetMapping
    public JsonForm findMedicationByApi(@ModelAttribute @Validated MedicationForm medicationForm,
                                        BindingResult bindingResult,
                                        @RequestParam(defaultValue = "1") int pageNo,
                                        @RequestParam(defaultValue = "10") int numOfRows) {

        if (bindingResult.hasErrors()) {
            throw new PillBuddyCustomException(ErrorCode.REQUIRED_VALUE);
        }
        //DB에 레코드가 있다면 DB에서 레코드 반환
        Page<MedicationDTO> allByName = medicationApiService.findAllByName(medicationForm.getItemName(),pageNo-1,numOfRows);
        if (!allByName.isEmpty()) {
            return new JsonForm((int)allByName.getTotalElements(),pageNo,allByName.getTotalPages(),allByName.getContent());
        }
        //DB에 레코드가 없다면 외부API 통신을 통해 DB에 레코드를 저장한 후 레코드 반환
        List<MedicationDTO> medicationDTOS = medicationApiService.createDto(medicationForm);
        medicationApiService.saveMedication(medicationDTOS);
        allByName = medicationApiService.findAllByName(medicationForm.getItemName(), pageNo - 1, numOfRows);
        return new JsonForm((int)allByName.getTotalElements(), pageNo,allByName.getTotalPages(), allByName.getContent());
    }
}