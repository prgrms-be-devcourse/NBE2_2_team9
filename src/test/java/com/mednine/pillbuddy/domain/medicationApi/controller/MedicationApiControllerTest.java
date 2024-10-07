package com.mednine.pillbuddy.domain.medicationApi.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mednine.pillbuddy.domain.medicationApi.dto.MedicationDTO;
import com.mednine.pillbuddy.domain.medicationApi.dto.MedicationForm;
import com.mednine.pillbuddy.domain.medicationApi.service.MedicationApiService;
import com.mednine.pillbuddy.global.exception.ErrorCode;
import com.mednine.pillbuddy.global.exception.PillBuddyCustomException;
import com.mednine.pillbuddy.global.jwt.JwtTokenProvider;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = MedicationApiController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(ObjectMapper.class)
class MedicationApiControllerTest {
    @MockBean
    MedicationApiService medicationApiService;
    @MockBean
    JpaMetamodelMappingContext jpaMetamodelMappingContext;
    @MockBean
    JwtTokenProvider jwtTokenProvider;
    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    void setUp(){
        MedicationDTO medicationDTO = new MedicationDTO();
        medicationDTO.setItemName("아스피린");
        MedicationForm medicationForm = new MedicationForm();
        medicationForm.setItemName("에러발생");
        List<MedicationDTO> medicationList = List.of(medicationDTO);
        Mockito.when(medicationApiService.findAllByName("아스피린",0,10)).thenReturn(new PageImpl<>(medicationList));
        Mockito.when(medicationApiService.findAllByName("아스피린",1,10)).thenThrow(new PillBuddyCustomException(ErrorCode.OUT_OF_PAGE));
        Mockito.when(medicationApiService.findAllByName("에러발생", 0, 10)).thenReturn(new PageImpl<>(List.of()));
        Mockito.when(medicationApiService.createDto(Mockito.eq(medicationForm))).thenThrow(new PillBuddyCustomException(ErrorCode.ERROR_CONNECTION));
    }

    @Test
    @DisplayName("데이터를 반환해줄 수 있어야 한다.")
    void test_Success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/search")
                        .param("itemName","아스피린")
                        .param("pageNo", "1")
                        .param("numOfRows", "10"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].itemName").value("아스피린"));
    }

    @Test
    @DisplayName("필수 값을 넣지 않았을때 bindingResult에 체크돼야 한다.")
    void test_ValidationError() throws Exception {
        mockMvc.perform(get("/api/search")
                        .param("pageNo", "1")
                        .param("numOfRows", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("가능한 페이징 수 이상의 값을 입력했을 때 예외가 발생해야한다.")
    void test_OutOfPageError() throws Exception {
        mockMvc.perform(get("/api/search")
                        .param("itemName","아스피린")
                        .param("pageNo", "2")
                        .param("numOfRows", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("외부 API 서버에 문제가 생겼을때 예외가 발생해야한다.")
    void test_TimeOut() throws Exception {
        // MockMvc로 GET 요청 수행 및 상태 코드 검증
        mockMvc.perform(get("/api/search")
                        .param("itemName","에러발생")
                        .param("pageNo", "1")
                        .param("numOfRows", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isGatewayTimeout());
    }
}

