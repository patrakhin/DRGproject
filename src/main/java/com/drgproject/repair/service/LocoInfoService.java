package com.drgproject.repair.service;

import com.drgproject.repair.dto.LocoInfoDTO;
import com.drgproject.repair.entity.LocoInfo;
import com.drgproject.repair.entity.LocoList;
import com.drgproject.repair.repository.LocoInfoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional
public class LocoInfoService {

    private final LocoInfoRepository locoInfoRepository;

    public LocoInfoService(LocoInfoRepository locoInfoRepository) {
        this.locoInfoRepository = locoInfoRepository;
    }

    // Создание нового LocoInfo
    public LocoInfoDTO createLocoInfo(LocoInfoDTO locoInfoDTO) {
        LocoInfo locoInfo = convertToEntity(locoInfoDTO);
        LocoInfo locoInfoSave = locoInfoRepository.save(locoInfo);

        return convertToDTO(locoInfoSave);
    }

    // Получение всех LocoInfo
    public List<LocoInfo> getAllLocoInfos() {
        return locoInfoRepository.findAll();
    }

    // Получение LocoInfo по ID
    public LocoInfoDTO getLocoInfoById(Long id) {
        Optional<LocoInfo> middleResult = locoInfoRepository.findById(id);
        if (middleResult.isPresent()) {
            return convertToDTO(middleResult.get());
        }else {
            throw new IllegalArgumentException("Локомотив по ID не найден сервисный слой");
        }
    }

    // Получение LocoInfo по региону и серии локомотива с пагинацией
    public Page<LocoInfoDTO> getLocoInfoByRegionAndType(String region, String locoType, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return locoInfoRepository.findByRegionAndLocoType(region, locoType, pageable)
                .map(this::convertToDTO);
    }

    //Получение LocoInfo по региону, депо приписки, номеру локомотива
    public LocoInfoDTO getLocoInfoByRegionAndHomeDepotAndLocoNumber(String region, String homeDepot, String locoUnit){
        Optional<LocoInfo> middleResult = locoInfoRepository.findByRegionAndHomeDepotAndLocoUnit(region, homeDepot, locoUnit);
        if (middleResult.isPresent()) {
            return convertToDTO(middleResult.get());
        }else {
            throw new IllegalArgumentException("Локомотив " + locoUnit + " региона " + region + " депо приписки " + homeDepot + " еще не сформирован сервисный слой");
        }
    }

    //Получение локомотива по номеру и серии
    public LocoInfoDTO getLocoInfoByNumber(String locoUnit, String locoType){
        Optional<LocoInfo> locoForSection = locoInfoRepository.findByLocoUnitAndLocoType(locoUnit, locoType);
        if(locoForSection.isPresent()){
            return convertToDTO(locoForSection.get());
        }else {
            throw new IllegalArgumentException("Локомотив " + locoUnit  + " для создания свободных секций не найден сервисный слой");
        }
    }

    //Получение локомотива по номеру и серии и депо приписки
    public LocoInfoDTO getLocoInfoByHomeDepotAndLocoTypeAndLocoUnit(String homeDepot, String locoType, String locoUnit){
        Optional<LocoInfo> locoForSection1 = locoInfoRepository.findLocoInfoByHomeDepotAndLocoTypeAndLocoUnit(homeDepot, locoType, locoUnit);
        if(locoForSection1.isPresent()){
            return convertToDTO(locoForSection1.get());
        }else {
            throw new IllegalArgumentException("Локомотив " + locoUnit  + " для создания свободных секций не найден сервисный слой");
        }
    }

    //Получение локомотива по номеру и серии для истории ремонта
    public LocoInfoDTO getLocoByNumber(String locoUnit, String locoType){
        Optional<LocoInfo> locoNumber = locoInfoRepository.findByLocoUnitAndLocoType(locoUnit, locoType);
        return locoNumber.map(this::convertToDTO).orElse(null);
    }

    // Получение локомотива по номеру первой секции
    public String getLocoByFirstNumberSection(String firstSectionNumber){
        Optional <LocoInfo> numberLoco = locoInfoRepository.findLocoInfoByLocoSection1(firstSectionNumber);
        if (numberLoco.isEmpty()){
            return null;
        }
        return locoInfoRepository.findLocoInfoByLocoSection1(firstSectionNumber).get().getLocoUnit();
    }

    // Получение локомотива по любому номеру секции
    public Optional<LocoInfo> getLocoInfoByDepotAndSection(String homeDepot, String locoSection) {
        return locoInfoRepository.findByHomeDepotAndAnySection(homeDepot, locoSection);
    }

