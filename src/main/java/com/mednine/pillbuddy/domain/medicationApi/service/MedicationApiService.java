package com.mednine.pillbuddy.domain.medicationApi.service;

import static com.mednine.pillbuddy.global.exception.ErrorCode.ERROR_CONNECTION;
import static com.mednine.pillbuddy.global.exception.ErrorCode.MEDICATION_NOT_FOUND;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mednine.pillbuddy.domain.medicationApi.dto.MedicationDTO;
import com.mednine.pillbuddy.domain.medicationApi.dto.MedicationForm;
import com.mednine.pillbuddy.domain.medicationApi.entity.Medication;
import com.mednine.pillbuddy.domain.medicationApi.repository.MedicationApiRepository;
import com.mednine.pillbuddy.global.exception.PillBuddyCustomException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class MedicationApiService {

    private final MedicationApiRepository medicationApiRepository;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;


    @Value("${openApi.dataType}")
    private String dataType;
    @Value("${openApi.serviceKey}")
    private String serviceKey;
    @Value("${openApi.callbackUrl}")
    private String callbackUrl;

    public List<MedicationDTO> createDto(MedicationForm medicationForm) {

        try {
            String JsonToString = restTemplate.getForObject(createUrl(medicationForm, 0),
                    String.class);
            Integer totalCount = objectMapper.readValue(
                    objectMapper.readTree(JsonToString).path("body").path("totalCount").toString(), Integer.class);
            JsonToString = restTemplate.getForObject(createUrl(medicationForm, totalCount),
                    String.class);

            JsonNode rootNode = objectMapper.readTree(JsonToString);
            JsonNode itemsNode = rootNode.path("body").path("items");
            return objectMapper.readValue(itemsNode.toString(), new TypeReference<>() {
            });

        } catch (JsonProcessingException | IllegalArgumentException e) {
            throw new PillBuddyCustomException(MEDICATION_NOT_FOUND);
        } catch (ResourceAccessException e) {
            throw new PillBuddyCustomException(ERROR_CONNECTION);
        }
    }

    @Transactional(readOnly = true)
    public Page<MedicationDTO> findAllByName(String itemName, int pageNo, int numOfRows) {
        PageRequest pageRequest = PageRequest.of(pageNo,numOfRows, Sort.by(Sort.Direction.ASC, "itemSeq"));
        Page<Medication> allByItemNameLike = medicationApiRepository.findAllByItemNameLike(itemName,pageRequest);
        return allByItemNameLike.map(Medication::changeDTO);
    }

    @Transactional
    public void saveMedication(List<MedicationDTO> medicationDTOList){
        List<Medication> medicationList = medicationDTOList.stream().map(Medication::createMedication).collect(
                Collectors.toList());
        medicationApiRepository.saveAll(medicationList);
    }

    private URI createUrl(MedicationForm medicationForm, int numOfRows) {
        String encodedItemName = stringEncoding(medicationForm.getItemName());
        String url = callbackUrl + "?serviceKey=" + serviceKey + "&type=" + dataType + "&itemName=" + encodedItemName+"&pageNo=1"+"&numOfRows="+numOfRows;
        if (medicationForm.getEntpName() != null) {
            String encodedEntpName = stringEncoding(medicationForm.getEntpName());
            url += "&entpName=" + encodedEntpName;
        }
        if (medicationForm.getItemSeq() != null) {
            String encodedItemSeq = stringEncoding(medicationForm.getItemSeq());
            url += "&itemSeq=" + encodedItemSeq;
        }
        log.info("generateUrl={}",url);
        return URI.create(url);
    }

    private String stringEncoding(String parameter){
        return URLEncoder.encode(parameter, StandardCharsets.UTF_8);
    }

    @Configuration
    static class RestTemplateConfig{
        @Bean
        RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {

            return restTemplateBuilder.setConnectTimeout(Duration.ofSeconds(3)).setReadTimeout(Duration.ofSeconds(3)).build();
        }
    }
}

