package uk.ac.ncl.csc8019backend.business.raildata.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Service
public class StationMappingService {

    private static final Logger logger = LoggerFactory.getLogger(StationMappingService.class);

    private final Map<String, String> stanoxToNameMap = new HashMap<>();
    private final Map<String, String> tiplocToStanoxMap = new HashMap<>();
    private final Map<String, String> crsToNameMap = new HashMap<>();

    @PostConstruct
    public void init() {
        logger.info("========== Loading station mappings ==========");


        loadFromFile();


        stanoxToNameMap.putAll(StationNames.getStationNameMap());

        logger.info("Total STANOX mappings after manual addition: {}", stanoxToNameMap.size());


        logger.info("Sample: 30120 -> {}", getStationNameByStanox("30120"));
        logger.info("Sample: 62011 -> {}", getStationNameByStanox("62011"));
        logger.info("Sample: 52733 -> {}", getStationNameByStanox("52733"));
        logger.info("========== Station mappings loaded ==========");
    }

    private void loadFromFile() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream inputStream = new ClassPathResource("data/CORPUSExtract.json").getInputStream();

            JsonNode root = mapper.readTree(inputStream);
            logger.info("Root node type: {}", root.getNodeType());

            int stanoxCount = 0;
            int tiplocCount = 0;


            if (root.has("TIPLOCDATA") && root.get("TIPLOCDATA").isArray()) {
                JsonNode dataArray = root.get("TIPLOCDATA");
                logger.info("Found TIPLOCDATA array with {} entries", dataArray.size());

                for (JsonNode node : dataArray) {
                    String stanox = node.path("STANOX").asText(null);
                    String tiploc = node.path("TIPLOC").asText(null);
                    String stationName = node.path("NLCDESC").asText(null);

                    if (stanox != null && !stanox.trim().isEmpty() &&
                            stationName != null && !stationName.trim().isEmpty()) {
                        stanoxToNameMap.put(stanox.trim(), stationName.trim());
                        stanoxCount++;
                    }

                    if (tiploc != null && !tiploc.trim().isEmpty() &&
                            stanox != null && !stanox.trim().isEmpty()) {
                        tiplocToStanoxMap.put(tiploc.trim(), stanox.trim());
                        tiplocCount++;
                    }
                }
            }

            logger.info("Loaded {} STANOX mappings from file", stanoxCount);
            logger.info("Loaded {} TIPLOC to STANOX mappings", tiplocCount);

        } catch (Exception e) {
            logger.error("Failed to load station mappings from file", e);
        }
    }

    public String getStationNameByStanox(String stanox) {
        if (stanox == null || stanox.isEmpty()) {
            return "Unknown Station";
        }
        String trimmed = stanox.trim();


        String name = stanoxToNameMap.get(trimmed);
        if (name != null) {
            return name;
        }


        String withoutLeadingZeros = trimmed.replaceFirst("^0+", "");
        if (!withoutLeadingZeros.equals(trimmed)) {
            name = stanoxToNameMap.get(withoutLeadingZeros);
            if (name != null) {
                return name;
            }
        }


        return "Station " + trimmed;
    }

    public String getStanoxByTiploc(String tiploc) {
        if (tiploc == null || tiploc.isEmpty()) {
            return null;
        }
        return tiplocToStanoxMap.get(tiploc.trim());
    }

    public String getStationNameByTiploc(String tiploc) {
        String stanox = getStanoxByTiploc(tiploc);
        if (stanox != null) {
            return getStationNameByStanox(stanox);
        }
        return null;
    }

    public String getStationNameByCrs(String crs) {
        if (crs == null || crs.isEmpty()) {
            return null;
        }
        return crsToNameMap.get(crs.trim().toUpperCase());
    }

    public String getStationName(String code) {
        if (code == null || code.isEmpty()) {
            return "Unknown";
        }

        String name = getStationNameByStanox(code);
        if (name != null) return name;

        name = getStationNameByTiploc(code);
        if (name != null) return name;

        name = getStationNameByCrs(code);
        if (name != null) return name;

        return "Station " + code;
    }
}