    //поиск номера локомотива по первым двум цифрам
    public List<String> getFindNumbersByPrefix(String prefix) {
        return locoInfoRepository.findByLocoUnitStartingWith(prefix)
                .stream()
                .map(LocoInfo::getLocoUnit)
                .toList();
    }

    //Получение списка секций из локомотива
    public List<String> getLocoSections(LocoInfoDTO locoInfoDTO) {
        List<String> sectionsNumber = new ArrayList<>();

        addSectionIfValid(sectionsNumber, locoInfoDTO.getLocoSection1());
        addSectionIfValid(sectionsNumber, locoInfoDTO.getLocoSection2());
        addSectionIfValid(sectionsNumber, locoInfoDTO.getLocoSection3());
        addSectionIfValid(sectionsNumber, locoInfoDTO.getLocoSection4());

        return sectionsNumber;
    }

    private void addSectionIfValid(List<String> sectionsNumber, String section) {
        if (section != null && !section.isEmpty() && !section.toLowerCase().contains("нет") && !section.contains("Выберите секцию")) {
            sectionsNumber.add(section);
        }
    }

    //Получение "чистого" списка с номерами секций
    public List<String> getClearLocoSections(List<String> locoSections) {
        List<String> clearSectionsNumber = new ArrayList<>();

        addClearSectionIfValid(clearSectionsNumber, locoSections.get(0));
        addClearSectionIfValid(clearSectionsNumber, locoSections.get(1));
        addClearSectionIfValid(clearSectionsNumber, locoSections.get(2));
        addClearSectionIfValid(clearSectionsNumber, locoSections.get(3));

        return clearSectionsNumber;
    }

    private void addClearSectionIfValid(List<String> sectionsNumber, String section) {
        if (section != null && !section.isEmpty() && !section.contains("нет") && !section.contains("Выберите секцию")) {
            sectionsNumber.add(section);
        }
    }
    // Обновление LocoInfo
    public LocoInfo updateLocoInfo(Long id, LocoInfo updatedLocoInfo) {
        return locoInfoRepository.findById(id)
                .map(existingLocoInfo -> {
                    existingLocoInfo.setRegion(updatedLocoInfo.getRegion());
                    existingLocoInfo.setHomeDepot(updatedLocoInfo.getHomeDepot());
                    existingLocoInfo.setLocoType(updatedLocoInfo.getLocoType());
                    existingLocoInfo.setLocoSection1(updatedLocoInfo.getLocoSection1());
                    existingLocoInfo.setLocoFit1(updatedLocoInfo.getLocoFit1());
                    existingLocoInfo.setLocoSection2(updatedLocoInfo.getLocoSection2());
                    existingLocoInfo.setLocoFit2(updatedLocoInfo.getLocoFit2());
                    existingLocoInfo.setLocoSection3(updatedLocoInfo.getLocoSection3());
                    existingLocoInfo.setLocoFit3(updatedLocoInfo.getLocoFit3());
                    existingLocoInfo.setLocoSection4(updatedLocoInfo.getLocoSection4());
                    existingLocoInfo.setLocoFit4(updatedLocoInfo.getLocoFit4());
                    existingLocoInfo.setLocoUnit(calculateLocoUnit(updatedLocoInfo.getLocoSection1(),updatedLocoInfo.getLocoSection2(),
                            updatedLocoInfo.getLocoSection3(), updatedLocoInfo.getLocoSection4())); // Пересчёт locoUnit
                    return locoInfoRepository.save(existingLocoInfo);
                })
                .orElseThrow(() -> new RuntimeException("LocoInfo not found with id " + id));
    }

    @Transactional
    // Удаление LocoInfo по ID Секции делаются свободными в контроллере
    public void deleteLocoInfo(Long id) {
        if (locoInfoRepository.existsById(id)) {
            locoInfoRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Локомотив с таким id: " + id + " не найден сервисный слой");
        }
    }

    @Transactional
    // Удаление LocoInfo по locoUnit Секции делаются свободными в контроллере
    public boolean deleteLocoInfoByLocoUnit(String locoUnit, String locoType) {
        Optional<LocoInfo> locoInfoOptional = locoInfoRepository.findByLocoUnit(locoUnit);
        if (locoInfoOptional.isPresent()) {
            locoInfoRepository.deleteByLocoUnitAndLocoType(locoUnit, locoType);
            return true;
        } else {
            throw new IllegalArgumentException("Локомотив " + locoType + " номер: " + locoUnit + " не найден сервисный слой");
        }
    }

