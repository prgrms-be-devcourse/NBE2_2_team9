package com.medinine.pillbuddy.domain.medicationApi.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.medinine.pillbuddy.domain.medicationApi.dto.MedicationDTO;
import com.medinine.pillbuddy.domain.medicationApi.dto.MedicationForm;
import com.medinine.pillbuddy.domain.medicationApi.entity.Medication;
import com.medinine.pillbuddy.global.exception.PillBuddyCustomException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
@Transactional
class MedicationApiServiceTest {

    @Autowired
    private MedicationApiService medicationApiService;
    @MockBean
    RestTemplate restTemplate;
    @BeforeEach
    void setUp() {
        List<Medication> medicationList = new ArrayList<>();
        MedicationDTO medicationDTO1 = new MedicationDTO();
        medicationDTO1.setItemSeq("199000505");
        medicationDTO1.setItemName("아스피린1");
        MedicationDTO medicationDTO2 = new MedicationDTO();
        medicationDTO2.setItemSeq("199000506");
        medicationDTO2.setItemName("아스피린2");
        medicationList.add(Medication.createMedication(medicationDTO1));
        medicationList.add(Medication.createMedication(medicationDTO2));
        medicationApiService.saveMedication(medicationList.stream().map(Medication::changeDTO).collect(Collectors.toList()));
        String jsonToString = "{\"header\":{\"resultCode\":\"00\",\"resultMsg\":\"NORMAL SERVICE.\"},\"body\":{\"pageNo\":1,\"totalCount\":1,\"numOfRows\":10,\"items\":[{\"entpName\":\"한국존슨앤드존슨판매(유)\",\"itemName\":\"어린이타이레놀산160밀리그램(아세트아미노펜)\",\"itemSeq\":\"202005623\",\"depositMethodQesitm\":\"실온에서 보관하십시오.\\n\\n어린이의 손이 닿지 않는 곳에 보관하십시오.\\n\",\"itemImage\":null,\"bizrno\":\"1068649891\"}]}}";
        Mockito.when(restTemplate.getForObject(Mockito.argThat(uri -> uri.toString().contains("%ED%83%80%EC%9D%B4%EB%A0%88%EB%86%80")),Mockito.eq(String.class))).thenReturn(jsonToString);
    }


    @Test
    @DisplayName("JSON을 파싱해 DB에 저장되고, 조회시 저장된 DTO가 반환돼야 한다.")
    void testCreateDto_Success(){
        MedicationForm medicationForm = new MedicationForm();
        medicationForm.setItemName("타이레놀");
        List<MedicationDTO> dto = medicationApiService.createDto(medicationForm);
        assertThat(dto.size()).isEqualTo(1);
        assertThat(dto.get(0).getItemName()).isEqualTo("어린이타이레놀산160밀리그램(아세트아미노펜)");
        medicationApiService.saveMedication(dto);
        Page<MedicationDTO> findDto = medicationApiService.findAllByName("타이레놀", 0, 10);
        assertThat(findDto.getContent().size()).isEqualTo(1);
        assertThat(findDto.getContent().get(0).getItemName()).isEqualTo("어린이타이레놀산160밀리그램(아세트아미노펜)");
        assertThat(findDto.getTotalPages()).isEqualTo(1);
    }

    @Test
    @DisplayName("없는 약 이름을 입력했을 때 예외가 발생해야 한다.")
    void testCreateDto_Fail() {
        MedicationForm medicationForm = new MedicationForm();
        medicationForm.setItemName("asdasd");
        assertThatThrownBy(() -> medicationApiService.createDto(medicationForm)).isInstanceOf(
                PillBuddyCustomException.class);
    }


    @Test
    @DisplayName("저장된 데이터가 조회돼야 한다.")
    void save_Success() {
        Page<MedicationDTO> allByName = medicationApiService.findAllByName("아스피린",0,10);
        assertThat(allByName.getTotalElements()).isEqualTo(2);
        assertThat(allByName.getTotalPages()).isEqualTo(1);
    }

    @Test
    @DisplayName("필수값을 입력하지 않은 데이터는 저장되지 않아야 한다.")
    void save_fail_noName(){
        List<MedicationDTO> medicationDTOList = new ArrayList<>();
        MedicationDTO medicationDTO1 = new MedicationDTO();
        MedicationDTO medicationDTO2 = new MedicationDTO();
        medicationDTOList.add(medicationDTO1);
        medicationDTOList.add(medicationDTO2);
        assertThatThrownBy(() -> medicationApiService.saveMedication(medicationDTOList)).isInstanceOf(
                JpaSystemException.class);
    }
}