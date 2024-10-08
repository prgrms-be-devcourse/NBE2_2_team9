package com.medinine.pillbuddy.domain.medicationApi.controller;

import com.medinine.pillbuddy.domain.medicationApi.dto.JsonForm;
import com.medinine.pillbuddy.domain.medicationApi.dto.MedicationDTO;
import com.medinine.pillbuddy.domain.medicationApi.dto.MedicationForm;
import com.medinine.pillbuddy.domain.medicationApi.service.MedicationApiService;
import com.medinine.pillbuddy.global.exception.ErrorCode;
import com.medinine.pillbuddy.global.exception.PillBuddyCustomException;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
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
@Tag(name = "약 검색 기능",description = "공공API e약은요 를 이용해 일반의약품 정보를 검색한다")
@Slf4j
public class MedicationApiController {
    private final MedicationApiService medicationApiService;
    @ApiResponse(responseCode = "200",description = "조회 성공",content = @Content(mediaType = "application/json",examples = @ExampleObject(value = "{\"totalCount\": 1,\"nowPageNum\": 1,\"maxPageNum\": 1,\"data\": [{\"entpName\": \"한국존슨앤드존슨판매(유)\",\"itemName\": \"어린이타이레놀산160밀리그램(아세트아미노펜)\",\"itemSeq\": \"202005623\"}]}"),schema = @Schema(implementation = MedicationDTO.class)))
    @ApiResponse(responseCode = "404",description = "API에 약이 없을 경우",content = @Content(mediaType = "application/json",examples = @ExampleObject(value = "{\"httpStatus\": \"NOT_FOUND\",\"message\": \"약 정보를 찾을 수 없습니다.\"}")))
    @ApiResponse(responseCode = "400",description = "페이지 설정이 잘못됐을 경우",content = @Content(mediaType = "application/json",examples = @ExampleObject(value = "{\"httpStatus\": \"NOT_FOUND\",\"message\": \"페이지 설정이 잘못됐습니다.\"}")))
    @ApiResponse(responseCode = "504",description = "외부 API와의 통신 오류의 경우",content = @Content(mediaType = "application/json",examples = @ExampleObject(value = "{\"httpStatus\": \"NOT_FOUND\",\"message\": \"네트워크 통신 중 오류가 발생했습니다.\"}")))

    @GetMapping
    public JsonForm findMedicationByApi(
            @ParameterObject
            @ModelAttribute @Validated MedicationForm medicationForm,
            BindingResult bindingResult,
            @Parameter(description = "페이지 번호")
            @RequestParam(defaultValue = "1") int pageNo,
            @Parameter(description = "한 페이지의 데이터 개수")
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