    //Проверка на существование уже созданного локомотива
    public boolean ifLociUnitIsExists(String region, String homeDepot, String locoType, String locoSection1){
        Optional<LocoInfo> locoInfoIsExist = locoInfoRepository.findByRegionAndHomeDepotAndLocoTypeAndLocoSection1(region, homeDepot, locoType, locoSection1);
        return locoInfoIsExist.isPresent();
    }


    // Метод для вычисления locoUnit
    public String calculateLocoUnit(String locoSection1, String locoSection2, String locoSection3, String locoSection4) {
        // Создание изменяемого списка из параметров
        List<String> sections = new ArrayList<>(Arrays.asList(locoSection1, locoSection2, locoSection3, locoSection4));

        // Буфер для формирования результата
        StringBuilder result = new StringBuilder();

        // Регулярное выражение для выделения номера и буквы
        String regex = "(\\d+)([А-ЯA-Z]?)";

        // Разделение секций на части (номер и буква)
        List<String[]> parsedSections = new ArrayList<>();
        for (String section : sections) {
            Matcher matcher = Pattern.compile(regex).matcher(section);
            if (matcher.matches()) {
                parsedSections.add(new String[] { matcher.group(1), matcher.group(2) });
            } else {
                parsedSections.add(new String[] { "", "" }); // Добавление пустой части для "нет"
            }
        }

        // Логика для формирования locoUnit
        if (allSectionsEmpty(parsedSections)) {
            return "нет";
        }

        if (isSingleSection(parsedSections)) {
            result.append(parsedSections.get(0)[0]);
        } else if (isDoubleSectionSame(parsedSections)) {
            result.append(parsedSections.get(0)[0]);
        } else if (isDoubleSectionDifferent(parsedSections)) {
            result.append(parsedSections.get(0)[0]).append(parsedSections.get(0)[1])
                    .append("/").append(parsedSections.get(1)[0]).append(parsedSections.get(1)[1]);
        } else if (isTripleSectionSame(parsedSections)) {
            result.append(parsedSections.get(0)[0])
                    .append("/").append(parsedSections.get(2)[0]).append(parsedSections.get(2)[1]);
        } else if (isTripleSectionDifferent(parsedSections)) {
            result.append(parsedSections.get(0)[0]).append(parsedSections.get(0)[1])
                    .append("/").append(parsedSections.get(1)[0]).append(parsedSections.get(1)[1])
                    .append("/").append(parsedSections.get(2)[0]).append(parsedSections.get(2)[1]);
        } else if (isQuadrupleSectionSame(parsedSections)) {
            result.append(parsedSections.get(0)[0])  // было result.append("2ВЛ80С ").append(parsedSections.get(0)[0])
                    .append("/").append(parsedSections.get(2)[0]);
        } else if (isQuadrupleSectionDifferent(parsedSections)) {
            result.append(parsedSections.get(0)[0]).append(parsedSections.get(0)[1])
                    .append("/").append(parsedSections.get(1)[0]).append(parsedSections.get(1)[1])
                    .append("/").append(parsedSections.get(2)[0]).append(parsedSections.get(2)[1])
                    .append("/").append(parsedSections.get(3)[0]).append(parsedSections.get(3)[1]);
        } else if (isQuadrupleSectionDifferentLastTwoSections(parsedSections)) {
            result.append(parsedSections.get(0)[0])
                    .append("/").append(parsedSections.get(2)[0]).append(parsedSections.get(2)[1])
                    .append("/").append(parsedSections.get(3)[0]).append(parsedSections.get(3)[1]);
        } else if (isQuadrupleSectionDifferentFirstTwoSection(parsedSections)) {
            result.append(parsedSections.get(0)[0]).append(parsedSections.get(0)[1])
                    .append("/").append(parsedSections.get(1)[0]).append(parsedSections.get(1)[1])
                    .append("/").append(parsedSections.get(2)[0]);
        }

        return result.toString();
    }

    private boolean allSectionsEmpty(List<String[]> sections) {
        return sections.stream().allMatch(s -> s[0].isEmpty());
    }

    private boolean isSingleSection(List<String[]> sections) {
        return !sections.get(0)[0].isEmpty() && sections.get(1)[0].isEmpty() && sections.get(2)[0].isEmpty() && sections.get(3)[0].isEmpty();
    }

