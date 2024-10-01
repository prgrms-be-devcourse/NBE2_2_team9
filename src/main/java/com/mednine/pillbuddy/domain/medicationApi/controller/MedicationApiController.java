package com.mednine.pillbuddy.domain.medicationApi.controller;

import com.mednine.pillbuddy.domain.medicationApi.dto.JsonForm;
import com.mednine.pillbuddy.domain.medicationApi.dto.MedicationDTO;
import com.mednine.pillbuddy.domain.medicationApi.dto.MedicationForm;
import com.mednine.pillbuddy.domain.medicationApi.service.MedicationApiService;
import com.mednine.pillbuddy.global.exception.ErrorCode;
import com.mednine.pillbuddy.global.exception.PillBuddyCustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController @RequiredArgsConstructor
@RequestMapping("/api/search") @Slf4j
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

        //db에 데이터가 있다면 바로 반환
        Page<MedicationDTO> allByName = medicationApiService.findAllByName(medicationForm.getItemName(),pageNo-1,numOfRows);
        if (!allByName.isEmpty()) {
            if (pageNo > allByName.getTotalPages()) throw new PillBuddyCustomException(ErrorCode.OUT_OF_PAGE);
            return new JsonForm((int)allByName.getTotalElements(),pageNo,allByName.getTotalPages(),allByName.getContent());
        }

        //없다면 totalcount 개수만큼 api에 요청하여 db에 저장후 반환
        String[] onlyTotalCount = medicationApiService.jsonToString(medicationForm,0,0);
        int totalCount = Integer.parseInt(onlyTotalCount[1]);
        String[] realResult = medicationApiService.jsonToString(medicationForm, pageNo, totalCount);
        List<MedicationDTO> medicationDTOS = medicationApiService.StringToDTOs(realResult[0]);
        medicationApiService.saveMedication(medicationDTOS);
        allByName = medicationApiService.findAllByName(medicationForm.getItemName(),pageNo-1,numOfRows);
        if (pageNo > allByName.getTotalPages()) throw new PillBuddyCustomException(ErrorCode.OUT_OF_PAGE);  // 페이지 초과 시 예외 발생
        return new JsonForm((int)allByName.getTotalElements(), pageNo,allByName.getTotalPages(), allByName.getContent());
    }
}
