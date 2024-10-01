package com.mednine.pillbuddy.domain.medicationApi.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mednine.pillbuddy.domain.medicationApi.dto.MedicationDTO;
import com.mednine.pillbuddy.domain.medicationApi.dto.MedicationForm;
import com.mednine.pillbuddy.domain.medicationApi.service.MedicationApiService;
import com.mednine.pillbuddy.global.exception.ErrorCode;
import com.mednine.pillbuddy.global.exception.PillBuddyCustomException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = MedicationApiController.class)
@Import(ObjectMapper.class)
class MedicationApiControllerTest {
    @MockBean
    MedicationApiService medicationApiService;
    @MockBean
    JpaMetamodelMappingContext jpaMetamodelMappingContext;
    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    void setUp(){
        MedicationDTO medicationDTO = new MedicationDTO();
        medicationDTO.setItemName("아스피린");
        List<MedicationDTO> medicationList = List.of(medicationDTO);
        String jsonResponse = "[{\"itemName\":\"아스피린\"}]";
        String[] jsonResponses = new String[2];
        jsonResponses[0] = jsonResponse;
        jsonResponses[1] = "5";
        medicationApiService.jsonToString(Mockito.any(MedicationForm.class), Mockito.anyInt(), Mockito.anyInt());
        Mockito.when(medicationApiService.jsonToString(Mockito.any(MedicationForm.class), Mockito.anyInt(),
                        Mockito.anyInt()))
                .thenReturn(jsonResponses);
        Mockito.when(medicationApiService.StringToDTOs(Mockito.anyString()))
                .thenReturn(medicationList);
    }

    @Test
    void test_Success() throws Exception {
        MedicationDTO medicationDTO = new MedicationDTO();
        medicationDTO.setItemName("아스피린");
        List<MedicationDTO> medicationList = List.of(medicationDTO);
        Page<MedicationDTO> medicationPage = new PageImpl<>(medicationList, PageRequest.of(0, 10), 5);
        Mockito.when(medicationApiService.findAllByName(Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(medicationPage);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/search")
                        .param("itemName","아스피린")
                        .param("pageNo", "1")
                        .param("numOfRows", "10"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].itemName").value("아스피린"));
    }

    @Test
    void test_ValidationError() throws Exception {
        mockMvc.perform(get("/api/search")
                        .param("pageNo", "1")
                        .param("numOfRows", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    @Test
    void test_OutOfPageError() throws Exception {
        MedicationDTO medicationDTO = new MedicationDTO();
        medicationDTO.setItemName("아스피린");
        List<MedicationDTO> medicationList = List.of(medicationDTO);
        Page<MedicationDTO> medicationPage = new PageImpl<>(medicationList, PageRequest.of(0, 10), 5);
        Mockito.when(medicationApiService.findAllByName(Mockito.anyString(), Mockito.anyInt(), Mockito.anyInt()))
                .thenReturn(medicationPage);

        mockMvc.perform(get("/api/search")
                        .param("itemName","아스피린")
                        .param("pageNo", "2")
                        .param("numOfRows", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    @Test
    void test_TimeOut() throws Exception {
        Mockito.when(medicationApiService.jsonToString(Mockito.any(), Mockito.anyInt(), Mockito.anyInt()))
                .thenThrow(new PillBuddyCustomException(ErrorCode.ERROR_CONNECTION));

        // MockMvc로 GET 요청 수행 및 상태 코드 검증
        mockMvc.perform(get("/api/search")
                        .param("itemName","아스피린")
                        .param("pageNo", "1")
                        .param("numOfRows", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isGatewayTimeout());
    }


}