    private boolean isDoubleSectionSame(List<String[]> sections) {
        return sections.get(0)[0].equals(sections.get(1)[0]) && !sections.get(0)[0].isEmpty() && sections.get(2)[0].isEmpty() && sections.get(3)[0].isEmpty();
    }

    private boolean isDoubleSectionDifferent(List<String[]> sections) {
        return !sections.get(0)[0].equals(sections.get(1)[0]) && !sections.get(0)[0].isEmpty() && !sections.get(1)[0].isEmpty() && sections.get(2)[0].isEmpty() && sections.get(3)[0].isEmpty();
    }

    private boolean isTripleSectionSame(List<String[]> sections) {
        return sections.get(0)[0].equals(sections.get(1)[0]) && !sections.get(0)[0].isEmpty() && !sections.get(2)[0].isEmpty() && sections.get(3)[0].isEmpty();
    }

    private boolean isTripleSectionDifferent(List<String[]> sections) {
        return !sections.get(0)[0].equals(sections.get(1)[0]) && !sections.get(0)[0].isEmpty() && !sections.get(1)[0].isEmpty() && !sections.get(2)[0].isEmpty() && sections.get(3)[0].isEmpty();
    }

    private boolean isQuadrupleSectionSame(List<String[]> sections) {
        return sections.get(0)[0].equals(sections.get(1)[0]) && sections.get(2)[0].equals(sections.get(3)[0]) && !sections.get(0)[0].isEmpty() && !sections.get(2)[0].isEmpty();
    }

    private boolean isQuadrupleSectionDifferent(List<String[]> sections) {

        return !sections.get(0)[0].equals(sections.get(1)[0]) && !sections.get(2)[0].equals(sections.get(3)[0]) && !sections.get(0)[0].isEmpty() && !sections.get(1)[0].isEmpty() && !sections.get(2)[0].isEmpty() && !sections.get(3)[0].isEmpty();
    }

    private boolean isQuadrupleSectionDifferentLastTwoSections(List<String[]> sections){
        return sections.get(0)[0].equals(sections.get(1)[0]) && !sections.get(2)[0].equals(sections.get(3)[0]) && !sections.get(0)[0].isEmpty() && !sections.get(1)[0].isEmpty() && !sections.get(2)[0].isEmpty() && !sections.get(3)[0].isEmpty();
    }

    private boolean isQuadrupleSectionDifferentFirstTwoSection(List<String[]> sections){
        return !sections.get(0)[0].equals(sections.get(1)[0]) && sections.get(2)[0].equals(sections.get(3)[0]) && !sections.get(0)[0].isEmpty() && !sections.get(1)[0].isEmpty() && !sections.get(2)[0].isEmpty() && !sections.get(3)[0].isEmpty();
    }

