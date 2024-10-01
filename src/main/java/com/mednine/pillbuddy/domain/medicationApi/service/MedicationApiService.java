package com.mednine.pillbuddy.domain.medicationApi.service;

import static com.mednine.pillbuddy.global.exception.ErrorCode.ERROR_CONNECTION;
import static com.mednine.pillbuddy.global.exception.ErrorCode.MEDICATION_NOT_FOUND;
import static com.mednine.pillbuddy.global.exception.ErrorCode.NETWORK_ERROR;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mednine.pillbuddy.domain.medicationApi.dto.MedicationDTO;
import com.mednine.pillbuddy.domain.medicationApi.dto.MedicationForm;
import com.mednine.pillbuddy.domain.medicationApi.entity.Medication;
import com.mednine.pillbuddy.domain.medicationApi.repository.MedicationApiRepository;
import com.mednine.pillbuddy.global.exception.PillBuddyCustomException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MedicationApiService {
    private final MedicationApiRepository medicationApiRepository;
    private final ObjectMapper objectMapper;

    @Value("${openApi.dataType}")
    private String dataType;
    @Value("${openApi.serviceKey}")
    private String serviceKey;
    @Value("${openApi.callbackUrl}")
    private String callbackUrl;

    @Transactional(readOnly = true)
    public Page<MedicationDTO> findAllByName(String itemName,int pageNo,int numOfRows) {
        PageRequest pageRequest = PageRequest.of(pageNo,numOfRows, Sort.by(Sort.Direction.ASC, "itemSeq"));
        Page<Medication> allByItemNameLike = medicationApiRepository.findAllByItemNameLike(itemName,pageRequest);
        if (allByItemNameLike.isEmpty()) {
            return null;
        }
        return allByItemNameLike.map(Medication::changeDTO);
    }

    @Transactional
    public void saveMedication(List<MedicationDTO> medicationDTOList){
        List<Medication> medicationList = medicationDTOList.stream().map(Medication::createMedication).collect(Collectors.toList());
        medicationApiRepository.saveAll(medicationList);
    }

    public String[] jsonToString(MedicationForm medicationForm, int pageNo, int numOfRows) {

        HttpURLConnection urlConnection = null;
        InputStream stream;
        String result;
        String[] results = new String[2];
        try {
            URL url = new URL(createUrl(medicationForm,pageNo,numOfRows));
            urlConnection = (HttpURLConnection) url.openConnection();
            stream = getNetworkConnection(urlConnection);
            result = readStreamToString(stream);
            Integer totalCount = objectMapper.readValue(objectMapper.readTree(result).path("body").path("totalCount").toString(), Integer.class);
            results[1] = String.valueOf(totalCount);
            if (stream != null) {
                stream.close();
            }

        } catch (IOException e) {
            throw new PillBuddyCustomException(NETWORK_ERROR);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        results[0] = result;
        return results;
    }
    public List<MedicationDTO> StringToDTOs(String jsonData) {
        try {
            JsonNode rootNode = objectMapper.readTree(jsonData);
            JsonNode itemsNode = rootNode.path("body").path("items");
            return objectMapper.readValue(itemsNode.toString(), new TypeReference<>(){});

        } catch (JsonProcessingException e) {
            throw new PillBuddyCustomException(MEDICATION_NOT_FOUND);
        }
    }


    private String createUrl(MedicationForm medicationForm, int pageNo, int numOfRows) {
        String encodedItemName = stringEncoding(medicationForm.getItemName());
        String url = callbackUrl + "?serviceKey=" + serviceKey + "&type=" + dataType + "&itemName=" + encodedItemName+"&pageNo="+pageNo+"&numOfRows="+numOfRows;
        if (medicationForm.getEntpName() != null) {
            String encodedEntpName = stringEncoding(medicationForm.getEntpName());
            url += "&entpName=" + encodedEntpName;
        }
        if (medicationForm.getItemSeq() != null) {
            String encodedItemSeq = stringEncoding(medicationForm.getItemSeq());
            url += "&itemSeq=" + encodedItemSeq;
        }
        log.info("generateUrl={}",url);
        return url;
    }

    private String stringEncoding(String parameter){
        return URLEncoder.encode(parameter,StandardCharsets.UTF_8);
    }

    private InputStream getNetworkConnection(HttpURLConnection urlConnection)  {
        try {
            urlConnection.setConnectTimeout(3000);
            urlConnection.setReadTimeout(3000);
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);

            if(urlConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new PillBuddyCustomException(ERROR_CONNECTION);
            }
            return urlConnection.getInputStream();
        } catch (IOException e) {
            throw new PillBuddyCustomException(ERROR_CONNECTION);
        }
    }

    private String readStreamToString(InputStream stream){
        StringBuilder result = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
            String readLine;
            while ((readLine = br.readLine()) != null) {
                result.append(readLine).append("\n\r");
            }
        } catch (IOException e) {
            throw new PillBuddyCustomException(NETWORK_ERROR);
        }
        return result.toString();
    }

}
