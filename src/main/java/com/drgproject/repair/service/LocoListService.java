package com.drgproject.repair.service;

import com.drgproject.repair.dto.LocoFilterDTO;
import com.drgproject.repair.dto.LocoListDTO;
import com.drgproject.repair.entity.BlockOnLoco;
import com.drgproject.repair.entity.LocoInfo;
import com.drgproject.repair.entity.LocoList;
import com.drgproject.repair.repository.BlockOnLocoRepository;
import com.drgproject.repair.repository.LocoListRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class LocoListService {

    private final LocoListRepository locoListRepository;
    private final BlockOnLocoRepository blockOnLocoRepository;
    private final LocoInfoService locoInfoService;

    public LocoListService(LocoListRepository locoListRepository,
                           BlockOnLocoRepository blockOnLocoRepository,
                           LocoInfoService locoInfoService) {
        this.locoListRepository = locoListRepository;
        this.blockOnLocoRepository = blockOnLocoRepository;
        this.locoInfoService = locoInfoService;
    }

    public List<LocoListDTO> getAllLocoLists() {
        return locoListRepository.findAll().stream().map(this::convertToDTO).toList();
    }

    public List<LocoListDTO> getAllLocoListsByRegion(String region) {
        return locoListRepository.findAllByHomeRegion(region).stream().map(this::convertToDTO).toList();
    }

    public LocoListDTO getLocoListById(Long id) {
        Optional<LocoList> locoList = locoListRepository.findById(id);
        return locoList.map(this::convertToDTO).orElse(null);
    }

    public LocoListDTO getLocoListByNumberLoco(String numberLoco) {
        List<LocoList> locoLists = locoListRepository.findAll();
        Optional<LocoList> locoListOptional = locoLists.stream()
                .filter(loco -> loco.getLocoNumber().equals(numberLoco))
                .findFirst();
        return locoListOptional.map(this::convertToDTO).orElse(null);
    }

    public LocoListDTO getLocoListByNumberLocoAndTypeLoco(String numberLoco, String typeLoco){
        Optional<LocoList> locoListByType = locoListRepository.findLocoListByLocoNumberAndTypeLoco(numberLoco, typeLoco);
        return locoListByType.map(this::convertToDTO).orElse(null);
    }

    //Проверка на дублирование перед созданием секции
    public boolean ifLociListIsExists(String homeRegion, String homeDepot, String locoNumber){
        Optional<LocoList> locoListIsExist = locoListRepository.findLocoListByHomeRegionAndHomeDepotAndLocoNumber(homeRegion, homeDepot, locoNumber);
        return locoListIsExist.isPresent();
    }

    //Проверка на дублирование перед созданием секции при загрузке из файла
    public boolean ifSectionIsExists(String homeRegion, String homeDepot, String typeLoco, String locoNumber){
        Optional<LocoList> locoListIsExist = locoListRepository.findLocoListByHomeRegionAndHomeDepotAndTypeLocoAndLocoNumber(homeRegion, homeDepot, typeLoco, locoNumber);
        return locoListIsExist.isPresent();
    }

    // Приверка ну сущестование секции (для создания блока на секцию)
    public boolean ifSectionExist (String typeLoco, String sectionNumber){
        Optional<LocoList> sectionIsExist = locoListRepository.findLocoListByTypeLocoAndLocoNumber(typeLoco, sectionNumber);
        return sectionIsExist.isPresent();
    }

    // Метод сортировки, который учитывает как числовую, так и буквенную части
    public List<LocoListDTO> sortLocoListByNumber(List<LocoListDTO> locoList) {
        return locoList.stream()
                .sorted((l1, l2) -> {
                    // Извлекаем числовую и буквенную части из locoNumber
                    int number1 = extractNumericPart(l1.getLocoNumber());
                    int number2 = extractNumericPart(l2.getLocoNumber());

                    // Сравниваем числовые части
                    int numericComparison = Integer.compare(number1, number2);

                    if (numericComparison == 0) {
                        // Если числовые части одинаковы, сравниваем буквенные части
                        String letter1 = extractLetterPart(l1.getLocoNumber());
                        String letter2 = extractLetterPart(l2.getLocoNumber());
                        return letter1.compareTo(letter2);
                    }
                    return numericComparison;
                })
                .toList();
    }

    // Вспомогательный метод для извлечения числовой части из номера секции
    private int extractNumericPart(String locoNumber) {
        String numericPart = locoNumber.replaceAll("\\D+", "");  // Убираем все нецифровые символы
        return numericPart.isEmpty() ? 0 : Integer.parseInt(numericPart);  // Если пусто, возвращаем 0
    }

    // Вспомогательный метод для извлечения буквенной части из номера секции
    private String extractLetterPart(String locoNumber) {
        return locoNumber.replaceAll("\\d+", "");  // Убираем все цифры, остаются только буквы
    }

    // Вспомогательный метод для гарантированого изменения буквы в номере секции на кириллическ
    public String getCyrillicSection (String locoNumber){
        String numberWithoutLetter = String.valueOf(extractNumericPart(locoNumber));
        String letter = locoInfoService.extractLetter(locoNumber);
        return numberWithoutLetter + letter;
    }

    // Получаем список список всех номеров секций по Region, homeDepot, typeLoco
    public List<LocoListDTO> getSectionByRegionAndHomeDepotAndTypeLoco(String region, String homeDepot, String typeLoco){
        return locoListRepository.findAllByHomeRegionAndHomeDepotAndTypeLoco(region, homeDepot, typeLoco).stream()
                .map(this::convertToDTO)
                .toList();
    }

    // Приверка на сущ по дороге депо прип серии секци и номеру секции
    public boolean existsSectionByRegionAndDepotAndTypeAndNumber(String region, String depot, String type, String number){
        return locoListRepository.existsByHomeRegionAndHomeDepotAndTypeLocoAndLocoNumber(region, depot, type, number);
    }

    public Page<LocoListDTO> getSectionByRegionAndHomeDepotAndTypeLoco(String region, String homeDepot, String typeLoco, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        // Получаем данные с пагинацией из репозитория
        Page<LocoList> locoListPage = locoListRepository.findByHomeRegionAndHomeDepotAndTypeLoco(region, homeDepot, typeLoco, pageable);

        // Преобразуем сущности в DTO с сохранением данных о пагинации
        return locoListPage.map(this::convertToDTO);
    }

    /**
     * Метод для получения отфильтрованного и отсортированного списка свободных секций
     * @param locoList список отфильтрованных секций из LocoListDTO
     * @param locoFilter список занятых секций из LocoFilterDTO
     * @return отсортированный список свободных номеров секций
     */
    public List<String> getSortedFreeSections(List<LocoListDTO> locoList, List<LocoFilterDTO> locoFilter) {
        // Получаем список всех номеров секций из LocoListDTO
        List<String> locoNumbers = locoList.stream()
                .map(LocoListDTO::getLocoNumber)
                .toList();

        // Получаем список занятых номеров секций из LocoFilterDTO, где секции заняты
        List<String> busySections = locoFilter.stream()
                .filter(filter -> "да".equals(filter.getBusySection()) && "нет".equals(filter.getFreeSection()))
                .map(LocoFilterDTO::getSectionNumber)
                .toList();

        // Формируем список свободных секций, отфильтровывая занятые секции
        List<String> freeSections = locoNumbers.stream()
                .filter(section -> !busySections.contains(section))  // Оставляем только те секции, которые не заняты
                .toList();

        // Сортировка свободных секций по числовой части без учета букв
        return freeSections.stream()
                .sorted((section1, section2) -> {
                    // Извлечение числовой части из номеров секций (например, 001А -> 001, 1234Б -> 1234)
                    Integer num1 = Integer.parseInt(section1.replaceAll("[^0-9]", ""));
                    Integer num2 = Integer.parseInt(section2.replaceAll("[^0-9]", ""));

                    // Если числовая часть равна, сортируем по буквенной части
                    int compare = num1.compareTo(num2);
                    if (compare == 0) {
                        // Сравниваем буквенные части
                        char char1 = section1.replaceAll("[0-9]", "").charAt(0);
                        char char2 = section2.replaceAll("[0-9]", "").charAt(0);
                        return Character.compare(char1, char2);
                    }
                    return compare;
                })
                .toList();
    }




    //Создаем секции из документа Excel
    public LocoListDTO createSectionList(String contractNumber, String typeLoco, String typeSystem,
                                         String locoNumber, String homeRegion, String homeDepot, String comment ) {
        LocoList locoList = new LocoList();
        locoList.setContractNumber(contractNumber);
        locoList.setTypeLoco(typeLoco);
        locoList.setTypeSystem(typeSystem);
        locoList.setLocoNumber(locoNumber);
        locoList.setHomeRegion(homeRegion);
        locoList.setHomeDepot(homeDepot);
        locoList.setComment(comment);

        // Пустой список блоков при создании локомотива
        locoList.setBlockOnLocos(new ArrayList<>());

        LocoList savedLocoList = locoListRepository.save(locoList);
        return convertToDTO(savedLocoList);
    }

    public LocoListDTO createLocoList(LocoListDTO locoListDTO) {
        LocoList locoList = new LocoList();
        locoList.setContractNumber(locoListDTO.getContractNumber());
        locoList.setTypeLoco(locoListDTO.getTypeLoco());
        locoList.setTypeSystem(locoListDTO.getTypeSystem());
        locoList.setLocoNumber(locoListDTO.getLocoNumber());
        locoList.setHomeRegion(locoListDTO.getHomeRegion());
        locoList.setHomeDepot(locoListDTO.getHomeDepot());
        locoList.setComment(locoListDTO.getComment());

        // Пустой список блоков при создании локомотива
        locoList.setBlockOnLocos(new ArrayList<>());

        LocoList savedLocoList = locoListRepository.save(locoList);
        return convertToDTO(savedLocoList);
    }

    public LocoListDTO updateLocoList(Long id, LocoListDTO locoListDTO) {
        Optional<LocoList> optionalLocoList = locoListRepository.findById(id);
        if (optionalLocoList.isPresent()) {
            LocoList locoList = optionalLocoList.get();
            locoList.setContractNumber(locoListDTO.getContractNumber());
            locoList.setTypeLoco(locoListDTO.getTypeLoco());
            locoList.setTypeSystem(locoListDTO.getTypeSystem());
            locoList.setLocoNumber(locoListDTO.getLocoNumber());
            locoList.setHomeRegion(locoListDTO.getHomeRegion());
            locoList.setHomeDepot(locoListDTO.getHomeDepot());
            locoList.setComment(locoListDTO.getComment());
            LocoList updatedLocoList = locoListRepository.save(locoList);
            return convertToDTO(updatedLocoList);
        }
        return null;
    }

    public boolean deleteLocoList(Long id) {
        if (locoListRepository.existsById(id)) {
            locoListRepository.deleteById(id);
            return true;
        }
        return false;
    }

    //поиск номера локомотива по первым двум цифрам
    public List<String> findNumbersByPrefix(String prefix) {
        return locoListRepository.findByLocoNumberStartingWith(prefix)
                .stream()
                .map(LocoList::getLocoNumber)
                .toList();
    }

    private LocoListDTO convertToDTO(LocoList locoList) {
        List<Long> blockOnLocosIds = locoList.getBlockOnLocos().stream()
                .map(BlockOnLoco::getId)
                .toList();

        return new LocoListDTO(locoList.getId(), locoList.getContractNumber(), locoList.getTypeLoco(),
                locoList.getTypeSystem(), locoList.getLocoNumber(), locoList.getHomeRegion(),
                locoList.getHomeDepot(), locoList.getComment(), blockOnLocosIds);
    }
}