    public List<String> createSections(String series, String number) {
        String section1 = "";
        String section2 = "";
        String section3 = "";
        String section4 = "";

        // Определяем количество секций на основе серии
        int sectionCount = getSectionCount(series);

        // Разбиваем номер локомотива на части, если есть разделитель "/"
        String[] parts = formatSplitSection(number.split("/"));

        // В зависимости от количества секций, присваиваем значения переменным
        switch (sectionCount) {
            case 1:
                section1 = formatSection(parts[0], "А");
                break;
            case 2:
                section1 = formatSection(parts[0], "А");
                section2 = formatSection(parts[0], "Б");
                if (parts.length > 1) {
                    section1 = parts[0];
                    section2 = parts[1];
                }
                break;
            case 3:
                section1 = formatSection(parts[0], "А");
                section2 = formatSection(parts[0], "Б");
                if (parts.length > 1) {
                    section3 = parts[1];
                }
                if (parts.length > 2) {
                    section1 = parts[0];
                    section2 = parts[1];
                    section3 = parts[2];
                }
                break;
            case 4:
                section1 = formatSection(parts[0], "А");
                section2 = formatSection(parts[0], "Б");
                if (parts.length > 1) {
                    section3 = formatSection(parts[1], "А");
                    section4 = formatSection(parts[1], "Б");
                }
                if (parts.length > 2 && (parts[0].contains("А") || parts[0].contains("Б"))) {
                    section1 = parts[0];
                    section2 = parts[1];
                    section3 = formatSection(parts[2], "А");
                    section4 = formatSection(parts[2], "Б");
                }
                if (parts.length > 2 && ((!parts[0].contains("А") || !parts[0].contains("Б")) && (parts[1].contains("А") || parts[1].contains("Б")))) {
                    section1 = formatSection(parts[0], "А");
                    section2 = formatSection(parts[0], "Б");
                    section3 = parts[1];
                    section4 = parts[2];
                }
                if (parts.length > 2 && ((parts[0].contains("А") || parts[0].contains("Б")) && (parts[1].contains("А") || parts[1].contains("Б")) && (!parts[2].contains("А") || !parts[2].contains("Б")))) {
                    section1 = parts[0];
                    section2 = parts[1];
                    section3 = formatSection(parts[2], "А");
                    section4 = formatSection(parts[2], "Б");
                }
                if (parts.length > 3) {
                    section1 = parts[0];
                    section2 = parts[1];
                    section3 = parts[2];
                    section4 = parts[3];
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid series format: " + series);
        }

        // Выводим результат
        List<String> sectionsNumber = new ArrayList<>();

        addClearSectionIfValid(sectionsNumber, section1);
        addClearSectionIfValid(sectionsNumber, section2);
        addClearSectionIfValid(sectionsNumber, section3);
        addClearSectionIfValid(sectionsNumber, section4);
        return sectionsNumber;
    }

    // Функция для определения количества секций на основе серии
    private int getSectionCount(String series) {
        if (series.startsWith("1.5") || series.startsWith("3ТЭ") || series.startsWith("3ЭС")) {
            return 3;
        } else if (series.startsWith("2ВЛ")) {
            return 4;
        } else if (series.startsWith("ТЭМ14") || series.startsWith("ТЭМ18ДМ") || series.startsWith("ЭП1")
                || series.startsWith("ТЭП70бс") || series.startsWith("ТЭП70") || series.startsWith("ТЭМ7а")
        || series.startsWith("ЭП1М") || series.startsWith("ТЭМ2") || series.startsWith("ЭП20")) {
            return 1;
        }else {
            return 2; // Если серия начинается с других символов, то это 2 секции
        }
    }

    // Форматирование номера секции (добавление буквы в конце)
    private String formatSection(String numberPart, String letter) {
        // Удаляем буквы из части номера, если они есть
        String digits = numberPart.replaceAll("[^\\d]", "");
        return digits + letter;
    }

    // Форматирование номера секции (добавление буквы в конце) когда только посплитен весь номер
    private String[] formatSplitSection(String[] parts) {
        // Создаем новый массив для хранения отформатированных номеров
        String[] formattedParts = new String[parts.length];

        // Проходим по каждому элементу массива и форматируем его
        for (int i = 0; i < parts.length; i++) {
            // Удаляем буквы из части номера, если они есть
            String digits = parts[i].replaceAll("[^\\d]", "");
            String letters = extractLetter(parts[i]);
            formattedParts[i] = digits + letters; // Добавляем результат в новый массив
        }

        return formattedParts; // Возвращаем массив с отформатированными номерами
    }

    // Маппинг для преобразования латинских букв в кириллические
    private static final Map<Character, Character> LATIN_TO_CYRILLIC = new HashMap<>();

    static {
        LATIN_TO_CYRILLIC.put('A', 'А'); // A -> А
        LATIN_TO_CYRILLIC.put('B', 'В'); // B -> В
        LATIN_TO_CYRILLIC.put('E', 'Е'); // E -> Е
        LATIN_TO_CYRILLIC.put('C', 'С'); // C -> С
    }

    // Извлечение буквы из части номера
    public String extractLetter(String part) {
        String letter = part.replaceAll("[^A-ZА-Я]", ""); // Извлекаем букву из номера
        if (!letter.isEmpty()) {
            char extractedChar = letter.charAt(0); // Извлекаем первый символ
            // Если это латинская буква, которую нужно заменить на кириллическую
            if (LATIN_TO_CYRILLIC.containsKey(extractedChar)) {
                // Заменяем её на кириллическую
                return String.valueOf(LATIN_TO_CYRILLIC.get(extractedChar));
            }
            // Если буква уже кириллическая, возвращаем её без изменений
            return letter;
        }
        return ""; // Если буквы нет, возвращаем пустую строку
    }

    // Получение серии для секций из серии локомотива
    public String getTypeLocoListFromLoco (String typeLocoInfo){
        String typeSection = typeLocoInfo;
        if (typeLocoInfo.startsWith("1.5")){
            //вызываем конструктор со строкой в качестве аргумента
            typeSection = typeLocoInfo.substring(3);
        }
        if (typeLocoInfo.startsWith("2ВЛ")){
            //вызываем конструктор со строкой в качестве аргумента
            typeSection = typeLocoInfo.substring(1);
        }
        return typeSection;
    }

    // Получение серии локомотива из серии секций и количества секций
    public String getTypeLocoFromTypeSection (String typeSection, int countSection){
        String typeLocoInfo = typeSection;
        if (countSection == 3){
            typeLocoInfo = "1.5" + typeSection;
        }
        if (countSection == 4){
            typeLocoInfo = "2" + typeSection;
        }
        return typeLocoInfo;
    }

    //Гарантированное получение заглавных кирилл симв в конце номера секций
    public String convertToCyrillic(String sectionNumber) {
        if (sectionNumber == null || sectionNumber.isEmpty()) {
            return sectionNumber;
        }

        // Получаем последний символ
        char lastChar = sectionNumber.charAt(sectionNumber.length() - 1);

        // Карта соответствия латинских и кириллических букв
        switch (lastChar) {
            case 'A': return sectionNumber.substring(0, sectionNumber.length() - 1) + 'А';
            case 'B': return sectionNumber.substring(0, sectionNumber.length() - 1) + 'Б';
            case 'C': return sectionNumber.substring(0, sectionNumber.length() - 1) + 'С';
            case 'E': return sectionNumber.substring(0, sectionNumber.length() - 1) + 'Е';
            case 'H': return sectionNumber.substring(0, sectionNumber.length() - 1) + 'Н';
            case 'K': return sectionNumber.substring(0, sectionNumber.length() - 1) + 'К';
            case 'M': return sectionNumber.substring(0, sectionNumber.length() - 1) + 'М';
            case 'O': return sectionNumber.substring(0, sectionNumber.length() - 1) + 'О';
            case 'P': return sectionNumber.substring(0, sectionNumber.length() - 1) + 'Р';
            case 'T': return sectionNumber.substring(0, sectionNumber.length() - 1) + 'Т';
            case 'X': return sectionNumber.substring(0, sectionNumber.length() - 1) + 'Х';
            // Заменяем кириллическую А на латинскую A
            //case 'А': return sectionNumber.substring(0, sectionNumber.length() - 1) + 'A'; // кириллическая А -> латинская A
            default: return sectionNumber; // Если буква уже кириллическая или не требует замены
        }
    }

    // Функция для обработки списка номеров секций
    public List<String> convertAllSectionsToCyrillic(List<String> clearNumbers) {
        List<String> convertedSections = new ArrayList<>();

        for (String section : clearNumbers) {
            convertedSections.add(convertToCyrillic(section));
        }

        return convertedSections;
    }

    // Конвертация сущности в DTO
    public LocoInfoDTO convertToDTO(LocoInfo locoInfo) {
        LocoInfoDTO dto = new LocoInfoDTO();
        dto.setId(locoInfo.getId());
        dto.setRegion(locoInfo.getRegion());
        dto.setHomeDepot(locoInfo.getHomeDepot());
        dto.setLocoType(locoInfo.getLocoType());
        dto.setLocoSection1(locoInfo.getLocoSection1());
        dto.setLocoFit1(locoInfo.getLocoFit1());
        dto.setLocoSection2(locoInfo.getLocoSection2());
        dto.setLocoFit2(locoInfo.getLocoFit2());
        dto.setLocoSection3(locoInfo.getLocoSection3());
        dto.setLocoFit3(locoInfo.getLocoFit3());
        dto.setLocoSection4(locoInfo.getLocoSection4());
        dto.setLocoFit4(locoInfo.getLocoFit4());
        dto.setLocoUnit(locoInfo.getLocoUnit());
        return dto;
    }

    // Конвертация DTO в сущность
    public LocoInfo convertToEntity(LocoInfoDTO dto) {
        LocoInfo locoInfo = new LocoInfo();
        locoInfo.setRegion(dto.getRegion());
        locoInfo.setHomeDepot(dto.getHomeDepot());
        locoInfo.setLocoType(dto.getLocoType());
        locoInfo.setLocoSection1(dto.getLocoSection1());
        locoInfo.setLocoFit1(dto.getLocoFit1());
        locoInfo.setLocoSection2(dto.getLocoSection2());
        locoInfo.setLocoFit2(dto.getLocoFit2());
        locoInfo.setLocoSection3(dto.getLocoSection3());
        locoInfo.setLocoFit3(dto.getLocoFit3());
        locoInfo.setLocoSection4(dto.getLocoSection4());
        locoInfo.setLocoFit4(dto.getLocoFit4());
        locoInfo.setLocoUnit(dto.getLocoUnit());
        return locoInfo;
    }

}

