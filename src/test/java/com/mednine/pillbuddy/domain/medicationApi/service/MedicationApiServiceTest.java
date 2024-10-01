package com.mednine.pillbuddy.domain.medicationApi.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.mednine.pillbuddy.domain.medicationApi.dto.MedicationDTO;
import com.mednine.pillbuddy.domain.medicationApi.dto.MedicationForm;
import com.mednine.pillbuddy.domain.medicationApi.entity.Medication;
import com.mednine.pillbuddy.domain.medicationApi.repository.MedicationApiRepository;
import com.mednine.pillbuddy.global.exception.PillBuddyCustomException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.orm.jpa.JpaSystemException;

@SpringBootTest
class MedicationApiServiceTest {

    @Autowired
    private MedicationApiService medicationApiService;
    @Autowired
    private MedicationApiRepository medicationApiRepository;
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
        medicationApiRepository.saveAll(medicationList);
    }


    @Test
    void testJsonToString_Success(){
        MedicationForm medicationForm = new MedicationForm();
        medicationForm.setItemName("아스피린");
        String[] strings = medicationApiService.jsonToString(medicationForm, 1, 1);
        assertThat(strings[0]).contains("NORMAL SERVICE");
        assertThat(strings[1]).isEqualTo("52");
    }


    @Test
    void testJsonToString_Fail(){
        MedicationForm medicationForm = new MedicationForm();
        medicationForm.setItemName("afadfsfa");
        String[] strings = medicationApiService.jsonToString(medicationForm, 1, 1);
        assertThatThrownBy(() -> medicationApiService.StringToDTOs(strings[0])).isInstanceOf(PillBuddyCustomException.class);
    }


    @Test
    void stringToDTOs_success(){
        MedicationForm form = new MedicationForm();
        form.setItemName("아스피린");
        int numOfRows = 10;
        String[] strings = medicationApiService.jsonToString(form, 1, numOfRows);
        List<MedicationDTO> medicationDTOS = medicationApiService.StringToDTOs(strings[0]);
        assertThat(medicationDTOS.size()).isLessThanOrEqualTo(numOfRows);
        for (MedicationDTO medicationDTO : medicationDTOS) {
            assertThat(medicationDTO.getItemName()).contains("아스피린");
        }
    }
    @Test
    void stringToDTOs_Fail(){
        MedicationForm medicationForm = new MedicationForm();
        medicationForm.setItemName("afadfsfa");
        String[] strings = medicationApiService.jsonToString(medicationForm, 1, 1);
        assertThatThrownBy(() -> medicationApiService.StringToDTOs(strings[0])).isInstanceOf(PillBuddyCustomException.class);

    }

    @Test
    void save_Success() {

        PageRequest pageRequest = PageRequest.of(0,2, Sort.by(Sort.Direction.ASC, "itemSeq"));
        Page<Medication> allByName = medicationApiRepository.findAllByItemNameLike("아스피린",pageRequest);
        assertThat(allByName.getTotalElements()).isEqualTo(2);
        assertThat(allByName.getTotalPages()).isEqualTo(1);

    }
    @Test
    void save_fail_noName(){
        List<Medication> medicationList = new ArrayList<>();
        MedicationDTO medicationDTO1 = new MedicationDTO();
        MedicationDTO medicationDTO2 = new MedicationDTO();
        medicationList.add(Medication.createMedication(medicationDTO1));
        medicationList.add(Medication.createMedication(medicationDTO2));
        assertThatThrownBy(() -> medicationApiRepository.saveAll(medicationList)).isInstanceOf(
                JpaSystemException.class);
    }
    @Test
    void save_fail_duplicationId(){
        List<Medication> medicationList = new ArrayList<>();
        MedicationDTO medicationDTO1 = new MedicationDTO();
        MedicationDTO medicationDTO2 = new MedicationDTO();
        medicationList.add(Medication.createMedication(medicationDTO1));
        medicationList.add(Medication.createMedication(medicationDTO2));
        medicationDTO1.setItemSeq("19990999");
        medicationDTO2.setItemSeq("19990999");
        assertThatThrownBy(() -> medicationApiRepository.saveAll(medicationList)).isInstanceOf(
                JpaSystemException.class);
    }

    @Test
    void findByName_fail() {
        PageRequest pageRequest = PageRequest.of(0,2, Sort.by(Sort.Direction.ASC, "itemSeq"));
        Page<Medication> allByName = medicationApiRepository.findAllByItemNameLike("sadasd",pageRequest);
        assertThat(allByName).isEmpty();
        assertThat(allByName.getTotalElements()).isEqualTo(0);
        assertThat(allByName.getTotalPages()).isEqualTo(0);
    }

}
